package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.view.UserInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Handles the connection of the client, is a part of the View
 */
public class ClientNetworkManager {

    private static final int DEFAULT_PORT_NUMBER = 54321;
    private static final String DEFAULT_HOSTNAME = "127.0.0.1";
    private final Duration timeout = Duration.ofSeconds(5);

    private int progressiveIdRequest, progressiveIdPingRequest;
    private Socket mainSocket, pingSocket;
    private String hostname;
    private int mainPortNumber, pingPortNumber;
    private MessageBroker mainBroker, pingBroker;
    private boolean connected;
    private int idUser;  // May be removed

    public ClientNetworkManager(String hostname, int portNumber) {
        this.hostname = hostname;
        this.mainPortNumber = portNumber;
        this.pingPortNumber = portNumber;
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


    /**
     * Handles quitting the game on the client side
     */
    private void quitGame() {
        //todo
    }

    private void setHost(String host) {
        this.hostname = host;
    }

    private void setPort(Integer port) {
        this.mainPortNumber = port;
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

        //start the user's ping routine in a new thread
        new Thread(this::ping).start();

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
     * Sends the gamerule preferences to the server
     * @param gamemode the gamemode chosen, "1" for Simple, "2" for Advanced
     * @param numPlayers the number of players chosen
     * @return true if the call to the server succeeded and a successful reply was received
     */
    public boolean sendGameModePreference(int gamemode, int numPlayers){

        GameRuleEnum rule;
        //Converts the input to a gamerule
        if(gamemode == 1){
            rule = GameRuleEnum.getBasicRule(numPlayers);
        }
        else rule = GameRuleEnum.getAdvancedRule(numPlayers);

        mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.PLAY_GAME);
        mainBroker.addToMessage(NetworkFieldEnum.GAME_RULE, rule);

        if(!sendToServer()) return false;

        return checkSuccessfulReply();

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
     * This method gets the streams of the server, adds the request ID
     * and tries to send the message that was previously constructed in the appropriate
     * methods
     * It then receives the reply from the server
     * @throws IOException
     */
    //This method could potentially block the client interface if the reply doesn't arrive
    private boolean sendToServer() {
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
        return true;
    }

    /**
     *
     * @return true if the message received from the server replied successfully
     * to the previously made request
     */
    private boolean checkSuccessfulReply(){
        return "OK".equals(
                (String) mainBroker.readField(NetworkFieldEnum.SERVER_REPLY_MESSAGE));
    }

    /**
     * Sends the chosen nickname to the server to evaluate whether it's acceptable or not
     * @param nickname the nickname chosen by the user
     * @return a boolean : true if the nickname was accepted
     */
    private boolean sendNickname(String nickname){

        mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.CONNECTION_REQUEST);
        mainBroker.addToMessage(NetworkFieldEnum.NICKNAME, nickname);

        if (!sendToServer()) return false;

        return checkSuccessfulReply();

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
    //TODO implement this check in the message receiving part
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
