package com.jllsq.server;

import com.jllsq.util.ByteArrayUtil;
import com.jllsq.util.IntegerUtil;
import sun.nio.ch.EPollSelectorProvider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NioServer extends Thread {

    private final String MAGIC = "YANLIBABA";

    private final String CONNECTION = "CONNECTION";

    private final byte[] CONNECTION_BYTES = CONNECTION.getBytes();

    private final byte[] CONNECTION_LEN_BYTES = new byte[]{'1', '0'};

    private final byte[] SIGNAL = new byte[]{'$'};

    private final byte[] LRLN = new byte[]{'\r', '\n'};

    private ConcurrentHashMap<String, Channel> channelContainer;

    @Override
    public void run() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("0.0.0.0",8000));
            serverSocketChannel.configureBlocking(false);
            Selector selector = new EPollSelectorProvider().openSelector();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    service(key);
                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void service(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            doAcceptable(key);
        } else if (key.isWritable()) {
            doWrite(key);
        } else {
            doError(key);
        }

    }

    private void doError(SelectionKey key) {
        Channel channel = key.channel();
        channel.isOpen();
    }

    private void doWrite(SelectionKey key) {
    }

    private void doAcceptable(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = null;
        try {
            channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            channel.finishConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] data = createConnectionData();
        try {
            assert channel != null;
            channel.write(ByteBuffer.wrap(data));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] createConnectionData() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().replace("-", "");
        byte[] result = null;
        try {
            byte[] idBytes = id.getBytes("UTF-8");
            byte[] idBytesLen = IntegerUtil.unsignedIntegerToBytes(idBytes.length);
            result = ByteArrayUtil.appendBytes(SIGNAL, CONNECTION_LEN_BYTES, LRLN,
                    CONNECTION_BYTES, LRLN,
                    SIGNAL, idBytesLen, LRLN,
                    idBytes, LRLN);
            System.out.println(new String(result));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }

}
