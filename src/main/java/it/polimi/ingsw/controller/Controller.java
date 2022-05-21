package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.game.SimpleGame;
import it.polimi.ingsw.view.VirtualView;

import java.util.List;

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
    protected SelectionHandler selectionHandler;
    protected final List<Integer> playerNumbers;
    protected IslandHandler islandHandler;
    protected VirtualView virtualView;


    /**
     * Creates a new game controller
     * @param playerNumbers the idUsers of the users playing:
     *                      the user whose id is in position 0 will be PlayerEnum.PLAYER1, etc..
     * @param gameRule the rules chosen for this game
     */
    public Controller(List<Integer> playerNumbers, GameRuleEnum gameRule){
        createPlayerCreation();
        this.playerNumbers = playerNumbers;
        this.gameRule = gameRule;

        // Should we create it here or when the game starts?
        createView();
    }

    /** Used for tests only */
    @Deprecated
    public Controller(){
        virtualView = new VirtualView();
        playerNumbers = null;
    }

    /**
     * Creates a game with simple rules using the wizards, tower colors and nicknames stored in playerCreation
     */
    public boolean createSimpleGame(){
        try{
            simpleGame = new SimpleGame(GameRuleEnum.getNumPlayers(gameRule.id),
                    playerCreation.getWizards(),
                    playerCreation.getTeamColors(),
                    playerCreation.getNicknames(),
                    virtualView);
        } catch (IncorrectPlayersException e) {
            System.err.println("Error creating the game!");
            return false;
        }

        return true;
    }

    /**
     * Creates a game with advanced rules using the wizards, tower colors and nicknames stored in playerCreation,
     * and the number of coins and character cards stored within the method
     */
    public boolean createAdvancedGame(){
        final int numCoins = 20;
        final int numCharacterCards = 3;

        try{
            advancedGame = new AdvancedGame(GameRuleEnum.getNumPlayers(gameRule.id),
                    playerCreation.getWizards(),
                    playerCreation.getTeamColors(),
                    playerCreation.getNicknames(),
                    numCoins,
                    numCharacterCards);
        } catch (IncorrectPlayersException e) {
            System.err.println("Error creating the game!");
            return false;
        }
        return true;
    }




    public void createPlayerCreation(){
        playerCreation = new PlayerCreation(this);
    }

    /**
     * Creates the handlers for this controller, depending on whether the game is simple or advanced
     * It doesn't create the handlers that were necessary prior to the start of the actual game
     * @param advanced true if the game needs to be its advanced variation
     */
    public void createBasicHandlers(boolean advanced){
        assistantHandler = new AssistantHandler(this);
        turnHandler = new TurnHandler(this);
        winnerHandler = new WinnerHandler(this);
        if(advanced){
            selectionHandler = new AdvancedSelectionHandler(this);
            characterCardHandler = new CharacterCardHandler(this);
            boardHandler = new AdvancedBoardHandler(this);
            islandHandler = new AdvancedIslandHandler(this);
        }
        else {
            selectionHandler = new SelectionHandler(this);
            boardHandler = new BoardHandler(this);
            islandHandler = new IslandHandler(this);
        }


    }

    private void createView(){
        virtualView = new VirtualView();
    }

    /** Only for testing purposes */
    public SimpleGame getSimpleGame() {
        return simpleGame;
    }
    /** Only for testing purposes */
    public AdvancedGame getAdvancedGame() {
        return advancedGame;
    }

    // Lucario : questi javadoc parlano del caso in cui il controller faccia da proxy, in caso così non fosse basta
    // togliere a tutti il "By calling the appropriate handler," iniziale e hai i javadoc "pronti" per i vari handler



    /**
     * Gets the position from the associative array playerNumbers
     * @param idUser the user of which we want to know the numbering in the game
     * @return the number of this user in the game
     */
    private int getPositionFromUserId (Integer idUser) {
        //We need to translate the idUser to the actual enumeration of the players
        int position = this.playerNumbers.indexOf(idUser);
        if(position < 0){
            return -1;
        }
        else return position;
    }

    /**
     * Checks whether the game has all the parameters needed to start
     * @return true if all wizards and colors have been assigned
     */
    private boolean isAllSet(){
        return playerCreation.allSet();
    }

    /**
     * Once the relevant information have been obtained, creates and starts the actual game
     * @return true if the game was created successfully
     */
    public boolean startPlayingGame(){
        if (!isAllSet()){
            return false;
        }
        if(GameRuleEnum.isSimple(gameRule.id)){
            if(!createSimpleGame()) return false;
        }
        if(GameRuleEnum.isAdvanced(gameRule.id)){
            if(!createAdvancedGame()) return false;
        }
        createBasicHandlers(GameRuleEnum.isAdvanced(gameRule.id));

        simpleGame.initializeGame();

        return true;
    }

    /**
     * By calling the appropriate handler, checks whether all students have moved for this turn
     * @return true if all students have moved for this action phase
     */
    public boolean allStudentsMoved(){
        return boardHandler.allStudentsMoved();
    }


    /*____________________________
        Network command handling
    ______________________________*/


    /**
     * By calling the appropriate handler, sets the nickname of the user with the given id in the PlayerCreation class
     * @param nickname a string representing the nickname of the user
     * @param idUser the user with the given nickname
     * @return true if the assignment succeeded
     */
    public boolean setNickname(String nickname, Integer idUser){
        int position = getPositionFromUserId(idUser);
        if (position >= 0){
            this.playerCreation.setNickname(nickname, position);
            return true;
        }
        return false;
    }

    /**
     * By calling the appropriate handler, selects the wizard for this user
     * @param idWizard the wizard chosen by the user
     * @param idUser the user that selects the wizard
     * @return true if the assignment succeeded
     */
    public boolean setWizard(Integer idWizard, Integer idUser) {
        int position = getPositionFromUserId(idUser);
        Object lock = new Object();
        if (position >= 0){
            //The user might have already selected a wizard and changed their mind
            synchronized (lock) {
                if (!this.playerCreation.isWizardTaken(idWizard)){
                    this.playerCreation.clearWizard(position);
                    this.playerCreation.setWizard(idWizard, position);
                }
                // If the wizard was already taken by someone else then the method rightly fails
                // If it was taken by the same player, the method exits without reassigning an already assigned wizard
            }
            return true;
        }
        return false;
    }

    /**
     * By calling the appropriate handler, sets the team color for this user
     * @param towerColor the tower color chosen
     * @param idUser the user that chooses the team
     * @return true if the assignment succeeded
     */
    public boolean setTeamColor(TeamEnum towerColor, Integer idUser) {
        int position = getPositionFromUserId(idUser);
        Object lock = new Object();
        if (position >= 0){
            //The user might have already selected a color and changed their mind
            synchronized (lock) {
                if (!this.playerCreation.isColorTaken(towerColor)){
                    this.playerCreation.clearTeamColor(position);
                    this.playerCreation.setTeamColor(towerColor, position);
                }
                // If the color was already taken by someone else then the method rightly fails
                // If it was taken by the same player, the method exits without reassigning an already assigned team color
            }
            return true;
        }
        return false;
    }

    /**
     * By calling the appropriate handler, the user sends a request asking if it's their turn to move
     * @param idUser the user requesting control
     * @param gamePhase the phase that the user is requesting control for
     * @return true if the user gained control
     */
    public boolean askForControl(Integer idUser, PhaseEnum gamePhase) {
        return turnHandler.askForControl(idUser, gamePhase);
    }

    /**
     * By calling the appropriate handler, the user plays an assistant card. idUser is not necessary
     * since only one player is the active player at this point of the game
     * If the planning phase is over, starts the action phase
     * @param idAssistant the id of the assistant card to play
     * @return true if the action succeeded
     */
    public boolean playAssistant(Integer idAssistant) {
        if(!assistantHandler.playCard(idAssistant)){
            return false;
        }
        turnHandler.endPlayerPhase();

        //If everyone played their assistants, go to the next phase
        if(turnHandler.isPhaseOver()) {
            turnHandler.nextPhase();
            simpleGame.sortPlayers(); // move to turn handler?
        }
        return true;
    }

    /**
     * By calling the appropriate handler, selects the given student, if no other was previously selected
     * @param selectedStudent the position of the student to select
     * @return true if the selection was successful
     */
    public boolean selectStudent(Integer selectedStudent) {
        //needs to check that the player doesn't move more students than they're allowed
        if(boardHandler.allStudentsMoved()) return false;
        selectionHandler.selectStudentAtEntrance(selectedStudent);
        return true;
    }

    /**
     * By calling the appropriate handler, the selected student is put in the active player's hall
     * @return true if the action succeeded
     */
    public boolean putInHall() {
        if(!boardHandler.moveFromEntranceToHall()) return false;
        return true;
    }

    /**
     * By calling the appropriate handler, puts the student on the specified island group
     * @param idIsland the id of the islandGroup on which to put the student
     * @return true if the action succeeded
     */
    public boolean putInIsland(Integer idIsland) {
        if(!boardHandler.moveFromEntranceToIsland(idIsland)) return false;
        return true;
    }

    /**
     * By calling the appropriate handler, deselects the currently selected student
     * @return true if the action succeeded
     */
    public boolean deselectStudent(Integer position) {
        selectionHandler.deselectStudentAtEntrance(position);
        return true;
    }

    /**
     * By calling the appropriate handler, moves mother nature by the given amount of steps
     * Calls all the methods needed to check for changes in the board like tower building
     * @param steps the amount of steps MN will take
     * @return true if the action succeeded
     */
    public boolean moveMNToIsland(Integer steps) {
        if(!islandHandler.moveMN(steps)){
            return false;
        }
        return true;
    }

    /**
     * By calling the appropriate handler, this method refills the player's entrance
     * with the students from the selected cloud
     * @param idCloud the id of the cloud to take students from
     * @return true if the action succeeded
     */
    public boolean chooseCloud(Integer idCloud) {
        if(!boardHandler.takeFromCloud(idCloud)){
            return false;
        }
        turnHandler.endPlayerPhase();
        if(turnHandler.isPhaseOver()){
            turnHandler.nextPhase();
        }
        return true;
    }

    /**
     * By calling the appropriate handler, this method checks whether a player can play a character card
     * @param cardPosition the position of the character card in the view
     * @return true if the card can be played
     */
    public boolean selectCard(Integer cardPosition) {
        // will need to convert the position into the actual id of the character card before calling
        // the CharacterCardHandler method

        int cardId = characterCardHandler.getIdFromPosition(cardPosition);

        if(characterCardHandler.selectCard(cardId)) {
            return true;
        }
        else return false;
    }

    /**
     * By calling the appropriate handler, this method selects the chosen color
     * @param colors the student colors the user selected
     * @return true if the action succeeded
     */
    public boolean selectStudentColor(List<StudentEnum> colors) {

        return selectionHandler.selectStudentType(colors);

    }

    /**
     * By calling the appropriate handler, this method selects the students on the character card
     * @param students the positions of the students on the card
     * @return true if the action succeeded
     */
    public boolean selectStudentOnCard(List<Integer> students) {

        return selectionHandler.selectStudentAtEntrance(students);

    }

    /**
     * By calling the appropriate handler, this method selects the students at the entrance
     * @param students the positions of the students at the entrance
     * @return true if the action succeeded
     */
    public boolean selectEntranceStudents(List<Integer> students) {

        return selectionHandler.selectStudentAtEntrance(students);
    }

    /**
     * By calling the appropriate handler, this method selects the island groups
     * @param islandIds the ids of the island groups chosen
     * @return true if the action succeeded
     */
    public boolean selectIslandGroups(List<Integer> islandIds) {
        return selectionHandler.selectIsland(islandIds);
    }

    /**
     * By calling the appropriate handler, this method plays the character card
     * @return true if the activation was successful
     */
    public boolean playCard(){

        return characterCardHandler.playCard();

    }

    //What is this for?
    public void createSimpleGame(int numPlayers, List<Integer> selectedWizards, List<TeamEnum> teamColors, List<String> nicknames) {
        try {

            simpleGame = new SimpleGame(numPlayers,selectedWizards,teamColors,nicknames, virtualView);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }
}