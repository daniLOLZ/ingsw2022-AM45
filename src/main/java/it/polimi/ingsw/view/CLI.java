package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLI implements UserInterface {
    private StringBuilder View;
    private StringBuilder LastView;
    private StringBuilder LastElement;
    private final int startPosition = 0;
    private final int centerPosition = 10;
    private List<Bean> beans;
    private List<CommandEnum> availableCommands;
    private ClientNetworkManager networkManager;
    private String chosenNickname;


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
        GameElementBean curr = (GameElementBean) beans.get(0);
        GameElementBean bean;
        int index = 0;
        int min = lowestPriority;

        //I must draw all beans. When I draw one I remove it.
        while(!beans.isEmpty()){
            min = lowestPriority;
            //choosing bean to draw (min priority go first)
            for(int id = 0; id < beans.size(); id++){
                bean = (GameElementBean) beans.get(id);
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
        int gameMode;
        int numPlayers;
        boolean errorChoice, serverError;
        do {
            errorChoice = false;
            serverError = false;

            System.out.println("""
                    Which type of game would you like to play? (Type in the corresponding number)
                    1 - Simple game: No character cards
                    2 - Advanced game: Character cards allowed""");
            do {
                gameMode = scanner.nextInt();
                if (!(gameMode == 1 ||
                        gameMode == 2)) {
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
                numPlayers = scanner.nextInt();
                if (!(numPlayers == 2 ||
                        numPlayers == 3 ||
                        numPlayers == 4)) {
                    errorChoice = true;
                    System.out.println("Wrong choice! Please select a correct amount of players");
                } else errorChoice = false;

            } while (errorChoice);

            if (!networkManager.sendGameModePreference(gameMode, numPlayers)) {
                serverError = true;
                System.out.println("There was a problem sending the gamemode preferences, please try again");
            }
        } while(serverError);
    }

    @Override
    public void showLobby() {
        Scanner scanner = new Scanner(System.in);
        AtomicBoolean ready = new AtomicBoolean(false);
        AtomicBoolean gameStarting = new AtomicBoolean(false);
        String selection;
        System.out.println("""
                You are waiting for players to join a game with you...
                1 - Set yourself as ready
                2 - Set yourself as not ready
                S - Try to start the game
                """);

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
                if(networkManager.sendReadyStatus(ready.get())){
                    //signal we're starting the game
                    gameStarting.set(true);
                    return;
                }
                lobbyBean = networkManager.getLobbyUpdates();
                if(!lobbyBean.equals(oldLobbyBean)) {
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
            else if (selection.equals("S")) {
                networkManager.startGame();
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
        System.out.println("Congrats, at least you got the game to start");
        while(true);
        //todo
    }

    @Override
    public void showGameInterface() {
        //todo
    }

    @Override
    public void startInterface() {

        showWelcomeScreen();
        showLoginScreen();
        showGameruleSelection();
        showLobby();
        showTowerAndWizardSelection();

    }

    @Override
    public void addBean(Bean bean) {
        beans.add(bean);
    }

    @Override
    public Bean removeBean(int index) {
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
