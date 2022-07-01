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
public class ClientReceiver {

    private InputStream inputStream;
    private boolean receiverStarted;
    private boolean parseAsyncStarted;
    private boolean hasBeenClosedAlready;
    private MessageBroker mainBroker;
    private AtomicBoolean connected;
    private Thread parseAsyncThread;
    private Thread receiveMessagesThread;

    private AtomicBoolean isCommandScheduled;
    private final InitialConnector initialConnector; //Used only to communicate network error to the sender
    private ClientController clientController;

    public ClientReceiver(InitialConnector initialConnector){
        receiverStarted = false;
        hasBeenClosedAlready = false;
        this.initialConnector = initialConnector;
        this.clientController = new ClientController();
    }

    /**
     * Initializes the receiver of messages
     * @param inputStream the stream from which to read
     * @param mainBroker the message broker that will hold the messages that this class should receive
     * @param connected a boolean shared with the initialConnector and the sender
     *                 to notify each other of a network problem
     * @param isCommandScheduled a lock-like atomic boolean used to avoid congesting the queue,
     *                           also accessed by the sender
     */
    public void initialize(InputStream inputStream, MessageBroker mainBroker, AtomicBoolean connected, AtomicBoolean isCommandScheduled) {
        this.inputStream = inputStream;
        this.mainBroker = mainBroker;
        clientController.setBroker(mainBroker);
        this.connected = connected;
        this.isCommandScheduled = isCommandScheduled;
        Thread receiveMessages = receiveMessages();
        Thread parseAsync = parseAsyncMessages();
        if(receiveMessages != null){
            this.receiveMessagesThread = receiveMessages;
        }
        if(parseAsync != null){
            this.parseAsyncThread = parseAsync;
        }
    }

    /**
     * Resets the flags for the receiver and puts it in a state
     * ready to start a new connection with the server
     * It closes the previously opened threads
     */
    public void reset() {
        receiverStarted = false;
        hasBeenClosedAlready = false;
        parseAsyncStarted = false;
        if(parseAsyncThread != null) {
            parseAsyncThread.interrupt();
            try {
                parseAsyncThread.join();
            } catch (InterruptedException e) {
                //System.err.println("This is quite a conundrum 2"); //should never get here
                //e.printStackTrace();
            }
        }
        if(receiveMessagesThread != null) {
            receiveMessagesThread.interrupt();
            try {
                receiveMessagesThread.join();
            } catch (InterruptedException e) {
                //System.err.println("This is quite a conundrum 2"); //should never get here
                //e.printStackTrace();
            }
        }
    }

    /**
     * Starts the listening procedure on a new thread and returns
     */
    public Thread receiveMessages() {

        if (receiverStarted) return null;

        Thread receiveMessages = new Thread(new Runnable() {
            public void run() {
                while (connected.get()) {
                    try {
                        mainBroker.receive(inputStream);
                    } catch (IOException e) {
                        closeConnection();
                    }
                }
            }
        });
        receiveMessages.setName("receiveMessages");
        receiveMessages.start();

        receiverStarted = true;
        return receiveMessages;
    }

    /**
     * The main routine.
     * After initialization, this method will continously wait for messages
     * from the server, in response to ones sent by the sender.
     * After receiving one, it will evaluate the command received and call the appropriate
     * ClientController method
     */
    public void parseMessages(){

        while(connected.get()) {
            try {
                mainBroker.waitSyncMessage();
            } catch (InterruptedException e) {
                //System.err.println("Interrupted while waiting for sync message, continuing...");
                continue;
            }

            isCommandScheduled.set(false);

            if(!checkCorrectIdRequest()) continue;

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
                case SELECT_STUDENT_COLORS -> clientController.validateSelectStudentColor();
                case SELECT_ENTRANCE_STUDENTS -> clientController.validateSelectEntranceStudents();
                case SELECT_ISLAND_GROUP -> clientController.validateSelectIslandGroup();
                case SELECT_STUDENTS_ON_CARD -> clientController.validateSelectStudentOnCard();
                case PLAY_CHARACTER -> clientController.validatePlayCharacter();
//              case QUIT -> closeConnection();
//              it doesn't make sense that the client waits for confirmation to quit from the server,
//              it should be the sender that triggers the application quitting process when it sees "QUIT"
                default -> closeConnection();
            }
            mainBroker.flushFirstSyncMessage();
        }

    }

    /**
     * Checks that the received reply has the same id as the one sent by the
     * sender just before
     * @return true if the id matches the expected one
     */
    private boolean checkCorrectIdRequest() {
        if(initialConnector.getCurrentIdRequest() !=
        ApplicationHelper.getIntFromBrokerField(mainBroker.readField(NetworkFieldEnum.ID_REQUEST))) {
            //Wrong id received, akin to a network error, close the connection
            initialConnector.notifyNetworkError("The id for the last request doesn't match the expected one");
            return false;
        }
        return true;
    }

    /**
     * Creates a thread to parse asynchronous messages from the server and returns
     * @return a Thread containing the asynchronous parsing routine
     */
    public Thread parseAsyncMessages(){
        if(parseAsyncStarted) return null;

        Thread parseAsyncThread = new Thread( new Runnable()  {
            public void run() {
                while (connected.get()) {
                    try {
                        mainBroker.waitAsyncMessage();
                    } catch (InterruptedException e) {
                        //System.err.println("Interrupted while waiting for async message, continuing...");
                        continue;
                    }

                    switch (CommandEnum.fromObjectToEnum(mainBroker.readAsyncField(NetworkFieldEnum.COMMAND))) {
                        case SERVER_LOBBY_STATUS -> clientController.handleLobbyUpdate();
                        case SERVER_LOBBY_START -> clientController.handleLobbyStart();
                        case SERVER_GAME_INITIALIZATION_STATUS -> clientController.handleGameInitUpdate();
                        case SERVER_GAME_START -> clientController.handleGameStart();
                        case SERVER_YOUR_TURN -> clientController.handleTurnUpdate();
                        case SERVER_GAME_UPDATE -> clientController.handleGameUpdate();
                        case SERVER_GAME_WON -> clientController.handleGameWon();
                        case SERVER_USER_DISCONNECTED -> clientController.handleUserDisconnection();
                        default -> closeConnection();
                    }
                    if (connected.get()) {
                        sendAsynchronousACK();
                    }
                    mainBroker.flushFirstAsyncMessage();
                }
            }
        });
        parseAsyncThread.setName("parseAsyncThread");
        parseAsyncThread.start();
        parseAsyncStarted = true;
        return parseAsyncThread;
    }

    /**
     * After receiving an asynchronous message from the server, sends an ack message back to the server. <br>
     * Not implemented
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
        hasBeenClosedAlready = true;
    }


    public void setUI(UserInterface ui){
        this.clientController.setUI(ui);
    }


}
