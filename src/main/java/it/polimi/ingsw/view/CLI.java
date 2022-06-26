package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.client.InitialConnector;
import it.polimi.ingsw.network.client.InterfaceInterrupt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CLI implements UserInterface {
    private StringBuilder View;
    private StringBuilder LastView;
    private StringBuilder LastElement;
    private AtomicReference<String> readCommand;
    private Thread readerThread;

    private final int startPosition = 0;
    private final int centerPosition = 10;
    private List<GameElementBean> beans;
    private VirtualViewBean viewBean; // todo see what needs to be kept
    private LobbyBean lobbyBean;
    private GameInitBean gameInitBean;
    private List<CommandEnum> availableCommands;
    InterfaceInterrupt connected;
    InterfaceInterrupt lobbyStarting;
    InterfaceInterrupt gameStarting;
    InterfaceInterrupt gameInterrupted;
    InterfaceInterrupt updateAvailable;
    InterfaceInterrupt lobbyUpdateAvailable;
    InterfaceInterrupt gameInitUpdateAvailable;
    InterfaceInterrupt yourTurn;
    InterfaceInterrupt gameWon;
    List<InterfaceInterrupt> gameInterrupts; //There might be multiple "interrupts" that the main game interface should react to
    List<InterfaceInterrupt> lobbyInterrupts;
    List<InterfaceInterrupt> gameInitInterrupts;
    boolean errorLogin;
    boolean userQuit;

    private String chosenNickname;
    private GameRuleEnum gameMode;
    private int numberOfPlayers;
    private String gameType;
    private PhaseEnum phase;
    private boolean commandError;
    private String commandErrorMessage;
    private String currentTeamColor = TeamEnum.NOTEAM.name;
    private String currentWizard = WizardEnum.NO_WIZARD.name;
    int islandsRequired;
    int studentsOnCardRequired;
    int studentsAtEntranceRequired;
    int colorsRequired;
    private TeamEnum winnerTeam;

    private final String colorList3Players = "B - Black, G - Grey, W - White";
    private final String colorList2or4Players = "B - Black, W - White";

    private InitialConnector initialConnector;
    private ClientSender sender;

    /**
     * Creates a new CLI and calls the method to create a new NetworkManager
     * @param initialConnector the connector that allows the CLI to first communicate
     */
    public CLI(InitialConnector initialConnector){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
        View = new StringBuilder();
        LastView = new StringBuilder();
        LastElement = new StringBuilder();
        lobbyBean = null;
        gameInitBean = null;
        userQuit = false;

        setGameMode(GameRuleEnum.NO_RULE);

        lobbyStarting               = new InterfaceInterrupt(true, new AtomicBoolean(false));
        gameStarting                = new InterfaceInterrupt(true, new AtomicBoolean(false));
        gameInterrupted             = new InterfaceInterrupt(true, new AtomicBoolean(false));
        updateAvailable             = new InterfaceInterrupt(true, new AtomicBoolean(false));
        lobbyUpdateAvailable        = new InterfaceInterrupt(true, new AtomicBoolean(false));
        gameInitUpdateAvailable     = new InterfaceInterrupt(true, new AtomicBoolean(false));
        yourTurn                    = new InterfaceInterrupt(true, new AtomicBoolean(false));
        gameWon                     = new InterfaceInterrupt(true, new AtomicBoolean(false));
        connected                   = new InterfaceInterrupt(false, initialConnector.getConnected());
        commandError = false;
        this.initialConnector = initialConnector;

        gameInterrupts = new ArrayList<>();
        gameInterrupts.add(connected);
        gameInterrupts.add(gameInterrupted);
        gameInterrupts.add(updateAvailable);
        gameInterrupts.add(yourTurn);
        gameInterrupts.add(gameWon);

        lobbyInterrupts = new ArrayList<>();
        lobbyInterrupts.add(connected);
        lobbyInterrupts.add(lobbyUpdateAvailable);
        lobbyInterrupts.add(lobbyStarting);

        gameInitInterrupts = new ArrayList<>();
        gameInitInterrupts.add(connected);
        gameInitInterrupts.add(gameInitUpdateAvailable);
        gameInitInterrupts.add(gameStarting);
        gameInitInterrupts.add(gameInterrupted);

        readCommand = new AtomicReference<>("");
        Thread readerThread = new Thread(() ->{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String command = "";
            while(true) {
                try {
                    command = reader.readLine();
                } catch (IOException e) {
                    System.err.println("Error reading from standard input");
                }
                readCommand.set(command);
            }
        });
        readerThread.setName("readerThread");
        this.readerThread = readerThread;
    }

    /**
     * Resets the CLI after a severe network error
     */
    public void reset(){

        resetInterrupts(gameInterrupts);
        resetInterrupts(lobbyInterrupts);
        resetInterrupts(gameInitInterrupts);

        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();

        connected = new InterfaceInterrupt(false, initialConnector.getConnected());

        gameInterrupts = new ArrayList<>();
        gameInterrupts.add(connected);
        gameInterrupts.add(gameInterrupted);
        gameInterrupts.add(updateAvailable);
        gameInterrupts.add(yourTurn);
        gameInterrupts.add(gameWon);

        lobbyInterrupts = new ArrayList<>();
        lobbyInterrupts.add(connected);
        lobbyInterrupts.add(lobbyUpdateAvailable);
        lobbyInterrupts.add(lobbyStarting);

        gameInitInterrupts = new ArrayList<>();
        gameInitInterrupts.add(connected);
        gameInitInterrupts.add(gameInitUpdateAvailable);
        gameInitInterrupts.add(gameStarting);
        gameInitInterrupts.add(gameInterrupted);

        resetGameInfo();
    }

    /**
     * Network-less constructor, used for testing
     */
    @Deprecated
    public CLI(){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
        View = new StringBuilder();
        LastView = new StringBuilder();
        LastElement = new StringBuilder();
    }

    /**
     *
     * @return the string received as input by user
     */
    @Deprecated
    public String askCommand(){
        System.out.println("ENTER COMMAND: ");
        Scanner keyboard = new Scanner(System.in);
        String command = keyboard.nextLine();
        return command;
    }

    @Deprecated
    public void show(){
        final int highestPriority = 1;
        final int lowestPriority = 10;
        final int timeToNewLine = 1;

        View = new StringBuilder();
        LastView = new StringBuilder();

        int precPriority = lowestPriority;
        int positionOnScreen = 0;
        GameElementBean curr = beans.get(0);
        GameElementBean bean;
        int index = 0;
        int min = lowestPriority;

        //I must draw all beans. When I draw one I remove it.
        while(!beans.isEmpty()){
            min = lowestPriority;
            //choosing bean to draw (min priority go first)
            for(int id = 0; id < beans.size(); id++){
                bean = beans.get(id);
                if(bean.getPriority() < min ){
                    min = bean.getPriority();
                    curr = bean;
                    index = id;
                }
            }

            if(precPriority != curr.getPriority())
                positionOnScreen = startPosition;

            if(precPriority == curr.getPriority())
                positionOnScreen++;

            if(positionOnScreen > timeToNewLine)
                positionOnScreen = startPosition;

            if(curr.getPriority() == highestPriority )
                positionOnScreen = centerPosition;

            draw(positionOnScreen, curr.toString());
            precPriority = curr.getPriority();
            beans.remove(index);


        }

        drawOption();

        System.out.println(View.toString());
    }

    @Deprecated
    private void draw(int position, String elementToDraw){
        final int width = 4;
        final String tab = "    ";
        final String center = "\t\t\t\t\t\t\t\t";


        if(position == startPosition){
            LastView = new StringBuilder(View.toString());
            View.append("\n\n");
            View.append(elementToDraw);
            LastElement = new StringBuilder(elementToDraw);
            return;
        }

        if(position == centerPosition){
            LastView = new StringBuilder(View.toString());
            View.append("\n\n");
            Scanner scanner = new Scanner(elementToDraw);
            scanner.useDelimiter("\n");
            while (scanner.hasNext()){
                View.append(center).append(scanner.next()).append("\n");
            }
            LastElement = new StringBuilder(elementToDraw);
            return;
        }

        Scanner moreRowsLastElement = new Scanner(LastElement.toString());
        Scanner moreRowsThisElement = new Scanner(elementToDraw);
        moreRowsLastElement.useDelimiter("\n");
        moreRowsThisElement.useDelimiter("\n");
        int lastElementRows = 0;
        int thisElementRows = 0;
        while(moreRowsLastElement.hasNext()){
            moreRowsLastElement.next();
            lastElementRows++;
        }

        while(moreRowsThisElement.hasNext()){
            moreRowsThisElement.next();
            thisElementRows++;
        }

        if(thisElementRows > lastElementRows){
            String toggle = elementToDraw;
            elementToDraw = LastElement.toString();
            LastElement = new StringBuilder(toggle);
        }


        //Old element to show in left position on screen
        Scanner scannerLastElement = new Scanner(LastElement.toString());
        scannerLastElement.useDelimiter("\n");

        //new element to show in right position on screen
        Scanner scannerThisElement = new Scanner(elementToDraw);
        scannerThisElement.useDelimiter("\n");

        //OFFSET
        StringBuilder offsetBuilder = new StringBuilder();
        for(int i = 0; i < width * position; i++){
            offsetBuilder.append(tab);
        }
        String offset = offsetBuilder.toString();
        String last = LastView.toString();






        /*
        now for each old element's row I append the new element's row with an offset.
        I must start from the last view, before the old element was inserted, and append
        both two elements.
         */
        while(scannerLastElement.hasNext() || scannerThisElement.hasNext()){
            if(!scannerThisElement.hasNext())
                LastView.append(scannerLastElement.next()).append("\n");
            else
                LastView.append(scannerLastElement.next()).append(offset).append(scannerThisElement.next()).append("\n");
        }


        View = new StringBuilder(LastView.toString());
        LastElement = new StringBuilder(elementToDraw);
    }

    /**
     * Append to View (String builder) the available command string
     * in order to show in the CLI
     */
    @Deprecated
    public void drawOption(){
        View.append("\n\t\t\t\t::CHOICES::\n");
        for(int index=0;index<availableCommands.size();index++){
            CommandEnum command = availableCommands.get(index);
            View.append(" ________________________________________\n");
            View.append("|" + command +"\n");
            View.append("|REQUIRES: ");
            for(NetworkFieldEnum field: command.allowedFields){
                if(field != NetworkFieldEnum.ID_USER &&
                        field != NetworkFieldEnum.ID_REQUEST &&
                        field!= NetworkFieldEnum.ID_PING_REQUEST)
                View.append("-"+field+"- ");
            }
            View.append("\n| PRESS " + index + " to choose this action\n");
            View.append("|________________________________________\n");
        }

    }

    /**
     * Returs a string containing the available commands during the game
     * and the relative required fields
     */
    private void printAvailableCommands(){
        StringBuilder retString = new StringBuilder();
        List<NetworkFieldEnum> requiredFields = new ArrayList<>();
        retString.append("\n\t\t::CHOICES::\n");
        for (CommandEnum command : availableCommands) {
            retString.append("|" + command);
            requiredFields = CommandEnum.getFieldsNeeded(command);
            if (requiredFields.size() > 0) retString.append("   -   REQUIRES: ");
            for (NetworkFieldEnum field : requiredFields) {
                retString.append("-" + field.toString() + "- ");
            }
            retString.append("\n");
        }
        System.out.println(retString);
    }



    @Override
    public void showWelcomeScreen() {
        System.out.println("""
                                   ---   ---   --- WELCOME TO ERIANTYS! ---   ---   ---
                            (For the best experience, make sure the command line is in full screen)
                            """);

    }

    @Override
    public void showGoodbyeScreen() {
        System.out.println("""
                 ~~~ See you soon! ~~~
                """);
    }

    //<editor-fold desc="Synchronous methods">

    @Override
    public void showLoginScreen() {

        String inputNickname;

        do{
            errorLogin = false;
            System.out.println("Insert your username: ");

            inputNickname = getInputNonBlocking();

            if(!initialConnector.login(inputNickname)){
                errorLogin = true;
                System.out.println("There was a problem connecting to the server, try again");
                return;
            }
            else errorLogin = false;

        } while(errorLogin);
    }

    /**
     * Redirects to the showLoginScreen method
     */
    @Override
    public void showLoginScreenFailure(){
        System.out.println("The username was rejected by the server, choose another one");
    }

    /**
     * Redirects to the showGameruleSelection method
     */
    @Override
    public void showSuccessLoginScreen() {
        System.out.println("Username " + chosenNickname + " was accepted");
    }

    @Override
    public void showGameruleSelection() {

        String gameMode;
        String numPlayers;
        boolean errorChoice;

        errorChoice = false;

        clearOldScreen();

        System.out.println("""
                Which type of game would you like to play? (Type in the corresponding number)
                1 - Simple game: No character cards
                2 - Advanced game: Character cards allowed
                """);
        do {
            gameMode = getInputNonBlocking(connected);
            if(connected.isTriggered()){
                return;
            }
            if (!(gameMode.equals("1") ||
                    gameMode.equals("2"))) {
                errorChoice = true;
                System.out.println("Wrong choice! Please select a correct game mode");
            } else errorChoice = false;

        } while (errorChoice);

        System.out.println("""
                How many players do you want the game to have? (Type in the corresponding number)
                2 - 2 Players
                3 - 3 Players
                4 - 4 Players
                """);
        do {
            numPlayers = getInputNonBlocking(connected);
            if (!(numPlayers.equals("2") ||
                    numPlayers.equals("3") ||
                    numPlayers.equals("4"))) {
                errorChoice = true;
                System.out.println("Wrong choice! Please select a correct amount of players");
            } else errorChoice = false;

        } while (errorChoice);

        sender.sendGameModePreference(Integer.parseInt(gameMode),Integer.parseInt(numPlayers));
    }

    @Override
    public void showErrorJoiningLobby(){
        System.out.println("There was a problem sending the gamemode preferences, please try again");
    }

    @Override
    public void showSuccessJoiningLobby(){
        System.out.println("You joined a lobby!");
    }

    @Override
    public void showLobby() {
        boolean repeatSelection;
        String selection = "";

        clearOldScreen();
        if(!lobbyStarting.isTriggered()) printWaitingForPlayers();
        if(lobbyBean != null) printLobbyBean();
        do {

            repeatSelection = false;
            selection = "";
            selection = getInputNonBlocking(lobbyInterrupts);

            if (lobbyStarting.isTriggered()){
                showTowerAndWizardSelection();
                break;
            }
            else if(lobbyUpdateAvailable.isTriggered()){
                clearOldScreen();
                printWaitingForPlayers();
                printLobbyBean();
                lobbyUpdateAvailable.clearInterrupt();
                repeatSelection = true;
                continue;
            }
            else if(connected.isTriggered()){
                //Error occured
                return;
            }

            if (selection.equals("1")) {
                sender.sendReadyStatus(true);
            } else if (selection.equals("2")) {
                sender.sendReadyStatus(false);
            } else if (selection.toUpperCase(Locale.ROOT).equals("S")) {
                sender.startGame();
            } else if (selection.toUpperCase(Locale.ROOT).equals("L")) {
                sender.leaveLobby();
            } else {
                System.out.println("Insert a correct option please!");
                repeatSelection = true;
            }
        } while (repeatSelection);
    }

    private void printWaitingForPlayers(){
        System.out.println(MessageFormat.format("""
                    Game type selected:
                        Number of players : {0}
                        Game type : {1}
                    
                    You are waiting for players to join a game with you...
                    1 - Set yourself as ready
                    2 - Set yourself as not ready
                    S - Try to start the game
                    L - Leave the lobby, go back to selecting the game rules
                    """, this.numberOfPlayers, this.gameType));
    }

    @Override
    public void showSuccessReadyStatus(boolean status) {
        String readyOrNotHereICome = status?"":"not";
        System.out.println("You set yourself as "+readyOrNotHereICome+" ready");
    }

    @Override
    public void showErrorReadyStatus(boolean status) {
        System.out.println("There was an error sending your readiness status");
    }

    @Override
    public void showSuccessStartGame() {
        System.out.println("The game could start");
    }

    @Override
    public void showErrorStartGame() {
        System.out.println("The game couldn't start");
    }

    @Override
    public void showSuccessLeaveLobby() {
        System.out.println("Leaving the current lobby...");
    }

    @Override
    public void showErrorLeaveLobby() {
        System.out.println("Couldn't leave, you're stuck in the lobby °_°");
    }


    @Override
    public void showTowerAndWizardSelection() {

        String selection = null;
        String colorList = numberOfPlayers == 3 ? colorList3Players : colorList2or4Players;
        boolean repeatSelection = false;

        clearOldScreen();

        if(!gameStarting.isTriggered()) printChooseYourTowerWizard(colorList);
        if(gameInitBean != null) printGameInitBean();

        do {
            selection = getInputNonBlocking(gameInitInterrupts);

            if(gameStarting.isTriggered()){
                System.out.println("Everyone made their choice, the game is starting!");
                showMainGameInterface();
                return;
            }
            else if(gameInitUpdateAvailable.isTriggered()){
                clearOldScreen();
                printChooseYourTowerWizard(colorList);
                printGameInitBean();
                gameInitUpdateAvailable.clearInterrupt();
                repeatSelection = true;
                continue;
            }
            else if(connected.isTriggered()){
                //Error occured, return
                return;
            }
            else if(gameInterrupted.isTriggered()){
                // Go back to the login screen
                gameInterrupted.clearInterrupt();
                resetInterrupts(lobbyInterrupts);
                resetInterrupts(gameInitInterrupts);
                resetGameInfo();
                showGameruleSelection();
                return;
            }

            repeatSelection = false;
            switch (selection.toUpperCase(Locale.ROOT)) {
                //Team color
                case "B":
                    sender.sendTeamColorChoice(TeamEnum.BLACK);
                    break;
                case "G":
                    if (numberOfPlayers == 3) { //we do a little hardcoding
                        sender.sendTeamColorChoice(TeamEnum.GREY);
                        break;
                    }
                    //else it falls through to the default case
                case "W":
                    sender.sendTeamColorChoice(TeamEnum.WHITE);
                    break;
                //Wizard
                case "1":
                    sender.sendWizardChoice(WizardEnum.KING);
                    break;
                case "2":
                    sender.sendWizardChoice(WizardEnum.PIXIE);
                    break;
                case "3":
                    sender.sendWizardChoice(WizardEnum.SORCERER);
                    break;
                case "4":
                    sender.sendWizardChoice(WizardEnum.WIZARD);
                    break;
                default:
                    System.out.println("Please make a valid selection");
                    repeatSelection = true;
                    break;
            }
        } while(repeatSelection);
    }

    private void printChooseYourTowerWizard(String colorList){
        System.out.println(MessageFormat.format("""
            Select your team color ({2})
            and wizard (1 - King, 2 - Pixie, 3 - Sorcerer, 4 - Wizard)
            (one at a time):
            
            Your selection :
                Team color : {0}
                Wizard : {1}
            """, currentTeamColor, currentWizard, colorList));
    }

    @Override
    public void showErrorSelectingColor(String color) {
        System.out.println("There was an error selecting the color " + color);
    }

    @Override
    public void showSuccessSelectingColor(String color) {
        System.out.println("You chose the " + color + " team.");
    }

    @Override
    public void showErrorSelectingWizard(String wizard) {
        System.out.println("There was an error selecting the " + wizard);
    }

    @Override
    public void showSuccessSelectingWizard(String wizard) {
        System.out.println("You chose the " + wizard);
    }

    @Override
    public void showMainGameInterface() {
        boolean correctInput = false;
        String input = "";
        CommandEnum parsedInput;

        // Show the current view
        if(commandError){
            // Print command error message
            System.out.println("Your command was incorrect! " + commandErrorMessage);
            commandError = false;
            commandErrorMessage = "";
        }

        // THERE ALWAYS NEEDS TO BE A WAY FOR THE USER TO INPUT ANYTHING
        do {
            //The game was interrupted, show it to the client (might be removed as there are async handlers already)
            if(gameInterrupted.isTriggered()){
                System.out.println("The game was interrupted :(\n");
                gameInterrupted.clearInterrupt();
                resetInterrupts(lobbyInterrupts);
                resetInterrupts(gameInitInterrupts);
                resetInterrupts(gameInterrupts);
                resetGameInfo();
                showGameruleSelection();
                return;
            }

            if(updateAvailable.isTriggered()){
                if (yourTurn.isTriggered()){
                    clearOldScreen();
                    // Show the current view
                    printInterface();
                    // Print your turn message
                    printYourTurn();
                    // Print available commands
                    printAvailableCommands();
                    yourTurn.clearInterrupt();
                    updateAvailable.clearInterrupt();
                }
                else {
                    clearOldScreen();
                    //Show the new current view
                    printInterface();
                    // Print available commands
                    printAvailableCommands();
                    updateAvailable.clearInterrupt();
                }
            }
            else if(yourTurn.isTriggered()){
                clearOldScreen();
                // Show the current view
                printInterface();
                // Print your turn message
                printYourTurn();
                // Print available commands
                printAvailableCommands();
                yourTurn.clearInterrupt();
            }
            if(gameWon.isTriggered()){
                //The game is over, show the winner and exit from this method
                gameWon.clearInterrupt();
                showGameOverScreen();
            }

            if(!checkInterrupt(gameInterrupts)){
                System.out.println("Type your command (space separated values):\t");
            }
            input = getInputNonBlocking(gameInterrupts);

            if(isCorrectInput(input)) {
                correctInput = true;
            }
            // only print this when a trigger has NOT occurred -> the user actually put in a wrong command
            else if (!checkInterrupt(gameInterrupts)){
                System.out.println("Your input is incorrect");
            }
        } while (!correctInput);

        // Handle the correct input.
        // Input is already completely inserted and checked for both availability and syntax correctness

        parsedInput = CommandEnum.valueOf(input.split(" ")[0].toUpperCase(Locale.ROOT));
        switch (parsedInput){
            case QUIT -> {
                sender.sendQuit();
                userQuit = true;
            }
            case CHOOSE_ASSISTANT -> {
                int idAssistant = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendAssistantChosen(idAssistant);
            }
            case SELECT_STUDENT -> {
                int chosenStudent = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                chosenStudent--;
                sender.sendSelectedStudent(chosenStudent);
            }
            case PUT_IN_HALL -> {
                sender.sendPutInHall();
            }
            case PUT_IN_ISLAND -> {
                int idIsland = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendPutInIsland(idIsland);
            }
            case DESELECT_STUDENT -> {
                sender.sendDeselectStudent();
            }
            case MOVE_MN -> {
                int steps = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendMoveMN(steps);
            }
            case CHOOSE_CLOUD -> {
                int cloudId = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendChooseCloud(cloudId);
            }
            case END_TURN -> {
                sender.sendEndTurn();
            }
            case SELECT_CHARACTER -> {
                int position = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                position--;
                sender.sendSelectCharacter(position);
            }
            case SELECT_STUDENT_COLORS -> {
                List<StudentEnum> colors = ApplicationHelper.getStudentEnumListFromString(input.split(" ")[1]);
                sender.sendSelectStudentColors(colors);
            }
            case SELECT_ENTRANCE_STUDENTS -> {
                List<Integer> students = ApplicationHelper.getIntListFromString(input.split(" ")[1]);
                students = students.stream()
                            .map(i -> i-1)
                            .collect(Collectors.toList());
                sender.sendSelectEntranceStudents(students);
            }
            case SELECT_ISLAND_GROUP -> {
                int idIslandGroup = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendSelectIslandGroup(idIslandGroup);
            }
            case SELECT_STUDENTS_ON_CARD -> {
                List<Integer> selectedStudent = ApplicationHelper.getIntListFromString(input.split(" ")[1]);
                selectedStudent = selectedStudent.stream()
                                    .map(i -> i-1)
                                    .collect(Collectors.toList());
                sender.sendSelectStudentsOnCard(selectedStudent);
            }
            case PLAY_CHARACTER -> {
                sender.sendPlayCharacter();
            }
            default -> showMainGameInterface(); // todo handle this case
        }

    }

    @Override
    public void showGameCommandError(String errorMessage) {
        this.commandError = true;
        this.commandErrorMessage = errorMessage;
    }

    @Override
    public void showGameCommandError() {
        showGameCommandError("");
    }

    @Override
    public void showGameCommandSuccess() {

    }

    private void showGameOverScreen() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(MessageFormat.format("""
                
                The game is over! {0} won!
                Press enter to go back to the game selection screen.
                
                """, this.winnerTeam.name));
        scanner.nextLine();

        resetGameInfo();

        showGameruleSelection();
    }

    /**
     * Resets the ui variables related to a game, keeping intact
     * the ones relative to the connection
     */
    private void resetGameInfo() {
        lobbyBean = null;
        gameInitBean = null;
        winnerTeam = TeamEnum.NOTEAM;
        gameMode = GameRuleEnum.NO_RULE;
        numberOfPlayers = 0;
        gameType = "No game";
        commandError = false;
        commandErrorMessage = "";
        currentTeamColor = TeamEnum.NOTEAM.name;
        currentWizard = WizardEnum.NO_WIZARD.name;
        islandsRequired = 0;
        studentsAtEntranceRequired = 0;
        studentsOnCardRequired = 0;
        colorsRequired = 0;

    }

    // </editor-fold>

    //<editor-fold desc="Network Errors">


    @Override
    public void showNetworkError() {
        //If the network error was intended (i.e. due to the user quitting)
        if(!userQuit) System.out.println("Connection with Server lost, you will now return to the login screen");
    }

    @Override
    public void showUserDisconnected(String user) {
        System.out.println("The user "+user+ " disconnected.");
    }

    // </editor-fold>

    @Override
    public void startInterface() {
        startReaderThread();
        showWelcomeScreen();
        while(!userQuit){
            //This method ends here basically, everything else derives from the receiver
            communicationEntryPoint();
        }
        showGoodbyeScreen();
    }


    private void communicationEntryPoint() {
        showLoginScreen();
        if(!errorLogin) initialConnector.startReceiving();
        //If the communication is cut short, then this routine will reset it and try again
        initialConnector.reset();
        this.reset();
    }

    private void startReaderThread() {
        readerThread.start();
    }

    //<editor-fold desc="Asynchronous methods">

    public void printLobby(LobbyBean lobbyBean) {
        this.lobbyBean = lobbyBean;
        lobbyUpdateAvailable.trigger();
    }

    private void printLobbyBean(){
        for(int index = 0; index < lobbyBean.getNicknames().size(); index++){

            String optionalNot = "";
            String optionalHost = "";
            if(!lobbyBean.getReadyPlayers().get(index)) optionalNot = " not";
            if(lobbyBean.getHost() == index) optionalHost = "[host]";

            System.out.println(String.format("%s :%s ready %s",
                    lobbyBean.getNicknames().get(index),
                    optionalNot,
                    optionalHost));
        }
    }

    public void printGameInitInfo(GameInitBean gameInitBean){
        this.gameInitBean = gameInitBean;
        gameInitUpdateAvailable.trigger();
    }

    private void printGameInitBean(){
        System.out.println(gameInitBean.toString());
    }

    private void printYourTurn(){
        System.out.println("It's your turn! Starting your "+ phase.toString()+" phase");
    }

    /**
     * Actually prints the interface with the last saved view
     */
    public void printInterface(){
        VirtualViewBean view = this.viewBean;
        GameBoardBean simpleGame = view.getGameBoardBean();
        AdvancedGameBoardBean advancedGame = view.getAdvancedGameBoardBean();
        List<CloudBean> clouds = view.getCloudBeans();
        List<PlayerBean> simplePlayers = view.getPlayerBeans();
        List<AdvancedPlayerBean> advancedPlayers = view.getAdvancedPlayerBeans();
        List<IslandGroupBean> simpleIslands = view.getIslandGroupBeans();
        List<AdvancedIslandGroupBean> advancedIslands = view.getAdvancedIslandGroupBeans();
        List<CharacterCardBean> characterCardBeans = view.getCharacterCardBeans();

        if(simplePlayers != null){
            simplePlayers.sort(Comparator.comparingInt(PlayerBean::getTurn));
            List<Integer> islandsOrder = simpleGame.getIdIslandGroups();
            List<IslandGroupBean> sortedIslands = new ArrayList<>();
            for(Integer x : islandsOrder){
                for(IslandGroupBean bn : simpleIslands)
                    if(bn.getIdIslandGroup() == x)
                        sortedIslands.add(bn);
            }
            simpleIslands = sortedIslands;
        }

        else if(advancedPlayers != null){
            advancedPlayers.sort(Comparator.comparingInt(AdvancedPlayerBean::getTurn));
            List<Integer> islandsOrder = advancedGame.getIdIslandGroups();
            List<AdvancedIslandGroupBean> sortedIslands = new ArrayList<>();
            for(Integer x : islandsOrder){
                for(AdvancedIslandGroupBean bn : advancedIslands)
                    if(bn.getIdIslandGroup() == x)
                        sortedIslands.add(bn);
            }
            advancedIslands = sortedIslands;
        }

        //Show gameBoardBean.
        if(simpleGame != null)
            System.out.println(simpleGame);
        else
            System.out.println(advancedGame);

        if(advancedGame != null) {
            printCharacterCard(characterCardBeans);
        }

        //Show clouds
        printCloud(clouds);

        //ShowIslands
        if(simpleIslands != null){
            int MN = simpleIslands.stream().filter(x -> x.isPresentMN()).findFirst().get().getIdIslandGroup();
            System.out.println("\nMother nature is on Island " + MN);
            printIsland(simpleIslands, false);
        }
        else if(advancedIslands != null){
            int MN = advancedIslands.stream().filter(x -> x.isPresentMN()).findFirst().get().getIdIslandGroup();
            System.out.println("\nMother nature is on Island " + MN);
            List<IslandGroupBean> lis = new ArrayList<>();
            for(AdvancedIslandGroupBean b: advancedIslands)
                lis.add(b);
            printIsland(lis, true);
        }
        //Show Players
        if(simplePlayers != null){
            printPlayer(simplePlayers, false);
        }
        else if(advancedPlayers != null){
            List<PlayerBean> listP = new ArrayList<>();
            for(AdvancedPlayerBean b: advancedPlayers)
                listP.add(b);
            printPlayer(listP,true);
        }
    }

    /**
     * Clears the screen
     * Only works with ANSI-compliant shells
     */
    private void clearOldScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //todo get the beans from this method and store them. The main game interface
    // will then use the beans to render the view
    /**
     * This method simply retrieves the view bean and stores it to later show in the main method
     * @param view the view bean received from the controller
     */
    @Override
    public void printGameInterface(VirtualViewBean view) {
        this.viewBean = view;
    }

    @Override
    public void showItsYourTurn(PhaseEnum phase) {
        this.phase = phase;
    }

    // </editor-fold>

    //<editor-fold desc="Utility methods">


    /**
     * Gets user input from the stream in the reader, via a thread dedicated to reading user input.
     * When this method is called, every input is reset.
     * @return the string input by the user
     */
    public String getInputNonBlocking(){

        List<InterfaceInterrupt> interrupts = new ArrayList<>();
        return getInputNonBlocking(interrupts);

    }

    /**
     * Gets user input from the stream in the reader, via a thread dedicated to reading user input.
     * When this method is called, every input is reset.
     * This method returns with the empty string in case the interruptingConditions is triggered
     * @param interruptingCondition the condition that makes the method return prematurely
     * @return the string input by the user, or the empty string if the interruptingCondition
     * was triggered
     */
    public String getInputNonBlocking(InterfaceInterrupt interruptingCondition){

        List<InterfaceInterrupt> interrupts = new ArrayList<>();
        interrupts.add(interruptingCondition);
        return getInputNonBlocking(interrupts);

    }


    /**
     * Gets user input from the stream in the reader, via a thread dedicated to reading user input.
     * When this method is called, every input is reset.
     * This method returns with the empty string in case one of the interruptingConditions is triggered
     * @param interruptingConditions the conditions that make the method return prematurely
     * @return the string input by the user, or the empty string if one of the interruptingConditions
     * was triggered
     */
    public String getInputNonBlocking(List<InterfaceInterrupt> interruptingConditions){

        readCommand.set("");
        //Empty the current string, whatever was present needs to be read again
        String currentSelection = "";

        while(currentSelection.equals("") && !checkInterrupt(interruptingConditions)){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for input, starting a new cycle");
                //e.printStackTrace();
                continue;
            }
            if(checkInterrupt(interruptingConditions)){
                //The interrupts come first in terms of importance
                return "";
            }
            currentSelection = readCommand.get();
            if(!currentSelection.equals("")){
                return currentSelection;
            }
        }
        return "";
    }

    private boolean checkInterrupt(List<InterfaceInterrupt> interruptingConditions) {
        for(InterfaceInterrupt condition : interruptingConditions){
            //If the condition is triggered when true(false) and the interrupt IS true(false), then return true
            if( condition.isTriggered() ) return true;
        }
        return false;
    }

    /**
     * Gets user input from the stream in the reader, exiting and returning the empty string
     * in case one of the interruptingConditions is true
     * @param interruptingConditions the conditions that make the method return, in case the input
     *                              wasn't read already
     * @return the string input by the user, or the empty string if one of the interruptingConditions
     * was triggered
     */
    public String getInputNonBlockingLegacy(List<InterfaceInterrupt> interruptingConditions){

        String selection = "";
        ExecutorService readerExecutor = Executors.newSingleThreadExecutor();
        Future<String> handler;
        Callable<String> readCallable = () -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(!reader.ready() && !Thread.currentThread().isInterrupted()); // Can't wait or the input is invisible, can't interrupt because readLine is non-blocking, i hate I/O so much here take 20% of my cpu resources but please read the input
            if(Thread.currentThread().isInterrupted()) return ""; // Only interrupted if we need to exit out of the function
            return reader.readLine();
        };

        handler = readerExecutor.submit(readCallable);

        while(selection.equals("") && !checkInterrupt(interruptingConditions)) {
            try {
                selection = handler.get(200, TimeUnit.MILLISECONDS);
                if(selection.equals("")){
                    //Reset the handler
                    handler = readerExecutor.submit(readCallable);
                }
            } catch (InterruptedException e) {
                //Run the next loop
                System.err.println("I got interrupted");
                continue;
            } catch (ExecutionException e) {
                System.err.println("Exception in thread: " + e.getCause());
            } catch (TimeoutException e) {
                //Do another round
                continue;
            }
        }
        handler.cancel(true);
        readerExecutor.shutdown();
        return selection;
    }



    /**
     * Check all available commands with the respective parameters required and compare them to the input string
     * The command must be in the same form as the enum (may be changed) and the parameters must be in the same number as those required
     * @param input the string input by the user containing the command (and its parameters)
     * @return true if the input matches with one of the commands' names and parameters
     */
    //todo can be changed to check for the positioning, for example,
    // i.e. if there are 4 commands available here we check if the input is < 4
    private boolean isCorrectInput(String input) {
        List<String> splitInput = Arrays.stream(input.split(" ")).collect(Collectors.toList());
        String inputCommand;
        CommandEnum actualCommand;
        List<NetworkFieldEnum> requiredFields;

        if(splitInput.size() > 0){
            inputCommand = splitInput.get(0);
            splitInput.remove(0);
        } else return false;

        //Check if command available. Note that the command must match exactly
        try{
            if (!availableCommands.contains(CommandEnum.valueOf(inputCommand.toUpperCase(Locale.ROOT)))) return false;
        } catch (IllegalArgumentException e){
            return false;
        }
        actualCommand = CommandEnum.valueOf(inputCommand.toUpperCase(Locale.ROOT));

        //Check whether the input fields are same in number with the needed ones
        requiredFields = CommandEnum.getFieldsNeeded(actualCommand);
        if(requiredFields.size() != splitInput.size()) return false;

        //Check the type of each field
        for(int fieldPos = 0; fieldPos < requiredFields.size(); fieldPos++){
            if(!isInputCompatible(splitInput.get(fieldPos) , requiredFields.get(fieldPos))) return false;
        }

        //All checks were passed
        return true;
    }

    /**
     * Tests whether the input string is compatible with the given network field's class
     * @param inputString the string containing the input field
     * @param networkFieldEnum the network field that the string needs to be compared to
     * @return true if inputString can be converted to the type of the network field
     */
    private boolean isInputCompatible(String inputString, NetworkFieldEnum networkFieldEnum) {
        //todo find a better way if possible instead of parsing all possible class types
        // available to the user to input
        // A switch isn't allowed here
        if (String.class.equals(NetworkFieldEnum.getClass(networkFieldEnum))) {
            return true;
        } else if (int.class.equals(NetworkFieldEnum.getClass(networkFieldEnum))) {
            return ApplicationHelper.isInt(inputString);
        } else if (GameRuleEnum.class.equals(NetworkFieldEnum.getClass(networkFieldEnum))) {
            return ApplicationHelper.isGameRuleEnum(inputString);
        } else if (int[].class.equals(NetworkFieldEnum.getClass(networkFieldEnum))) {
            return ApplicationHelper.isIntArray(inputString);
        } else if (StudentEnum[].class.equals(NetworkFieldEnum.getClass(networkFieldEnum))) {
            return ApplicationHelper.isStudentEnumArray(inputString);
        }
        return false;
    }

    /**
     * Resets the interrupts in the given list
     * @param interrupts the list of interrupts to reset
     */
    private void resetInterrupts(List<InterfaceInterrupt> interrupts){
        for(InterfaceInterrupt interfaceInterrupt : interrupts){
            interfaceInterrupt.clearInterrupt();
        }
    }

    // </editor-fold>


    @Override
    public void setSender(ClientSender sender) {
        this.sender = sender;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setGameMode(GameRuleEnum gameMode) {
        this.gameMode = gameMode;
        this.numberOfPlayers = GameRuleEnum.getNumPlayers(gameMode.id);
        this.gameType = GameRuleEnum.isAdvanced(gameMode.id) ? "Advanced" : "Simple";
    }

    public void setChosenNickname(String chosenNickname) {
        this.chosenNickname = chosenNickname;
    }

    @Override
    public void setInLobby(boolean inLobby) {
    }

    @Override
    public void setTeamColor(String teamColor) {
        this.currentTeamColor = teamColor;
    }

    @Override
    public void setWizard(String wizard) {
        this.currentWizard = wizard;
    }

    @Override
    public void setLobbyStarting() {
        this.lobbyStarting.trigger();
    }

    @Override
    public void setGameStarting() {
        this.gameStarting.trigger();
    }

    @Override
    public void setGameInterrupted(boolean interrupted) {
        if(interrupted) gameInterrupted.trigger();
        else gameInterrupted.clearInterrupt();
    }

    @Override
    public void setUpdateAvailable(boolean available) {
        if(available) updateAvailable.trigger();
        else updateAvailable.clearInterrupt();
    }

    @Override
    public void setYourTurn(boolean isYourTurn) {
        if(isYourTurn) yourTurn.trigger();
        else yourTurn.clearInterrupt();
    }

    @Override
    public void setGameWon(TeamEnum winner) {
        gameWon.trigger();
        this.winnerTeam = winner;
    }

    @Override
    public void setCardRequirements(int islandsRequired, int studentsOnCardRequired, int studentsAtEntranceRequired, int colorsRequired) {
        this.islandsRequired = islandsRequired;
        this.studentsOnCardRequired = studentsOnCardRequired;
        this.studentsAtEntranceRequired = studentsAtEntranceRequired;
        this.colorsRequired = colorsRequired;
    }

    @Override
    public void addBean(GameElementBean bean) {
        beans.add(bean);
    }

    @Override
    public GameElementBean removeBean(int index) {
        return beans.remove(index);
    }

    @Override
    public void clearBeans() {
        beans.clear();
    }

    @Override
    public void addCommand(CommandEnum command) {
        if(!availableCommands.contains(command)) availableCommands.add(command);
    }

    @Override
    public CommandEnum removeCommand(int index) {
        return availableCommands.remove(index);
    }

    @Override
    public void clearCommands() {
        availableCommands.clear();
    }

    private List<String> studentsToNumStud(List<StudentEnum> list1){
        List<String> list1s = new ArrayList<>();
        list1s.add(StaticColorCLI.ANSI_GREEN +
                String.valueOf(list1.stream().filter(x -> x == StudentEnum.GREEN).count()) +
                StaticColorCLI.ANSI_RESET);
        list1s.add(StaticColorCLI.ANSI_RED +
                String.valueOf(list1.stream().filter(x -> x == StudentEnum.RED).count())+
                StaticColorCLI.ANSI_RESET);
        list1s.add(StaticColorCLI.ANSI_YELLOW +
                String.valueOf(list1.stream().filter(x -> x == StudentEnum.YELLOW).count())+
                StaticColorCLI.ANSI_RESET);
        list1s.add(StaticColorCLI.ANSI_PURPLE +
                String.valueOf(list1.stream().filter(x -> x == StudentEnum.PINK).count())+
                StaticColorCLI.ANSI_RESET);
        list1s.add(StaticColorCLI.ANSI_BLUE +
                String.valueOf(list1.stream().filter(x -> x == StudentEnum.BLUE).count())+
                StaticColorCLI.ANSI_RESET);

        return list1s;
    }



    public void printCharacterCard(List<CharacterCardBean> characterCardBeans){
        StringBuilder characterString = new StringBuilder();
        int size = characterCardBeans.size();
        String s = "--------------------";
        characterString.append(String.format("%-30s", s).repeat(size));
        characterString.append("\n");

        //Name
        for (CharacterCardBean bean: characterCardBeans) {
            s = bean.getName();
            characterString.append(String.format("%-30s", s));
        }

        characterString.append("\n");

        //Cost
        for (CharacterCardBean bean: characterCardBeans) {
            s = "Cost: " + bean.getCost();
            characterString.append(String.format("%-30s", s));
        }

        characterString.append("\n");

        //Students or Blocks
        for (CharacterCardBean bean: characterCardBeans) {
            if(bean.getNumBlocks() != null){
                s="BLOCKS: " + bean.getNumBlocks();
                characterString.append(String.format("%-30s", s));
            }
            else if(bean.getStudents() == null){
                s = "[]";
                characterString.append(String.format("%-30s", s));
            }
            else{
                s = bean.getStudents().toString();
                if(bean.getStudents().size() > 5)
                    characterString.append(String.format("%-84s", s));
                else
                    characterString.append(String.format("%-66s", s));
            }
        }

        characterString.append("\n");
        s = "--------------------";
        characterString.append(String.format("%-30s", s).repeat(size));
        characterString.append("\n");

        System.out.println(characterString);

    }

    private void printCloud(List<CloudBean> clouds ){
        StringBuilder cloudsString = new StringBuilder();
        int size = clouds.size();
        String s = "--------------------";
        cloudsString.append(String.format("%-30s", s).repeat(size));
        cloudsString.append("\n");

        //ID
        for (CloudBean cloud : clouds) {
            s = "CLOUD: " + cloud.getIdCloud();
            cloudsString.append(String.format("%-30s", s));
        }

        cloudsString.append("\n");

        //Students
        for (CloudBean cloud : clouds) {
            if(size == 4 || size == 2){
                if(cloud.getStudents().isEmpty()){
                    s = cloud.getStudents().toString();
                    cloudsString.append(String.format("%-30s", s));
                }

                else{
                s = cloud.getStudents().toString();
                cloudsString.append(String.format("%-57s", s));
                }
            }
            else{
                if(cloud.getStudents().isEmpty()){
                    s = cloud.getStudents().toString();
                    cloudsString.append(String.format("%-30s", s));
                }

                else{
                    s = cloud.getStudents().toString();
                    cloudsString.append(String.format("%-66s", s));
                }
            }
        }

        cloudsString.append("\n");

        s = "--------------------";
        cloudsString.append(String.format("%-30s", s).repeat(size));
        cloudsString.append("\n");

        System.out.println(cloudsString);

    }

    private void printIsland(List<IslandGroupBean> islands, boolean advanced){
        StringBuilder islandsString = new StringBuilder();
        int size = islands.size();
        int done = 0;
        int col = 0;
        String s = "--------------------";

        while(done < size){
            if(done + 5 <= size)
                col = 5;
            else if(done + 4 <= size)
                col = 4;
            else if(done + 3 <= size)
                col = 3;
            else if(done + 2 <= size)
                col = 2;
            else
                col = 1;


            s = ">------------------>";
            islandsString.append(String.format("%-30s", s).repeat(col));
            islandsString.append("\n");

            //ID
            for(int i = done; i< col + done; i++){
                s = "ISLAND " + islands.get(i).getIdIslandGroup() +" ["+
                        islands.get(i).getIdIslands().size() + "]";
                islandsString.append(String.format("%-30s", s));
            }

            islandsString.append("\n");

            //Tower color
            for(int i = done; i< col + done; i++){
                s = "TOWER COLOR: " + islands.get(i).getTowersColor();
                islandsString.append(String.format("%-30s", s));
            }

            islandsString.append("\n");

            //Students
            for(int i = done; i< col + done; i++){

                List<String> list1 = studentsToNumStud(islands.get(i).getStudentsOnIsland());

                s = list1.toString();
                islandsString.append(String.format("%-75s", s));
            }

            islandsString.append("\n");

            //Block
            if(advanced){
                for(int i = done; i< col + done; i++){
                    AdvancedIslandGroupBean is = (AdvancedIslandGroupBean) islands.get(i);
                    s = "BLOCK: " + is.getNumBlockTiles();
                    islandsString.append(String.format("%-30s", s));
                }

                islandsString.append("\n");
            }

            s = ">------------------>";
            islandsString.append(String.format("%-30s", s).repeat(col));
            islandsString.append("\n");

            done += col;
        }

        System.out.println(islandsString);
    }

    private void printPlayer(List<PlayerBean> players, boolean advanced){
        StringBuilder playerString = new StringBuilder();
        int size = players.size();
        String s = "--------------------";
        playerString.append(String.format("%-30s", s).repeat(size));
        playerString.append("\n");

        //ID
        for(PlayerBean player: players){
            s= player.getPlayerId().toString();
            playerString.append(String.format("%-30s",s));
        }
        playerString.append("\n");

        //Nickname
        for(PlayerBean player: players){
            s= player.getNickname();
            playerString.append(String.format("%-30s",s));
        }
        playerString.append("\n");

        //Towers
        for(PlayerBean player: players){
            s= "Towers: " + player.getTowerColor() + " [" + player.getNumTowers() + "]";
            playerString.append(String.format("%-30s",s));
        }
        playerString.append("\n");

        if(advanced) {
            //Coins
            for (PlayerBean player : players) {
                AdvancedPlayerBean p = (AdvancedPlayerBean) player;
                s = "Coins: " + p.getNumCoins();
                playerString.append(String.format("%-30s", s));
            }
            playerString.append("\n");
        }

        //Entrance 1
        for(PlayerBean player: players){
            while(player.getStudentsAtEntrance().size() <= 9 )
                player.getStudentsAtEntrance().add(StudentEnum.NOSTUDENT);
            List<StudentEnum> list = player.getStudentsAtEntrance().subList(0,3);
            s= "Entrance: " + list ;
            playerString.append(String.format("%-57s",s));
        }
        playerString.append("\n");

        //Entrance 2
        for(PlayerBean player: players){
            List<StudentEnum> list = player.getStudentsAtEntrance().subList(3,9);
            s= list.toString() ;
            playerString.append(String.format("%-84s",s));
        }
        playerString.append("\n");

        //Prof
        for(PlayerBean player: players){
            while(player.getProfessors().size() < StudentEnum.getNumStudentTypes())
                player.getProfessors().add(StudentEnum.NOSTUDENT);
            s= "Prof: " + player.getProfessors();
            playerString.append(String.format("%-75s",s));
        }
        playerString.append("\n");

        //Table
        for(PlayerBean player: players){
            s = ("Table:[");
            for(int i=0; i < StudentEnum.getNumStudentTypes(); i++ )
                s+= (StaticColorCLI.getColor(i))+(player.getStudentsPerTable().get(i)) + (" ");
            s+= (StaticColorCLI.ANSI_RESET) + ("]");

            playerString.append(String.format("%-59s",s));
        }
        playerString.append("\n");


        //Assistant 1
        for(PlayerBean player: players){
            player.setIdAssistants(player.getIdAssistants().stream().mapToInt( x ->{
                int y= x % 10;
                if(y == 0)
                    y = 10;
                return y;
            }).collect(ArrayList::new,ArrayList::add,ArrayList::addAll));
            int assistantsSize = player.getIdAssistants().size();
            if(assistantsSize > 0){
                int a = Math.min(assistantsSize, 3);
                s= "Assistant: " + player.getIdAssistants().subList(0,a);
            }
            else
                s = "Assistant: []";
            playerString.append(String.format("%-30s",s));
        }
        playerString.append("\n");

        //Assistant 2
        for(PlayerBean player: players){
            int assistantsSize = player.getIdAssistants().size();
            if(assistantsSize > 3){
                int a = Math.min(assistantsSize, 8);
                s= player.getIdAssistants().subList(3,a).toString();
            }
            else
                s="[]";
            playerString.append(String.format("%-30s",s));
        }
        playerString.append("\n");

        //Assistant 3
        for(PlayerBean player: players){
            int assistantsSize = player.getIdAssistants().size();
            if(assistantsSize > 8){
                s= player.getIdAssistants().subList(8,assistantsSize).toString();
            }
            else
                s="[]";
            playerString.append(String.format("%-30s",s));
        }
        playerString.append("\n");


        s = "--------------------";
        playerString.append(String.format("%-30s", s).repeat(size));
        playerString.append("\n");

        System.out.println(playerString);
    }


}
