package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.characterCards.Requirements;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.commandHandler.CommandHandler;
import it.polimi.ingsw.network.commandHandler.PingHandler;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.connectionState.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ClientHandler implements Runnable{

    private static final Duration timeout = Duration.ofSeconds(5);

    private Socket mainSocket;
    private Socket pingSocket;
    private MessageBroker mainBroker;
    private MessageBroker pingBroker;
    private boolean isConnected;

    private int idUser;
    private ConnectionState connectionState;
    private ConnectionState callbackConnectionState;
    private Lobby userLobby;
    private Controller userController;


    /**
     * Creates a new client handler with its own message broker and set to state Authentication
     * @param mainSocket the main socket of the connection that's been created in the server
     */
    public ClientHandler(Socket mainSocket) {
        this.mainSocket = mainSocket;
        this.mainBroker = new MessageBroker();
        this.pingBroker = new MessageBroker();
        this.connectionState = new Authentication();
        this.userLobby = null;
        this.userController = null;
        this.isConnected = true;
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
        isConnected = true; // todo remove after implementing ping

        idUser = LoginHandler.getNewUserId();
        try {
            clientInput = mainSocket.getInputStream();
            clientOutput = mainSocket.getOutputStream();
        } catch (IOException e) {
            System.err.println("Error obtaining streams");
            System.err.println(e.getMessage());
            quitGame(); // Maybe useless
            return;
        }

        new Thread(()->{while (isConnected) mainBroker.receive(clientInput);}).start();

        new Thread(this::pong).start();

        while(isConnected){ // Message listener loop

            if(!mainBroker.lock()){ // Received an invalid message
                continue;
            }
            CommandEnum command = CommandEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.COMMAND));

            if(!connectionState.isAllowed(command)){ // Trashes a command given at the wrong time
                mainBroker.unlock();
                continue;
            }
            handleCommand(command); // runs the appropriate routine depending on the command received
            // Sends a reply to the client
            mainBroker.send(clientOutput);
            mainBroker.unlock();

        }
        // This point should never be reached in normal circumstances (unless the client disconnects)
    }

    /**
     * Adds the reply fields in the server message in case of a successful operation (Message and status)
     */
    private void notifySuccessfulOperation(){
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "OK");
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 0);
        mainBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, mainBroker.readField(NetworkFieldEnum.ID_REQUEST));
    }

    /**
     * Adds the reply fields in the server message in case of a failed operation (Message and status)
     * @param errorMessage A verbose message describing the error
     */
    private void notifyError(String errorMessage){ // parametrize reply status as well
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "ERR");
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 1);
        mainBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, mainBroker.readField(NetworkFieldEnum.ID_REQUEST));
        mainBroker.addToMessage(NetworkFieldEnum.ERROR_STATE, errorMessage);
    }

    /**
     * Closes the current connection
     */
    private void closeConnection() {
        //todo
    }

    /**
     * Finds the command that's been given and runs the appropriate methods
     * @param command the command that was received by the client
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
            case SELECT_TOWER_COLOR -> selectTowerColor();
            case ASK_FOR_CONTROL -> askForControl();
            case CHOOSE_ASSISTANT -> chooseAssistant();
            case SELECT_STUDENT -> selectEntranceStudent();
            case PUT_IN_HALL -> putInHall();
            case PUT_IN_ISLAND -> putInIsland();
            case DESELECT_STUDENT -> deselectStudent();
            case MOVE_MN_TO_ISLAND -> moveMNToIsland();
            case CHOOSE_CLOUD -> chooseCloud();
            case SELECT_CHARACTER -> selectCharacter();
            case SELECT_STUDENT_COLOR -> selectStudentColor();
            case SELECT_STUDENT_ON_CARD -> selectStudentOnCard();
            case SELECT_ENTRANCE_STUDENTS -> selectEntranceStudents();
            case SELECT_ISLAND_GROUP -> selectIslandGroup();
            case PLAY_CHARACTER -> playCharacter();
        }
    }


    /*
     *  For all the methods below, the game connection state updates accordingly
     */

    /**
     * The player plays the card for which they (optionally) selected the requirements
     */
    private void playCharacter() {
//        Integer cardPosition = (Integer)broker.readField(NetworkFieldEnum.CHARACTER_CARD_POSITION);
        if(userController.playCard()){
            notifySuccessfulOperation();
            setConnectionState(callbackConnectionState);
        }
        else {
            notifyError("Couldn't play the character card");
        }
    }


    private void pong(){//TEMPORARY

        InputStream clientInput;
        OutputStream clientOutput;

        try {
            clientInput = pingSocket.getInputStream();
            clientOutput = pingSocket.getOutputStream();
        } catch (IOException e) {
            System.err.println("Error obtaining streams");
            System.err.println(e.getMessage());
            quitGame(); // Maybe useless
            return;
        }

        ExecutorService pingExecutor = Executors.newSingleThreadExecutor();

        final Future<Void> handler = pingExecutor.submit(() -> {

            while (!pingBroker.lock()); //operation to execute with timeout

            return null; //no need for a return value
        });

        while (isConnected) {

            pingBroker.receive(clientInput);
            try {
                handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                handler.cancel(true);
                isConnected = false;
                System.err.println("Connection timed out");
                pingBroker.unlock();
                break;
            } catch (InterruptedException | ExecutionException e) {
                handler.cancel(true);
                isConnected = false;
                e.printStackTrace();
                pingBroker.unlock();
                break;
            }

            if (pingBroker.readField(NetworkFieldEnum.COMMAND) != CommandEnum.PING){
                System.err.println("ERROR: socket was not dedicated for ping routine");
                isConnected = false;
            }
            else {
                CommandHandler pingHandler = new PingHandler();
                try {
                    pingHandler.executeCommand(pingBroker, clientOutput);
                } catch (UnexecutableCommandException e) { //should never happen, condition is already verified
                    e.printStackTrace(); //this method has been overridden
                }
            }
            pingBroker.unlock();
        }
    }

    /**
     * Card requirements method.
     * The user sends the selected islands
     */
    private void selectIslandGroup() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.CHOSEN_ISLANDS);
        List<Integer> islandIds = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            islandIds.add((Integer)o);
        }
        if(userController.selectIslandGroups(islandIds)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the islands");
        }
    }

    /**
     * Card requirements method.
     * The user sends the selected students at their entrance
     */
    private void selectEntranceStudents() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_POSITIONS);
        List<Integer> students = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            students.add((Integer)o);
        }
        if(userController.selectEntranceStudents(students)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the students at the entrance");
        }
    }

    /**
     * Card requirements method.
     * The user sends the selected students on the card they previously selected
     */
    private void selectStudentOnCard() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.CHOSEN_CARD_POSITIONS);
        List<Integer> students = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            students.add((Integer)o);
        }
        if(userController.selectStudentOnCard(students)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the students on the card");
        }
    }

    /**
     * Card requirements method.
     * The user sends the selected student color
     */
    private void selectStudentColor() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.COLORS_REQUIRED);
        List<StudentEnum> colors = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            colors.add(StudentEnum.fromObjectToEnum(o));
        }

        if(userController.selectStudentColor(colors)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select student color");
        }
    }

    /**
     * The user selects a character card to play
     */
    private void selectCharacter() {
        //Here we need to memorize what the previous state the user had to resume the turn
        // after the character takes effect
        callbackConnectionState = connectionState;

        Integer cardPosition = (Integer) mainBroker.readField(NetworkFieldEnum.CHARACTER_CARD_POSITION);
        if(!userController.selectCard(cardPosition)){
            notifyError("Couldn't play the card, not enough coins");
        }
        else {
            notifySuccessfulOperation();
            Requirements requirements = userController.getAdvancedGame()
                                        .getAdvancedParameters().getRequirementsForThisAction();
            mainBroker.addToMessage(NetworkFieldEnum.ENTRANCE_REQUIRED, requirements.studentAtEntrance);
            mainBroker.addToMessage(NetworkFieldEnum.COLORS_REQUIRED, requirements.studentType);
            mainBroker.addToMessage(NetworkFieldEnum.ISLANDS_REQUIRED, requirements.islands);
            mainBroker.addToMessage(NetworkFieldEnum.ON_CARD_REQUIRED, requirements.studentOnCard);
            //TODO maybe incapsulate somewhere else?

            setConnectionState(new CharacterCardActivation());
        }
    }

    /**
     * The user chooses a cloud to refill their entrance with
     */
    private void chooseCloud() {
        Integer idCloud = (Integer) mainBroker.readField(NetworkFieldEnum.ID_CLOUD);
        if(userController.chooseCloud(idCloud)){
            setConnectionState(new WaitingForControl());
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the cloud");
        }
    }

    /**
     * The user moves mother nature
     */
    private void moveMNToIsland() {
        Integer steps = (Integer)mainBroker.readField(NetworkFieldEnum.STEPS_MN);
        if(userController.moveMNToIsland(steps)){
            notifySuccessfulOperation();
            setConnectionState(new CloudChoosing());
        }
        else {
            notifyError("Couldn't move mother nature");
        }
    }

    /**
     * The user deselects the student
     */
    private void deselectStudent() {
        Integer studentPosition = (Integer)mainBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_STUDENT);
        if(userController.deselectStudent(studentPosition)){
            notifySuccessfulOperation();
            setConnectionState(new StudentChoosing());
        }
        else {
            notifyError("Couldn't deselect the student");
        }
    }

    /**
     * The user chooses to put the student on the selected island
     */
    private void putInIsland() {
        Integer idIsland = (Integer)mainBroker.readField(NetworkFieldEnum.CHOSEN_ISLAND);
        if(userController.putInIsland(idIsland)){
            notifySuccessfulOperation();
            if(userController.allStudentsMoved()){
                setConnectionState(new MNMoving());
            }
            else {
                setConnectionState(new StudentChoosing());
            }
        }
        else {
            notifyError("Couldn't put the student on the island");
        }
    }

    /**
     * The user chooses to put their student in their hall
     */
    private void putInHall() {
        if(userController.putInHall()){
            notifySuccessfulOperation();
            if(userController.allStudentsMoved()){
                setConnectionState(new MNMoving());
            }
            else {
                setConnectionState(new StudentChoosing());
            }
        }
        else {
            notifyError("Couldn't put the student in the hall");
        }
    }

    /**
     * The user asks to select a student from their entrance
     */
    private void selectEntranceStudent() {
        Integer selectedStudent = (Integer)mainBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_STUDENT);
        if(userController.selectStudent(selectedStudent)){
            notifySuccessfulOperation();
            setConnectionState(new StudentMoving());
        }
        else {
            notifyError("Error selecting the student, you've chosen all students already");

        }
    }

    /**
     * The user selects which assistant they want to play, the server replies with an error
     * if the assistant can't be played
     */
    private void chooseAssistant() {
        Integer idAssistant = (Integer)mainBroker.readField(NetworkFieldEnum.ID_ASSISTANT);
        if(userController.playAssistant(idAssistant)){
            notifySuccessfulOperation();
            setConnectionState(new WaitingForControl());
        }
        else {
            notifyError("The assistant couldn't be played");
        }
    }

    /**
     * While waiting for their turn, the user periodically sends this message to ask for their turn to start
     */
    private void askForControl() {
        PhaseEnum gamePhase = PhaseEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.GAME_PHASE));
        if(userController.askForControl(this.idUser, gamePhase)){
            if(gamePhase.equals(PhaseEnum.PLANNING)){
                setConnectionState(new PlanningPhaseTurn());
            }
            else if(gamePhase.equals(PhaseEnum.ACTION)){
                setConnectionState(new StudentChoosing());
            }
            notifySuccessfulOperation();
        }
        else{
            notifyError("Not your turn to play");
        }
    }

    // Above this point, the commands relate to the actual game

    /**
     * The user selects a team to be part of, this method calls the game controller to know whether the
     * team chosen is available, and notifies it to the user
     */
    private void selectTowerColor() {
        TeamEnum teamColor = TeamEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.ID_TOWER_COLOR));
        if(userController.setTeamColor(teamColor, this.idUser)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("The chosen team isn't available, please change your selection");
        }
        if (userController.startPlayingGame()){
            setConnectionState(new WaitingForControl());
        } // todo might need better handling
    }

    /**
     * The user selects a wizard from the four available, this method calls the game controller to know whether the
     * wizard chosen is available, and notifies it to the user
     */
    private void selectWizard() {
        Integer idWizard = (Integer)mainBroker.readField(NetworkFieldEnum.ID_WIZARD);
        if(userController.setWizard(idWizard, this.idUser)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("The chosen wizard isn't available, please change your selection");
        }
        if (userController.startPlayingGame()){
            setConnectionState(new WaitingForControl());
        } // todo might need better handling
    }

    /**
     * The user requests to start the game
     * The request will be successful only if the host coincides with the user and all players are ready
     */
    private void startGame() {
        if(!userLobby.isHost(this.idUser)){
            notifyError("You're not the host! You can't start the game.");
        }
        else {
            if(ActiveLobbies.startGame(userLobby)){
                setConnectionState(new StartingGame());
                this.userController = ActiveGames.getGameFromUserId(this.idUser);
                notifySuccessfulOperation();
            }
            else {
                notifyError("The game couldn't start, returning to lobby");
            }
        }
    }

    /**
     * The user requests to play a game with the given rules
     */
    private void playGame() {

        GameRuleEnum rules = GameRuleEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.GAME_RULE));

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
        if(!userController.equals(null)){
            // The user decides to quit the game while still in the lobby, so the other users
            // shouldn't be kicked out automatically
            userLobby.removePlayer(this.idUser);
        }
        else {
            // The game is already starting or already started, so the game shall end for
            // every player
            ActiveGames.endGame(this.idUser);
            closeConnection();
        }
    }


    /**
     * Handles the connections of a new user, checking whether their nickname
     * satisfies the requirement of uniqueness
     */
    public void connectionRequest(){
        boolean loginSuccessful;
        loginSuccessful= LoginHandler.login((String)mainBroker.readField(NetworkFieldEnum.NICKNAME), idUser);
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

    public Socket getMainSocket(){
        return mainSocket;
    }

}
