package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.commandHandler.asynchronous.AsyncCommandHandler;
import it.polimi.ingsw.network.commandHandler.synchronous.CommandHandler;
import it.polimi.ingsw.network.commandHandler.FactoryCommandHandler;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable{

    private final List<CommandHandler> commandHandlers = FactoryCommandHandler.getAllCommandHandlers();
    private final List<AsyncCommandHandler> asyncHandlers = FactoryCommandHandler.getAsyncCommandHandlers();

    private Socket mainSocket;
    private Socket pingSocket;
    private final MessageBroker mainBroker;
    private final MessageBroker pingBroker;
    private PongHandler pongHandler;
    private AtomicBoolean connected;
    private final ReentrantLock commandLock;
    private int asyncIdRequest;
    private boolean alreadyAlerted;

    private ClientHandlerParameters parameters;

    /**
     * Creates a new client handler with its own message broker and set to state Authentication
     * @param mainSocket the main socket of the connection that's been created in the server
     */
    public ClientHandler(Socket mainSocket) {
        this.mainSocket = mainSocket;
        this.mainBroker = new MessageBroker();
        this.pingBroker = new MessageBroker();
        this.parameters = new ClientHandlerParameters();
        this.connected = new AtomicBoolean(true);
        this.commandLock = new ReentrantLock();
        alreadyAlerted = false;
    }

    /**
     * Assigns the socket that will be used to perform the ping routine
     * @param pingSocket The socket that will be used to perform the ping routine
     */
    public void assignPingSocket(Socket pingSocket){
        this.pingSocket = pingSocket;
    }

    @Override
    public void run(){
        InputStream clientInput;
        OutputStream clientOutput;

        // Start the handler's main communication with the client
        parameters.setIdUser(LoginHandler.getNewUserId());
        try {
            clientInput = mainSocket.getInputStream();
            clientOutput = mainSocket.getOutputStream();
        } catch (IOException e) {
            connectionLostAlert("Error obtaining streams\n" + e.getMessage());
            return;
        }

        pongHandler = new PongHandler(this, pingBroker, pingSocket);

        // Starts the pong routine
        pongHandler.startPonging();

        //Starts a new thread which constantly receives new messages
        // from the client
        new Thread(()->{
            while (connected.get()){
                try{
                    mainBroker.receive(clientInput);
                }catch(IOException e){
                    connectionLostAlert("Error receiving commands from the socket");
                }
            }
        }).start();

        //As the rest of the connection, we trust that the login procedure will go well,
        // otherwise it will get undone once the login fails
        ActiveClients.setHandler(parameters.getIdUser(), this);

        while(connected.get()){ // Message listener loop

            try {
                while(!mainBroker.waitSyncMessage(2000)){
                    if(!connected.get()) return;
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for message");
                e.printStackTrace();
                continue;
            }

            System.out.println("---Starting to parse a message [idUser: " + parameters.getIdUser() + "]");
            CommandEnum command = CommandEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.COMMAND));

            System.out.println("---Message parsed : "+command.toString());

            forwardRequest();

            if(!parameters.getConnectionState().isAllowed(command)){ // Trashes a command given at the wrong time
                System.err.println("-+-Command not allowed");
                //We also need to send an error to the client, not leaving it hanging
                // duplicate code
                mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "ERR");
                mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 1);
                mainBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, mainBroker.readField(NetworkFieldEnum.ID_REQUEST));
                mainBroker.addToMessage(NetworkFieldEnum.ERROR_STATE, "The command couldn't be handled");
            }
            else {
                handleCommand(mainBroker); // runs the appropriate routine depending on the command received
                System.out.println("---Command handled");
            }
            // This line is necessary for the quit command, we stop before trying to send
            // a message which would result in failure anyways since the user disconnected
            if(!connected.get()) continue;

            commandLock.lock();
            try {
                // Sends a reply to the client
                mainBroker.send(clientOutput);

                mainBroker.flushFirstSyncMessage();

                //Here, we handle other asynchronous commands to all other players
                //todo if more than one command is sent per request, it could happen that two async
                // commands end up having the same async id
                tryAsyncCommandsForAll();

                clearAsyncConditions(); // here we clear the conditions for calling async methods
                // on this client handler only, whereas the actual sending of commands happens for
                // all of handlers

            }
            catch (IOException e){
                connectionLostAlert("Error while sending the reply to the client");
            }
            finally {
                commandLock.unlock();
            }
        }
        // This point should never be reached in normal circumstances (unless the client disconnects)
    }

    /**
     * Called after trying to send the async commands for all clients;
     * clears the condition that (eventually) made the commands trigger
     */
    private void clearAsyncConditions() {
        for(AsyncCommandHandler handler : asyncHandlers){
            handler.clearCondition(parameters);
        }
    }

    /**
     * Requests all handlers for all clients in this user's lobby (if there are any) to try and
     * send asynchronous commands relative to the current game state
     */
    private void tryAsyncCommandsForAll() {
        if(parameters.getUserLobby() == null){
            sendAsynchronousCommands();
            return;
        }
        // The user is in a lobby with others
        ClientHandler handler;

        for(Integer user : parameters.getUserLobby().getPlayers()){
            handler = ActiveClients.getHandlerFromId(user);
            handler.sendAsynchronousCommands();
        }
    }

    /**
     * This method copies the request and puts it back in the answer <br>
     * This is used to make the client's receiver know what kind of message was sent
     * and what action has been performed
     */
    private void forwardRequest() {
        for(NetworkFieldEnum field : NetworkFieldEnum.values()){
            Object readField = mainBroker.readField(field);
            if(readField != null){
                mainBroker.addToMessage(field, readField);
            }
        }
    }

    /**
     * Handle the exceptions thrown by losing connection.
     * This method is called by any class that has a way of intercepting a connection loss
     * Set error state in order to show the error to the other players in future view.
     * Closes connection.
     */
    public void connectionLostAlert(String error){
        if(alreadyAlerted) return;
        alreadyAlerted = true;

        if(parameters.getUserLobby() != null){
            parameters.getUserLobby().removePlayer(parameters.getIdUser());
            for(Integer idUser : parameters.getUserLobby().getPlayers()){
                if(idUser != parameters.getIdUser()){
                    //We prematurely send async commands to make sure the players know someone disconnected right away
                    ActiveClients.getHandlerFromId(idUser).sendAsynchronousCommands();
                }
            }
        }
        if(parameters.getUserController() != null) {
            //Save the players that will need to be notified after the lobby is cleared
            List<Integer> playersToSignal = new ArrayList<>(parameters.getUserLobby().getPlayers());

            //Clear the lobby
            parameters.getUserLobby().destroyLobby(true);

            //Handle the controller
            parameters.getUserController().setError("Connection lost with " + LoginHandler.getNicknameFromId(parameters.getIdUser()));
            parameters.getUserController().lostConnectionHandle(parameters.getIdUser());
            for(Integer idUser : playersToSignal){
                if(idUser != parameters.getIdUser()){
                    //We prematurely send async commands to make sure the players know someone disconnected right away
                    ActiveClients.getHandlerFromId(idUser).sendAsynchronousCommands();
                }
            }
        }
        System.err.println(error);
        ActiveClients.removeUserIdToClientHandlerAssociation(parameters.getIdUser());
        LoginHandler.removeNicknameFromId(parameters.getIdUser());
        closeConnection();
    }

    //below methods moved to CommandHandler

    /**
     * Closes the current connection
     * Sets isConnected to false.
     */
    private void closeConnection() {
        connected.set(false);
        try{
            mainSocket.close();
            pingSocket.close();
            System.out.println("[ user " + parameters.getIdUser() + " ] Connection closed");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Finds the command that's been given and runs the appropriate methods
     * @param messageBroker The broker containing the message with the command to handle
     */
    public void handleCommand(MessageBroker messageBroker){

        boolean successfulOperation = false;
        for (CommandHandler commandHandler:
             commandHandlers) {
            try {
                successfulOperation = commandHandler.executeCommand(messageBroker, parameters);
            }
            catch (UnexecutableCommandException e){
                continue;
            }
            break;
        }

        if (!successfulOperation){
            System.err.println("- Error: No command could be executed");
            messageBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "ERR");
            messageBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 1);
            messageBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, messageBroker.readField(NetworkFieldEnum.ID_REQUEST));
        } //TODO close connection?
    }

    /**
     * Each handler will see if it's appropriate to send the respective command, based on what happened
     * in the sync commands before
     * This method is called only after a synchronous command has been handled, which is correct as
     * the server can't send messages of its own volition
     */
    public void sendAsynchronousCommands(){

        OutputStream outStream = null;
        try {
            outStream = mainSocket.getOutputStream();
        } catch (IOException e) {
            connectionLostAlert("Couldn't get the client's output stream");
            return;
        }
        for(AsyncCommandHandler asyncCommandHandler : asyncHandlers){
            if(asyncCommandHandler.executeCommand(mainBroker, parameters)){
                mainBroker.addToMessage(NetworkFieldEnum.ASYNC_ID_REQUEST, ActiveClients.increaseAndGetAsyncIdRequest());
                try {
                    mainBroker.send(outStream);
                    mainBroker.flushFirstAsyncMessage();
                    System.out.println("-++Async message sent [idUser: "+parameters.getIdUser()+"]");
                } catch (IOException e) {
                    connectionLostAlert("Couldn't send the asynchronous message");
                }
            }
        }
    }

    public boolean isConnected() {
        return connected.get();
    }

    public Socket getMainSocket(){
        return mainSocket;
    }

}
