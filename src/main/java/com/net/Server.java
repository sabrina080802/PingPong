package net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket server;
    private Socket client;
    private Scanner in;
    private PrintWriter out;


    public Server(int port) throws IOException {
        server = new ServerSocket(port);
    }
    public void listen() throws IOException{
        if(client != null){
            client.close();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run(){
                try {
                    readData();
                } catch (Exception e) { }
            }
        };
    }
    private void readData() throws Exception{
        while(true){
            Thread.sleep(32);
        }
    }
}
