package it.polimi.ingsw.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerMain {
    private int portNumber; // java doesn't support unsigned int
    private ServerSocket serverSocket;

    public ServerMain(int port){
        this.portNumber = port;
    }

    public static void main(String[] args){
        //TODO network parameters should be read from parameters or from json as a default
        ServerMain eriantysServer = new ServerMain(54321);
        eriantysServer.startServer();
    }

    /**
     * Starts a server accepting multiple clients
     */
    public void startServer(){
        ExecutorService executor = Executors.newCachedThreadPool();
        System.out.println("Server starting...");
        try{
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e){
            // Optional closing operations here
            System.out.println("Error: Couldn't open the server socket");
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Server started");
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("New connection from " + socket.getInetAddress());
                executor.submit(new ClientHandler(socket));
            } catch (IOException e){
                System.out.println(e.getMessage());
                break;
            }
        }
        executor.shutdown();
    }
}
