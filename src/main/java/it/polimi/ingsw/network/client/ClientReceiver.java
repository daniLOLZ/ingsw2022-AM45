package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ApplicationHelper;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.view.UserInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class receives messages from the server, whether synchronous (initiated by the client in the
 * Sender) or asynchronous (receieved from the server as a result of another user's command)
 * This class doesn't deal with the ping routine
 */
//TODO: implemente failure and closing of receiver and sender
public class ClientReceiver {

    private InputStream inputStream;
    private boolean receiverStarted, parseAsyncStarted;
    private MessageBroker mainBroker;
    private AtomicBoolean connected;
    private boolean hasBeenClosedAlready;

    private AtomicBoolean isCommandScheduled;
    private final InitialConnector initialConnector; //Used only to communicate network error to the sender
    private ClientController clientController;

    public ClientReceiver(InitialConnector initialConnector){
        receiverStarted = false;
        hasBeenClosedAlready = false;
        this.initialConnector = initialConnector;
        this.clientController = new ClientController();
    }

    public void initialize(InputStream inputStream, MessageBroker mainBroker, AtomicBoolean connected, AtomicBoolean isCommandScheduled) {
        this.inputStream = inputStream;
        this.mainBroker = mainBroker;
        clientController.setBroker(mainBroker);
        this.connected = connected;
        this.isCommandScheduled = isCommandScheduled;
        receiveMessages();
        parseAsyncMessages();
    }

    public void reset() {
        receiverStarted = false;
        hasBeenClosedAlready = false;
    }

    /**
     * Starts the listening procedure on a new thread and returns
     */
    public void receiveMessages() {

        if (receiverStarted) return;

        new Thread(() -> {
            while (connected.get()) {
                try {
                    mainBroker.receive(inputStream);
                } catch (IOException e) {
                    closeConnection();
                }
            }
        }).start();

        receiverStarted = true;
    }

    //todo a method to check and compare id requests between the received one and the
    // one that we know we're supposed to receive

    public void parseMessages(){

        while(connected.get()) {
            try {
                mainBroker.waitSyncMessage();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for async message, continuing...");
                continue;
            }

            //This locking/unlocking across different classes might lead to some errors, be careful
            // are we sure it's the same thread that's requesting and releasing the lock?
            // After the first command, theoretically yes
            isCommandScheduled.set(false);

            switch (CommandEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.COMMAND))) {
                //Synchronous commands
                case CONNECTION_REQUEST -> openConnection();
                case PLAY_GAME -> clientController.validateLobbyJoin();
                case READY_TO_START -> clientController.validateReadyToStart();
                case NOT_READY -> clientController.validateNotReady();
                case START_GAME -> clientController.validateStartGame();
                case LEAVE_LOBBY -> clientController.validateLobbyLeave();
                case SELECT_TOWER_COLOR -> clientController.validateSelectTeamColor();
                case SELECT_WIZARD -> clientController.validateSelectWizard();
                case CHOOSE_ASSISTANT -> clientController.validateChooseAssistant();
                case SELECT_STUDENT -> clientController.validateSelectStudent();
                case PUT_IN_HALL -> clientController.validatePutInHall();
                case PUT_IN_ISLAND -> clientController.validatePutInIsland();
                case DESELECT_STUDENT -> clientController.validateDeselectStudent();
                case MOVE_MN -> clientController.validateMoveMN();
                case CHOOSE_CLOUD -> clientController.validateChooseCloud();
                case END_TURN -> clientController.validateEndTurn();
                case SELECT_CHARACTER -> clientController.validateSelectCharacter();
                case SELECT_STUDENT_COLOR -> clientController.validateSelectStudentColor();
                case SELECT_ENTRANCE_STUDENTS -> clientController.validateSelectEntranceStudents();
                case SELECT_ISLAND_GROUP -> clientController.validateSelectIslandGroup();
                case SELECT_STUDENT_ON_CARD -> clientController.validateSelectStudentOnCard();
                case PLAY_CHARACTER -> clientController.validatePlayCharacter();
                case QUIT -> closeConnection(); //todo make the response to QUIT its own method, right now it's the same as if a network error occurred
                default -> closeConnection();
            }
            mainBroker.flushFirstSyncMessage();
        }

    }

    public void parseAsyncMessages(){
        if(parseAsyncStarted) return;

        new Thread( () -> {
            while (connected.get()) {
                try {
                    mainBroker.waitAsyncMessage();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for async message, continuing...");
                }

                switch (CommandEnum.fromObjectToEnum(mainBroker.readAsyncField(NetworkFieldEnum.COMMAND))) {
                    case SERVER_LOBBY_STATUS ->                     clientController.handleLobbyUpdate();
                    case SERVER_LOBBY_START ->                      clientController.handleLobbyStart();
                    case SERVER_GAME_INITIALIZATION_STATUS ->       clientController.handleGameInitUpdate();
                    case SERVER_GAME_START ->                       clientController.handleGameStart();
                    case SERVER_YOUR_TURN ->                        clientController.handleTurnUpdate();
                    case SERVER_GAME_UPDATE ->                      clientController.handleGameUpdate();
                    case SERVER_GAME_WON ->                         clientController.handleGameWon();
                    case SERVER_USER_DISCONNECTED ->                {
                        clientController.handleUserDisconnection();
                        closeConnection();
                    }
                    default -> closeConnection();
                }
                if(connected.get()){
                    sendAsynchronousACK();
                }
                mainBroker.flushFirstAsyncMessage();
            }

        }).start();

        parseAsyncStarted = true;
    }

    /**
     * After receiving an asynchronous message from the server, sends an ack message back to the server
     */
    private void sendAsynchronousACK() {
        //Todo
    }

    /**
     * The networking part of this command has been moved to the receiver, the client controller will
     * only handle the visual result of the failed connection
     */
    private void openConnection(){
        if(!"OK".equals(
                (String) mainBroker.readField(NetworkFieldEnum.SERVER_REPLY_MESSAGE))){
            closeConnection();
        }
        else{
            //Sends the received user id to all the network components that require it
            int idUser = ApplicationHelper.getIntFromBrokerField(mainBroker.readField(NetworkFieldEnum.ID_USER));
            initialConnector.loginSuccessful(idUser);
        }
        clientController.validateLogin();
    }

    /**
     * Notifies the initial connector (and in turn the sender) that a problem occurred
     * then, it calls the appropriate ui screen
     */
    private synchronized void closeConnection() {
        if(hasBeenClosedAlready) return;
        initialConnector.notifyNetworkError("The server closed the connection");
        clientController.connectionClose();
        connected.set(false);
        hasBeenClosedAlready = true;
    }


    public void setUI(UserInterface ui){
        this.clientController.setUI(ui);
    }


}
