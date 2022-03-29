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

    public SimpleGame(int numPlayers){}

    /**
     * increments by one the current group id and returns it
     * this way there won't be, two equal IDs
     * @return a new islandGroup id
     */
    public int getNewCurrentIslandId(){
        currentIslandGroupId += 1;
        return currentIslandGroupId;
    }

    //TODO implement the rest of the class
}
