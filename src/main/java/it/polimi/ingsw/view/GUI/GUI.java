package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.model.beans.VirtualViewBean;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
import it.polimi.ingsw.view.UserInterface;

import javafx.application.Application;
import javafx.application.Platform;

import java.util.List;

public class GUI implements UserInterface {

    private boolean
            loginError           = false,
            searchGameError      = false,
            startingGameError    = false,
            selectingWizardError = false,
            selectingColorError  = false;

    private static final int
            numAssistants = 10,
            entranceSlots2_4Players = 7,
            entranceSlots3Players = 9,
            numTables = 5,
            numCharacterCards = 3;

    private GameRuleEnum currentGameRule = GameRuleEnum.NO_RULE;

    private ClientSender sender;

    private GameToolBoxContainer gameToolBoxContainer;

    private LobbyBean lobbyBean;
    private VirtualViewBean viewData;
    private GameInitBean gameInitData;
    private boolean selectedTowerColor = false;

    private String nickname;

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

    @Override
    public void addCommand(CommandEnum command) {
        gameToolBoxContainer.allowCommand(command, sender);
    }

    @Override
    public CommandEnum removeCommand(int index) {
        return null;
    }

    @Override
    public void clearCommands() {
        gameToolBoxContainer.clearCommands();
    }


    @Override
    public void setSender(ClientSender sender) {
        this.sender = sender;
    }

    @Override
    public void showWelcomeScreen() {
        //due to inexperience with JavaFX, I don't know any elegant way to separate this method from startInterface,
        //that's why I'm not implementing this method
    }

    @Override
    public void showLoginScreen() {
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showLoginScreen(loginError));
    }

    public static void main(String[] args) {

        UserInterface Gui = new GUI();

        new Thread(Gui::startInterface).start();
    }

    @Override
    public void showLoginScreenFailure() {
        loginError = true;
    }

    @Override
    public void showSuccessLoginScreen() {
        loginError = false;
        searchGameError = false;
    }

    @Override
    public void showGameruleSelection() {
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showSearchGameScreen(searchGameError));
    }

    @Override
    public void showSuccessJoiningLobby() {
        searchGameError = false;
        startingGameError = false;
    }

    @Override
    public void showErrorJoiningLobby() {
        searchGameError = true;
    }

    @Override
    public void showLobby() {
        printLobby(lobbyBean);
    }

    @Override
    public void showSuccessReadyStatus(boolean status) {

    }

    @Override
    public void showErrorReadyStatus(boolean status) {

    }

    @Override
    public void showSuccessStartGame() {
        startingGameError = false;
    }

    @Override
    public void showErrorStartGame() {
        startingGameError = true;
    }

    @Override
    public void showSuccessLeaveLobby() {

    }

    @Override
    public void showErrorLeaveLobby() {

    }

    @Override
    public void showTowerAndWizardSelection() {
        selectedTowerColor = false;
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showWizardSelection(selectingWizardError));
    }

    @Override
    public void showErrorSelectingColor(String color) {
        selectingColorError = true;
    }

    @Override
    public void showSuccessSelectingColor(String color) {
        selectingColorError = false;
        selectingWizardError = false;
        selectedTowerColor = true;
    }

    @Override
    public void showErrorSelectingWizard(String wizard) {
        selectingWizardError = true;
    }

    @Override
    public void showSuccessSelectingWizard(String wizard) {
        selectingWizardError = false;
    }

    @Override
    public void startInterface() {
        Application.launch(GUIApplication.class);
    }

    @Override
    public void showNetworkError() {
       if (GUIApplication.isStarted()) Platform.runLater(GUIApplication::showNetworkError);
    }

    @Override
    public void showUserDisconnected(String disconnectedUser) {

    }

    @Override
    public void printLobby(LobbyBean lobbyBean) {
        if (!this.lobbyBean.equals(lobbyBean)) this.lobbyBean = lobbyBean;
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showLobbyScreen(lobbyBean, startingGameError));
    }

    @Override
    public void printGameInitInfo(GameInitBean gameInitBean) {
        gameInitData = gameInitBean;
        if (GUIApplication.isStarted()) {
            if (selectedTowerColor) {
                Platform.runLater(() -> GUIApplication.showWizardSelection(false));
            } else Platform.runLater(() -> GUIApplication.showTowerColorSelection(false));
        }
    }

    @Override
    public void setChosenNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void setGameMode(GameRuleEnum gameMode) {
        this.currentGameRule = gameMode;
        gameToolBoxContainer = new GameToolBoxContainer(
                numAssistants,
                GameRuleEnum.getNumPlayers(gameMode.id) == 3 ? entranceSlots3Players : entranceSlots2_4Players,
                numTables,
                GameRuleEnum.getNumPlayers(gameMode.id),
                GameRuleEnum.isAdvanced(gameMode.id) ? numCharacterCards : 0);
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

    @Override
    public void setLobbyStarting() {

    }

    @Override
    public void setGameStarting() {

    }

    @Override
    public void showMainGameInterface() {

        int user = 0;

        List<AdvancedPlayerBean> advancedPlayers = viewData.getAdvancedPlayerBeans();

        if (advancedPlayers != null){
            for (AdvancedPlayerBean advancedPlayer:
                 advancedPlayers) {
                if (advancedPlayer.getNickname().equals(nickname)) user = advancedPlayers.indexOf(advancedPlayer);
            }
        }
        else {
            List<PlayerBean> players = viewData.getPlayerBeans();
            for (PlayerBean player :
                players) {
                if (player.getNickname().equals(nickname)) user = players.indexOf(player);
            }
        }

        if (GUIApplication.isStarted()) {
            int finalUser = user;
            Platform.runLater(() -> GUIApplication.showGameInterface(viewData, gameToolBoxContainer, finalUser));
        }
    }

    @Override
    public void showItsYourTurn(PhaseEnum phase) {

    }

    @Override
    public void setCardRequirements(int islandsRequired, int studentsOnCardRequired, int studentsAtEntranceRequired, int colorsRequired) {

    }

    @Override
    public void showGameCommandError() {

    }

    @Override
    public void showGameCommandError(String error) {

    }

    @Override
    public void showGameCommandSuccess() {

    }

    @Override
    public void printGameInterface(VirtualViewBean virtualView) {
        viewData = virtualView;
        if (!gameToolBoxContainer.areCharactersInitialized()) {
            for (int character = 0; character < numCharacterCards; character++) {
                gameToolBoxContainer
                        .setNumStudentsOnCharacterCard(
                                character,
                                virtualView
                                        .getCharacterCardBeans()
                                        .get(character)
                                        .getStudents()
                                        .size());
            }
        }
        gameToolBoxContainer.updateIslandGroups(virtualView.getIslandGroupBeans());
        showMainGameInterface();
    }

    @Override
    public void setGameInterrupted(boolean alive) {

    }

    @Override
    public void setUpdateAvailable(boolean available) {

    }

    @Override
    public void setYourTurn(boolean isYourTurn) {

    }

    @Override
    public void setGameWon(TeamEnum winner) {

    }
}
