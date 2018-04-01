package Server.Message;

import Server.Channel.Channel;
import Server.Message.Utilities.Constants;
import Server.Peer.Peer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Message {

    private Message _message = this;
    private Header header;
    private byte[] body;
    private Thread thread;
    private Peer peer;

    public Message(String[] headerArgs) throws IllegalAccessException {
        this.header = new Header(headerArgs);
        body = null;
        System.out.println(new String(this.header.getHeaderProtocol()));
    }

    public Message(String[] headerArgs, byte[] body) throws IllegalAccessException {
        header = new Header(headerArgs);
        this.body = body;
        this.thread = new Thread(new SenderRunnable());
    }

    public Message(DatagramPacket packet, Peer peer) throws IOException, IllegalAccessException {
        readPacket(packet);
        this.peer = peer;
        this.thread = new Thread(new ReceiverRunnable());
    }

    private void readPacket(DatagramPacket packet) throws IOException, IllegalAccessException {
        ByteArrayInputStream message = new ByteArrayInputStream(packet.getData());
        StringBuilder headerBuilder = new StringBuilder();
        byte character;
        while ((character = (byte) message.read()) != Constants.CR) {
            headerBuilder.append((char) character);
        }
        if ((byte) message.read() != Constants.LF || (byte) message.read() != Constants.CR || (byte) message.read() != Constants.LF) {
            System.out.println("Invalid Header");
            return;
        }

        String[] headerArgs = headerBuilder.toString().split(" ");
        this.header = new Header(headerArgs);

        MessageType messageType = header.getMessageType();
        if (messageType == null) {
            System.out.println("Invalid Header");
            return;
        }
        if (messageType == MessageType.PUTCHUNK || messageType == MessageType.CHUNK) {
            this.body = new byte[packet.getLength() - headerBuilder.length() - 4]; // 4 -> <crlf> <crlf>
            if (message.read(body) == -1) {
                System.out.println("Invalid Body");
            }
        }

        message.close();
    }

    private byte[] getBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(this.header.getHeaderProtocol());
        if(this.body!=null) {
            outputStream.write(this.body);
        }
        return outputStream.toByteArray();
    }


    public void start() {
        this.thread.start();
    }

    public void send(Channel channel) {
        new Thread(() -> {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[0];
            try {
                buffer = _message.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, channel.getAddress(), channel.getPort());
            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private class ReceiverRunnable implements Runnable {

        @Override
        public void run() {
            if (_message.getSenderID().equals(peer.getServerID())) { //A peer must never store the chunks of its own files.
                return;
            }
            switch (_message.getMessageType()) {
                case PUTCHUNK:
                    try {
                        peer.receivePutChunk(_message);
                    } catch (IOException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                case STORED:
                    peer.receiveStored(_message);
                    break;
                case GETCHUNK:
                    break;
                case CHUNK:
                    break;
                case DELETE:
                    break;
                case REMOVED:
                    break;

            }
        }
    }

    private class SenderRunnable implements Runnable {
        @Override
        public void run() {
            switch (_message.getMessageType()) {
                case PUTCHUNK:
                    peer.sendPutChunk(_message);
                    break;
                case STORED:
                    try {
                        peer.sendStored(_message);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                case GETCHUNK:
                    break;
                case CHUNK:
                    break;
                case DELETE:
                    break;
                case REMOVED:
                    break;

            }
        }
    }

    private Integer getSenderID() {
        return header.getSenderID();
    }

    private MessageType getMessageType() {
        return header.getMessageType();
    }

    public Integer getReplicationDegree(){
        return this.header.getReplicationDegree();
    }
    public String getFileID(){
        return this.header.getFileID();
    }

    public Integer getBodyLength() {
        return body.length;
    }

    public byte[] getBody() {
        return body;
    }

    public Integer getChunKNo(){
        return this.header.getChuckNo();

    }

}
