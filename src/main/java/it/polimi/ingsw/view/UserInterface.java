package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.CommandEnum;

/**
 * This class is an interface to what the client will see, it contains a NetworkManager which
 * takes requests from the View and sends them to the server
 */
public interface UserInterface {

    void addBean(GameElementBean bean);

    Bean removeBean(int index);

    void clearBeans();

    void addCommand(CommandEnum command);

    CommandEnum removeCommand(int index);

    void clearCommands();

    /**
     * Sets the message sender for this UI
     * @param sender the sender that will communicate with the server
     */
    public void setSender(ClientSender sender);

    /**
     * Shows a welcome screen for the game
     */
    void showWelcomeScreen();

    /**
     * Shows the login screen;
     * This screen should allow the player to enter their desired username
     */
    void showLoginScreen();

    /**
     * To show in case the login procedure failed
     * It will then call showLoginScreen
     */
    void showLoginScreenFailure();

    /**
     * To show in case the login procedure succeeded
     * It will then call showGameruleSelection
     */
    void showSuccessLoginScreen();

    /**
     * Shows the selection of the game rules
     * Here the user can select whether the game is simple or advanced
     * and the number of players to play with
     */
    void showGameruleSelection();

    /**
     * This method is called if the user was able to join a lobby successfully
     * It will then call showLobby
     */
    void showSuccessJoiningLobby();

    /**
     * This method is called if the user was not able to join a lobby
     * It will then call showGameruleSelection
     */
    void showErrorJoiningLobby();

    /**
     * Once a lobby with the given rules is found,
     * this screen will let the user decide if they're ready,
     * start the game if they're the host and see how many players are ready
     * in the lobby
     */
    void showLobby();

    /**
     * Shows that the status was successfully received by the server
     * @param status the status that the user is currently in
     *               true if the choice was "ready"
     */
    void showSuccessReadyStatus(boolean status);

    /**
     * Shows that the status was not correctly received by the server
     * @param status the status that the user tried to send
     *               true if the choice was "ready"
     */
    void showErrorReadyStatus(boolean status);

    /**
     * Shows that the request to start the game was successful
     */
    void showSuccessStartGame();

    /**
     * Shows that the request to start the game wasn't successful
     */
    void showErrorStartGame();

    void showSuccessLeaveLobby();

    void showErrorLeaveLobby();

    /**
     * This screen lets the user choose the wizard and tower color
     * for the game
     */
    void showTowerAndWizardSelection();

    /**
     * Shows an error with the color selection, calls showTowerAndWizardSelection
     * @param color the color chosen
     */
    void showErrorSelectingColor(String color);

    /**
     * Shows success with the color selection, calls showTowerAndWizardSelection
     * @param color the color chosen
     */
    void showSuccessSelectingColor(String color);

    /**
     * SShows an error with the wizard selection, calls showTowerAndWizardSelection
     * @param wizard the wizard chosen
     */
    void showErrorSelectingWizard(String wizard);

    /**
     * Shows success with the wizard selection, calls showTowerAndWizardSelection
     * @param wizard the wizard chosen
     */
    void showSuccessSelectingWizard(String wizard);

    /**
     * The main game interface
     */
    void showGameInterface();

    /**
     * Initializes and starts the application interface
     * it will call showWelcomeScreen, showLoginScreen and start the message parser with
     * initialConnector.startReceiving()
     */
    void startInterface();

    /**
     * This screen will be shown when an error occurred, the user will
     * then be brought back to the login screen
     */
    void showNetworkError();

    void showUserDisconnected();

    // Asynchronous methods

    void printLobby(LobbyBean lobbyBean);

    void printGameInitInfo(GameInitBean gameInitBean);

    // Setters

    void setChosenNickname(String nickname);

    void setNumberOfPlayers(int numberOfPlayers);

    void setGameMode(String gameMode);

    void setTeamColor(String teamColor);

    void setWizard(String wizard);

    void setInLobby(boolean inLobby);

    /**
     * Used to notify the UI that the lobby has started and that any previous methods
     * should return (via the lobbyStarting flag which is set to true)
     */
    void setLobbyStarting();

    /**
     * Used to notify the UI that the actual game has started and that any previous methods
     * should return (via the gameStarting flag which is set to true)
     */
    void setGameStarting();
}
