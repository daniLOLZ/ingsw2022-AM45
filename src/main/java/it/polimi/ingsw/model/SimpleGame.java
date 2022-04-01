package it.polimi.ingsw.model;

import java.util.List;

public class SimpleGame {
    private int numPlayers;
    private int maxStudentsEntrance;
    private int maxStudentsByType;
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

    public SimpleGame(int numPlayers){}

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
}
