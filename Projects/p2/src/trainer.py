from __future__ import absolute_import

import argparse
import dill as pickle
import multiprocessing as mp
import os
import tempfile

import tensorflow as tf
import tensorflow_transform as tft

from tensorflow_transform.saved import saved_transform_io


def load(filename):
    with tf.gfile.Open(filename, 'rb') as f:
        return pickle.load(f)


def _make_train_or_eval_input_fn(feature_spec, labels, file_pattern, batch_size, mode, shuffle=True):


    def input_fn():
        def decode(elem):
            model_features = tf.parse_single_example(elem, features=feature_spec)
            model_labels = tf.stack([model_features.pop(label) for label in labels])
            return model_features, model_labels

        files = tf.data.Dataset.list_files(file_pattern)
        dataset = files.apply(tf.data.experimental.parallel_interleave(
            tf.data.TFRecordDataset, cycle_length=mp.cpu_count()))
        dataset = dataset.map(decode, num_parallel_calls=mp.cpu_count())
        dataset = dataset.take(-1)
        if mode == tf.estimator.ModeKeys.TRAIN:
            if shuffle:
                dataset = dataset.apply(tf.data.experimental.shuffle_and_repeat(
                    batch_size * 8))
            else:
                dataset = dataset.cache()
                dataset = dataset.repeat()
        dataset = dataset.batch(batch_size)
        dataset = dataset.prefetch(1)

        iterator = dataset.make_one_shot_iterator()
        batch_features, batch_labels = iterator.get_next()
        return batch_features, batch_labels

    return input_fn


def make_train_input_fn(feature_spec, labels, file_pattern, batch_size, shuffle=True):
    """Makes an input_fn for training."""
    return _make_train_or_eval_input_fn(
        feature_spec,
        labels,
        file_pattern,
        batch_size,
        tf.estimator.ModeKeys.TRAIN,
        shuffle)


def make_eval_input_fn(feature_spec, labels, file_pattern, batch_size):
    """Makes an input_fn for evaluation."""
    return _make_train_or_eval_input_fn(
        feature_spec,
        labels,
        file_pattern,
        batch_size,
        tf.estimator.ModeKeys.EVAL)


def make_serving_input_fn(tft_output, input_feature_spec, labels):
    """Makes an input_fn for serving prediction.
    This will use the inputs format produced by the preprocessing PTransform. This
    applies the transformations from the tf.Transform preprocessing_fn before
    serving it to the TensorFlow model.
    """
    def serving_input_fn():
        input_features = {}
        for feature_name in input_feature_spec:
            if feature_name in labels:
                continue
            dtype = input_feature_spec[feature_name].dtype
            input_features[feature_name] = tf.placeholder(
                dtype, shape=[None], name=feature_name)

        inputs = tft_output.transform_raw_features(input_features)

        return tf.estimator.export.ServingInputReceiver(inputs, input_features)

    return serving_input_fn


def train_and_evaluate(
        work_dir,
        input_feature_spec,
        labels,
        train_files_pattern,
        eval_files_pattern,
        batch_size=64,
        train_max_steps=1000):
    """Trains and evaluates the estimator given.
    The input functions are generated by the preprocessing function.
    """

    model_dir = os.path.join(work_dir, 'model')
    if tf.gfile.Exists(model_dir):
        tf.gfile.DeleteRecursively(model_dir)

    # Specify where to store our model
    run_config = tf.estimator.RunConfig()
    run_config = run_config.replace(model_dir=model_dir)

    # This will give us a more granular visualization of the training
    run_config = run_config.replace(save_summary_steps=10)

    # Create a Deep Neural Network Regressor estimator
    feature_columns = []
    for key in input_feature_spec.keys():
        if key != 'LoS':
            feature_columns.append(tf.feature_column.numeric_column(key, dtype=tf.float32))

    estimator = tf.estimator.DNNRegressor(
        feature_columns=feature_columns,
        hidden_units=[128, 64],
        dropout=0.5,
        config=run_config)

    #Get the transformed feature_spec
    tft_output = tft.TFTransformOutput(work_dir)
    feature_spec = tft_output.transformed_feature_spec()

    # Create the training and evaluation specifications
    train_spec = tf.estimator.TrainSpec(
        input_fn=make_train_input_fn(
            feature_spec, labels, train_files_pattern, batch_size),
        max_steps=train_max_steps)
    #
    exporter = tf.estimator.FinalExporter(
        'final', make_serving_input_fn(tft_output, input_feature_spec, labels))
    #
    eval_spec = tf.estimator.EvalSpec(
        input_fn=make_eval_input_fn(
            feature_spec, labels, eval_files_pattern, batch_size),
        exporters=[exporter])
    #
    # # Train and evaluate the model
    tf.estimator.train_and_evaluate(estimator, train_spec, eval_spec)


if __name__ == '__main__':
    """Main function called by AI Platform."""
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter)

    parser.add_argument(
        '-o',
        '--work-dir',
        required=True,
        help='Directory for staging and working files. '
             'This can be a Google Cloud Storage path.')

    parser.add_argument(
        '-b',
        '--batch-size',
        type=int,
        default=64,
        help='Batch size for training and evaluation.')

    parser.add_argument(
        '-t',
        '--train-max-steps',
        type=int,
        default=1000,
        help='Number of steps to train the model')

    args = parser.parse_args()

    preprocess_data = load(os.path.join(args.work_dir, 'PreprocessData'))

    train_and_evaluate(
        args.work_dir,
        preprocess_data.input_feature_spec,
        preprocess_data.labels,
        preprocess_data.train_files_pattern,
        preprocess_data.eval_files_pattern,
        batch_size=args.batch_size,
        train_max_steps=args.train_max_steps)
