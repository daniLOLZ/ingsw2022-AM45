package it.polimi.ingsw.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.*;

public class ClientMain {

    private static final int DEFAULT_PING_PORT_NUMBER = 54322;
    private final Duration timeout = Duration.ofSeconds(5);

    private int progressiveIdRequest, progressiveIdPingRequest;
    private Socket mainSocket, pingSocket;
    private String hostname;
    private int mainPortNumber, pingPortNumber;
    private String nickname;
    private MessageBroker mainBroker, pingBroker;
    private boolean connected;
    private int idUser;  // May be removed

    public ClientMain(String hostname, int portNumber, String nickname) {
        this.hostname = hostname;
        this.mainPortNumber = portNumber;
        this.pingPortNumber = DEFAULT_PING_PORT_NUMBER;
        this.nickname = nickname;
        this.mainBroker = new MessageBroker();
        this.pingBroker = new MessageBroker();
        this.progressiveIdRequest = 0;
        this.progressiveIdPingRequest = 0;
        this.connected = true; //the ping routine will start before this field has been checked
    }

    /**
     * Increases idRequest and returns it
     * @return The increased idRequest
     */
    private int increaseAndGetRequestId(){

        progressiveIdRequest++;
        return progressiveIdRequest;
    }

    /**
     * Increases idPingRequest and returns it
     * @return The increased idPingRequest
     */
    private int increaseAndGetPingRequestId(){

        progressiveIdPingRequest++;
        return progressiveIdPingRequest;
    }

    public static void main(String[] args){
        ClientMain client = new ClientMain("127.0.0.1", 54321, "username"); //TODO remove hardcoded network parameters

        Scanner scanner = new Scanner(System.in);

        System.out.println("Insert your username");
        String inputNickname = scanner.nextLine();
        client.setNickname(inputNickname);

        if(!client.login(client.getHostname(), client.getPortNumber(), client.getNickname())){
            System.err.println("Error logging in");
            return;
        }
        System.out.println("Username " + client.nickname + " was accepted");

        new Thread(client::ping).start();

        String userInput;
        while(client.isConnected()){ // generic game loop
            userInput = scanner.nextLine();
        }
    }

    /**
     * Connects to a Server and sends the user nickname via private methods
     * @param hostname the host to connect to
     * @param port the host's port to connect to
     * @param nickname a nickname chosen by the user to be used during the game
     */
    public boolean login(String hostname, int port, String nickname){

        try {
            mainSocket = connect(hostname,port);
        } catch (ConnectionFailedException e){
            e.printErrorMessage();
            return false;
        }

        if(!sendNickname(nickname)) {
            System.err.println("Nickname rejected");
            try {
                mainSocket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } finally {
                System.err.println("Connection closed");
            }
            return false;
        }

        return true;
    }

    /**
     * Establishes a connection to a Server
     * @param hostname the host ip address
     * @param port the host's port to connect to
     * @return The client's side socket of the established connection if successful
     * @exception ConnectionFailedException Contains a message error if the connection was unsuccessful
     */
    private Socket connect(String hostname, int port) throws ConnectionFailedException{

        Socket returnableSocket;

        try {
            returnableSocket = new Socket(hostname, port);
        }
        catch (UnknownHostException e){
            throw new ConnectionFailedException("Can't find host " + hostname);
        }
        catch (IOException e){
            throw new ConnectionFailedException("Couldn't get I/O for the connection to " + hostname);
        }
        return returnableSocket;
    }

    /**
     * Sends the chosen nickname to the server to evaluate whether it's acceptable or not
     * @param nickname the nickname chosen by the user
     * @return a boolean : true if the nickname was accepted
     */
    private boolean sendNickname(String nickname){

        mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.CONNECTION_REQUEST);
        mainBroker.addToMessage(NetworkFieldEnum.NICKNAME, nickname);
        addIdRequest();
        OutputStream outStream;
        InputStream inStream;
        try {
            outStream = mainSocket.getOutputStream();
            inStream = mainSocket.getInputStream();
        } catch (IOException e) {
            System.err.println("Couldn't get input/output streams");
            e.printStackTrace();
            return false;
        }
        mainBroker.send(outStream);
        System.out.println("Sent message to the server");
        mainBroker.receive(inStream);
        System.out.println("Received reply from the server");

        return "OK".equals(
                (String) mainBroker.readField(NetworkFieldEnum.SERVER_REPLY_MESSAGE));

    }

    /**
     * Adds the idRequest field to the current outgoing message
     */
    private void addIdRequest(){
        mainBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, increaseAndGetRequestId());
    }

    /**
     * Compares the idRequest of the Server reply and returns true if it matches with the current progressiveIdRequest
     * @return true if the Server reply has the same idRequest as the last request that has been sent
     */
    private boolean checkIdRequest(){
        return progressiveIdRequest == (int) mainBroker.readField(NetworkFieldEnum.ID_REQUEST);
    }

    /**
     * Continuously sends ping messages to the server and sets connected field to false when connection is no longer stable
     */
    private void ping(){

        OutputStream outStream;
        InputStream inStream;

        try {
            pingSocket = connect(hostname, pingPortNumber); //open connection
        } catch (ConnectionFailedException e) {
            e.printErrorMessage();
            return;
        }

        try {
            inStream = pingSocket.getInputStream();
            outStream = pingSocket.getOutputStream();
        } catch (IOException e){
            System.err.println("Couldn't get input/output streams");
            e.printStackTrace();
            connected = false;
            return;
        }

        ExecutorService pingExecutor = Executors.newSingleThreadExecutor();

        final Future<Void> handler = pingExecutor.submit(() -> {

            while (!pingBroker.lock()); //operation to execute with timeout

            return null; //no need for a return value
        });

        do{
            //send ping message
            while (!pingBroker.lock());
            pingBroker.addToMessage(NetworkFieldEnum.ID_USER, idUser);
            pingBroker.addToMessage(NetworkFieldEnum.ID_PING_REQUEST, increaseAndGetPingRequestId());
            pingBroker.send(outStream);
            pingBroker.unlock();
            pingBroker.receive(inStream);

            //receive pong message
            try {
                handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                handler.cancel(true);
                connected = false;
                System.err.println("Connection timed out");
                pingBroker.unlock();
                break;
            } catch (InterruptedException | ExecutionException e) {
                handler.cancel(true);
                connected = false;
                e.printStackTrace();
                pingBroker.unlock();
                break;
            }
            pingExecutor.shutdownNow();

            //maybe encapsulate operations below

            int receivedIdUser = (int) pingBroker.readField(NetworkFieldEnum.ID_USER);
            int receivedIdPingRequest = (int) pingBroker.readField(NetworkFieldEnum.ID_PING_REQUEST);

            if ( !(receivedIdUser  == idUser)){

                System.err.println("Server Error: identification failed");
                connected = false;
            }

            else if ( !( receivedIdPingRequest == progressiveIdPingRequest)){

                System.err.println("Wrong Request Id. Expected: " + progressiveIdPingRequest + ". Received: " + receivedIdPingRequest);
                connected = false;
            }
            pingBroker.unlock();
        } while (connected);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPortNumber() {
        return mainPortNumber;
    }

    public boolean isConnected() {
        return connected;
    }
}
