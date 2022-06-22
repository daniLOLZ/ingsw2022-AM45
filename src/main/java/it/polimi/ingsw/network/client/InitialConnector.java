package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ConnectionFailedException;
import it.polimi.ingsw.network.MessageBroker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class InitialConnector {
    private ClientSender sender;
    private ClientReceiver receiver;
    private String hostname;
    private int portNumber, pingPortNumber;
    private Socket mainSocket, pingSocket;
    private MessageBroker mainBroker, pingBroker;
    private PingHandler pingHandler;
    private Thread pingThread;
    private AtomicBoolean alreadyNotified;

    private AtomicBoolean connected;
    private AtomicBoolean isCommandScheduled; //Related to the main broker


    public InitialConnector(String hostname, int portNumber){
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.pingPortNumber = portNumber;
        this.mainBroker = new MessageBroker();
        this.pingBroker = new MessageBroker();
        this.connected = new AtomicBoolean(true); //the ping routine will start before this field has been checked
        this.isCommandScheduled = new AtomicBoolean(false);
        alreadyNotified = new AtomicBoolean(false);
        pingThread = null;
    }

    /**
     * After a network error, this method resets the network components of the client to get
     * ready for a new connection
     */
    public void reset() {

        // It's necessary to interrupt the ping when restarting the network, otherwise it will immediately
        // set the newly connected boolean to false
        if(pingThread != null){
            pingThread.interrupt();
            try {
                pingThread.join();
            } catch (InterruptedException e) {
                System.err.println("This is quite a conundrum");
                e.printStackTrace();
            }
        }

        //Reset receiver
        receiver.reset();
        //Reset sender
        sender.reset();

        this.mainBroker = new MessageBroker();
        this.pingBroker = new MessageBroker();

        this.connected = new AtomicBoolean(true);
        this.isCommandScheduled = new AtomicBoolean(false);
        alreadyNotified.set(false);

    }

    /**
     * Connects to a Server with the already present hostname and port,
     * and sends the user nickname via private methods
     * @param nickname a nickname chosen by the user to be used during the game
     */
    public boolean login(String nickname){

        InputStream inputStream;
        OutputStream outputStream;

        try {
            mainSocket = connect(hostname,portNumber);
            pingSocket = connect(hostname,portNumber);
        } catch (ConnectionFailedException e){
            notifyNetworkError(e.getErrorMessage());
            return false;
        }

        try{
            inputStream = mainSocket.getInputStream();
            outputStream = mainSocket.getOutputStream();
        } catch (IOException e){
            notifyNetworkError("Couldn't get the I/O streams");
            return false;
        }

        this.pingHandler = new PingHandler(this, pingBroker, pingSocket);

        //Start the sender
        sender.initialize(outputStream, mainBroker, connected, isCommandScheduled);

        //Start the receiver
        receiver.initialize(inputStream, mainBroker, connected, isCommandScheduled);

        sender.sendNickname(nickname);

        return true;
    }

    /**
     * Starts the routine to parse incoming (synch) messages
     */
    public void startReceiving(){
        receiver.parseMessages();
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
     * This will be called by either the sender, the receiver or the initial connector itself
     * and will cause this class to notify both of them to let
     * them know the connection was lost in an unexpected manner <br>
     * It closes all open sockets
     */
    public void notifyNetworkError(String error){
        if(alreadyNotified.get()) return;
        alreadyNotified.set(true);
        System.err.println(error);
        connected.set(false);
        try{
            mainSocket.close();
            pingSocket.close();
            System.err.println("Sockets closed");
        }
        catch (IOException | NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Called by the sender, this method notifies all parts of the client application
     * that the user wants to quit. <br>
     * It closes all connections and quits the application
     */
    public void notifyApplicationQuit() {
        connected.set(false);
        try{
            mainSocket.close();
            pingSocket.close();
            System.err.println("Sockets closed");
        }
        catch (IOException | NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Actions to take once the connection is established
     * - Starts the ping routine
     * - Sets the user id for all the network components that require it
     */
    public void loginSuccessful(int idUser) {

        sender.assignIdUser(idUser);
        pingHandler.assignIdUser(idUser);
        this.pingThread = pingHandler.startPinging();
        //start the user's ping routine in a new thread, this will send messages
        // through the sender and receive them through the receiver as normal

    }

    public void setSender(ClientSender sender) {
        this.sender = sender;
    }

    public void setReceiver(ClientReceiver receiver) {
        this.receiver = receiver;
    }

    public boolean isConnected(){
        return connected.get();
    }

    public AtomicBoolean getConnected(){
        return this.connected;
    }

}
