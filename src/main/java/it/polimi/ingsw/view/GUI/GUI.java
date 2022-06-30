package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.client.InitialConnector;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
import it.polimi.ingsw.view.UserInterface;

import javafx.application.Application;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class GUI implements UserInterface {

    private static final int
            numAssistants = 10,
            entranceSlots2_4Players = 7,
            entranceSlots3Players = 9,
            numTables = 5,
            numCharacterCards = 3;

    private boolean
            loginError           = false,
            searchGameError      = false,
            startingGameError    = false,
            leavingLobbyError    = false,
            readyError           = false,
            selectingWizardError = false,
            selectingColorError  = false;

    private GameRuleEnum currentGameRule = GameRuleEnum.NO_RULE;

    private InitialConnector initialConnector;
    private ClientSender sender;

    private GameToolBoxContainer gameToolBoxContainer;

    private LobbyBean lobbyBean;
    private VirtualViewBean viewData;
    private GameInitBean gameInitData;
    private boolean selectedTowerColor = false;
    private boolean yourTurn = false;

    private int
            islandsRequired = 0,
            studentsOnCardRequired = 0,
            studentsAtEntranceRequired = 0,
            colorsRequired = 0;

    private String nickname;

    public GUI(InitialConnector initialConnector){

        this.initialConnector = initialConnector;

        GUIApplication.setInitialConnector(initialConnector);
    }

    @Override
    public void addBean(GameElementBean bean) {

    }

    @Override
    public Bean removeBean(int index) {
        return null;
    }

    @Override
    public void clearBeans() {

    }

    /**
     * Proxy method for allowCommand in GameToolBoxContainer.
     * @param command the command for which the GameToolBoxContainer will provide the handling resource if asked
     */
    @Override
    public void addCommand(CommandEnum command) {
        gameToolBoxContainer.allowCommand(command, sender);
    }

    @Override
    public CommandEnum removeCommand(int index) {
        return null;
    }

    /**
     * Proxy method for clearCommands in GameToolBoxContainer.
     */
    @Override
    public void clearCommands() {
        gameToolBoxContainer.clearCommands();
    }


    /**
     * Sets the sender that will communicate with the server.
     * @param sender the sender that will communicate with the server
     */
    @Override
    public void setSender(ClientSender sender) {
        this.sender = sender;
        GUIApplication.setDefaultSender(sender);
    }

    @Override
    public void showWelcomeScreen() {
        //due to inexperience with JavaFX, I don't know any elegant way to separate this method from startInterface,
        //that's why I'm not implementing this method
    }

    @Override
    public void showGoodbyeScreen() {

    }

    /**
     * Shows the login screen to the user.
     */
    @Override
    public void showLoginScreen() {
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showLoginScreen(loginError));
    }

    /**
     * Will remind the GUI to inform the user that his login attempt failed.
     */
    @Override
    public void showLoginScreenFailure() {
        loginError = true;
    }

    /**
     * Ensures that the interface doesn't display any errors on a successful operation.
     */
    @Override
    public void showSuccessLoginScreen() {
        resetErrors();
    }

    /**
     * Show the game rule selection screen which allows the user to look for a lobby to join (or create a new one)
     */
    @Override
    public void showGameruleSelection() {
        selectedTowerColor = false;
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showSearchGameScreen(searchGameError));
    }

    /**
     * Ensures that the interface doesn't display any errors on a successful operation.
     */
    @Override
    public void showSuccessJoiningLobby() {
        resetErrors();
    }

    /**
     * Will remind the GUI to inform the user that his attempt to join a lobby failed.
     */
    @Override
    public void showErrorJoiningLobby() {
        searchGameError = true;
    }

    /**
     * Displays the lobby with the current available information.
     */
    @Override
    public void showLobby() {
        printLobby(lobbyBean);
    }

    /**
     * Ensures that the interface doesn't display any errors on a successful operation.
     * @param status The status the player wanted to set himself as (ignored here)
     */
    @Override
    public void showSuccessReadyStatus(boolean status) {
        resetErrors();
    }

    /**
     * Will remind the GUI to inform the user that his attempt to set his ready status failed.
     * @param status The status the player wanted to set himself as (ignored here)
     */
    @Override
    public void showErrorReadyStatus(boolean status) {
        readyError = true;
    }

    /**
     * Ensures that the interface doesn't display any errors on a successful operation.
     */
    @Override
    public void showSuccessStartGame() {
        resetErrors();
    }

    /**
     * Will remind the GUI to inform the user that his attempt to start the game failed.
     */
    @Override
    public void showErrorStartGame() {
        startingGameError = true;
    }

    /**
     * Ensures that the interface doesn't display any errors on a successful operation.
     */
    @Override
    public void showSuccessLeaveLobby() {
        resetErrors();
    }

    /**
     * Will remind the GUI to inform the user that his attempt to leave the lobby failed.
     */
    @Override
    public void showErrorLeaveLobby() {
        leavingLobbyError = true;
    }

    /**
     * Show the Wizards and Tower selection to allow the user to make his choice.
     */
    @Override
    public void showTowerAndWizardSelection() {
        if (GUIApplication.isStarted()) printGameInitInfo(gameInitData);

    }

    /**
     * Will remind the GUI to inform the user that his attempt to select a color failed.
     * @param color The color the user wanted to select (ignored here)
     */
    @Override
    public void showErrorSelectingColor(String color) {
        selectingColorError = true;
    }

    /**
     * Ensures that the interface doesn't display any errors on a successful operation.
     * Sets the selectedTowerColor property to true, so the interface will display the Wizard selection screen
     * @param color the color chosen (ignored here)
     */
    @Override
    public void showSuccessSelectingColor(String color) {
        resetErrors();
        selectedTowerColor = true;
    }

    /**
     * Will remind the GUI to inform the user that his attempt to select a color failed.
     * @param wizard The wizard the user wanted to select (ignored here)
     */
    @Override
    public void showErrorSelectingWizard(String wizard) {
        selectingWizardError = true;
        printGameInitInfo(gameInitData);
    }

    /**
     * Ensures that the interface doesn't display any errors on a successful operation.
     */
    @Override
    public void showSuccessSelectingWizard(String wizard) {
        resetErrors();
    }

    /**
     * Starts the interface which automatically shows the welcome screen
     */
    @Override
    public void startInterface() {
        new Thread(() -> Application.launch(GUIApplication.class)).start();
    }

    /**
     * Notifies the user that a network error has occurred and brings him back to the login screen.
     */
    @Override
    public void showNetworkError() {
       if (GUIApplication.isStarted()) {
           Platform.runLater(GUIApplication::showNetworkError);
           // initialConnector.reset(); // maybe here?
           Platform.runLater(this::showLoginScreen);
       }
    }

    /**
     * Notifies that another user disconnected and bring the current user back to the game rule selection screen.
     * @param disconnectedUser the nickname of the user that disconnected
     */
    @Override
    public void showUserDisconnected(String disconnectedUser) {
        if (GUIApplication.isStarted()) {
            Platform.runLater(() -> GUIApplication.showUserDisconnected(disconnectedUser));
            resetErrors();
            showGameruleSelection();
        }

    }

    /**
     * Stores the LobbyBean and uses it to display the lobby screen.
     * @param lobbyBean the updated lobby bean received from the server
     */
    @Override
    public void printLobby(LobbyBean lobbyBean) {

        if (lobbyBean == null) return;

        if (!lobbyBean.equals(this.lobbyBean)) this.lobbyBean = lobbyBean;

        int yourSlot = lobbyBean.getNicknames().indexOf(nickname);

        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showLobbyScreen(lobbyBean, yourSlot, startingGameError, leavingLobbyError));
    }

    /**
     * Stores the GameInitBean and uses it to display the Tower and Wizard selection.
     * @param gameInitBean the updated game init bean received from the server
     */
    @Override
    public void printGameInitInfo(GameInitBean gameInitBean) {

        if (gameInitBean == null) gameInitData = new GameInitBean(TeamEnum.getTeams(), WizardEnum.getWizards());
        else gameInitData = gameInitBean;

        if (GUIApplication.isStarted()) {
            if (selectedTowerColor) {
                Platform.runLater(() -> GUIApplication.showWizardSelection(gameInitData, selectingWizardError));
            } else Platform.runLater(() -> GUIApplication.showTowerColorSelection(gameInitData, selectingColorError));
        }
    }

    /**
     * Stores the user's nickname.
     * @param nickname
     */
    @Override
    public void setChosenNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the game mode of the game.
     * Creates the GameToolBoxContainer in preparation.
     * @param gameMode The selected game mode
     */
    @Override
    public void setGameMode(GameRuleEnum gameMode) {
        CharacterCardSelection selections = new CharacterCardSelection(genericEvent -> showMainGameInterface());
        this.currentGameRule = gameMode;
        gameToolBoxContainer = new GameToolBoxContainer(
                numAssistants,
                GameRuleEnum.getNumPlayers(gameMode.id) == 3 ? entranceSlots3Players : entranceSlots2_4Players,
                numTables,
                GameRuleEnum.getNumPlayers(gameMode.id),
                GameRuleEnum.isAdvanced(gameMode.id) ? numCharacterCards : 0,
                selections);
    }

    @Override
    public void setTeamColor(String teamColor) {

    }

    @Override
    public void setWizard(String wizard) {

    }

    @Override
    public void setInLobby(boolean inLobby) {

    }

    /**
     * Shows the Tower and Wizard selection screen while waiting for the data from the server.
     */
    @Override
    public void setLobbyStarting() {
        showTowerAndWizardSelection();
    }

    /**
     * Shows a mock game interface until data about the game has been received.
     * Resets gameInitData property.
     */
    @Override
    public void setGameStarting() {
        gameInitData = null;
        showMainGameInterface();
    }

    /**
     * Displays the interface using the stored properties.
     */
    @Override
    public void showMainGameInterface() {

        System.out.println("Printing Interface");

        int user = 0;

        if (viewData == null) printGameInterface(VirtualViewBean.getMockBean());

        else {
            List<AdvancedPlayerBean> advancedPlayers = viewData.getAdvancedPlayerBeans();

            if (advancedPlayers != null) {
                for (AdvancedPlayerBean advancedPlayer :
                        advancedPlayers) {
                    if (advancedPlayer.getNickname().equals(nickname)) user = advancedPlayers.indexOf(advancedPlayer);
                }
            } else {
                List<PlayerBean> players = viewData.getPlayerBeans();
                for (PlayerBean player :
                        players) {
                    if (player.getNickname().equals(nickname)) user = players.indexOf(player);
                }
            }
        }

        if (GUIApplication.isStarted()) {
            int finalUser = user;
            Platform.runLater(() -> GUIApplication.showGameInterface(viewData, gameToolBoxContainer, finalUser));
        }
    }

    /**
     * Notifies the user that it's his turn to play.
     * @param phase the phase in which the game is currently in (ignored here)
     */
    @Override
    public void showItsYourTurn(PhaseEnum phase) {

        //todo
        System.out.println("Updated Commands");
        showMainGameInterface();
    }

    /**
     * Sets the requirement for Character Card activation.
     * @param islandsRequired The number of islands required.
     * @param studentsOnCardRequired The number of students on card required.
     * @param studentsAtEntranceRequired The number of students at entrance required.
     * @param colorsRequired The number of colors required.
     */
    @Override
    public void setCardRequirements(int islandsRequired, int studentsOnCardRequired, int studentsAtEntranceRequired, int colorsRequired) {

        this.islandsRequired = islandsRequired;
        this.studentsOnCardRequired = studentsOnCardRequired;
        this.studentsAtEntranceRequired = studentsAtEntranceRequired;
        this.colorsRequired = colorsRequired;

        GUIApplication.setMaxRequirement(this.islandsRequired, this.studentsOnCardRequired, this.studentsAtEntranceRequired, this.colorsRequired);

    }

    /**
     * Resets the GUI to its previous state before the failed command was called.
     */
    @Override
    public void showGameCommandError() {
        if (GUIApplication.areCharacterCardRequirementsInSelection()) {
            gameToolBoxContainer.resetSelections();
        }
    }

    /**
     * Resets the GUI to its previous state before the failed command was called.
     * @param error the error message (ignored here)
     */
    @Override
    public void showGameCommandError(String error) {
        if (GUIApplication.areCharacterCardRequirementsInSelection()) {
            gameToolBoxContainer.resetSelections();
        }
    }

    @Override
    public void showGameCommandSuccess() {
        System.out.println("Updated Commands");
    }

    /**
     * Stores the VirtualViewBean and sets everything up to display the interface.
     * @param virtualView The Bean containing all relevant information about the game
     */
    @Override
    public void printGameInterface(VirtualViewBean virtualView) {
        System.out.println("Updated VirtualView");
        viewData = virtualView;
        if (virtualView.getCharacterCardBeans() != null) {
            for (int character = 0; character < numCharacterCards; character++) {

                List<StudentEnum> studentsOnCard =
                        virtualView
                        .getCharacterCardBeans()
                        .get(character)
                        .getStudents();

                if (studentsOnCard == null) studentsOnCard = new ArrayList<>();

                gameToolBoxContainer.setCharacterCardInfo(character, studentsOnCard.size());
            }
        }

        List<IslandGroupBean> islands = virtualView.getIslandGroupBeans();

        if (islands != null) gameToolBoxContainer.updateIslandGroups(islands);
        else gameToolBoxContainer.updateAdvancedIslandGroups(virtualView.getAdvancedIslandGroupBeans());

        if (yourTurn) {
            int yourSlot = lobbyBean.getNicknames().indexOf(nickname);

            int MNSteps = 0;

            List<PlayerBean> allPlayers = virtualView.getPlayerBeans();
            Assistant assistantPlayed;
            if (allPlayers != null) assistantPlayed = virtualView.getPlayerBeans().get(yourSlot).getAssistantPlayed();
            else {
                assistantPlayed = virtualView.getAdvancedPlayerBeans().get(yourSlot).getAssistantPlayed();
            }

            if (assistantPlayed != null) {
                MNSteps = assistantPlayed.motherNatureSteps;
            }


            gameToolBoxContainer.setMaxMNSteps(MNSteps);
        }
    }

    @Override
    public void setGameInterrupted(boolean interrupted) {

    }

    /**
     * Updates the view if an update is available and the information have been received.
     * @param available true if an updated view has been received
     */
    @Override
    public void setUpdateAvailable(boolean available) {
        if (available && viewData != null) showMainGameInterface();
    }

    /**
     * Set local property yourTurn.
     * @param isYourTurn true if it's this player's turn
     */
    @Override
    public void setYourTurn(boolean isYourTurn) {
        yourTurn = isYourTurn;
    }

    /**
     * Displays the winner of the game, resets everything and takes the player back to the game rule selection screen.
     * @param winner the team that won the game
     */
    @Override
    public void setGameWon(TeamEnum winner) {
        if (GUIApplication.isStarted()) {
            Platform.runLater(() -> GUIApplication.showWinner(winner));
            resetErrors();
            showGameruleSelection();
        }
    }

    /**
     * Resets all errors.
     */
    private void resetErrors(){
        loginError           = false;
        searchGameError      = false;
        startingGameError    = false;
        leavingLobbyError    = false;
        readyError           = false;
        selectingWizardError = false;
        selectingColorError  = false;
    }
}
