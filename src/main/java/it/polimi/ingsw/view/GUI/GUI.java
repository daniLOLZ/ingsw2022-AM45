package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.GUI.handlingToolbox.*;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
import it.polimi.ingsw.view.UserInterface;

import javafx.application.Application;
import javafx.application.Platform;

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
            numCharacterCards = 3,
            numIslands = 12;

    private GameRuleEnum gameRule = GameRuleEnum.NO_RULE;

    private ClientSender sender;

    private AssistantHandlingToolbox assistantHandlingToolbox = new AssistantHandlingToolbox(numAssistants);
    private BoardHandlingToolbox boardHandlingToolbox =
            new BoardHandlingToolbox(
                GameRuleEnum.getNumPlayers(gameRule.id) == 3 ?
                    entranceSlots2_4Players : entranceSlots3Players,
                numTables);
    private CharacterCardHandlingToolbox characterCardHandlingToolbox = new CharacterCardHandlingToolbox(numCharacterCards);
    private CloudHandlingToolbox cloudHandlingToolbox = new CloudHandlingToolbox(GameRuleEnum.getNumPlayers(gameRule.id));
    private IslandHandlingToolbox islandHandlingToolbox = new IslandHandlingToolbox(numIslands);

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

    }

    @Override
    public CommandEnum removeCommand(int index) {
        return null;
    }

    @Override
    public void clearCommands() {

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
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showWizardSelection(selectingWizardError));
    }

    @Override
    public void showErrorSelectingColor(String color) {
        selectingColorError = true;
    }

    @Override
    public void showSuccessSelectingColor(String color) {
        selectingColorError = false;
    }

    @Override
    public void showErrorSelectingWizard(String wizard) {
        selectingWizardError = true;
    }

    @Override
    public void showSuccessSelectingWizard(String wizard) {
        selectingWizardError = false;
        selectingColorError = false;
    }

    @Override
    public void showGameInterface() {

    }

    @Override
    public void startInterface() {
        Application.launch(GUIApplication.class);
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showUserDisconnected() {

    }

    @Override
    public void printLobby(LobbyBean lobbyBean) {
        if (GUIApplication.isStarted()) Platform.runLater(() -> GUIApplication.showLobbyScreen(lobbyBean, startingGameError));
    }

    @Override
    public void printGameInitInfo(GameInitBean gameInitBean) {

    }

    @Override
    public void setChosenNickname(String nickname) {

    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {

    }

    @Override
    public void setGameMode(String gameMode) {
        this.gameRule = GameRuleEnum.fromObjectToEnum(gameMode);
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
}
