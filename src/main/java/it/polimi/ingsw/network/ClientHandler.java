package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.connectionState.Authentication;
import it.polimi.ingsw.network.connectionState.ConnectionState;
import it.polimi.ingsw.network.connectionState.InLobby;
import it.polimi.ingsw.network.connectionState.LookingForLobby;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private boolean isConnected;
    private int idUser;
    private MessageBroker broker;
    private ConnectionState connectionState;
    private Lobby userLobby;

    /**
     * Creates a new client handler with its own message broker and set to state Authentication
     * @param socket the socket of the connection that's been created in the server
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.broker = new MessageBroker();
        this.connectionState = new Authentication();
        this.userLobby = null;
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
            CommandEnum command = CommandEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.COMMAND));

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
        broker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "OK");
        broker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 0);
        broker.addToMessage(NetworkFieldEnum.ID_REQUEST, broker.readField(NetworkFieldEnum.ID_REQUEST));
    }

    /**
     * Adds the reply fields in the server message in case of a failed operation (Message and status)
     * @param errorMessage A verbose message describing the error
     */
    private void notifyError(String errorMessage){ // parametrize reply status as well
        broker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "ERR");
        broker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 1);
        broker.addToMessage(NetworkFieldEnum.ERROR_STATE, errorMessage);
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
            case START_GAME -> startGame();
            case SELECT_WIZARD -> selectWizard();

            //TODO rest of commands
        }
    }

    private void selectWizard() {

    }

    /**
     * The users requests to start the game
     * The request will be successful only if the host coincides with the user and all players are ready
     */
    private void startGame() {
        if(this.idUser != userLobby.getHost()){
            notifyError("You're not the host! You can't start the game.");
        }
        else {
            ActiveLobbies.startGame(userLobby);
            notifySuccessfulOperation();
        }
    }

    /**
     * The user requests to play a game with the given rules
     */
    private void playGame() {

        GameRuleEnum rules = GameRuleEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.GAME_RULE));

        userLobby = ActiveLobbies.assignLobby(rules);
        userLobby.addPlayer(this.idUser);
        setConnectionState(new InLobby());
        notifySuccessfulOperation();
    }

    /**
     * The user requests to leave the current lobby
     */
    private void requestLeaveLobby() {
        userLobby.removePlayer(this.idUser);
        userLobby = null;
        setConnectionState(new LookingForLobby());
        notifySuccessfulOperation();
    }

    /**
     * Sends a message to the server letting it know the user is not ready to start the game
     */
    private void sendNotReady() {
        userLobby.removeReady(this.idUser);
        notifySuccessfulOperation(); // other checks to do?
    }

    /**
     * Sends a message to the server letting it know the user is ready to start the game
     */
    private void sendReady() {
        userLobby.addReady(this.idUser);
        notifySuccessfulOperation(); // other checks to do?
    }

    /**
     * Closes the connection for this user
     */
    public void quitGame(){
        //TODO later, when all cases have been accounted for
    }

    /**
     * Handles the connections of a new user, checking whether their nickname
     * satisfies the requirement of uniqueness
     */
    public void connectionRequest(){
        boolean loginSuccessful;
        loginSuccessful= LoginHandler.login((String)broker.readField(NetworkFieldEnum.NICKNAME), idUser);
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
