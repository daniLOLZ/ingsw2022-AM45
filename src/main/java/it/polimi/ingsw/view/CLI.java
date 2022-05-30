package it.polimi.ingsw.view;

import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.*;

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
    private ClientNetworkManager networkManager;
    private String chosenNickname;
    private int numberOfPlayers;
    private String gameMode;
    private final String colorList3Players = "B - Black, G - Grey, W - White";
    private final String colorList2or4Players = "B - Black, W - White";

    /**
     * Network-less constructor, used for testing
     */
    public CLI(){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
        View = new StringBuilder("");
        LastView = new StringBuilder("");
        LastElement = new StringBuilder();
    }

    /**
     * Creates a new CLI and calls the method to create a new NetworkManager
     * @param hostname the name of the host
     * @param port the port to connect to
     */
    public CLI(String hostname, int port){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
        networkManager = new ClientNetworkManager(hostname, port);
        View = new StringBuilder("");
        LastView = new StringBuilder("");
        LastElement = new StringBuilder();
        numberOfPlayers = 0;
        gameMode = "No game started yet";
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


    @Override
    public void showWelcomeScreen() {
        System.out.println("-- WELCOME TO ERIANTYS! --");
    }

    @Override
    public void showLoginScreen() {
        Scanner scanner = new Scanner(System.in);
        String inputNickname;
        boolean errorLogin = false;

        do{
            System.out.println("Insert your username: ");
            inputNickname = scanner.nextLine();
            if(!networkManager.login(networkManager.getHostname(), networkManager.getPortNumber(), inputNickname)){
                errorLogin = true;
                System.out.println("Username was rejecetd cus it sucks, choose another one");
            }
            else errorLogin = false;

        } while(errorLogin);

        chosenNickname = inputNickname;
        System.out.println("Username " + inputNickname + " was accepted");
    }

    @Override
    public void showGameruleSelection() {
        Scanner scanner = new Scanner(System.in);
        String gameMode;
        String numPlayers;
        boolean errorChoice, serverError;
        do {
            errorChoice = false;
            serverError = false;

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

            if (!networkManager.sendGameModePreference(Integer.parseInt(gameMode),Integer.parseInt(numPlayers))) {
                serverError = true;
                System.out.println("There was a problem sending the gamemode preferences, please try again");
            }
        } while(serverError);
        this.numberOfPlayers = Integer.parseInt(numPlayers);
        this.gameMode = Integer.parseInt(gameMode) == 1 ? "Simple" : "Advanced"; // find a nicer way
    }

    @Override
    public void showLobby() {
        Scanner scanner = new Scanner(System.in);
        AtomicBoolean ready = new AtomicBoolean(false);
        AtomicBoolean gameStarting = new AtomicBoolean(false);
        String selection;
        System.out.println(MessageFormat.format("""
                Game type selected:
                    Number of players : {0}
                    Game type : {1}
                
                You are waiting for players to join a game with you...
                1 - Set yourself as ready
                2 - Set yourself as not ready
                S - Try to start the game
                """, this.numberOfPlayers, this.gameMode));

        //todo: See if this could be an "interactive" wait, where you can see the players joining, or even
        // just a simple counter
        // make a thread for the updating of the lobby and for checking whether or not the game is starting here

        new Thread(()-> {

            // Lucario: We might not need to check for sameness here, the server should only update if the
            // lobby was updated (if there's time to implement that)
            LobbyBean lobbyBean;
            LobbyBean oldLobbyBean = new LobbyBean(null, null, false, null);

            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("Interrupted before waiting the full length, requesting lobby updates now");
                }
                /*
                if(gameStarting.get()){
                    //This is necessary for the host, for which the sendReadyStatus won't
                    // stop the thread, since its server-side game state already changed
                    return;
                }
                 */
                if(networkManager.sendReadyStatus(ready.get())){
                    //signal we're starting the game
                    gameStarting.set(true);
                    return;
                }
                lobbyBean = networkManager.getLobbyUpdates();
                if(lobbyBean != null && !lobbyBean.equals(oldLobbyBean)) {
                    System.out.println(lobbyBean.toString());
                    oldLobbyBean = lobbyBean;
                }
            }
        }).start();

        while(true) {
            selection = scanner.next();

            // If the game started, any input is actually ignored
            if(gameStarting.get()){
                //TODO: This is probably an ugly way of exchanging information across threads
                // The player won't be notified the game is starting until they actually
                // make a selection, even though it will get discarded
                break;
            }

            if (selection.equals("1")) {
                ready.set(true);
                System.out.println("You set yourself as ready");
            }
            else if (selection.equals("2")){
                ready.set(false);
                System.out.println("You set yourself as not ready");
            }
            else if (selection.toUpperCase(Locale.ROOT).equals("S")) {
                if(networkManager.startGame()){
                    gameStarting.set(true);
                    System.out.println("Everyone is ready!");
                    break;
                }
                else {
                    System.out.println("The game  couldn't start");
                }
            } else continue;
        }

    }

    private void printLobby(LobbyBean lobbyBean) {
        for(int index = 0; index < lobbyBean.getNicknames().size(); index++){
            String optionalNot = "";
            if(!lobbyBean.getReadyPlayers().get(index)) optionalNot = " not";

            System.out.println(String.format("User %s is%s ready.",
                    lobbyBean.getNicknames().get(index),
                    optionalNot));
        }
    }

    @Override
    public void showTowerAndWizardSelection() {

        Scanner scanner = new Scanner(System.in);
        String currentTower = TeamEnum.NOTEAM.name;
        String currentWizard = WizardEnum.NO_WIZARD.name;
        AtomicBoolean gameStarting = new AtomicBoolean(false);
        String selection = null;
        String colorList = numberOfPlayers == 3 ? colorList3Players : colorList2or4Players;


        //Start periodically fetching initialization information
        new Thread(()->{
            // Lucario: Same as before, again if we have time
            GameInitBean initBean;
            GameInitBean oldInitBean = new GameInitBean(null, null, false);

            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("Interrupted before waiting the full length, requesting initialization updates now");
                }

                initBean = networkManager.getGameInitUpdates();

                //Check whether all players have made their selection and if so, signal that the game is starting
                if(initBean.isAllSetGameStarted()){
                    gameStarting.set(true);
                    break;
                }

                if(!initBean.equals(oldInitBean)) {
                    System.out.println(initBean.toString());
                    oldInitBean = initBean;
                }

            }
        }).start();

        while(true){
            System.out.println(MessageFormat.format("""
                Select your team color ({2}) 
                and wizard (1 - King, 2 - Pixie, 3 - Sorcerer, 4 - Wizard)
                (one at a time):
                
                Your selection : 
                    Team color : {0}
                    Wizard : {1} 
                """, currentTower, currentWizard, colorList));
            selection = scanner.nextLine();

            //todo Same as before, the cli waits to notify the user that the game started
            // until after they made another selection because scanner.nextLine is blocking
            if(gameStarting.get()){
                System.out.println("Everyone made their choice, the game is starting!");
                return;
            }

            switch (selection) {
                //Team color
                case "B":
                    if(networkManager.sendTeamColorChoice(TeamEnum.BLACK)){
                        currentTower = TeamEnum.BLACK.name;
                        System.out.println("You chose the black team.");
                    }
                    break;
                case "G":
                    if(numberOfPlayers != 3){ //we do a little hardcoding
                        System.out.println("Please make a valid selection");
                        break;
                    }
                    if(networkManager.sendTeamColorChoice(TeamEnum.GREY)){
                        currentTower = TeamEnum.GREY.name;
                        System.out.println("You chose the grey team.");
                    }
                    break;
                case "W":
                    if(networkManager.sendTeamColorChoice(TeamEnum.WHITE)){
                        currentTower = TeamEnum.WHITE.name;
                        System.out.println("You chose the white team.");
                    }
                    break;
                //Wizard
                case "1":
                    if(networkManager.sendWizardChoice(WizardEnum.KING)){
                        currentWizard = WizardEnum.KING.name;
                        System.out.println("You chose the king");
                    }
                    break;
                case "2":
                    if(networkManager.sendWizardChoice(WizardEnum.PIXIE)){
                        currentWizard = WizardEnum.PIXIE.name;
                        System.out.println("You chose the pixie");
                    }
                    break;
                case "3":
                    if(networkManager.sendWizardChoice(WizardEnum.SORCERER)){
                        currentWizard = WizardEnum.SORCERER.name;
                        System.out.println("You chose the sorcerer");
                    }
                    break;
                case "4":
                    if(networkManager.sendWizardChoice(WizardEnum.WIZARD)){
                        currentWizard = WizardEnum.WIZARD.name;
                        System.out.println("You chose the wizard");
                    }
                    break;
                default:
                    System.out.println("Please make a valid selection");
                    break;
            }
        }

    }

    @Override
    public void showGameInterface() {
        System.out.println("started woohoo");
    }

    @Override
    public void startInterface() {

        showWelcomeScreen();
        showLoginScreen();
        showGameruleSelection();
        showLobby();
        showTowerAndWizardSelection();
        showGameInterface();

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
}
