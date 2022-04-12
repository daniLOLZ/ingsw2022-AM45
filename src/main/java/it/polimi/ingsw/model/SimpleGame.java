package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.Collectors;


public class SimpleGame {
    private ErrorState errorState;
    private final int numPlayers;
    private int maxStudentsEntrance;
    private final int maxStudentsByType;
    private final int amountOfIslands;
    private List<PlayerEnum> professors;
    private Player currentPlayer;
    private TeamEnum currentTeam;
    private boolean isLastTurn;
    private int numTowers;
    private int studentsPerCloud;
    private int currentIslandGroupId;
    private List<IslandGroup> islandGroups; // These are not in order of navigation, the order
                                            // is given by the pointers
    private List<Player> players; // These will not be in order, they will get shifted around
    private List<Cloud> clouds;
    private MotherNature MN;
    protected Sack sack;

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

        //TODO change this constructor into a JSON reader to remove magic numbers
        int numberOfClouds = numPlayers;
        this.hasBeenInitialized = false;
        this.amountOfIslands = 12;
        this.numPlayers = numPlayers;
        this.maxStudentsByType = 120/StudentEnum.getNumStudentTypes();
        this.professors = new ArrayList<PlayerEnum>();
        for (StudentEnum studEnum : StudentEnum.values()){
            professors.add(PlayerEnum.NOPLAYER);
        }
        this.isLastTurn = false;
        this.currentIslandGroupId = 0;
        this.islandGroups = IslandGroup.getCollectionOfIslandGroup(this, currentIslandGroupId, amountOfIslands);
        currentIslandGroupId += amountOfIslands;
        this.players = new ArrayList<>();
        this.clouds = new ArrayList<>();
        for (int cloudNumber = 0; cloudNumber < numberOfClouds; cloudNumber++){
            clouds.add(new Cloud(cloudNumber, studentsPerCloud));
        }
        // Mother Nature starts on the first island group, will get moved in the initalization of the game
        this.MN = new MotherNature(islandGroups.get(0));
        //Creates the sack for the initialization phase, it will get used up and replaced in the initializeGame method
        this.sack = new Sack(2);
        switch(numPlayers){
            case 2: case 4:
                this.maxStudentsEntrance = 7;
                this.numTowers = 8;
                this.studentsPerCloud = 3;
                break;

            case 3:
                this.maxStudentsEntrance = 9;
                this.numTowers = 6;
                this.studentsPerCloud = 4;
                break;

        }
        this.players = FactoryPlayer.getNPlayers(this, numPlayers);

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
        sack = new Sack(24);
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
        this.professors.set(professor.index, player);
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
            cloud.fill(sack.drawNStudents(studentsPerCloud));
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

    public int getNumTowers() {
        return numTowers;
    }

    public List<PlayerEnum> getProfessors() {
        return professors;
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
    //TODO implement the rest of the class


    public Sack getSack() {
        return sack;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isHasBeenInitialized() {
        return hasBeenInitialized;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setErrorState(ErrorState errorState) {
        this.errorState = errorState;
    }
    public ErrorState getErrorState() {
        return errorState;
    }
}
