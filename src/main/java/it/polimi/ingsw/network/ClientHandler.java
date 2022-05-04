package it.polimi.ingsw.network;

import it.polimi.ingsw.network.connectionState.Authentication;
import it.polimi.ingsw.network.connectionState.ConnectionState;
import it.polimi.ingsw.network.connectionState.LookingForLobby;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private boolean isConnected;
    private int idUser;
    private MessageBroker broker;
    private ConnectionState connectionState;

    /**
     * Creates a new client handler with its own message broker and set to state Authentication
     * @param socket the socket of the connection that's been created in the server
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.broker = new MessageBroker();
        this.connectionState = new Authentication();
    }

    @Override
    public void run(){
        InputStream clientInput;
        OutputStream clientOutput;

        idUser = LoginHandler.getNewUserId();
        try {
            clientInput = socket.getInputStream();
            clientOutput = socket.getOutputStream();
        } catch (IOException e) {
            System.err.println("Error obtaining streams");
            System.err.println(e.getMessage());
            quitGame(); // Maybe useless
            return;
        }
        while(true){ // Message listener loop

            if(!broker.receive(clientInput)){ // Received an invalid message
                continue;
            }
            CommandEnum command = CommandEnum.valueOf((String)broker.readField("command"));

            if(!connectionState.isAllowed(command)){ // Trashes a command given at the wrong time
                continue;
            }
            handleCommand(command); // runs the appropriate routine depending on the command received
            // Sends a reply to the client
            broker.send(clientOutput);

        }
        // This point should never be reached in normal circumstances
    }

    /**
     * Adds the reply fields in the server message in case of a successful operation (Message and status)
     */
    private void notifySuccessfulOperation(){
        broker.addToMessage("serverReplyMessage", "OK");
        broker.addToMessage("serverReplyStatus", 0);
    }

    /**
     * Adds the reply fields in the server message in case of a failed operation (Message and status)
     * @param errorMessage A verbose message describing the error
     */
    private void notifyError(String errorMessage){ // parametrize reply status as well
        broker.addToMessage("serverReplyMessage", "ERR");
        broker.addToMessage("serverReplyStatus", 1);
        broker.addToMessage("errorState", errorMessage);
    }

    /**
     * Finds the command that's been given and runs the appropriate methods
     */
    public void handleCommand(CommandEnum command){
        switch(command){
            case QUIT -> quitGame();
            case CONNECTION_REQUEST -> connectionRequest();
            case PLAY_GAME -> playGame();
            case READY_TO_START -> sendReady();
            case NOT_READY -> sendNotReady();
            case LEAVE_LOBBY -> requestLeaveLobby();
            //TODO rest of commands
        }
    }

    /**
     * The user requests to play a game with the given rules
     */
    private void playGame() {

    }

    /**
     * The user requests to leave the current lobby
     */
    private void requestLeaveLobby() {

    }

    /**
     * Sends a message to the server letting it know the user is not ready to start the game
     */
    private void sendNotReady() {
        // TODO
    }

    /**
     * Sends a message to the server letting it know the user is ready to start the game
     */
    private void sendReady() {
        // TODO
    }

    /**
     * Closes the connection for this user
     */
    public void quitGame(){
        //TODO
    }

    /**
     * Handles the connections of a new user, checking whether their nickname
     * satisfies the requirement of uniqueness
     */
    public void connectionRequest(){
        boolean loginSuccessful;
        loginSuccessful= LoginHandler.login((String)broker.readField("nickname"), idUser);
        if(!loginSuccessful){
            notifyError("Nickname already taken");
            quitGame();
        }
        else notifySuccessfulOperation();
        setConnectionState(new LookingForLobby());
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }
}
