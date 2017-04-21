package com.kircherelectronics.accelerationexplorer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.lang.String;

public class Server implements Runnable {

    private static final int REQUEST_ENABLE_INTERNET = 1;
    private static String LOCAL_IP = "";

    public Socket client;
    public Scanner serverIn;
    public PrintStream serverOut;
    public ServerSocket server;

    Server() throws IOException, UnknownHostException {

        server = new ServerSocket(8080); // Start server
        LOCAL_IP = getIPAddress(true); // Get local ip
    }

    Server(int port) throws IOException, UnknownHostException {

        server = new ServerSocket(port); // Start server
        LOCAL_IP = getIPAddress(true); // Get local ip
    }

    public static String getIp(){ return LOCAL_IP; }

    @Override
    public void run(){

        try {
            
            // Wait for client to connect
            System.out.println("[Debug]: Waiting for client");
            this.client = this.server.accept();
            System.out.println("[Debug]: Client connected: " + this.client);

        } catch(Exception e){
            System.out.println("[Debug]: Connection error");
            e.printStackTrace();
        }

        System.out.println("[Debug]: client: " + this.client);

        try {

            // Get I/O 
            System.out.println("[Debug]: Opening input stream");
            this.serverIn = new Scanner(client.getInputStream());
            System.out.println("[Debug]: Input stream open");
            
            System.out.println("[Debug]: Opening output stream");
            this.serverOut = new PrintStream(client.getOutputStream());
            System.out.println("[Debug]: Output stream open");

        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public static String getIPAddress(boolean useIPv4) {
        
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }


    /*
    TODO: 

    see how to request enable wifi
    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    */
}
