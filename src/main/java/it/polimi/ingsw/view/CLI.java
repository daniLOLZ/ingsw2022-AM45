package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.VirtualViewBean;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.client.InitialConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLI implements UserInterface {
    private StringBuilder View;
    private StringBuilder LastView;
    private StringBuilder LastElement;
    private final int startPosition = 0;
    private final int centerPosition = 10;
    private List<GameElementBean> beans;
    private VirtualViewBean viewBean; // todo see what needs to be kept
    private List<CommandEnum> availableCommands;
    AtomicBoolean lobbyStarting;
    AtomicBoolean gameStarting;
    AtomicBoolean gameInterrupted;
    AtomicBoolean updateAvailable;
    AtomicBoolean yourTurn;
    List<AtomicBoolean> interrupts; //There might be multiple "interrupts" that the main game interface should react to

    private String chosenNickname;
    private GameRuleEnum gameMode;
    private int numberOfPlayers;
    private String gameType;
    private boolean inLobby;
    private boolean commandError;
    private String currentTeamColor = TeamEnum.NOTEAM.name;
    private String currentWizard = WizardEnum.NO_WIZARD.name;

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

        setGameMode(GameRuleEnum.NO_RULE);

        lobbyStarting = new AtomicBoolean(false);
        gameStarting = new AtomicBoolean(false);
        gameInterrupted = new AtomicBoolean(false);
        updateAvailable = new AtomicBoolean(false);
        yourTurn = new AtomicBoolean(false);
        commandError = false;
        this.initialConnector = initialConnector;

        interrupts = new ArrayList<>();
        interrupts.add(gameInterrupted);
        interrupts.add(updateAvailable);
        interrupts.add(yourTurn);
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
        inLobby = false;
    }

    /**
     *
     * @return the string received as input by user
     */
    public String askCommand(){
        System.out.println("ENTER COMMAND: ");
        Scanner keyboard = new Scanner(System.in);
        String command = keyboard.nextLine();
        return command;
    }

    //change in a more descriptive name
    //actually parse the beans instead of letting them draw themselves
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
    public String getOptions(){
        StringBuilder retString = new StringBuilder();
        retString.append("\n\t\t\t\t::CHOICES::\n");
        for(int index=0;index<availableCommands.size();index++){
            CommandEnum command = availableCommands.get(index);
            retString.append(" ________________________________________\n");
            retString.append("|" + command +"\n");
            retString.append("|REQUIRES: ");
            for(NetworkFieldEnum field: command.allowedFields){
                if(field != NetworkFieldEnum.ID_USER &&
                        field != NetworkFieldEnum.ID_REQUEST &&
                        field!= NetworkFieldEnum.ID_PING_REQUEST)
                    retString.append("-"+field+"- ");
            }
            retString.append("\n| PRESS " + index + " to choose this action\n");
            retString.append("|________________________________________\n");
        }
        return retString.toString();
    }



    @Override
    public void showWelcomeScreen() {
        System.out.println("-- WELCOME TO ERIANTYS! --");
    }

    //<editor-fold desc="Synchronous methods">

    @Override
    public void showLoginScreen() {
        Scanner scanner = new Scanner(System.in);
        String inputNickname;
        boolean errorLogin = false;

        do{
            System.out.println("Insert your username: ");
            inputNickname = scanner.nextLine();
            if(!initialConnector.login(inputNickname)){
                errorLogin = true;
                System.out.println("There was a problem connecting to the server, try again");
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
        Scanner scanner = new Scanner(System.in);
        String gameMode;
        String numPlayers;
        boolean errorChoice;

        errorChoice = false;

        System.out.println("""
                Which type of game would you like to play? (Type in the corresponding number)
                1 - Simple game: No character cards
                2 - Advanced game: Character cards allowed""");
        do {
            gameMode = scanner.next();
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
            numPlayers = scanner.next();
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean repeatSelection = false;
        String selection = "";
        if(!lobbyStarting.get()) System.out.println(MessageFormat.format("""
                Game type selected:
                    Number of players : {0}
                    Game type : {1}
                
                You are waiting for players to join a game with you...
                1 - Set yourself as ready
                2 - Set yourself as not ready
                S - Try to start the game
                L - Leave the lobby, go back to selecting the game rules
                """, this.numberOfPlayers, this.gameType));

        do {
            selection = "";
            selection = getInputNonBlocking(reader, lobbyStarting);

            if (lobbyStarting.get()){
                showTowerAndWizardSelection();
                break;
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
        this.inLobby = false;
        showGameruleSelection();
    }

    @Override
    public void showErrorLeaveLobby() {
        System.out.println("Couldn't leave, you're stuck in the lobby °_°");
        showLobby();
    }


    @Override
    public void showTowerAndWizardSelection() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String selection = null;
        String colorList = numberOfPlayers == 3 ? colorList3Players : colorList2or4Players;
        boolean repeatSelection = false;

        if(!gameStarting.get())System.out.println(MessageFormat.format("""
            Select your team color ({2})
            and wizard (1 - King, 2 - Pixie, 3 - Sorcerer, 4 - Wizard)
            (one at a time):
            
            Your selection :
                Team color : {0}
                Wizard : {1}
            """, currentTeamColor, currentWizard, colorList));

        do {
            selection = getInputNonBlocking(reader, gameStarting);

            if(gameStarting.get()){
                System.out.println("Everyone made their choice, the game is starting!");
                showMainGameInterface();
                return;
            }

            repeatSelection = false;
            switch (selection) {
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean correctInput = false;
        String input;
        CommandEnum parsedInput;

        // Show the current view
        if(commandError){
            // Print command error message
            commandError = false;
        }

        // THERE ALWAYS NEEDS TO BE A WAY FOR THE USER TO INPUT ANYTHING
        do {
            System.out.println("Type your command (space separated values):\t");
            input = getInputNonBlocking(reader, interrupts);
            if(gameInterrupted.get()){ //The game was interrupted, show it to the client (might be removed as there are async handlers already
                System.out.println("The game was interrupted :(");
                return;
            }
            if(updateAvailable.get()){
                if (yourTurn.get()){
                    // Show the current view
                    // Print your turn message
                    // Print available commands
                }
                else {
                    //Show the new current view
                    // Print available commands
                }
            }
            else if(yourTurn.get()){
                // Show the current view
                // Print your turn message
                // Print available commands
            }

            if(isCorrectInput(input)){
                correctInput = true;
            }
        } while (!correctInput);

        // Handle the correct input.
        // Input is already completely inserted and checked for both availability and syntax correctness

        parsedInput = CommandEnum.valueOf(input.split(" ")[0]);
        switch (parsedInput){
            case CHOOSE_ASSISTANT -> {
                int idAssistant = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendAssistantChosen(idAssistant);
            }
            case SELECT_STUDENT -> {
                int chosenStudent = ApplicationHelper.getIntFromString(input.split(" ")[1]);
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
                sender.sendSelectCharacter(position);
            }
            case SELECT_STUDENT_COLOR -> {
                StudentEnum color = ApplicationHelper.getStudentEnumFromString(input.split(" ")[1]);
                sender.sendSelectStudentColor(color);
            }
            case SELECT_ENTRANCE_STUDENTS -> {
                List<Integer> students = ApplicationHelper.getIntListFromString(input.split(" ")[1]);
                sender.sendSelectEntranceStudents(students);
            }
            case SELECT_ISLAND_GROUP -> {
                int idIslandGroup = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendSelectIslandGroup(idIslandGroup);
            }
            case SELECT_STUDENT_ON_CARD -> {
                int selectedStudent = ApplicationHelper.getIntFromString(input.split(" ")[1]);
                sender.sendSelectStudentOnCard(selectedStudent);
            }
            case PLAY_CHARACTER -> {
                sender.sendPlayCharacter();
            }
            default -> showMainGameInterface(); // todo handle this case
        }

    }

    @Override
    public void showGameCommandError() {
        this.commandError = true;
    }

    @Override
    public void showGameCommandSuccess() {

    }


    // </editor-fold>

    //<editor-fold desc="Network Errors">


    @Override
    public void showNetworkError() {
        System.out.println("Connection with Server lost");

    }

    @Override
    public void showUserDisconnected() {
        System.out.println("TODO");
    }

    // </editor-fold>

    @Override
    public void startInterface() {
        showWelcomeScreen();
        showLoginScreen();
        initialConnector.startReceiving();
        //This ends here basically, everything else derives from the receiver
    }

    //<editor-fold desc="Asynchronous methods">

    public void printLobby(LobbyBean lobbyBean) {
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
        System.out.println(gameInitBean.toString());
    }

    @Override
    public void printGameInterface(VirtualViewBean view) {
        //todo get the beans from this method and store them. The main game interface
        // will then use the beans to render the view
        System.out.println(view.toString());
        this.viewBean = view;
    }

    @Override
    public void showItsYourTurn(PhaseEnum phase) {
        System.out.println("It's your turn! Starting your "+ phase.toString()+" phase");
    }

    // </editor-fold>

    //<editor-fold desc="Utility methods">

    /**
     * Gets user input from the stream in the reader, exiting and returning the empty string
     * in case the interruptingCondition is true
     * @param reader the BufferedReader to get input from
     * @param interruptingCondition the condition that exits the method, in case the input wasn't
     *                              read
     * @return the string input by the user, or the empty string if the interruptingCondition was triggered
     */
    public String getInputNonBlocking(BufferedReader reader, AtomicBoolean interruptingCondition){
        String selection = "";
        while(selection.equals("") && !interruptingCondition.get()) {
            try {
                while (!reader.ready() && !interruptingCondition.get()) {
                    Thread.sleep(200);
                }
                if(reader.ready()) selection = reader.readLine();
                //If not, we exited because the game is starting
            } catch (InterruptedException e) {
                //Run the next loop
                System.err.println("Interrupted before receiving input, continuing");
            } catch (IOException e) {
                System.err.println("I/O error, continuing");
            }
        }
        return selection;
    }

    /**
     * Gets user input from the stream in the reader, exiting and returning the empty string
     * in case one of the interruptingConditions is true
     * @param reader the BufferedReader to get input from
     * @param interruptingConditions the conditions that make the method return, in case the input
     *                              wasn't read already
     * @return the string input by the user, or the empty string if one of the interruptingConditions
     * was triggered
     */
    public String getInputNonBlocking(BufferedReader reader, List<AtomicBoolean> interruptingConditions){
        String selection = "";
        while(selection.equals("") && checkInterrupt(interruptingConditions)) {
            try {
                while (!reader.ready() && !checkInterrupt(interruptingConditions)) {
                    Thread.sleep(200);
                }
                if(reader.ready()) selection = reader.readLine();
                //If not, we exited because the game is starting
            } catch (InterruptedException e) {
                //Run the next loop
                System.err.println("Interrupted before receiving input, continuing");
            } catch (IOException e) {
                System.err.println("I/O error, continuing");
            }
        }
        return selection;
    }

    private boolean checkInterrupt(List<AtomicBoolean> interruptingConditions) {
        for(AtomicBoolean condition : interruptingConditions){
            if(condition.get()) return true;
        }
        return false;
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
        List<String> splitInput = Arrays.stream(input.split(" ")).toList();
        String inputCommand;
        CommandEnum actualCommand;
        List<NetworkFieldEnum> requiredFields;

        if(splitInput.size() > 0){
            inputCommand = splitInput.get(0);
            splitInput.remove(0);
        } else return false;

        //Check if command available. Note that the command must match exactly
        if (!availableCommands.contains(CommandEnum.valueOf(inputCommand))) return false;
        else actualCommand = CommandEnum.valueOf(inputCommand);

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

    //temp
    private void requestCommand() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(getOptions());
        getInputNonBlocking(reader, gameInterrupted);
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
        this.inLobby = inLobby;
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
        this.lobbyStarting.set(true);
    }

    @Override
    public void setGameStarting() {
        this.gameStarting.set(true);
    }

    @Override
    public void setGameInterrupted(boolean alive) {
        gameInterrupted.set(alive);
    }

    @Override
    public void setUpdateAvailable(boolean available) {
        updateAvailable.set(available);
    }

    @Override
    public void setYourTurn(boolean isYourTurn) {
        yourTurn.set(isYourTurn);
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
}
