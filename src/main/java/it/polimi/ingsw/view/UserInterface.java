package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an interface to what the client will see, it contains a NetworkManager which
 * takes requests from the View and sends them to the server
 */
public interface UserInterface {

    /*
    public UserInterface(){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
    }
    public UserInterface(String hostname, int port){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
        networkManager = new ClientNetworkManager(hostname, port);
    }
     */

    public void addBean(Bean bean);

    public Bean removeBean(int index);

    public void clearBeans();

    public void addCommand(CommandEnum command);

    public CommandEnum removeCommand(int index);

    public void clearCommands();

    /**
     * Shows a welcome screen for the game
     */
    public abstract void showWelcomeScreen();

    /**
     * Shows the login screen;
     * This screen should allow the player to enter their desired
     * username and receive confirmation from the server
     */
    public abstract void showLoginScreen();

    /**
     * Shows the selection of the game rules
     * Here the user can select whether the game is simple or advanced
     * and the number of players to play with
     */
    public abstract void showGameruleSelection();

    /**
     * Once a lobby with the given rules is found,
     * this screen will let the user decide if they're ready,
     * start the game if they're the host and see how many players are ready
     * in the lobby
     */
    public abstract void showLobby();

    /**
     * This screen lets the user choose the wizard and tower color
     * for the game
     */
    public abstract void showTowerAndWizardSelection();

    /**
     * The main game interface
     */
    public abstract void showGameInterface();

    /**
     * Initializes and starts the application interface
     */
    public abstract void startInterface();

}
