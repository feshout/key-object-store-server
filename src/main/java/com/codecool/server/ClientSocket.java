package com.codecool.server;

import com.codecool.connection.WrapperModel;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket implements Runnable {

    private Thread thread;
    private Socket socket;
    private StoreServer storeServer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    ClientSocket(Socket socket, StoreServer storeServer) {

        this.storeServer = storeServer;
        this.socket = socket;
        this.thread = new Thread(this);
    }

    void start() {
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(this.socket.getInputStream());
            oos = new ObjectOutputStream(this.socket.getOutputStream());

            while(!socket.isClosed()) {

                Object inputObject = ois.readObject();

                if(inputObject instanceof String) {

                    String key = (String)inputObject;

                    if(this.storeServer.isKeyPresent(key)) {
                        byte[] value = this.storeServer.getFromDB(key);
                        WrapperModel newWrapperModel = new WrapperModel(key, value);
                        oos.writeObject(newWrapperModel);
                    }

                } else if (inputObject instanceof WrapperModel) {
                    WrapperModel wrapperModel = (WrapperModel)inputObject;
                    String key = wrapperModel.getKey();
                    byte[] data = wrapperModel.getValue();
                    this.storeServer.saveToDB(key, data);
                }
            }

        } catch(IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected, closing connection");
        } finally {
            closeConnection();
        }
    }

    void stop() throws IOException{
        this.socket.close();
    }

    public String toString() {
        return this.socket.toString();
    }

    private void closeConnection() {

        try {
            this.ois.close();
            this.oos.close();
            this.storeServer.stopClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection closed successfully");
    }
}
