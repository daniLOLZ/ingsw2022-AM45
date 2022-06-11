package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.client.ClientNetworkManager;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.client.InitialConnector;
import org.junit.experimental.theories.DataPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLI implements UserInterface {
    private StringBuilder View;
    private StringBuilder LastView;
    private StringBuilder LastElement;
    private final int startPosition = 0;
    private final int centerPosition = 10;
    private List<GameElementBean> beans;
    private List<CommandEnum> availableCommands;
    AtomicBoolean lobbyStarting;
    AtomicBoolean gameStarting;

    private String chosenNickname;
    private int numberOfPlayers;
    private String gameMode;
    private boolean inLobby;
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

        numberOfPlayers = 0;
        gameMode = "No game started yet";

        lobbyStarting = new AtomicBoolean(false);
        gameStarting = new AtomicBoolean(false);
        this.initialConnector = initialConnector;
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
                """, this.numberOfPlayers, this.gameMode));

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
//              showGameInterface();
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
        requestCommand();
    }

    private void requestCommand() {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println(getOptions());
//        getInputNonBlocking(reader, lobbyStarting);
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
        //todo stub
        GameBoardBean simpleGame = view.getGameBoardBean();
        AdvancedGameBoardBean advancedGame = view.getAdvancedGameBoardBean();
        List<CloudBean> clouds = view.getCloudBeans();
        List<PlayerBean> simplePlayers = view.getPlayerBeans();
        List<AdvancedPlayerBean> advancedPlayers = view.getAdvancedPlayerBeans();
        List<IslandGroupBean> simpleIslands = view.getIslandGroupBeans();
        List<AdvancedIslandGroupBean> advancedIslands = view.getAdvancedIslandGroupBeans();
        List<CharacterCardBean> characterCardBeans = view.getCharacterCardBeans();

        //Show gameBoardBean.
        if(simpleGame != null)
            System.out.println(simpleGame);
        else
            System.out.println(advancedGame);

        //Show Character Cards
        StringBuilder characterString = new StringBuilder();
        int done= 0;
        while (done < characterCardBeans.size()) {
            characterString = new StringBuilder();
            characterString.append("\n--------------------\t\t--------------------\t\t--------------------\n");
            characterString.append(characterCardBeans.get(done).getName());
            if(characterCardBeans.get(done).getName().length() >= 8)
                characterString.append("\t\t\t\t\t");
            else
                characterString.append("\t\t\t\t\t\t");
            characterString.append(characterCardBeans.get(done + 1).getName());
            if(characterCardBeans.get(done + 1).getName().length() >= 8)
                characterString.append("\t\t\t\t\t");
            else
                characterString.append("\t\t\t\t\t\t");
            characterString.append(characterCardBeans.get(done + 2).getName());
            characterString.append("\nCost: ").append(characterCardBeans.get(done).getCost()).
                    append("\t\t\t\t\t\t").append("Cost: ").append(characterCardBeans.get(done + 1).getCost()).
                    append("\t\t\t\t\t\t").append("Cost: ").append(characterCardBeans.get(done + 2).getCost()).append("\n");
            if(characterCardBeans.get(done).getStudents()!= null &&
                    !characterCardBeans.get(done).getStudents().isEmpty())
                characterString.append(characterCardBeans.get(done).getStudents());
            else
                characterString.append("\t\t\t\t\t\t\t");

            if(characterCardBeans.get(done + 1).getStudents()!= null &&
                    !characterCardBeans.get(done + 1).getStudents().isEmpty())
                characterString.append("\t\t").append(characterCardBeans.get(done+1).getStudents());
            else
                characterString.append("\t\t\t\t\t\t\t");

            if(characterCardBeans.get(done + 2).getStudents()!= null &&
                    !characterCardBeans.get(done + 2).getStudents().isEmpty())
                characterString.append("\t\t").append(characterCardBeans.get(done+2).getStudents());
            characterString.append("\n--------------------\t\t--------------------\t\t--------------------\n");

            done+=3;
        }
        System.out.println(characterString.toString());

        //Show clouds
        if(clouds.size() == 2){
        String offsetStud = "    ";
        CloudBean cloudBean = clouds.get(0);
        CloudBean cloudBean2 = clouds.get(1);
        List<StudentEnum> list = cloudBean.getStudents();
        List<StudentEnum> list2 = cloudBean2.getStudents();
        StringBuilder cloudsString = new StringBuilder();
        cloudsString.append("---------------------\t\t---------------------\n");
        cloudsString.append("CLOUD: ").append(cloudBean.getIdCloud())
                .append("\t\t\t\t\tCLOUD: ").append(cloudBean2.getIdCloud());
        cloudsString.append("\nStudents:").append(list);
        if(!list.isEmpty())
            cloudsString.append(offsetStud);
        else
            cloudsString.append("\t\t\t\t");
        cloudsString.append("\tStudents:").append(list2);
        cloudsString.append("\n---------------------\t\t---------------------\n");
        System.out.println(cloudsString);
        }

        else{
            String offsetStud = "    ";
            CloudBean cloudBean = clouds.get(0);
            CloudBean cloudBean2 = clouds.get(1);
            CloudBean cloudBean3 = clouds.get(2);
            List<StudentEnum> list = cloudBean.getStudents();
            List<StudentEnum> list2 = cloudBean2.getStudents();
            List<StudentEnum> list3 = cloudBean3.getStudents();
            StringBuilder cloudsString = new StringBuilder();
            cloudsString.append("---------------------\t\t---------------------\t\t---------------------\n");
            cloudsString.append("CLOUD: ").append(cloudBean.getIdCloud())
                    .append("\t\t\t\t\tCLOUD: ").append(cloudBean2.getIdCloud())
                    .append("\t\t\t\t\tCLOUD: ").append(cloudBean3.getIdCloud());
            cloudsString.append("\nStudents:").append(list);
            if(!list.isEmpty())
                cloudsString.append(offsetStud);
            else
                cloudsString.append("\t\t\t\t");
            cloudsString.append("\tStudents:").append(list2);
            if(!list2.isEmpty())
                cloudsString.append(offsetStud);
            else
                cloudsString.append("\t\t\t\t");
            cloudsString.append("\tStudents:").append(list3);
            cloudsString.append("\n---------------------\t\t---------------------\t\t---------------------\n");
            System.out.println(cloudsString);
        }

        //ShowIslands
        if(simpleIslands != null){
            int MN = simpleIslands.stream().filter(x -> x.isPresentMN()).findFirst().get().getIdIslandGroup();
            System.out.println("\nMother nature is on Island " + MN);
            int numIslands = simpleIslands.size();
            done = 0;
            StringBuilder islandString = new StringBuilder();
            while(done < numIslands){


                if(done + 4 < numIslands){

                    List<String> list1 = studentsToNumStud(simpleIslands.get(done).getStudentsOnIsland());
                    List<String> list2 = studentsToNumStud(simpleIslands.get(done + 1).getStudentsOnIsland());
                    List<String> list3 = studentsToNumStud(simpleIslands.get(done + 2).getStudentsOnIsland());
                    List<String> list4 = studentsToNumStud(simpleIslands.get(done + 3).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(simpleIslands.get(done).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(simpleIslands.get(done + 1).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(simpleIslands.get(done + 2).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(simpleIslands.get(done + 3).getIdIslands());
                    islandString.append("\nTowers: ").append(simpleIslands.get(done).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(simpleIslands.get(done + 1).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(simpleIslands.get(done + 2).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(simpleIslands.get(done + 3).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list2);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list3);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list4);

                    islandString.append("\n---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    done+=4;

                }


                else if(done + 3 < numIslands){

                    List<String> list1 = studentsToNumStud(simpleIslands.get(done).getStudentsOnIsland());
                    List<String> list2 = studentsToNumStud(simpleIslands.get(done + 1).getStudentsOnIsland());
                    List<String> list3 = studentsToNumStud(simpleIslands.get(done + 2).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(simpleIslands.get(done).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(simpleIslands.get(done + 1).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(simpleIslands.get(done + 2).getIdIslands());
                    islandString.append("\nTowers: ").append(simpleIslands.get(done).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(simpleIslands.get(done + 1).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(simpleIslands.get(done + 2).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list2);
                    islandString.append("\t\t\t");

                    islandString.append("Students: ").append(list3);

                    islandString.append("\n---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    done+=3;

                }

                else if(done + 2 < numIslands){
                    List<String> list1 = studentsToNumStud(simpleIslands.get(done).getStudentsOnIsland());
                    List<String> list2 = studentsToNumStud(simpleIslands.get(done + 1).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\t\t>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(simpleIslands.get(done).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(simpleIslands.get(done + 1).getIdIslands());
                    islandString.append("\nTowers: ").append(simpleIslands.get(done).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(simpleIslands.get(done + 1).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list2);


                    islandString.append("\n---------------------------=>\t\t>---------------------------=>\n");
                    done+=2;
                }

                else{
                    List<String> list1 = studentsToNumStud(simpleIslands.get(done).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(simpleIslands.get(done).getIdIslands());
                    islandString.append("\nTowers: ").append(simpleIslands.get(done).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);

                    islandString.append("\n---------------------------=>\n");
                    done++;
                }
            }

            System.out.println(islandString);
        }
        else if(advancedIslands != null){
            int MN = advancedIslands.stream().filter(x -> x.isPresentMN()).findFirst().get().getIdIslandGroup();
            System.out.println("\nMother nature is on Island " + MN);
            int numIslands = advancedIslands.size();
            done = 0;
            StringBuilder islandString = new StringBuilder();
            while(done < numIslands){

                if(done + 4 <= numIslands){
                    List<String> list1 = studentsToNumStud(advancedIslands.get(done).getStudentsOnIsland());
                    List<String> list2 = studentsToNumStud(advancedIslands.get(done + 1).getStudentsOnIsland());
                    List<String> list3 = studentsToNumStud(advancedIslands.get(done + 2).getStudentsOnIsland());
                    List<String> list4 = studentsToNumStud(advancedIslands.get(done + 3).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(advancedIslands.get(done).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(advancedIslands.get(done + 1).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(advancedIslands.get(done + 2).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(advancedIslands.get(done + 3).getIdIslands());
                    islandString.append("\nTowers: ").append(advancedIslands.get(done).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(advancedIslands.get(done + 1).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(advancedIslands.get(done + 2).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(advancedIslands.get(done + 3).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list2);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list3);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list4);

                    islandString.append("\nBlocks: ").append(advancedIslands.get(done).getNumBlockTiles()).
                            append("\t\t\t\t\t\t\tBlocks: ").append(advancedIslands.get(done + 1).getNumBlockTiles()).
                            append("\t\t\t\t\t\t\tBlocks: ").append(advancedIslands.get(done + 2).getNumBlockTiles()).
                            append("\t\t\t\t\t\t\tBlocks: ").append(advancedIslands.get(done + 3).getNumBlockTiles());



                    islandString.append("\n---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    done+=4;

                }

                else if(done + 3 <= numIslands){
                    List<String> list1 = studentsToNumStud(advancedIslands.get(done).getStudentsOnIsland());
                    List<String> list2 = studentsToNumStud(advancedIslands.get(done + 1).getStudentsOnIsland());
                    List<String> list3 = studentsToNumStud(advancedIslands.get(done + 2).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(advancedIslands.get(done).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(advancedIslands.get(done + 1).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(advancedIslands.get(done + 2).getIdIslands());
                    islandString.append("\nTowers: ").append(advancedIslands.get(done).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(advancedIslands.get(done + 1).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(advancedIslands.get(done + 2).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list2);
                    islandString.append("\t\t\t");

                    islandString.append("Students: ").append(list3);

                    islandString.append("\nBlocks: ").append(advancedIslands.get(done).getNumBlockTiles()).
                            append("\t\t\t\t\t\t\tBlocks: ").append(advancedIslands.get(done + 1).getNumBlockTiles()).
                            append("\t\t\t\t\t\t\tBlocks: ").append(advancedIslands.get(done + 2).getNumBlockTiles());



                    islandString.append("\n---------------------------=>\t\t>---------------------------=>\t\t>---------------------------=>\n");
                    done+=3;

                }

                else if(done + 2 <= numIslands){
                    List<String> list1 = studentsToNumStud(advancedIslands.get(done).getStudentsOnIsland());
                    List<String> list2 = studentsToNumStud(advancedIslands.get(done + 1).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\t\t>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(advancedIslands.get(done).getIdIslands()).
                            append("\t\t\t\t\t\t\tISLAND: ").append(advancedIslands.get(done + 1).getIdIslands());
                    islandString.append("\nTowers: ").append(advancedIslands.get(done).getTowersColor()).
                            append("\t\t\t\t\t\tTowers: ").append(advancedIslands.get(done + 1).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);
                    islandString.append("\t\t\t");
                    islandString.append("Students: ").append(list2);
                    islandString.append("\nBlocks: ").append(advancedIslands.get(done).getNumBlockTiles()).
                            append("\t\t\t\t\t\t\tBlocks: ").append(advancedIslands.get(done + 1).getNumBlockTiles());



                    islandString.append("\n---------------------------=>\t\t>---------------------------=>\n");
                    done+=2;

                }

                else{
                    List<String> list1 = studentsToNumStud(advancedIslands.get(done).getStudentsOnIsland());

                    islandString.append("\n>---------------------------=>\n");
                    islandString.append("ISLAND: ").append(advancedIslands.get(done).getIdIslands());
                    islandString.append("\nTowers: ").append(advancedIslands.get(done).getTowersColor());
                    islandString.append("\nStudents: ").append(list1);
                    islandString.append("\nBlocks: ").append(advancedIslands.get(done).getNumBlockTiles());

                    islandString.append("\n---------------------------=>\n");
                    done++;

                }

            }

            System.out.println(islandString);
        }


        //Show Players
        if(simplePlayers != null){
            done = 1;
            StringBuilder playerString = new StringBuilder();
            playerString.append("\n--------------------\t\t");
            while(done < simplePlayers.size()){
                playerString.append("\t\t--------------------");
                done++;
            }

            playerString.append(simplePlayers.get(0).getPlayerId());
            done=1;
            while (done < simplePlayers.size()){
                playerString.append("\t\t\t\t").
                        append(simplePlayers.get(done).getPlayerId());
                done++;
            }

            playerString.append("\n");
            playerString.append(simplePlayers.get(0).getNickname());
            done=1;
            while (done < simplePlayers.size()){
                playerString.append("\t\t\t\t").
                    append(simplePlayers.get(done).getNickname());
                done++;
            }
            playerString.append("\n");

            playerString.append("Color: ").
                    append(simplePlayers.get(0).getTowerColor());
            done=1;
            while (done < simplePlayers.size()){
                playerString.append("\t\t\t\t").
                        append("Color: ").
                        append(simplePlayers.get(done).getTowerColor());
                done++;
            }
            playerString.append("\n");



            List<StudentEnum> list1 = new ArrayList<>();
            List<StudentEnum> list2 = new ArrayList<>();

            for(int i=0; i < 4; i++ )
                list1.add(simplePlayers.get(0).getStudentsAtEntrance().get(i));


            playerString.append("Entrance: ").append(list1);
            done=1;
            while (done < simplePlayers.size()){
                list1.clear();
                for(int i=0; i < 4; i++ )
                    list1.add(simplePlayers.get(done).getStudentsAtEntrance().get(i));


                playerString.append("\t\t\t\t").
                        append("Entrance: ").append(list1);
                done++;
            }
            playerString.append("\n");


            for(int i=4; i < simplePlayers.get(0).getStudentsAtEntrance().size(); i++ )
                list2.add(simplePlayers.get(0).getStudentsAtEntrance().get(i));


            playerString.append(list2);
            done=1;
            while (done < simplePlayers.size()){
                list2.clear();
                for(int i=0; i < simplePlayers.get(done).getStudentsAtEntrance().size(); i++ )
                    list2.add(simplePlayers.get(done).getStudentsAtEntrance().get(i));

                playerString.append("\t\t\t\t").
                        append(list2);
                done++;
            }
            playerString.append("\n");

            System.out.println(playerString);


        }
        else if(advancedPlayers != null){


            done = 1;
            StringBuilder playerString = new StringBuilder();
            playerString.append("\n--------------------");
            while(done < advancedPlayers.size()){
                playerString.append("\t\t--------------------");
                done++;
            }

            playerString.append("\n");


            playerString.append(advancedPlayers.get(0).getPlayerId());
            done=1;
            while (done < advancedPlayers.size()){
                playerString.append("\t\t\t\t\t\t").
                        append(advancedPlayers.get(done).getPlayerId());
                done++;
            }

            playerString.append("\n");
            playerString.append(advancedPlayers.get(0).getNickname());
            done=1;
            while (done < advancedPlayers.size()){
                playerString.append("\t\t\t\t\t\t").
                        append(advancedPlayers.get(done).getNickname());
                done++;
            }
            playerString.append("\n");

            playerString.append("Color: ").
                    append(advancedPlayers.get(0).getTowerColor());
            done=1;
            while (done < advancedPlayers.size()){
                playerString.append("\t\t\t\t").
                        append("Color: ").
                        append(advancedPlayers.get(done).getTowerColor());
                done++;
            }
            playerString.append("\n");

            playerString.append("Coins: ").
                    append(advancedPlayers.get(0).getNumCoins());
            done=1;
            while (done < advancedPlayers.size()){
                playerString.append("\t\t\t\t\t").
                        append("Coins: ").
                        append(advancedPlayers.get(done).getNumCoins());
                done++;
            }
            playerString.append("\n");



            List<StudentEnum> list1 = new ArrayList<>();
            List<StudentEnum> list2 = new ArrayList<>();

            if(!advancedPlayers.get(0).getStudentsAtEntrance().isEmpty())
            for(int i=0; i < 4; i++ )
                list1.add(advancedPlayers.get(0).getStudentsAtEntrance().get(i));


            playerString.append("Entrance: ").append(list1);
            done=1;
            while (done < advancedPlayers.size()){
                list1.clear();
                if(!advancedPlayers.get(done).getStudentsAtEntrance().isEmpty())
                    for(int i=0; i < 4; i++ )
                    list1.add(advancedPlayers.get(done).getStudentsAtEntrance().get(i));


                if(advancedPlayers.get(done - 1 ).getStudentsAtEntrance().size() > 0)
                    playerString.append("\t\t");
                else
                playerString.append("\t\t\t\t");
                playerString.append("Entrance: ").append(list1);
                done++;
            }
            playerString.append("\n");

            if(!advancedPlayers.get(0).getStudentsAtEntrance().isEmpty())
            for(int i=4; i < advancedPlayers.get(0).getStudentsAtEntrance().size(); i++ )
                list2.add(advancedPlayers.get(0).getStudentsAtEntrance().get(i));


            if(!list2.isEmpty())
                playerString.append(list2);

            done=1;
            while (done < advancedPlayers.size()){
                list2.clear();
                if(!advancedPlayers.get(done).getStudentsAtEntrance().isEmpty())
                    for(int i=4; i < advancedPlayers.get(done).getStudentsAtEntrance().size(); i++ )
                    list2.add(advancedPlayers.get(done).getStudentsAtEntrance().get(i));


                playerString.append("\t\t\t\t");
                if(!list2.isEmpty())
                    playerString.append(list2);
                done++;
            }
            playerString.append("\n");
            playerString.append("Table:[");
            for(int i=0; i < StudentEnum.getNumStudentTypes(); i++ )
                playerString.append(StaticColorCLI.getColor(i)).
                        append(advancedPlayers.get(0).getStudentsPerTable().get(i)).append(" ");
            playerString.append(StaticColorCLI.ANSI_RESET).append("]");
            done=1;
            while (done < advancedPlayers.size()){
                playerString.append("\t\t\t");
                playerString.append("Table: [");
                for(int i=0; i < StudentEnum.getNumStudentTypes(); i++ )
                    playerString.append(StaticColorCLI.getColor(i)).
                            append(advancedPlayers.get(done).getStudentsPerTable().get(i)).append(" ");
                playerString.append(StaticColorCLI.ANSI_RESET).append("]");


                done++;
            }

            playerString.append("\n");

            System.out.println(playerString);
        }

        //System.out.println(view.toString());
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

    // </editor-fold>


    @Override
    public void setSender(ClientSender sender) {
        this.sender = sender;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
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
        availableCommands.add(command);
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

    private String sideBySide(String left, String right ){
        final int width = 4;
        final int position = 1;
        final String tab ="    ";

        Scanner moreRowsLeftElement = new Scanner(left);
        Scanner moreRowsRightElement = new Scanner(right);
        moreRowsLeftElement.useDelimiter("\n");
        moreRowsRightElement.useDelimiter("\n");
        int leftElementRows = 0;
        int rightElementRows = 0;
        while(moreRowsLeftElement.hasNext()){
            moreRowsLeftElement.next();
            leftElementRows++;
        }

        while(moreRowsRightElement.hasNext()){
            moreRowsRightElement.next();
            rightElementRows++;
        }

        if(rightElementRows > leftElementRows){
            String toggle = right;
            right = left;
            left = right;
        }


        //Old element to show in left position on screen
        Scanner scannerLastElement = new Scanner(left);
        scannerLastElement.useDelimiter("\n");

        //new element to show in right position on screen
        Scanner scannerThisElement = new Scanner(right);
        scannerThisElement.useDelimiter("\n");

        //OFFSET
        StringBuilder offsetBuilder = new StringBuilder();
        for(int i = 0; i < width * position; i++){
            offsetBuilder.append(tab);
        }

        String offset = offsetBuilder.toString();
        StringBuilder newString = new StringBuilder();

         /*
        now for each left element's row I append the right element's row with an offset.
         */
        while(scannerLastElement.hasNext() || scannerThisElement.hasNext()){
            if(!scannerThisElement.hasNext())
                newString.append(scannerLastElement.next()).append("\n");
            else
                newString.append(scannerLastElement.next()).append(offset).append(scannerThisElement.next()).append("\n");
        }

        return newString.toString();

    }
}
