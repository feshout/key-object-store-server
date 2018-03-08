package com.codecool;

import com.codecool.server.StoreServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if(args.length == 1) {
            int portNr = Integer.parseInt(args[0]);
            StoreServer storeServer = new StoreServer(portNr, 1);
            storeServer.start();
        } else {
            System.out.println("Wrong args!");
        }
    }
}
