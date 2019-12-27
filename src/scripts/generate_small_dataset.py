import pandas as pd
from src.settings import COMBINED_DATASET, SMALL_COMBINED_DATASET

ROWS = 1000

big_pd = pd.read_csv(COMBINED_DATASET)
small_pd = big_pd.sample(n=ROWS)
small_pd.to_csv(SMALL_COMBINED_DATASET, encoding='utf-8', index=False)
