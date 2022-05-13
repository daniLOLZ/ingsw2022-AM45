package it.polimi.ingsw.network;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Arrays;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerMain {
    private static int portNumber; // java doesn't support unsigned int
    private static ServerSocket serverSocket;
    private static List<ClientHandler> pinglessClients;

    public ServerMain(int port){
        this.portNumber = port;
        pinglessClients = new ArrayList<>();
    }
    //Todo move to another class?
    private static final List<String> allowedParameters = Arrays.asList("--port");

    public static void main(String[] args){
        //TODO network parameters should be read from parameters or from json as a default

        assignDefaultNetworkParameters();
        readParameters(args);

        startServer();
    }

    private static void assignDefaultNetworkParameters() {
        portNumber = Preferences.readPortFromJson();
        //Other preferences here;
    }

    private static void readParameters(String[] arguments) {
        int argLength = arguments.length;
        for(int argumentIndex = 0; argumentIndex < argLength; argumentIndex+=2){
            if(!allowedParameters.contains(arguments[argumentIndex])){
                return;
            }
            if(argumentIndex+1 >= argLength) { // There is no actual value afterwards
                return;
            }

            //Starts actually parsing the parameters, akin to a switch
            if(arguments[argumentIndex].equals(allowedParameters.get(0))){
                Integer port = Integer.parseInt(arguments[argumentIndex+1]);
                setPort(port);
            }
            //Other parameters here, if needed
            /*
            else if(){

            };
            */
        }
    }

    private static void setPort(Integer port) {
        portNumber = port;
    }

    /**
     * Starts a server accepting multiple clients
     */
    private static void startServer(){
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
                        waitingClient = clientHandler;
                    }
                }

                if (waitingClient != null){
                    waitingClient.assignPingSocket(socket);
                    pinglessClients.remove(waitingClient);
                    System.out.println("Starting Handler of" + waitingClient.getMainSocket().getInetAddress());
                    executor.submit(waitingClient);
                }
                else {
                    System.out.println("New connection from " + socket.getInetAddress());
                    pinglessClients.add(new ClientHandler(socket));
                }


            } catch (IOException e){
                System.out.println(e.getMessage());
                break;
            }
        }
        executor.shutdown();
    }
}
