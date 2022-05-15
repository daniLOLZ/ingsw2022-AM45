package it.polimi.ingsw.network;

import it.polimi.ingsw.network.commandHandler.CommandHandler;
import it.polimi.ingsw.network.commandHandler.FactoryCommandHandler;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

public class ClientHandler implements Runnable{

    private static final Duration timeout = Duration.ofSeconds(5);

    private static List<CommandHandler> commandHandlers = FactoryCommandHandler.getAllCommandHandlers();

    private Socket mainSocket;
    private Socket pingSocket;
    private MessageBroker mainBroker;
    private MessageBroker pingBroker;
    private boolean isConnected;


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

        parameters.setIdUser(LoginHandler.getNewUserId());
        try {
            clientInput = mainSocket.getInputStream();
            clientOutput = mainSocket.getOutputStream();
        } catch (IOException e) {
            System.err.println("Error obtaining streams");
            System.err.println(e.getMessage());
            //quitGame(); // Maybe useless
            return;
        }

        new Thread(()->{while (isConnected) mainBroker.receive(clientInput);}).start();

        new Thread(this::pong).start();

        while(isConnected){ // Message listener loop

            if(!mainBroker.lock()){ // Received an invalid message
                continue;
            }
            CommandEnum command = CommandEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.COMMAND));

            if(!parameters.getConnectionState().isAllowed(command)){ // Trashes a command given at the wrong time
                mainBroker.unlock();
                continue;
            }
            handleCommand(mainBroker); // runs the appropriate routine depending on the command received
            // Sends a reply to the client
            mainBroker.send(clientOutput);
            mainBroker.unlock();

        }
        // This point should never be reached in normal circumstances (unless the client disconnects)
    }

    //below methods moved to CommandHandler

    /*
     * Adds the reply fields in the server message in case of a successful operation (Message and status)
     */
    /*private void notifySuccessfulOperation(){
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "OK");
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 0);
        mainBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, mainBroker.readField(NetworkFieldEnum.ID_REQUEST));
    }*/

    /*
     * Adds the reply fields in the server message in case of a failed operation (Message and status)
     * @param errorMessage A verbose message describing the error
     */
    /*private void notifyError(String errorMessage){ // parametrize reply status as well
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "ERR");
        mainBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 1);
        mainBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, mainBroker.readField(NetworkFieldEnum.ID_REQUEST));
        mainBroker.addToMessage(NetworkFieldEnum.ERROR_STATE, errorMessage);
    }*/

    /**
     * Closes the current connection
     */
    private void closeConnection() {
        //todo
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

        if (!successfulOperation); //TODO close connection?
    }


    //  For all the methods below, the game connection state updates accordingly

    // HANDLED ELSEWHERE
    /*
     * The player plays the card for which they (optionally) selected the requirements
     */
    /*private void playCharacter() {
//        Integer cardPosition = (Integer)broker.readField(NetworkFieldEnum.CHARACTER_CARD_POSITION);
        if(parameters.getUserController().playCard()){
            notifySuccessfulOperation();
            parameters.setConnectionState(parameters.getCallbackConnectionState());
        }
        else {
            notifyError("Couldn't play the character card");
        }
    }*/


    private void pong(){//TEMPORARY

        InputStream clientInput;
        OutputStream clientOutput;

        try {
            clientInput = pingSocket.getInputStream();
            clientOutput = pingSocket.getOutputStream();
        } catch (IOException e) {
            System.err.println("Error obtaining streams");
            System.err.println(e.getMessage());
            //quitGame(); //TODO handle this case better
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
                pingBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.PONG);
                pingBroker.addToMessage(NetworkFieldEnum.ID_PING_REQUEST, pingBroker.readField(NetworkFieldEnum.ID_PING_REQUEST));
                pingBroker.send(clientOutput);
            }
            pingBroker.unlock();
        }
    }

    // HANDLED ELSEWHERE
    /*
     * Card requirements method.
     * The user sends the selected islands
     */
    /*private void selectIslandGroup() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.CHOSEN_ISLANDS);
        List<Integer> islandIds = new ArrayList<>();
        for(Object o : objectArray){
            islandIds.add((Integer)o);
        }
        if(parameters.getUserController().selectIslandGroups(islandIds)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the islands");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * Card requirements method.
     * The user sends the selected students at their entrance
     */
    /*private void selectEntranceStudents() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_POSITIONS);
        List<Integer> students = new ArrayList<>();
        for(Object o : objectArray){
            students.add((Integer)o);
        }
        if(parameters.getUserController().selectEntranceStudents(students)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the students at the entrance");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * Card requirements method.
     * The user sends the selected students on the card they previously selected
     */
    /*private void selectStudentOnCard() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.CHOSEN_CARD_POSITIONS);
        List<Integer> students = new ArrayList<>();
        for(Object o : objectArray){
            students.add((Integer)o);
        }
        if(parameters.getUserController().selectStudentOnCard(students)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the students on the card");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * Card requirements method.
     * The user sends the selected student color
     */
    /*private void selectStudentColor() {
        Object[] objectArray = (Object[]) mainBroker.readField(NetworkFieldEnum.COLORS_REQUIRED);
        List<StudentEnum> colors = new ArrayList<>();
        for(Object o : objectArray){
            colors.add(StudentEnum.fromObjectToEnum(o));
        }

        if(parameters.getUserController().selectStudentColor(colors)){
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select student color");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user selects a character card to play
     */
    /*private void selectCharacter() {
        //Here we need to memorize what the previous state the user had to resume the turn
        // after the character takes effect
        parameters.setCallbackConnectionState(parameters.getConnectionState());

        Integer cardPosition = (Integer) mainBroker.readField(NetworkFieldEnum.CHARACTER_CARD_POSITION);
        if(!parameters.getUserController().selectCard(cardPosition)){
            notifyError("Couldn't play the card, not enough coins");
        }
        else {
            notifySuccessfulOperation();
            Requirements requirements = parameters.getUserController().getAdvancedGame()
                                        .getAdvancedParameters().getRequirementsForThisAction();
            mainBroker.addToMessage(NetworkFieldEnum.ENTRANCE_REQUIRED, requirements.studentAtEntrance);
            mainBroker.addToMessage(NetworkFieldEnum.COLORS_REQUIRED, requirements.studentType);
            mainBroker.addToMessage(NetworkFieldEnum.ISLANDS_REQUIRED, requirements.islands);
            mainBroker.addToMessage(NetworkFieldEnum.ON_CARD_REQUIRED, requirements.studentOnCard);

            parameters.setConnectionState(new CharacterCardActivation());
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user chooses a cloud to refill their entrance with
     */
    /*private void chooseCloud() {
        Integer idCloud = (Integer) mainBroker.readField(NetworkFieldEnum.ID_CLOUD);
        if(parameters.getUserController().chooseCloud(idCloud)){
            parameters.setConnectionState(new WaitingForControl());
            notifySuccessfulOperation();
        }
        else {
            notifyError("Couldn't select the cloud");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user moves mother nature
     */
    /*private void moveMNToIsland() {
        Integer steps = (Integer)mainBroker.readField(NetworkFieldEnum.STEPS_MN);
        if(parameters.getUserController().moveMNToIsland(steps)){
            notifySuccessfulOperation();
            parameters.setConnectionState(new CloudChoosing());
        }
        else {
            notifyError("Couldn't move mother nature");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user deselects the student
     */
    /*private void deselectStudent() {
        Integer studentPosition = (Integer)mainBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_STUDENT);
        if(parameters.getUserController().deselectStudent(studentPosition)){
            notifySuccessfulOperation();
            parameters.setConnectionState(new StudentChoosing());
        }
        else {
            notifyError("Couldn't deselect the student");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user chooses to put the student on the selected island
     */
    /*private void putInIsland() {
        Integer idIsland = (Integer)mainBroker.readField(NetworkFieldEnum.CHOSEN_ISLAND);
        if(parameters.getUserController().putInIsland(idIsland)){
            notifySuccessfulOperation();
            if(parameters.getUserController().allStudentsMoved()){
                parameters.setConnectionState(new MNMoving());
            }
            else {
                parameters.setConnectionState(new StudentChoosing());
            }
        }
        else {
            notifyError("Couldn't put the student on the island");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user chooses to put their student in their hall
     */
    /*private void putInHall() {
        if(parameters.getUserController().putInHall()){
            notifySuccessfulOperation();
            if(parameters.getUserController().allStudentsMoved()){
                parameters.setConnectionState(new MNMoving());
            }
            else {
                parameters.setConnectionState(new StudentChoosing());
            }
        }
        else {
            notifyError("Couldn't put the student in the hall");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user asks to select a student from their entrance
     */
    /*private void selectEntranceStudent() {
        Integer selectedStudent = (Integer)mainBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_STUDENT);
        if(parameters.getUserController().selectStudent(selectedStudent)){
            notifySuccessfulOperation();
            parameters.setConnectionState(new StudentMoving());
        }
        else {
            notifyError("Error selecting the student, you've chosen all students already");

        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user selects which assistant they want to play, the server replies with an error
     * if the assistant can't be played
     */
    /*private void chooseAssistant() {
        Integer idAssistant = (Integer)mainBroker.readField(NetworkFieldEnum.ID_ASSISTANT);
        if(parameters.getUserController().playAssistant(idAssistant)){
            notifySuccessfulOperation();
            parameters.setConnectionState(new WaitingForControl());
        }
        else {
            notifyError("The assistant couldn't be played");
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * While waiting for their turn, the user periodically sends this message to ask for their turn to start
     */
    /*private void askForControl() {
        PhaseEnum gamePhase = PhaseEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.GAME_PHASE));
        if(parameters.getUserController().askForControl(parameters.getIdUser(), gamePhase)){
            if(gamePhase.equals(PhaseEnum.PLANNING)){
                parameters.setConnectionState(new PlanningPhaseTurn());
            }
            else if(gamePhase.equals(PhaseEnum.ACTION)){
                parameters.setConnectionState(new StudentChoosing());
            }
            notifySuccessfulOperation();
        }
        else{
            notifyError("Not your turn to play");
        }
    }*/

    // Above this point, the commands relate to the actual game


    // HANDLED ELSEWHERE
    /*
     * The user selects a team to be part of, this method calls the game controller to know whether the
     * team chosen is available, and notifies it to the user
     */
    /*private void selectTowerColor() {
        TeamEnum teamColor = TeamEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.ID_TOWER_COLOR));
        if(parameters.getUserController().setTeamColor(teamColor, parameters.getIdUser())){
            notifySuccessfulOperation();
        }
        else {
            notifyError("The chosen team isn't available, please change your selection");
        }
        if (parameters.getUserController().startPlayingGame()){
            parameters.setConnectionState(new WaitingForControl());
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user selects a wizard from the four available, this method calls the game controller to know whether the
     * wizard chosen is available, and notifies it to the user
     */
    /*private void selectWizard() {
        Integer idWizard = (Integer)mainBroker.readField(NetworkFieldEnum.ID_WIZARD);
        if(parameters.getUserController().setWizard(idWizard, parameters.getIdUser())){
            notifySuccessfulOperation();
        }
        else {
            notifyError("The chosen wizard isn't available, please change your selection");
        }
        if (parameters.getUserController().startPlayingGame()){
            parameters.setConnectionState(new WaitingForControl());
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user requests to start the game
     * The request will be successful only if the host coincides with the user and all players are ready
     */
    /*private void startGame() {
        if(!parameters.getUserLobby().isHost(parameters.getIdUser())){
            notifyError("You're not the host! You can't start the game.");
        }
        else {
            if(ActiveLobbies.startGame(parameters.getUserLobby())){
                parameters.setConnectionState(new StartingGame());
                parameters.setUserController(ActiveGames.getGameFromUserId(parameters.getIdUser()));
                notifySuccessfulOperation();
            }
            else {
                notifyError("The game couldn't start, returning to lobby");
            }
        }
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user requests to play a game with the given rules
     */
    /*private void playGame() {

        GameRuleEnum rules = GameRuleEnum.fromObjectToEnum(mainBroker.readField(NetworkFieldEnum.GAME_RULE));

        parameters.setUserLobby(ActiveLobbies.assignLobby(rules));
        parameters.getUserLobby().addPlayer(parameters.getIdUser());
        parameters.setConnectionState(new InLobby());
        notifySuccessfulOperation();
    }*/

    // HANDLED ELSEWHERE
    /*
     * The user requests to leave the current lobby
     */
    /*private void requestLeaveLobby() {
        parameters.getUserLobby().removePlayer(parameters.getIdUser());
        parameters.setUserLobby(null);
        parameters.setConnectionState(new LookingForLobby());
        notifySuccessfulOperation();
    }*/

    // HANDLED ELSEWHERE
    /*
     * Sends a message to the server letting it know the user is not ready to start the game
     */
    /*private void sendNotReady() {
        parameters.getUserLobby().removeReady(parameters.getIdUser());
        notifySuccessfulOperation(); // other checks to do?
    }*/

    // HANDLED ELSEWHERE
    /*
     * Sends a message to the server letting it know the user is ready to start the game
     */
    /*private void sendReady() {
        parameters.getUserLobby().addReady(parameters.getIdUser());
        notifySuccessfulOperation(); // other checks to do?
    }*/

    // HANDLED ELSEWHERE but should it?
    /*
     * Closes the connection for this user
     */
    /*public void quitGame(){
        if(parameters.getUserController() != null){
            // The user decides to quit the game while still in the lobby, so the other users
            // shouldn't be kicked out automatically
            parameters.getUserLobby().removePlayer(parameters.getIdUser());
        }
        else {
            // The game is already starting or already started, so the game shall end for
            // every player
            ActiveGames.endGame(parameters.getIdUser());
            closeConnection();
        }
    }*/


    // HANDLED ELSEWHERE
    /*
     * Handles the connections of a new user, checking whether their nickname
     * satisfies the requirement of uniqueness
     */
    /*public void connectionRequest(){
        boolean loginSuccessful;
        loginSuccessful= LoginHandler.login((String)mainBroker.readField(NetworkFieldEnum.NICKNAME), parameters.getIdUser());
        if(!loginSuccessful){
            notifyError("Nickname already taken");
            quitGame();
        }
        else notifySuccessfulOperation();
        parameters.setConnectionState(new LookingForLobby());
    }*/


    public Socket getMainSocket(){
        return mainSocket;
    }

}
