package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.player.FactoryPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.*;
import java.util.stream.Collectors;


public class SimpleGame {
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
    private ParameterHandler parameters;

    private boolean hasBeenInitialized;

    /**
     * Must be initialized after creation via the initializeGame() function
     * @param numPlayers the number of players in the game [2,4]
     * @throws IncorrectPlayersException the number of players isn't in the
     * allowed range
     */
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

    //TODO this could become a private method called from the constructor;
    // for testing purposes, it's kept separate
    /**
     * Initializes the current game, mimics the setting up of the board
     */
    public void initializeGame(){
        //We must not initialize twice
        if (hasBeenInitialized == true) return;

        // Moves Mother nature to a random island
        int MNPosition = new Random().nextInt() % amountOfIslands;
        MN.move(MNPosition);

        //Puts one student from the initial sack on each* of the islands.
        StudentEnum drawnStudent;
        for(int curPosition = MNPosition; curPosition < MNPosition-1; curPosition = (curPosition+1)% amountOfIslands){
            // If the current position is MotherNature's or its opposite, we don't put a student there
            if(!(curPosition == MNPosition || curPosition == (MNPosition + amountOfIslands/2) % amountOfIslands)){
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
     * This method initializes the players held in the game
     * it can be overridden to account for the creation of AdvancedPlayers in advanced games
     * @param numPlayers the number of players to create
     */
    protected void createPlayers(int numPlayers){
        this.players = FactoryPlayer.getNPlayers(numPlayers, parameters);
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
     * Get students from a cloud aand put them at player's entrance
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
     * @param idIslandGroup
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

}
