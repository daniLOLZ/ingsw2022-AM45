package it.polimi.ingsw.network.client;

import com.google.gson.internal.LinkedTreeMap;
import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.network.ApplicationHelper;
import it.polimi.ingsw.network.BeanTranslator;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
import it.polimi.ingsw.view.UserInterface;

public class ClientController {
    private UserInterface userInterface;
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
        }
        else{
            userInterface.setChosenNickname((String)broker.readField(NetworkFieldEnum.NICKNAME));
            userInterface.showSuccessLoginScreen();
        }
    }

    public void validateLobbyJoin(){
        if(!checkSuccessfulReply()){
            userInterface.showErrorJoiningLobby();
        }
        else {
            GameRuleEnum chosenRule = GameRuleEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.GAME_RULE));
            userInterface.setNumberOfPlayers(GameRuleEnum.getNumPlayers(chosenRule.id));

            if(GameRuleEnum.isAdvanced(chosenRule.id)) userInterface.setGameMode("Advanced");
            else userInterface.setGameMode("Simple");

            userInterface.setInLobby(true);
            userInterface.showSuccessJoiningLobby();
        }

    }

    public void validateReadyToStart() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorReadyStatus(true);
        }
        else {
            userInterface.showSuccessReadyStatus(true);
        }
    }

    public void validateNotReady() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorReadyStatus(false);
        }
        else {
            userInterface.showSuccessReadyStatus(false);
        }
    }

    public void validateStartGame() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorStartGame();
        }
        else {
            userInterface.showSuccessStartGame();

        }
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
        }
        else {
            userInterface.setTeamColor(teamColor);
            userInterface.showSuccessSelectingColor(teamColor);
        }
    }

    public void validateSelectWizard(){
        int idWizard = ApplicationHelper.getIntFromBrokerField(broker.readField(NetworkFieldEnum.ID_WIZARD));
        String wizard = WizardEnum.getWizardFromId(idWizard).name;

        if(!checkSuccessfulReply()){
            userInterface.showErrorSelectingWizard(wizard);
        }
        else {
            userInterface.setWizard(wizard);
            userInterface.showSuccessSelectingWizard(wizard);
        }
    }



    /*
     * Asynchronous commands must not require the user to input anything
     * these methods are meant to be quick game updating events, which must not
     * interfere directly with the execution flow of the synchronous messages
     */
    /*
        ASYNCHRONOUS COMMANDS
                                 */

    public void handleLobbyUpdate(){
        LobbyBean lobbyBean = BeanTranslator.deserializeLobbyBean
                ((LinkedTreeMap<String, Object>) broker.readAsyncField(NetworkFieldEnum.BEAN));
        userInterface.printLobby(lobbyBean);
    }

    public void handleLobbyStart(){
        userInterface.setLobbyStarting(); //This will make the other method, showLobby, return after
                                        // the atomic boolean has been set
        userInterface.showTowerAndWizardSelection();
    }

    public void handleGameInitUpdate(){
        GameInitBean gameInitBean = BeanTranslator.deserializeGameInitBean
                ((LinkedTreeMap<String, Object>) broker.readAsyncField(NetworkFieldEnum.BEAN));
        userInterface.printGameInitInfo(gameInitBean);
    }

    public void handleGameStart(){
        userInterface.setGameStarting(); //This will make the other method, showTowerAndWizardSelection, return
                                        // after the atomic boolean has been set
        userInterface.showGameInterface();
    }

    public void handleTurnUpdate(){

    }

    public void handleGameUpdate(){

    }

    public void handleUserDisconnection(){
        userInterface.showUserDisconnected();
    }

    public void connectionClose() {
        userInterface.showNetworkError();

    }

    /*
          UTILITY
                     */

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

    public void setUI(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void setBroker(MessageBroker broker) {
        this.broker = broker;
    }

}
