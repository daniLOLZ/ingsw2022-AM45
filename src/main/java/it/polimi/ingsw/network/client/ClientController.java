package it.polimi.ingsw.network.client;

import com.google.gson.internal.LinkedTreeMap;
import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.VirtualViewBean;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
import it.polimi.ingsw.view.UserInterface;

public class ClientController {

    private UserInterface userInterface;
    private VirtualViewBean view;
    private MessageBroker broker;


    /*
       SYNCHRONOUS COMMANDS
                            */

    /**
     * Validate whether the login was successful and shows the correct screen
     */
    public void validateLogin(){
        if(!checkSuccessfulReply()){
            userInterface.showLoginScreenFailure();
            userInterface.showLoginScreen();
        }
        else{
            userInterface.setChosenNickname((String)broker.readField(NetworkFieldEnum.NICKNAME));
            userInterface.showSuccessLoginScreen();
            userInterface.showGameruleSelection();
        }
    }

    public void validateLobbyJoin(){
        if(!checkSuccessfulReply()){
            userInterface.showErrorJoiningLobby();
            userInterface.showGameruleSelection();
        }
        else {
            GameRuleEnum chosenRule = GameRuleEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.GAME_RULE));
            userInterface.setNumberOfPlayers(GameRuleEnum.getNumPlayers(chosenRule.id));

            if(GameRuleEnum.isAdvanced(chosenRule.id)) userInterface.setGameMode("Advanced");
            else userInterface.setGameMode("Simple");

            userInterface.setInLobby(true);
            userInterface.showSuccessJoiningLobby();
            userInterface.showLobby();
        }

    }

    public void validateReadyToStart() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorReadyStatus(true);
        }
        else {
            userInterface.showSuccessReadyStatus(true);
        }
        userInterface.showLobby();
    }

    public void validateNotReady() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorReadyStatus(false);
        }
        else {
            userInterface.showSuccessReadyStatus(false);
        }
        userInterface.showLobby();
    }

    public void validateStartGame() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorStartGame();
        }
        else {
            userInterface.showSuccessStartGame();
        }
        userInterface.showLobby(); // We return to showLobby waiting for the asynchronous message to arrive
        // todo: If the message is late, this will print again the lobby, fix, maybe by calling from here
        //  setLobbyStarting in case of success, doesn't change much if it's "the server" or client doing it
    }

    public void validateLobbyLeave() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorLeaveLobby();
        }
        else {
            userInterface.setGameMode("No game started yet");
            userInterface.setNumberOfPlayers(0);
            userInterface.setInLobby(false);
            userInterface.showSuccessLeaveLobby();
        }
    }

    public void validateSelectTeamColor(){
        int idTeamColor = ApplicationHelper.getIntFromBrokerField(broker.readField(NetworkFieldEnum.ID_TOWER_COLOR));
        String teamColor = TeamEnum.getTeamFromId(idTeamColor).name;

        if(!checkSuccessfulReply()){
            userInterface.showErrorSelectingColor(teamColor);
            userInterface.showTowerAndWizardSelection();
        }
        else {
            userInterface.setTeamColor(teamColor);
            userInterface.showSuccessSelectingColor(teamColor);
            userInterface.showTowerAndWizardSelection();
        }
    }

    public void validateSelectWizard(){
        int idWizard = ApplicationHelper.getIntFromBrokerField(broker.readField(NetworkFieldEnum.ID_WIZARD));
        String wizard = WizardEnum.getWizardFromId(idWizard).name;

        if(!checkSuccessfulReply()){
            userInterface.showErrorSelectingWizard(wizard);
            userInterface.showTowerAndWizardSelection();

        }
        else {
            userInterface.setWizard(wizard);
            userInterface.showSuccessSelectingWizard(wizard);
            userInterface.showTowerAndWizardSelection();

        }
    }

    public void validateChooseAssistant() {
    }

    public void validateSelectStudent() {
    }

    public void validatePutInHall() {
    }

    public void validatePutInIsland() {
    }

    public void validateDeselectStudent() {
    }

    public void validateMoveMN() {
    }

    public void validateChooseCloud() {
    }

    public void validateEndTurn() {
    }

    public void validateSelectCharacter() {
    }

    public void validateSelectStudentColor() {
    }

    public void validateSelectEntranceStudents() {
    }

    public void validateSelectIslandGroup() {
    }

    public void validateSelectStudentOnCard() {
    }

    public void validatePlayCharacter() {
    }


    /*
     * Asynchronous commands must not require the user to input anything
     * these methods are meant to be quick game updating events, handled by the synchronous
     * methods. These must not interfere directly with the execution flow of the synchronous messages
     * If one of the below methods calls for user input, then the listening of async messages is halted
     * until that methods is over
     */

    //<editor-fold desc="Asynchronous commands">

    public void handleLobbyUpdate(){
        LobbyBean lobbyBean = BeanTranslator.deserializeLobbyBean
                ((LinkedTreeMap<String, Object>) broker.readAsyncField(NetworkFieldEnum.BEAN));
        userInterface.printLobby(lobbyBean);
    }

    public void handleLobbyStart(){
        userInterface.setLobbyStarting(); //This will make the other method, showLobby, return after
                                        // the atomic boolean has been set
    }

    public void handleGameInitUpdate(){
        GameInitBean gameInitBean = BeanTranslator.deserializeGameInitBean
                ((LinkedTreeMap<String, Object>) broker.readAsyncField(NetworkFieldEnum.BEAN));
        userInterface.printGameInitInfo(gameInitBean);
    }

    public void handleGameStart(){
        userInterface.setGameStarting(); //This will make the other method, showTowerAndWizardSelection, return
                                        // after the atomic boolean has been set

    }

    /**
     * Depending on what turn it is (planning or action), shows the appropriate screen
     */
    public void handleTurnUpdate(){
        PhaseEnum phase = PhaseEnum.fromObjectToEnum(broker.readAsyncField(NetworkFieldEnum.ASYNC_GAME_PHASE));
        allowBasicCommands();

        if(phase.equals(PhaseEnum.PLANNING)){
            userInterface.addCommand(CommandEnum.CHOOSE_ASSISTANT);
        }
        else {
            userInterface.addCommand(CommandEnum.SELECT_STUDENT);
        }
        userInterface.showItsYourTurn(phase);
        //we now need to go in a situation where the game interface is shown and we allow user input
        userInterface.showMainGameInterface();
    }



    public void handleGameUpdate(){
        //update the beans, either giving them to the ui and then calling update() with the available beans,
        // or directly calling the method passing the beans as parameter
        VirtualViewBean receivedViewBean = BeanTranslator.deserializeViewBean(
                (LinkedTreeMap<String, Object>) broker.readAsyncField(NetworkFieldEnum.ASYNC_VIEW));
        this.view = receivedViewBean;
        userInterface.printGameInterface(view);
        //either show or update(which would read the previously stored beans)

        //todo add the beans
    }

    public void handleUserDisconnection(){
        userInterface.showUserDisconnected();
    }

    public void connectionClose() {
        userInterface.showNetworkError();

    }

    //</editor-fold>

    //<editor-fold desc="Utility">


    /**
     * Simply checks whether the server replied affirmatively
     * @return true if the message received from the server replied successfully
     * to the previously made request
     */
    private boolean checkSuccessfulReply(){
        return "OK".equals(
                (String) broker.readField(NetworkFieldEnum.SERVER_REPLY_MESSAGE));
    }

    /**
     * Useful for tests
     * @return the error message string sent by Server if this is present
     */
    public String getErrorMessage(){
        String errorMes = "NO ERROR";
        String replyMessage;

        if(!broker.messagePresent())
            return  errorMes;

        replyMessage = (String) broker.readField(NetworkFieldEnum.SERVER_REPLY_MESSAGE);
        if(replyMessage.equals("ERR"))
            errorMes = (String) broker.readField(NetworkFieldEnum.ERROR_STATE);

        return errorMes;
    }

    /**
     * Only allows the client to send basic commands
     * clears the previous commands and only adds the Quit option
     */
    private void allowBasicCommands() {
        userInterface.clearCommands();
        userInterface.addCommand(CommandEnum.QUIT);
    }

    // </editor-fold>

    public void setUI(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void setBroker(MessageBroker broker) {
        this.broker = broker;
    }


}
