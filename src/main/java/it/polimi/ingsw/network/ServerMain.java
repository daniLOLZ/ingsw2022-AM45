package it.polimi.ingsw.network;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Arrays;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerMain {
    private static int portNumber; // java doesn't support unsigned int
    private static ServerSocket serverSocket;

    //Todo move to another class?
    public static final List<String> allowedParameters = Arrays.asList("--port");
    public static final List<Boolean> parameterRequiresInput = Arrays.asList(true);


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

    /**
     * Reads the arguments from the command line
     * @param arguments the arguments read
     */
    private static void readParameters(String[] arguments) {

        //Invalid parameters are ignored

        int argLength = arguments.length;
        String readArgument;
        for(int argumentIndex = 0; argumentIndex < argLength; argumentIndex++){
            if(!allowedParameters.contains(arguments[argumentIndex])){
                continue;
            }
            if(parameterRequiresInput.get(argumentIndex) && argumentIndex+1 >= argLength) { // There is no actual value
                                                                                            // after a paramter that requires it
                continue;
            }

            readArgument = arguments[argumentIndex];

            //todo find a way to factor this for both client and server
            //Starts actually parsing the parameters, akin to a switch

            if (readArgument.equals(allowedParameters.get(0))){ // --port
                Integer port = Integer.parseInt(arguments[argumentIndex+1]);
                setPort(port);
            }

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
