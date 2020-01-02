package com.jllsq.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    public static void main(String[] args) {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress socketAddress = new InetSocketAddress("0.0.0.0",8000);
            serverSocket.bind(socketAddress);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(new ServerThread(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
