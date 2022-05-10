package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.game.SimpleGame;

import java.util.List;

//TODO
public class Controller {

    protected PlayerCreation playerCreation;
    protected GameRuleEnum gameRule;
    protected SimpleGame simpleGame;
    protected AdvancedGame advancedGame;
    protected CharacterCardHandler characterCardHandler;
    protected AssistantHandler assistantHandler;
    protected BoardHandler boardHandler;
    protected TurnHandler turnHandler;
    protected WinnerHandler winnerHandler;
    protected IslandHandler islandHandler;

    public void createPlayerCreation(){
        playerCreation = new PlayerCreation(this);
    }

    public Controller(){

        //TODO
    }

    public void createSimpleGame(int numPlayers){
        try {
            simpleGame = new SimpleGame(numPlayers);
            gameRule = GameRuleEnum.getBasicRule(numPlayers);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }

        //TODO
    }

    public void createAdvancedGame(int numPlayers){
        final int numCoins = 20;
        final int numCharacterCards = 3;

        try {
            advancedGame = new AdvancedGame(numPlayers, numCoins, numCharacterCards);
            gameRule = GameRuleEnum.getAdvancedRule(numPlayers);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }

        simpleGame = advancedGame;

        //TODO
    }

    public void createBasicHandlers(){
        characterCardHandler = new CharacterCardHandler(this);
        boardHandler = new BoardHandler(this);
    }

    private void createView(){
        //TODO
    }

    public SimpleGame getSimpleGame() {
        return simpleGame;
    }

    public AdvancedGame getAdvancedGame() {
        return advancedGame;
    }

    // Lucario : questi javadoc parlano del caso in cui il controller faccia da proxy, in caso così non fosse basta
    // togliere a tutti il "By calling the appropriate handler," iniziale e hai i javadoc "pronti" per i vari handler

    /**
     * By calling the appropriate handler, selects the wizard for this user
     * @param idWizard the wizard chosen by the user
     * @param idUser the user that selects the wizard
     * @return true if the assignment succeeded
     */
    public boolean setWizard(Integer idWizard, Integer idUser) {
        // TODO
        return false;
    }

    /**
     * By calling the appropriate handler, sets the team color for this user
     * @param idTowerColor the tower color chosen
     * @param idUser the user that chooses the team
     * @return true if the assignment succeeded
     */
    public boolean setTeamColor(TeamEnum idTowerColor, Integer idUser) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, the user sends a request asking if it's their turn to move
     * @param idUser the user requesting control
     * @param gamePhase the phase that the user is requesting control for
     * @return true if the user gained control
     */
    public boolean askForControl(Integer idUser, PhaseEnum gamePhase) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, the user plays an assistant card. idUser is not necessary
     * since only one player is the active player at this point of the game
     * @param idAssistant the id of the assistant card to play
     * @return true if the action succeeded
     */
    public boolean playAssistant(Integer idAssistant) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, selects the given student, if no other was previously selected
     * @param selectedStudent the position of the student to select
     * @return true if the selection was successful
     */
    public boolean selectStudent(Integer selectedStudent) {
        // TODO
        return false;
    }

    /**
     * By calling the appropriate handler, the selected student is put in the active player's hall
     * @return true if the action succeeded
     */
    public boolean putInHall() {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, puts the student on the specified island group
     * @param idIsland the id of the islandGroup on which to put the student
     * @return true if the action succeeded
     */
    public boolean putInIsland(Integer idIsland) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, deselects the currently selected student
     * @return true if the action succeeded
     */
    public boolean deselectStudent() {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, moves mother nature by the given amount of steps
     * @param steps the amount of steps MN will take
     * @return true if the actions succeeded
     */
    public boolean moveMNToIsland(Integer steps) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, this method refills the player's entrance
     * with the students from the selected cloud
     * @param idCloud the id of the cloud to take students from
     * @return true if the actions succeeded
     */
    public boolean chooseCloud(Integer idCloud) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, this method checks whether a player can play a character card
     * @param cardPosition the position of the character card in the view
     * @return true if the card can be played
     */
    public boolean selectCard(Integer cardPosition) {
        // will need to convert the position into the actual id of the character card before calling
        // the CharacterCardHandler method
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, this method selects the chosen color
     * @param color the student color the user selected
     * @return true if the actions succeeded
     */
    public boolean selectStudentColor(List<StudentEnum> color) {

        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, this method selects the students on the character card
     * @param students the positions of the students on the card
     * @return true if the actions succeeded
     */
    public boolean selectStudentOnCard(List<Integer> students) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, this method selects the students at the entrance
     * @param students the positions of the students at the entrance
     * @return true if the actions succeeded
     */
    public boolean selectEntranceStudents(List<Integer> students) {
        //TODO
        return false;
    }

    /**
     * By calling the appropriate handler, this method selects the island groups
     * @param islandIds the ids of the island groups chosen
     * @return true if the actions succeeded
     */
    public boolean selectIslandGroups(List<Integer> islandIds) {
        //TODO
        return false;
    }
}
