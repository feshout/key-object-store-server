package com.codecool.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientAccepter implements Runnable {

    private StoreServer storeServer;
    private Thread thread;

    public ClientAccepter(StoreServer storeServer) {

        this.storeServer = storeServer;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {

        boolean isRunning = true;

        while(isRunning) {

            try {
                Socket socket = storeServer.getServerSocket().accept();
                storeServer.getClientsQueue().addFirst(socket);
            } catch (IOException e) {
                isRunning = false;
            }
        }
    }

    public void startThread(){
        this.thread.start();
    }
}
