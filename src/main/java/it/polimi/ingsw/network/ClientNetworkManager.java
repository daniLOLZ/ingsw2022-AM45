package it.polimi.ingsw.network;

import com.google.gson.internal.LinkedTreeMap;
import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Handles the connection of the client, is a part of the View
 * Before each method that starts to build a message via the message broker, particularly before the call
 * broker.addToMessage(NetworkFieldEnum.COMMAND, ... ) that method must acquire the lock brokerLock to avoid
 * accidental overwriting of message parts
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
    private ReentrantLock brokerLock; //This lock is necessary because various methods add to the outgoing message
    // in an asynchronous way, thus they might mix up their inputs or overwrite previous ones
    // this lock needs to be set before each new message is starting to be built
    // generally, that means before any method adds the NetworkEnum.COMMAND value to the message


    public ClientNetworkManager(String hostname, int portNumber) {
        this.hostname = hostname;
        this.mainPortNumber = portNumber;
        this.pingPortNumber = portNumber;
        this.mainBroker = new MessageBroker();
        this.pingBroker = new MessageBroker();
        this.progressiveIdRequest = 0;
        this.progressiveIdPingRequest = 0;
        this.connected = true; //the ping routine will start before this field has been checked
        this.brokerLock = new ReentrantLock();
    }

    /*

            METHODS THAT REFLECT THE INTERFACE

     */

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

        boolean successfulReply = false;

        GameRuleEnum rule;
        //Converts the input to a gamerule
        if(gamemode == 1){
            rule = GameRuleEnum.getBasicRule(numPlayers);
        }
        else rule = GameRuleEnum.getAdvancedRule(numPlayers);

        brokerLock.lock();
        try {
            mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.PLAY_GAME);
            mainBroker.addToMessage(NetworkFieldEnum.GAME_RULE, rule);

            if (!sendToServer()) return false;

            // Read other params if necessary
            successfulReply = checkSuccessfulReply();

            mainBroker.flushFirstMessage();
            return successfulReply;
        } finally {
            brokerLock.unlock();
        }

    }

    /**
     * Handles quitting the game on the client side
     */
    public void quitGame() {
        //todo
        //remember the lock
    }

    //todo handle this true/false better?
    /**
     * Updates the server on the readiness of the user
     * @return true if the game is about to start;
     * false if the game is still not able to start or if there has been an error
     */
    public boolean sendReadyStatus(boolean ready){

        boolean successfulReply;

        brokerLock.lock();
        try {
            if (ready) {
                mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.READY_TO_START);
            } else {
                mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.NOT_READY);
            }
            if (!sendToServer()) return false;

            //The server will reply with either a "GameIsStarting" or a "GameNotStarting"

            successfulReply = checkSuccessfulReply();
        /*
        if(!successfulReply &&
                mainBroker.readField(NetworkFieldEnum.ERROR_STATE).equals("GameNotStarting")) { // hardcoding
            //The game isn't starting because not all players are ready
        }
         */

            mainBroker.flushFirstMessage();
            return successfulReply;
        }finally {
            brokerLock.unlock();
        }

    }


    public LobbyBean getLobbyUpdates() {
        LobbyBean returnBean = null;
        brokerLock.lock();
        try {
            mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.GET_LOBBY_STATUS);
            if (!sendToServer()) return null;

            if (!checkSuccessfulReply()) {
                mainBroker.flushFirstMessage();
                return null;
            }

            returnBean = BeanTranslator.deserializeLobbyBean(
                    (LinkedTreeMap<String, Object>) mainBroker.readField(NetworkFieldEnum.BEAN));
            mainBroker.flushFirstMessage();
            return returnBean;
        } finally {
            brokerLock.unlock();
        }

    }

    /**
     * Tries to start the game from the lobby
     * @return true if the game successfully started
     */
    public boolean startGame(){
        boolean successfulReply;

        brokerLock.lock();
        try {
            mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.START_GAME);
            if (!sendToServer()) return false;

            successfulReply = checkSuccessfulReply();

            mainBroker.flushFirstMessage();
            return successfulReply;

        } finally {
            brokerLock.unlock();
        }
    }

    /**
     * Fetches the current information regarding tower and wizard selection
     * @return a GameInitBean containing the teams and wizards that HAVEN'T been chosen already
     */
    public GameInitBean getGameInitUpdates(){
        GameInitBean returnBean = null;
        brokerLock.lock();

        try {
            mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.GET_GAME_INITIALIZATION_STATUS);
            if (!sendToServer()) return null;

            if (!checkSuccessfulReply()) {
                mainBroker.flushFirstMessage();
                return null;
            }

            returnBean = BeanTranslator.deserializeGameInitBean(
                    (LinkedTreeMap<String, Object>) mainBroker.readField(NetworkFieldEnum.BEAN));
            mainBroker.flushFirstMessage();
            return returnBean;
        } finally {
            brokerLock.unlock();
        }
    }

    public boolean sendTeamColorChoice(TeamEnum colorChosen){
        boolean successfulReply;
        brokerLock.lock();
        try {
//            System.err.println("---got color lock");
            mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.SELECT_TOWER_COLOR);
            mainBroker.addToMessage(NetworkFieldEnum.ID_TOWER_COLOR, colorChosen.index);

            if (!sendToServer()) return false;

            successfulReply = checkSuccessfulReply();

            mainBroker.flushFirstMessage();
            return successfulReply;
        } finally {
            brokerLock.unlock();
//            System.err.println("---released color lock");
        }
    }

    public boolean sendWizardChoice(WizardEnum wizardChosen){
        boolean successfulReply;
        brokerLock.lock();
        try {
//            System.err.println("---got wizard lock");

            mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.SELECT_WIZARD);
            mainBroker.addToMessage(NetworkFieldEnum.ID_WIZARD, wizardChosen.index);

            if (!sendToServer()) return false;

            successfulReply = checkSuccessfulReply();

            mainBroker.flushFirstMessage();
            return successfulReply;
        } finally {
            brokerLock.unlock();
//            System.err.println("---released wizard lock");
        }
    }


    /*

            UTILITY METHODS

     */

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



    private void setHost(String host) {
        this.hostname = host;
    }

    private void setPort(Integer port) {
        this.mainPortNumber = port;
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
     * @return true if the message was received successfully
     */
    //This method could potentially block the client interface if the reply doesn't arrive
    // Synchronization is probably necessary if we want to avoid receiving wrong replies
    private boolean sendToServer() {

        int requestId = addIdRequest();
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
        mainBroker.receive(inStream);

        //Checking whether the reply is the correct one
        //This method will not return until the first message of the broker
        // is the one needed
        // todo very heavy busy wait, change it somehow
        Double gottenId;
        do {
            gottenId = (Double) mainBroker.readField(NetworkFieldEnum.ID_REQUEST);
        } while(requestId != gottenId.intValue());

        return true;
    }

    /**
     * Simply checks whether the server replied affirmatively
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

        brokerLock.lock();
        boolean successfulReply;

        try {
            mainBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.CONNECTION_REQUEST);
            mainBroker.addToMessage(NetworkFieldEnum.NICKNAME, nickname);

            if (!sendToServer()) return false;

            successfulReply = checkSuccessfulReply();
            //Get the other useful info from the message

            //mainBroker.unlock();
            mainBroker.flushFirstMessage();
            return successfulReply;
        } finally {
            brokerLock.unlock();
        }


    }


    /**
     * Adds the idRequest field to the current outgoing message
     * @return the id that's just been added
     */
    private int addIdRequest(){
        int idReq = increaseAndGetRequestId();
        mainBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, idReq);
        return idReq;
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

            while (!pingBroker.messagePresent()); //operation to execute with timeout

            return null; //no need for a return value
        });

        do{
            //send ping message
            while (!pingBroker.messagePresent());
            pingBroker.addToMessage(NetworkFieldEnum.ID_USER, idUser);
            pingBroker.addToMessage(NetworkFieldEnum.ID_PING_REQUEST, increaseAndGetPingRequestId());
            pingBroker.send(outStream);
            //pingBroker.unlock();
            pingBroker.flushFirstMessage();
            pingBroker.receive(inStream);

            //receive pong message
            try {
                handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                handler.cancel(true);
                connected = false;
                System.err.println("Connection timed out");
                //pingBroker.unlock();
                pingBroker.flushFirstMessage();
                break;
            } catch (InterruptedException | ExecutionException e) {
                handler.cancel(true);
                connected = false;
                e.printStackTrace();
                //pingBroker.unlock();
                pingBroker.flushFirstMessage();
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
            //pingBroker.unlock();
            pingBroker.flushFirstMessage();
            //Ping only every second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Ping: Interrupted, starting new ping request immediately");
            }
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
