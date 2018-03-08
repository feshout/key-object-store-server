package com.codecool.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class StoreServer implements Runnable {

    private int serverPortNr;
    private Map<String, byte[]> db = new HashMap<>();
    private boolean isRunning;
    private List<ClientSocket> clients;
    private LinkedList<Socket> clientsQueue;
    private int maxNrOfClients;
    private ServerSocket serverSocket;
    private Thread runner;

    public StoreServer(int serverPortNr, int maxNrOfClients) {
        this.serverPortNr = serverPortNr;
        this.isRunning = true;
        clients = new LinkedList<>();
        clientsQueue = new LinkedList<>();
        this.maxNrOfClients = maxNrOfClients;
    }

    public void start() {

        runner = new Thread(this);
        runner.start();

        Scanner input = new Scanner(System.in);
        String userInput = "";

        while(!(userInput.equals("quit"))) {

            userInput = input.nextLine();

            if(userInput.equals("show")) {
                showAllClients();
            }
        }
        input.close();
        stop();
    }


    @Override
    public void run() {

        try {
            this.serverSocket = new ServerSocket(this.serverPortNr);
            ClientAccepter clientAccepter = new ClientAccepter(this);
            clientAccepter.startThread();
            while(this.isRunning) {
                runner.sleep(5000);
                if(clients.size() < this.maxNrOfClients && clientsQueue.size() != 0) {
                    ClientSocket client = new ClientSocket(clientsQueue.getFirst(), this);
                    clientsQueue.removeFirst();
                    client.start();
                    clients.add(client);
                }

            }
        } catch(IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    private void stop() {

        try{
            for(int i = clients.size() - 1; i >= 0; i--) {
                clients.get(i).stop();
            }
            this.isRunning = false;
            this.serverSocket.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void showAllClients() {
        for(ClientSocket client: clients) {
            System.out.println(client);
        }
    }

    void saveToDB(String key, byte[] data) {
        db.put(key, data);
    }

    byte[] getFromDB(String key) {
        return db.get(key);
    }

    boolean isKeyPresent(String key) {
        return db.containsKey(key);
    }

    void stopClient(ClientSocket client) {
        clients.remove(client);
    }

    public LinkedList<Socket> getClientsQueue() {
        return clientsQueue;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
