package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleGame {
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
    private List<Player> players;
    private List<Cloud> clouds;
    private MotherNature MN;
    protected Sack sack;

    private boolean hasBeenInitialized;

    public SimpleGame(int numPlayers) throws  IncorrectPlayersException{

        if(numPlayers > 4 || numPlayers < 2){
            throw new IncorrectPlayersException();
        }

        //TODO change this constructor into a JSON reader to remove magic numbers
        //TODO see if possible to not hardcode player constructors
        //TODO handle nicknames
        int numberOfClouds = numPlayers;
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
            case 2:
                this.maxStudentsEntrance = 7;
                this.numTowers = 8;
                this.studentsPerCloud = 3;
                players.add(new Player(this, PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true));
                players.add(new Player(this, PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.BLACK, true));
                break;

            case 3:
                this.maxStudentsEntrance = 9;
                this.numTowers = 6;
                this.studentsPerCloud = 4;
                players.add(new Player(this, PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true));
                players.add(new Player(this, PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.GREY, true));
                players.add(new Player(this, PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true));
                break;

            case 4:
                this.maxStudentsEntrance = 7;
                this.numTowers = 8;
                this.studentsPerCloud = 3;
                players.add(new Player(this, PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true));
                players.add(new Player(this, PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.WHITE, false));
                players.add(new Player(this, PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true));
                players.add(new Player(this, PlayerEnum.PLAYER4, "mockNickname4", TeamEnum.BLACK, false));
                break;
        }
    }

    //TODO add an override with in AdvancedGame
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
    private void createPlayingSack(){
        sack = new Sack(24);
    }

    @Deprecated
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

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
