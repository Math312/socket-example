package com.jllsq.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioClient extends Thread{

    private SocketChannel socketChannel;

    private String address;

    private int port;

    public NioClient(String address,int port) {
        try {
            socketChannel = SocketChannel.open();
            this.address = address;
            this.port = port;
//            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socketChannel.connect(new InetSocketAddress(address,port));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String doConnection(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        boolean complete = false;
        while (complete) {
            try {
               byte[] buffer = new byte[1024];
               byteBuffer.get(buffer);
               int index = 0;
               if (buffer[index] != '$') {

               }
               int current = index+1;
               int temp = current;
               while(buffer[temp] != '\r' && buffer[temp+1] != '\n') {
                   if (buffer[temp] >= '0' && buffer[temp] <= '9') {
                       temp ++;
                   }
               }
               int len = temp - current;
               byte[] lenBytes = new byte[len];
               System.arraycopy(buffer,current,lenBytes,0,len);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
