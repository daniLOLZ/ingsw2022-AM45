package it.polimi.ingsw.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    //TODO Make this a singleton class
    private int portNumber; // java doesn't support unsigned int
    private ServerSocket serverSocket;
    private List<ClientHandler> pinglessClients = new ArrayList<>(); //contains all clients not having a ping socket

    /**
     * Creates a new Eriantys Server
     * @param port The port on which the server starts listening for clients
     */
    public Server(int port){
        this.portNumber = port;
        pinglessClients = new ArrayList<>();
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

        ClientHandler waitingClient = null;

        while(true){
            try{
                Socket socket = serverSocket.accept();

                for (ClientHandler clientHandler:
                     pinglessClients) {
                    if (clientHandler.getMainSocket().getInetAddress().equals(socket.getInetAddress())) {
                        waitingClient = clientHandler; //if the client connecting is in the list, we assume he's
                                                       //asking to open a ping socket
                    }
                }

                if (waitingClient != null){ //client is connecting again to open the ping socket
                    waitingClient.assignPingSocket(socket);
                    pinglessClients.remove(waitingClient);
                    System.out.println("Starting Handler of" + waitingClient.getMainSocket().getInetAddress());
                    executor.submit(waitingClient);
                    // Resets the waiting client
                    waitingClient = null;
                }
                else {
                    System.out.println("New connection from " + socket.getInetAddress());
                    pinglessClients.add(new ClientHandler(socket)); //the clientHandler is kept in the pinglessClients list
                                                                    //waiting for the Client to establish another connection
                                                                    //for the ping routine
                }


            } catch (IOException e){
                System.out.println(e.getMessage());
                break;
            }
        }
        executor.shutdown();
    }



}
