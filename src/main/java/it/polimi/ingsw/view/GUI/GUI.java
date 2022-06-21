package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.model.beans.VirtualViewBean;
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
import java.util.Optional;

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
        GUIApplication.setDefaultSender(sender);
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

    @Override
    public void showLoginScreenFailure() {
        loginError = true;
    }

    @Override
    public void showSuccessLoginScreen() {
        resetErrors();
    }

    @Override
    public void showGameruleSelection() {
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showSearchGameScreen(searchGameError));
    }

    @Override
    public void showSuccessJoiningLobby() {
        resetErrors();
    }

    @Override
    public void showErrorJoiningLobby() {
        searchGameError = true;
    }

    @Override
    public void showLobby() {
        printLobby(Optional.ofNullable(lobbyBean).orElse(new LobbyBean(new ArrayList<>(), new ArrayList<>(), false, 0)));
    }

    @Override
    public void showSuccessReadyStatus(boolean status) {
        resetErrors();
    }

    @Override
    public void showErrorReadyStatus(boolean status) {
        readyError = true;
    }

    @Override
    public void showSuccessStartGame() {
        resetErrors();
    }

    @Override
    public void showErrorStartGame() {
        startingGameError = true;
    }

    @Override
    public void showSuccessLeaveLobby() {
        resetErrors();
    }

    @Override
    public void showErrorLeaveLobby() {
        leavingLobbyError = true;
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
        resetErrors();
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
        new Thread(() -> Application.launch(GUIApplication.class)).start();
    }

    @Override
    public void showNetworkError() {
       if (GUIApplication.isStarted()) Platform.runLater(GUIApplication::showNetworkError);
    }

    //@Override
    public void showUserDisconnected(int x) {
        //todo
    }

    @Override
    public void printLobby(LobbyBean lobbyBean) {
        if (!lobbyBean.equals(this.lobbyBean)) this.lobbyBean = lobbyBean;

        int yourSlot = lobbyBean.getNicknames().indexOf(nickname);

        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showLobbyScreen(lobbyBean, yourSlot, startingGameError, leavingLobbyError));
    }

    @Override
    public void printGameInitInfo(GameInitBean gameInitBean) {
        gameInitData = gameInitBean;
        if (GUIApplication.isStarted()) {
            if (selectedTowerColor) {
                Platform.runLater(() -> GUIApplication.showWizardSelection(false));
            } else Platform.runLater(() -> GUIApplication.showTowerColorSelection(gameInitData,false));
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
        //todo show lobby starting
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
    public void showGameCommandError() {

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
