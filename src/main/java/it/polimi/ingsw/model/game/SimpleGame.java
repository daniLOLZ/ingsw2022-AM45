package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import it.polimi.ingsw.model.beans.GameBoardBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.player.FactoryPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;


public class SimpleGame implements DrawableObject {
    private ErrorState errorState;
    private final int numPlayers;
    private final int maxStudentsByType;
    private final int amountOfIslands;
    private TeamEnum currentTeam;
    private boolean isLastTurn;
    private int currentIslandGroupId;
    protected List<IslandGroup> islandGroups; // These are not in order of navigation, the order
                                            // is given by the pointers
    protected List<Player> players; // These will not be in order, they will get shifted around
    private List<Cloud> clouds;
    private MotherNature MN;
    protected Sack sack;
    protected ParameterHandler parameters;

    private boolean hasBeenInitialized;


    /**
     * ! Old constructor without customization possible from the users !
     * Must be initialized after creation via the initializeGame() function
     * @param numPlayers the number of players in the game [2,4]
     * @throws IncorrectPlayersException the number of players isn't in the
     * allowed range
     */
    @Deprecated
    public SimpleGame(int numPlayers) throws  IncorrectPlayersException{

        if(numPlayers > 4 || numPlayers < 2){
            throw new IncorrectPlayersException();
        }

        // (Maybe unnecessary) change this constructor into a JSON reader to remove magic numbers
        int numberOfClouds = numPlayers;
        this.hasBeenInitialized = false;
        this.amountOfIslands = 12;
        this.numPlayers = numPlayers;
        this.maxStudentsByType = 130/StudentEnum.getNumStudentTypes();
        createParameters();
        //this.parameters = new ParameterHandler(numPlayers);
        this.isLastTurn = false;
        this.currentIslandGroupId = 0;
        this.players = new ArrayList<>();
        this.clouds = new ArrayList<>();
        for (int cloudNumber = 0; cloudNumber < numberOfClouds; cloudNumber++){
            clouds.add(new Cloud(cloudNumber, parameters.getStudentsPerCloud()));
        }
        createIslandGroups();

        // Mother Nature starts on the first island group, will get moved in the initialization of the game
        this.MN = new MotherNature(islandGroups.get(0));
        //Creates the sack for the initialization phase, it will get used up and replaced in the initializeGame method
        this.sack = new Sack(2);

        // TODO creation of players may be handled by someone else
        createPlayers(numPlayers);
        parameters.setPlayersAllegiance(players);
    }

    /**
     * Creates a new SimpleGame with the parameters chosen by the players,
     * must be initialized after creation via the initializeGame() function
     * @param numPlayers number of players in the game [2,4]
     * @param selectedWizards array containing users' selection for their wizards
     * @param selectedColors array containing users' selection for their tower colors
     * @param nicknames array containing users' nicknames
     * @throws IncorrectPlayersException the number of players isn't in the
     *                                  allowed range
     */
    public SimpleGame(int numPlayers, List<Integer> selectedWizards, List<TeamEnum> selectedColors, List<String> nicknames) throws  IncorrectPlayersException{

        if(numPlayers > 4 || numPlayers < 2){
            throw new IncorrectPlayersException();
        }

        int numberOfClouds = numPlayers;
        this.hasBeenInitialized = false;
        this.amountOfIslands = 12;
        this.numPlayers = numPlayers;
        this.maxStudentsByType = 130/StudentEnum.getNumStudentTypes();
        createParameters();
        this.isLastTurn = false;
        this.currentIslandGroupId = 0;
        this.players = new ArrayList<>();
        this.clouds = new ArrayList<>();
        for (int cloudNumber = 0; cloudNumber < numberOfClouds; cloudNumber++){
            clouds.add(new Cloud(cloudNumber, parameters.getStudentsPerCloud()));
        }
        createIslandGroups();

        // Mother Nature starts on the first island group, will get moved in the initialization of the game
        this.MN = new MotherNature(islandGroups.get(0));
        //Creates the sack for the initialization phase, it will get used up and replaced in the initializeGame method
        this.sack = new Sack(2);
        //createPlayers(numPlayers);
        createPlayers(numPlayers, selectedWizards, selectedColors, nicknames);
        parameters.setPlayersAllegiance(players);
    }

    //TODO this could become a private method called from the constructor;
    // for testing purposes, it's kept separate
    /**
     * Initializes the current game, mimics the setting up of the board
     */
    public void initializeGame(){
        //We must not initialize twice
        if (hasBeenInitialized) return;

        // Moves Mother nature to a random island
        int MNStartingPosition = abs(new Random().nextInt() % amountOfIslands);
        MN.move(MNStartingPosition);

        //Puts one student from the initial sack on each* of the islands.
        StudentEnum drawnStudent;
        int curPosition;

        for(int currentStep = 0; currentStep < amountOfIslands; currentStep++){
            curPosition = (MNStartingPosition + currentStep) % amountOfIslands;
            // If the current position is MotherNature's or its opposite, we don't put a student there
            if(!(curPosition == MNStartingPosition || curPosition == (MNStartingPosition + amountOfIslands/2) % amountOfIslands)){
                drawnStudent = sack.drawNStudents(1).get(0);
                islandGroups.get(curPosition).addStudent(drawnStudent);
            }
        }
        createPlayingSack();

        hasBeenInitialized = true;
    }

    /**
     * Creates the Sack that will be used during the game and assigns it
     * to this game's sack. Can be overridden
     */
    protected void createPlayingSack(){
        sack = new Sack(maxStudentsByType-2);
    }

    /**
     * Creates the island groups of this game and assigns them to
     * this.islandGroups. Can be overridden
     */
    protected void createIslandGroups(){
        this.islandGroups = IslandGroup.getCollectionOfIslandGroup(parameters, currentIslandGroupId, amountOfIslands);
        currentIslandGroupId += amountOfIslands;
    }

    /**
     * This method initializes the parameters of the game
     * This is meant to be overridden in the advancedIslandGroup class to
     * build both types of parameters
     */
    protected void createParameters(){
        this.parameters = new ParameterHandler(numPlayers);
    }

    /**
     * !This method doesn't allow for customization from the users!
     * This method initializes the players held in the game
     * it can be overridden to account for the creation of AdvancedPlayers in advanced games
     * @param numPlayers the number of players to create
     */
    @Deprecated
    protected void createPlayers(int numPlayers){
        this.players = FactoryPlayer.getNPlayers(numPlayers, parameters);
    }

    /**
     * Creates players based on the parameters received by the users (wizard, tower color and usernames selected)
     * @param numPlayers the number of players to create
     * @param selectedWizards array containing the correspondence between player and selected wizard
     * @param selectedColors array containing the correspondence between player and selected tower color
     * @param nicknames array containing the users' nicknames
     */
    protected void createPlayers(int numPlayers, List<Integer> selectedWizards, List<TeamEnum> selectedColors, List<String> nicknames){
        List<TeamEnum> alreadyAssignedLeaders = new ArrayList<>();

        //TODO check validity of the assumptions about leaders and playerIds
        // leaders -> is it always the first in order?
        // playerIds -> is it always the same as the order of the players?

        //TODO Lucario : Possibile necessità di unificare questo controllo di leader con quello in PlayerCreation.isLeader
        boolean isLeader;
        for(int player = 0; player < numPlayers; player++){
            TeamEnum currentColor = selectedColors.get(player);

            if (alreadyAssignedLeaders.contains(currentColor)){ // If a leader of that color has been
                // assigned already, then the next player(s)
                // won't be leaders
                isLeader = false;
            }
            else {
                isLeader = true;
                alreadyAssignedLeaders.add(currentColor);
            }

            this.players.add(
                    FactoryPlayer.getPlayer(
                            nicknames.get(player),
                            PlayerEnum.getPlayer(player),
                            selectedColors.get(player),
                            FactoryWizard.getWizard(selectedWizards.get(player)),
                            isLeader,
                            parameters,
                            false
                    ) // Check if playerId is actually the same as the order of these Arrays
            );
        }
    }

    /**
     * Checks whether a professor needs to change hands by comparing the respective tables
     * in the players' boards
     * @param professor the professor who needs to checked for updates
     */
    public void updateProfessor(StudentEnum professor){
        int maximumStudents = 0;
        int numberOfStudents;
        PlayerEnum currentWinner = PlayerEnum.NOPLAYER;

        for(Player player : players){
            numberOfStudents = player.getBoard().getStudentsAtTable(professor);
            if (numberOfStudents > maximumStudents){
                maximumStudents = numberOfStudents;
                currentWinner = player.getPlayerId();
            }
            else if (numberOfStudents == maximumStudents){
                currentWinner = PlayerEnum.NOPLAYER;
            }
        }
        if(!currentWinner.equals(PlayerEnum.NOPLAYER)) {
            assignProfessor(professor, currentWinner);
        }
    }

    /**
     * Updates the professor assigning it to the player chosen
     * @param professor the professor's color
     * @param player the player to assign it to
     */
    private void assignProfessor(StudentEnum professor, PlayerEnum player){
        this.parameters.getProfessors().set(professor.index, player);
    }

    /**
     * Sorts the players based on the assistant they played in the planning phase
     * Accounts for the possibility of players playing the same valued assistants in the same turn
     */
    public void sortPlayers(){
        List<Player> newPlayerOrder = new ArrayList<>();

        // We need to account for the possibility of two (or more) players playing the same card
        // so we need to remember the previous turn order
        // we do this by using the player List, which will be kept in this order until
        // the end of this method
        newPlayerOrder.addAll(players);
        newPlayerOrder.sort(Comparator.comparingInt((Player p) -> p.getAssistantPlayed().getTurnOrder()));

        // We check for possible duplicate cards played, and if that's the case we
        // make sure the first player who played the card goes first

        List<Player> sameCardsPlayers = new ArrayList<>();
        List<Player> toSubstitute = new ArrayList<>();
        for(int curPlayer = 0; curPlayer < numPlayers; curPlayer++){
            if (curPlayer != numPlayers-1 && // To make sure every case is covered, while also avoiding
                                             // out of bounds indexing
                    newPlayerOrder.get(curPlayer).getAssistantPlayed().getTurnOrder() ==
                    newPlayerOrder.get(curPlayer+1).getAssistantPlayed().getTurnOrder()){
                sameCardsPlayers.add(newPlayerOrder.get(curPlayer));
            }
            else { // We end one batch of players who played the same card
                sameCardsPlayers.add(newPlayerOrder.get(curPlayer));
                final List<Player> playersToSort = sameCardsPlayers;
                if(sameCardsPlayers.size() > 1){   // If only one person played that card,
                                                    // it's not a problem
                    // We get the ordered list of duplicates
                    sameCardsPlayers = players.stream()
                            .filter(x -> playersToSort.contains(x))
                            .collect(Collectors.toList());
                    // And substitute it to the old semi-sorted list
                    for(int index = 0; index < sameCardsPlayers.size(); index++){
                        newPlayerOrder.set(curPlayer+1-sameCardsPlayers.size()+index, sameCardsPlayers.get(index));
                    }

                }
                sameCardsPlayers = new ArrayList<>(); // We reset the control list
            }
        }
        this.players = newPlayerOrder;
    }

    public void setLastTurn(boolean isLast) {
        isLastTurn = isLast;
    }

    public void fillClouds(){
        for(Cloud cloud : clouds){
            cloud.fill(sack.drawNStudents(parameters.getStudentsPerCloud()));
        }
    }


    /*
     * increments by one the current group id and returns it
     * this way there won't be, two equal IDs
     * @return a new islandGroup id
     */
    public int getNewCurrentIslandId(){
        currentIslandGroupId += 1;
        return currentIslandGroupId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public TeamEnum getCurrentTeam() {
        return currentTeam;
    }

    public int getCurrentIslandGroupId() {
        return currentIslandGroupId;
    }

    public List<IslandGroup> getIslandGroups() {
        return islandGroups;
    }

    public int getAmountOfIslands() {
        return amountOfIslands;
    }

    public Sack getSack() {
        return sack;
    }

    public boolean isHasBeenInitialized() {
        return hasBeenInitialized;
    }

    public int getMaxStudentsByType() {
        return maxStudentsByType;
    }

    public void setCurrentIslandGroupId(int currentIslandGroupId) {
        this.currentIslandGroupId = currentIslandGroupId;
    }

    public void setErrorState(ErrorState errorState) {
        this.errorState = errorState;
    }
    public ErrorState getErrorState() {
        return errorState;
    }

    public ParameterHandler getParameters() {
        return parameters;
    }

    /**
     * Get students from a cloud and put them at player's entrance
     * @param player!=null
     * @param cloudId >=0 && < clouds.size()
     */
    public void getFromCloud(Player player, int cloudId){
        Cloud cloud = clouds.get(cloudId);
        player.getFromCloud(cloud);
    }

    /**
     *
     * @return true if there is a player with no assistant cards
     */
    public boolean noMoreAssistant(){
        boolean noMoreAssistant = false;
        for(Player player: players){
            if(player.noMoreAssistant()){
                noMoreAssistant = true;
                break;
            }
        }

        return noMoreAssistant;
    }

    /**
     *
     * @return the team that has zero towers
     */
    public TeamEnum noMoreTowers(){
        TeamEnum winnerTeam = TeamEnum.NOTEAM;

        for(Player player: players){
            if(player.noMoreTowers())
                winnerTeam = player.getTeamColor();
        }

        return winnerTeam;
    }

    /**
     *
     * @return true if there are less than 'criticalIslandsNumber' (3) islandGroups
     */
    public boolean islandShortage(){
        final int criticalIslandsNumber = 3;
        return islandGroups.size() <= criticalIslandsNumber;
    }

    public boolean isLastTurn() {
        return isLastTurn;
    }

    /**
     *
     * @return a Map with placed towers per team
     */
    public Map<TeamEnum, Integer> towersOnIslands(){
        Map<TeamEnum, Integer> numTowerOnIslands = new HashMap<>();
        int towersPerPlayer = parameters.getNumTowers();
        int placedTowers;
        TeamEnum team;
        for(Player player: players){
            if(player.isLeader()){
                team = player.getTeamColor();
                placedTowers = towersPerPlayer - player.getNumTowers();
                numTowerOnIslands.put(team, placedTowers);
            }
        }
        return numTowerOnIslands;
    }

    /**
     *
     * @return a Map of  the professors' numbers  owned by each team
     */
    public Map<TeamEnum, Integer> professorsPerTeam(){
        Map<TeamEnum, Integer> numProfessorsPerTeam = new HashMap<>();
        List<PlayerEnum> professors = parameters.getProfessors();
        TeamEnum team;
        int previous;

        //INITIALISE NUM OF OWNED PROFESSORS
        for(Player player: players){
            team = parameters.getPlayerTeamById(player.getPlayerId());
            numProfessorsPerTeam.put(team,0);
        }

        for (PlayerEnum playerWithProfessor : professors) {
            team = parameters.getPlayerTeamById(playerWithProfessor);
            previous = numProfessorsPerTeam.get(team);
            previous++;
            numProfessorsPerTeam.put(team, previous);
        }

        return numProfessorsPerTeam;
    }

    /**
     * player's turn starts and this player becomes the current player and the current phase
     * becomes PLANNING
     * @param player != null
     */
    public void startPhase(int player){
        parameters.setCurrentPlayer(players.get(player));
        parameters.setCurrentPhase(PhaseEnum.PLANNING);
    }

    /**
     * current phase becomes ACTION
     */
    public void actionPhase(){
        parameters.setCurrentPhase(PhaseEnum.ACTION);
    }

    /**
     * In the player's board:
     * move the student  from position parameter.selectedEntranceStudents
     * into the correct Hall's table.
     * Call updateProfessor
     * @param player != null
     */
    public void moveFromEntranceToHall(Player player){
        StudentEnum studentColor = player.moveFromEntranceToHall();
        updateProfessor(studentColor);
    }

    /**
     * select a student in player's Entrance
     * @param player != null
     * @param position >= 0
     */
    public void selectStudentAtEntrance(Player player, int position){
        player.selectStudentAtEntrance(position);
        selectEntranceStudent(position);
    }

    /**
     * Move the student  from position parameter.selectedEntranceStudents
     * to islandGroup with chosen idIslandGroup
     * @param player != null
     * @param idIslandGroup > 0 && < islandGroups.size()
     */
    public void moveFromEntranceToIsland(Player player, int idIslandGroup){
        IslandGroup island = islandGroups.get(idIslandGroup);
        player.moveFromEntranceToIsland(island);
    };


    public void selectIslandGroup(int idIslandGroup){
        IslandGroup islandGroup = islandGroups.get(idIslandGroup);
        if(parameters.getSelectedIslands().isEmpty()){
            List<IslandGroup> islandList = new ArrayList<>();
            islandList.add(islandGroup);
            parameters.setSelectedIslands(islandList);
        }
        else
        parameters.selectIsland(islandGroup);
    }

    public void selectEntranceStudent(int position){


        if(parameters.getSelectedEntranceStudents().isEmpty()){
            List<Integer> positionList = new ArrayList<>();
            positionList.add(position);
            parameters.setSelectedEntranceStudents(positionList);
        }
        else
        parameters.selectEntranceStudent(position);
    }

    public void selectStudentType(StudentEnum type){
        if(parameters.getSelectedStudentTypes().isEmpty()){
            List<StudentEnum> typeList = new ArrayList<>();
            typeList.add(type);
            parameters.setSelectedStudentTypes(typeList);
        }
        else
        parameters.selectStudentType(type);
    }


    /**
     *
     * @return true if sack is empty
     */
    public boolean emptySack(){
        return sack.isEmpty();
    }

    /**
     *
     * @param idIslandGroup id of islandGroup
     * @return true if there is an islandGroup that has the chosen id
     */
    public boolean checkValidIdIsland(final int idIslandGroup){
        return islandGroups.stream().anyMatch(island -> island.getIdGroup() == idIslandGroup);
    }

    /**
     *
     * @return islandGroup id where MN is placed
     */
    public int getIdIslandMN(){
        return MN.getPosition().getIdGroup();
    }

    /**
     *
     * @param studentColor != NO_STUDENT && != null
     * @return the player's PlayerEnum with more students with chosen studentColor
     */
    public PlayerEnum playerWithMoreStudent(StudentEnum studentColor){
        int max = 0;
        int curr = 0;
        PlayerEnum maxPlayer = PlayerEnum.NOPLAYER;

        for(Player player: players){
            curr = player.getNumStudentAtTable(studentColor);
            if(curr > max){
                max = curr;
                maxPlayer = player.getPlayerId();
            }

            if(curr == max){
                maxPlayer = PlayerEnum.NOPLAYER;
            }

        }
        return maxPlayer;
    }

    /**
     * move Mother Nature across island groups.
     * Update the currentPositionMN in parameters.
     * @param steps >= 0
     */
    public void moveMN(int steps){
        IslandGroup positionMN = MN.move(steps);
        parameters.setIdIslandGroupMN(positionMN.getIdGroup());
    }

    /**
     *
     * @return a java Bean with all general information about this game,
     * a list with island groups id, a list of played assistants,
     * a list of players id, the current player id, current turn number,
     * current phase
     */
    @Override
    public GameElementBean toBean() {
        List<Integer> idIslands = new ArrayList<>();
        List<Integer> idAssistants = new ArrayList<>();
        List<Integer> idPlayers = new ArrayList<>();

        int currentPlayerId;
        if( (parameters.getCurrentPlayer() != null))
            currentPlayerId = parameters.getCurrentPlayer().getPlayerId().index;
        else
            currentPlayerId = 0;

        int turn = parameters.getTurn();

        String phase;
        if((parameters.getCurrentPhase() != null))
             phase = parameters.getCurrentPhase().name;
        else
            phase = "No phase";
        for(IslandGroup islandGroup: islandGroups){
            idIslands.add(islandGroup.getIdGroup());
        }

        for(Player player: players){
            Assistant assistant = player.getAssistantPlayed();
            if(assistant != null)
                idAssistants.add(assistant.id);
            else
                idAssistants.add(0);
            idPlayers.add(player.getPlayerId().index);
        }
        GameBoardBean bean = new GameBoardBean(idIslands,idAssistants,idPlayers,currentPlayerId,turn,phase);
        return bean;
    }
}
