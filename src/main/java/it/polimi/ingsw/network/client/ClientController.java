package it.polimi.ingsw.network.client;

import com.google.gson.internal.LinkedTreeMap;
import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.VirtualViewBean;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.connectionState.*;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
import it.polimi.ingsw.view.UserInterface;

/**
 * This class contains methods to validate whether the command sent received
 * a positive response or not, updates the UI accordingly, and shows the appropriate screen
 * The validateX methods will notify to the UI the result of a synchronous method
 * The handleX methods will do the same for asynchronous methods
 * handleX methods are guaranteed not to block the UI as they're meant for
 * quick game updates that the main UI routine will then handle
 */
public class ClientController {

    private UserInterface userInterface;
    private VirtualViewBean view;
    private MessageBroker broker;
    private ConnectionState gameState;
    private GameRuleEnum chosenRule;
    private ConnectionState callBackGameState;
    //  make it its own state instead of leeching off of
    //  the connection states maybe

    //<editor-fold desc="Synchronous commands">

    public void validateLogin(){
        if(!checkSuccessfulReply()){
            userInterface.showLoginScreenFailure();

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
            chosenRule = GameRuleEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.GAME_RULE));

            userInterface.setGameMode(chosenRule);

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
    }

    public void validateLobbyLeave() {
        if(!checkSuccessfulReply()){
            userInterface.showErrorLeaveLobby();
            userInterface.showLobby();
        }
        else {
            userInterface.setGameMode(GameRuleEnum.NO_RULE);
            userInterface.setInLobby(false);
            userInterface.showSuccessLeaveLobby();
            userInterface.showGameruleSelection();
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
        }
        else {
            userInterface.setWizard(wizard);
            userInterface.showSuccessSelectingWizard(wizard);
        }
        userInterface.showTowerAndWizardSelection();
    }

    public void validateChooseAssistant() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError((String)broker.readField(NetworkFieldEnum.ERROR_STATE));
        }
        else {
            userInterface.clearCommands();
            gameState = new WaitingForControl();
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe

        }
        userInterface.showMainGameInterface();
    }

    public void validateSelectStudent() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.clearCommands();
            gameState = new StudentMoving(chosenRule);
            allowStateCommands();

            userInterface.showGameCommandSuccess(); // can be omitted maybe
        }
        userInterface.showMainGameInterface();
    }

    public void validatePutInHall() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.clearCommands();
            boolean moreStudentsToMove = (boolean) broker.readField(NetworkFieldEnum.MORE_STUDENTS_TO_MOVE);
            if(moreStudentsToMove){
                gameState = new StudentChoosing(chosenRule);
            }
            else {
                gameState = new MNMoving(chosenRule);
            }
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe

        }
        userInterface.showMainGameInterface();
    }

    public void validatePutInIsland() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.clearCommands();
            boolean moreStudentsToMove = (boolean) broker.readField(NetworkFieldEnum.MORE_STUDENTS_TO_MOVE);
            if(moreStudentsToMove){
                gameState = new StudentChoosing(chosenRule);
            }
            else {
                gameState = new MNMoving(chosenRule);
            }
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe

        }
        userInterface.showMainGameInterface();
    }

    public void validateDeselectStudent() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.clearCommands();
            gameState = new StudentChoosing(chosenRule);
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe
        }
        userInterface.showMainGameInterface();
    }

    public void validateMoveMN() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.clearCommands();
            gameState = new CloudChoosing(chosenRule);
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe

        }
        userInterface.showMainGameInterface();
    }

    public void validateChooseCloud() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.clearCommands();
            gameState = new EndTurn(chosenRule);
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe

        }
        userInterface.showMainGameInterface();
    }

    public void validateEndTurn() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.clearCommands();
            gameState = new WaitingForControl();
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe

        }
        userInterface.showMainGameInterface();
    }

    public void validateSelectCharacter() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError( (String) broker.readField(NetworkFieldEnum.ERROR_STATE) );
        }
        else {
            callBackGameState = gameState;

            int islandsRequired = ApplicationHelper.getIntFromBrokerField(broker.readField(NetworkFieldEnum.ISLANDS_REQUIRED));
            int studentsOnCardRequired = ApplicationHelper.getIntFromBrokerField(broker.readField(NetworkFieldEnum.ON_CARD_REQUIRED));
            int studentsAtEntranceRequired = ApplicationHelper.getIntFromBrokerField(broker.readField(NetworkFieldEnum.ENTRANCE_REQUIRED));
            int colorsRequired = ApplicationHelper.getIntFromBrokerField(broker.readField(NetworkFieldEnum.COLORS_REQUIRED));

            userInterface.clearCommands();
            gameState = new CharacterCardActivation();
            allowStateCommands();
            userInterface.setCardRequirements(islandsRequired, studentsOnCardRequired, studentsAtEntranceRequired, colorsRequired);
            userInterface.showGameCommandSuccess(); // can be omitted maybe

        }
        userInterface.showMainGameInterface();
    }

    public void validateSelectStudentColor() {
        // We might want to extract the info about the remaining selections to make
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.showGameCommandSuccess(); // can be omitted maybe
        }
        userInterface.showMainGameInterface();
    }

    public void validateSelectEntranceStudents() {
        // We might want to extract the info about the remaining selections to make
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.showGameCommandSuccess(); // can be omitted maybe
        }
        userInterface.showMainGameInterface();
    }

    public void validateSelectIslandGroup() {
        // We might want to extract the info about the remaining selections to make
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.showGameCommandSuccess(); // can be omitted maybe
        }
        userInterface.showMainGameInterface();
    }

    public void validateSelectStudentOnCard() {
        // We might want to extract the info about the remaining selections to make
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError();
        }
        else {
            userInterface.showGameCommandSuccess(); // can be omitted maybe
        }
        userInterface.showMainGameInterface();
    }

    public void validatePlayCharacter() {
        if(!checkSuccessfulReply()){
            userInterface.showGameCommandError((String)broker.readField(NetworkFieldEnum.ERROR_STATE));
            //If it fails, go back to the state before playing the card
            userInterface.clearCommands();
            gameState = callBackGameState;
            allowStateCommands();
        }
        else {
            userInterface.clearCommands();
            gameState = callBackGameState;
            allowStateCommands();
            userInterface.showGameCommandSuccess(); // can be omitted maybe
        }
        userInterface.showMainGameInterface();
    }

    //</editor-fold>
    /**
     * Allows the user interface to show the commands stated in the current game state
     */
    private void allowStateCommands(){
        for(CommandEnum command : gameState.allowedFields()) {
            userInterface.addCommand(command);
        }
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
        if(lobbyBean == null) return;
        userInterface.printLobby(lobbyBean);
    }

    public void handleLobbyStart(){
        userInterface.setLobbyStarting(); //This will make the other method, showLobby, return after
                                        // the atomic boolean has been set
    }

    public void handleGameInitUpdate(){
        GameInitBean gameInitBean = BeanTranslator.deserializeGameInitBean
                ((LinkedTreeMap<String, Object>) broker.readAsyncField(NetworkFieldEnum.BEAN));
        if(gameInitBean == null) return;
        userInterface.printGameInitInfo(gameInitBean);
    }

    public void handleGameStart(){
        userInterface.setGameStarting(); //This will make the other method, showTowerAndWizardSelection, return
                                        // after the atomic boolean has been set
        userInterface.clearCommands();
        gameState = new WaitingForControl();
        allowStateCommands();
    }

    /**
     * Depending on what turn it is (planning or action), shows the appropriate screen
     */
    public void handleTurnUpdate(){
        PhaseEnum phase = PhaseEnum.fromObjectToEnum(broker.readAsyncField(NetworkFieldEnum.ASYNC_GAME_PHASE));

        userInterface.clearCommands();
        if(phase.equals(PhaseEnum.PLANNING)){
            gameState = new PlanningPhaseTurn();
        }
        else {
            gameState = new StudentChoosing(chosenRule);
        }
        allowStateCommands();

        userInterface.setYourTurn(true);
        userInterface.showItsYourTurn(phase);
        //we now need to go in a situation where the game interface is shown and we allow user input
    }



    public void handleGameUpdate(){
        //update the beans, either giving them to the ui and then calling update() with the available beans,
        // or directly calling the method passing the beans as parameter
        VirtualViewBean receivedViewBean = BeanTranslator.deserializeViewBean(
                (LinkedTreeMap<String, Object>) broker.readAsyncField(NetworkFieldEnum.ASYNC_VIEW));
        this.view = receivedViewBean;
        userInterface.printGameInterface(view);
        userInterface.setUpdateAvailable(true);

    }

    public void handleGameWon(){
        TeamEnum winner = TeamEnum.fromObjectToEnum(broker.readAsyncField(NetworkFieldEnum.ASYNC_WINNER));
        userInterface.clearCommands();
        gameState = new LookingForLobby();
        allowStateCommands();
        userInterface.setGameWon(winner);
    }

    public void handleUserDisconnection(){
        userInterface.setGameInterrupted(true);
        // We call this to wake up the main game interface
        String problemUser = (String)(broker.readAsyncField(NetworkFieldEnum.ASYNC_USER_NICKNAME));
        userInterface.showUserDisconnected(problemUser);
        this.gameState = new LookingForLobby();
        allowStateCommands();
    }

    /**
     * Instructs the UI to show a network error message
     */
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

    // </editor-fold>

    public void setUI(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void setBroker(MessageBroker broker) {
        this.broker = broker;
    }


}
