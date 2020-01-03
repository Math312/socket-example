package com.jllsq.client;

import com.jllsq.common.entity.AtomicMessage;
import com.jllsq.common.reader.AtomicMessageReader;
import com.sun.org.apache.bcel.internal.generic.Select;
import sun.nio.ch.EPollSelectorProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient extends Thread {

    private SocketChannel socketChannel;

    private String address;

    private int port;

    private Selector selector;

    public NioClient(String address, int port, Selector selector) {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            this.address = address;
            this.port = port;
            this.selector = selector;
//            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!socketChannel.connect(new InetSocketAddress(address, port))) {
                socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String doConnection(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            socketChannel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byteBuffer.flip();
        byte[] buffer = new byte[byteBuffer.limit()];
        byteBuffer.get(buffer);
        byteBuffer.compact();
        int index = 0;
        AtomicMessage atomicMessage = AtomicMessageReader.readMessage(buffer, 0);
        index += atomicMessage.getTotal();
        AtomicMessage connectionIdMessage = AtomicMessageReader.readMessage(buffer, index);
        String id = connectionIdMessage.getMessage();
        System.out.println(id);
        return id;

    }

    public static void main(String[] args) {
        EPollSelectorProvider ePollSelectorProvider = new EPollSelectorProvider();
        try {
            Selector selector = ePollSelectorProvider.openSelector();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", 8000));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    service(key);
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void service(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (key.isConnectable()) {
            socketChannel.finishConnect();
            doConnection(socketChannel);
        }

    }
}
