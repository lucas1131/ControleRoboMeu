package com.kircherelectronics.accelerationexplorer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.lang.String;

public class Server implements Runnable {

    private static final int REQUEST_ENABLE_INTERNET = 1;

    public Socket client;    
    public Scanner serverIn;
    public PrintStream serverOut;
    public ServerSocket server;
    public final String LOCAL_IP;

    Server() throws IOException, UnknownHostException {

        server = new ServerSocket(8080); // Start server
        LOCAL_IP = InetAddress.getLocalHost().getHostAddress(); // Get local ip
    }

    Server(int port) throws IOException, UnknownHostException {

        server = new ServerSocket(port); // Start server
        LOCAL_IP = InetAddress.getLocalHost().getHostAddress(); // Get local ip
    }

    @Override
    public void run(){

        try {
            
            this.client = this.server.accept();
            this.serverIn = new Scanner(client.getInputStream());
            this.serverOut = new PrintStream(client.getOutputStream());
        
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    /*
    TODO: 

    see how to request enable wifi
    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    */
}
