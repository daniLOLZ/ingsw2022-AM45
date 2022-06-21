package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.VirtualViewBean;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.game.SimpleGame;
import it.polimi.ingsw.view.VirtualView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Controller {

    protected SimpleGame simpleGame;
    protected AdvancedGame advancedGame;

    protected PlayerCreation playerCreation;
    protected CharacterCardHandler characterCardHandler;
    protected AssistantHandler assistantHandler;
    protected BoardHandler boardHandler;
    protected TurnHandler turnHandler;
    protected WinnerHandler winnerHandler;
    protected SelectionHandler selectionHandler;
    protected IslandHandler islandHandler;

    protected VirtualView virtualView;

    protected GameRuleEnum gameRule;

    protected final List<Integer> playerNumbers;
    int disconnectedUser;

    //these two might be redundant
    private ReentrantLock teamLock;
    private ReentrantLock wizardLock;

    public ReentrantLock startLock; // not the greatest to be put public but it's needed in the GameInitHandler
    private boolean gameStarted;
    private AtomicBoolean gameUpdated;
    private AtomicBoolean newTurn;
    private AtomicBoolean networkError;


    /**
     * Creates a new game controller
     * @param playerNumbers the idUsers of the users playing:
     *                      the user whose id is in position 0 will be PlayerEnum.PLAYER1, etc..
     * @param gameRule the rules chosen for this game
     */
    public Controller(List<Integer> playerNumbers, GameRuleEnum gameRule){
        this.playerNumbers = playerNumbers;
        this.gameRule = gameRule;
        createPlayerCreation();
        teamLock = new ReentrantLock();
        wizardLock = new ReentrantLock();
        startLock = new ReentrantLock();
        gameStarted = false;
        gameUpdated = new AtomicBoolean(false);
        networkError = new AtomicBoolean(false);
        newTurn = new AtomicBoolean(false);
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
     * Once the relevant information has been obtained, creates and starts the actual game
     * @return true if the game was created successfully
     */
    public synchronized boolean startPlayingGame(){
        if (!isAllSet()){
            return false;
        }
        else if(GameRuleEnum.isSimple(gameRule.id)){
            if(!createSimpleGame()) return false;
        }
        else if(GameRuleEnum.isAdvanced(gameRule.id)){
            if(!createAdvancedGame()) return false;
        }
        createBasicHandlers(GameRuleEnum.isAdvanced(gameRule.id));

        simpleGame.initializeGame();
        this.gameStarted = true;
        this.gameUpdated.set(true);
        this.newTurn.set(true);

        return true;
    }


    /**
     * Checks whether all wizards and towers have been chosen for this game
     * @return true if all selections were made and the game started
     */
    public synchronized boolean isGameStarted(){
        return this.gameStarted;
    }

    /**
     * Sets the flag used for detecting whether the game started to true
     */
    //todo maybe instead of unsetting this flag, make the clientHandler remember whether the user
    // already joined the game
    public void unsetGameStarted(){
        this.gameStarted = false;
    }

    /**
     * By calling the appropriate handler, checks whether all students have moved for this turn
     * @return true if all students have moved for this action phase
     */
    public boolean allStudentsMoved(){
        return boardHandler.allStudentsMoved();
    }

    /**
     * Gets all the team colors that are still available for choosing
     * @return a list of all the team colors (as TeamEnum) chosen
     */
    public List<TeamEnum> getTowerColorsAvailable(){
        return  TeamEnum.getTeams().stream()
                .filter(x -> playerCreation.isColorAvailable(x))
                .collect(Collectors.toList());
    }

    /**
     * Gets all the wizards that are still available for choosing
     * @return a list of all the wizards (as WizardEnum) chosen
     */
    public List<WizardEnum> getWizardsAvailable(){
        return  WizardEnum.getWizards().stream()
                .filter(x -> playerCreation.isWizardAvailable(x.index*10)) // todo wizard refactoring here too
                .collect(Collectors.toList());
    }

    public boolean isPlayerCreationModified() {
        return playerCreation.isModified();
    }

    public void setPlayerCreationModified(boolean modified) {
        playerCreation.setModified(modified);
    }

    public boolean isGameUpdated(){
        return gameUpdated.get();
    }

    public void setGameUpdated(boolean value){
        gameUpdated.set(value);
    }

    public boolean isNewTurn() {
        return newTurn.get();
    }

    public void setNewTurn(boolean value) {
        newTurn.set(value);
    }

    public boolean isNetworkError(){
        return networkError.get();
    }

    private void setNetworkError(boolean value){
        networkError.set(value);
    }

    public int getDisconnectedUser(){
        return this.disconnectedUser;
    }

    /**
     * Returs true if the current player is the one whose id matches the input parameter
     * @param idUser the user asking whether they're the current player
     * @return true if it's this user's turn
     */
    public boolean isMyTurn(int idUser){
        if( playerNumbers.indexOf(idUser) ==
                simpleGame.getParameters().getCurrentPlayer().getPlayerId().index){
            return true;
        }
        return false;
    }

    public PhaseEnum getGamePhase(){
        return turnHandler.getCurrentPhase();
    }

    /**
     * Gets the current virtual view as a bean containing all the beans of game elements
     * @return a VirtualViewBean
     */
    public VirtualViewBean getView() {
        if(GameRuleEnum.isSimple(gameRule.id)){
            return virtualView.renderSimpleView();
        }
        else return virtualView.renderAdvancedView();
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
     * If the wizard was already taken by someone else then the method rightly fails
     * If it was taken by the same player, the method exits without reassigning an already assigned wizard,
     * returning false
     * @param idWizard the wizard chosen by the user
     * @param idUser the user that selects the wizard
     * @return true if the assignment succeeded
     */
    public synchronized boolean setWizard(Integer idWizard, Integer idUser) {
        int position = getPositionFromUserId(idUser);
        if (position >= 0){
            //The user might have already selected a wizard and changed their mind
            wizardLock.lock();
            try {
                if (this.playerCreation.isWizardAvailable(idWizard)){
                    this.playerCreation.clearWizard(position);
                    this.playerCreation.setWizard(idWizard, position);
                }
                else return false;
                // If the wizard was already taken by someone else then the method rightly fails
                // If it was taken by the same player, the method exits without reassigning an already assigned wizard
            }finally {
                wizardLock.unlock();
            }
            return true;
        }
        return false;
    }

    /**
     * By calling the appropriate handler, sets the team color for this user
     * If the color was already taken by someone else then the method rightly fails
     * If it was taken by the same player, the method exits without reassigning an already assigned team color,
     * returning false
     * @param towerColor the tower color chosen
     * @param idUser the user that chooses the team
     * @return true if the new assignment succeeded
     */
    public synchronized boolean setTeamColor(TeamEnum towerColor, Integer idUser) {
        int position = getPositionFromUserId(idUser);

        if (position >= 0) {
            //The user might have already selected a color and changed their mind
            teamLock.lock();
            try {
                if (this.playerCreation.isColorAvailable(towerColor)) {
                    this.playerCreation.clearTeamColor(position);
                    this.playerCreation.setTeamColor(towerColor, position);
                }
                else return false;
            } finally {
                teamLock.unlock();
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
    @Deprecated
    public boolean askForControl(Integer idUser, PhaseEnum gamePhase) {
        return turnHandler.askForControl(idUser, gamePhase);
    }

    /**
     * By calling the appropriate handler, the user plays an assistant card. idUser is not necessary
     * since only one player is the active player at this point of the game
     * If the planning phase is over, starts the action phase
     * @param idAssistant the id of the assistant card to play (1 <= id <= 10)
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
        }
        setGameUpdated(true);
        setNewTurn(true);
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
        if(!selectionHandler.selectStudentAtEntrance(selectedStudent)) return false;
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, the selected student is put in the active player's hall
     * @return true if the action succeeded
     */
    public boolean putInHall() {
        if(!boardHandler.moveFromEntranceToHall()) return false;
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, puts the student on the specified island group
     * @param idIsland the id of the islandGroup on which to put the student
     * @return true if the action succeeded
     */
    public boolean putInIsland(Integer idIsland) {
        if(!boardHandler.moveFromEntranceToIsland(idIsland)) return false;
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, deselects the currently selected student
     * @return true if the action succeeded
     */
    public boolean deselectStudent() {
        selectionHandler.deselectStudentAtEntrance();
        setGameUpdated(true);
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
        setGameUpdated(true);
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
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, ends this player's action phase
     * @return true if the action succeeded
     */
    public boolean endTurn(){
        turnHandler.endPlayerPhase();
        if(turnHandler.isPhaseOver()){
            turnHandler.nextPhase();
        }
        setNewTurn(true);
        setGameUpdated(true);
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

        if(!characterCardHandler.selectCard(cardId)) {
            return false;
        }
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, this method selects the chosen color
     * @param colors the student colors the user selected
     * @return true if the action succeeded
     */
    public boolean selectStudentColor(List<StudentEnum> colors) {

        if(!selectionHandler.selectStudentType(colors)){
            return false;
        }
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, this method selects the students on the character card
     * @param students the positions of the students on the card
     * @return true if the action succeeded
     */
    public boolean selectStudentOnCard(List<Integer> students) {

        if(!selectionHandler.selectStudentAtEntrance(students)) return false;
        setGameUpdated(true);
        return true;

    }

    /**
     * By calling the appropriate handler, this method selects the students at the entrance
     * @param students the positions of the students at the entrance
     * @return true if the action succeeded
     */
    public boolean selectEntranceStudents(List<Integer> students) {

        if(selectionHandler.selectStudentAtEntrance(students)) return false;
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, this method selects the island groups
     * @param islandIds the ids of the island groups chosen
     * @return true if the action succeeded
     */
    public boolean selectIslandGroups(List<Integer> islandIds) {
        if(selectionHandler.selectIsland(islandIds)) return false;
        setGameUpdated(true);
        return true;
    }

    /**
     * By calling the appropriate handler, this method plays the character card
     * @return true if the activation was successful
     */
    public boolean playCard(){

        if(characterCardHandler.playCard()) return false;
        setGameUpdated(true);
        return true;

    }

    /**
     * Set error message to show
     * @param error the error message to show
     */
    public void setError(String error){
        simpleGame.getParameters().setErrorState(error);
    }

    //What is this for?  Maybe for test when there was the deprecated one
    public void createSimpleGame(int numPlayers, List<Integer> selectedWizards, List<TeamEnum> teamColors, List<String> nicknames) {
        try {

            simpleGame = new SimpleGame(numPlayers,selectedWizards,teamColors,nicknames, virtualView);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the disconnected user as the parameter, then calls the parameter-less method
     * @param idUser the user that caused the disconnection
     */
    public void lostConnectionHandle(int idUser){
        this.disconnectedUser = idUser;
        lostConnectionHandle();
    }

    public void lostConnectionHandle(){
        /*TODO Aggiorna la view con il messaggio di errore settato dal ClientHandler
         * che ha perso la connessione (in teoria già fatto dal clientHandler)*/
        /*TODO invia la view a tutti gli user della lobby incriminata */
        //TODO chiudi le connessioni
        setNetworkError(true);
    }


}