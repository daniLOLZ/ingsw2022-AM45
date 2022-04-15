package it.polimi.ingsw.model;

import java.util.*;

public class ParameterHandler {

    private static final int studentsPerCloud2or4Players = 3;
    private static final int studentsPerCloud3Players = 4;
    private static final int maxStudentsAtEntrance2or4Players = 7;
    private static final int maxStudentsAtEntrance3Players = 9;
    private static final int numTowers2or4Players = 8;
    private static final int numTowers3Players = 6;

    private final int studentsPerCloud;
    private final int maxStudentsAtEntrance;
    private final int numTowers;
    private Map<PlayerEnum, TeamEnum> playersAllegiance;

    private Optional<List<IslandGroup>> selectedIslands;
    private Optional<List<Integer>> selectedEntranceStudents;
    private Optional<List<StudentEnum>> selectedStudentTypes;

    private String errorMessage = "";

    public ParameterHandler(int numPlayers){

        if (numPlayers == 3) {
            studentsPerCloud      = studentsPerCloud3Players;
            maxStudentsAtEntrance = maxStudentsAtEntrance3Players;
            numTowers             = numTowers3Players;
        } else {
            studentsPerCloud      = studentsPerCloud2or4Players;
            maxStudentsAtEntrance = maxStudentsAtEntrance2or4Players;
            numTowers             = numTowers2or4Players;
        }

        playersAllegiance = new HashMap<>();

        undoSelection();
    }

    public int getStudentsPerCloud() {
        return studentsPerCloud;
    }

    public int getMaxStudentsAtEntrance() {
        return maxStudentsAtEntrance;
    }

    public int getNumTowers() {
        return numTowers;
    }

    public void setPlayersAllegiance(List<Player> players){
        for (Player player : players){
            playersAllegiance.put(player.getPlayerId(), player.getTeamColor());
        }
    }

    /**
     * Returns the Team of a Player by its PlayerID without accessing the Player class
     * @param player The ID of the Player
     * @return The Team of the Player
     */
    public TeamEnum getPlayerTeamById(PlayerEnum player){
        if (playersAllegiance.containsKey(player)) return playersAllegiance.get(player);
        return TeamEnum.NOTEAM;
    }

    /**
     * @param team The Team whom you want the members
     * @return The IDs of the Team members
     */
    public List<PlayerEnum> getTeamComponentsID(TeamEnum team){
        List<PlayerEnum> teamPlayers = new ArrayList<>();

        for (PlayerEnum player: playersAllegiance.keySet()) {
            if (playersAllegiance.get(player) == team) teamPlayers.add(player);
        }

        return teamPlayers;
    }

    public String getErrorState(){
        return errorMessage;
    }

    public void setErrorState(String error){
        errorMessage = error;
    }

    public void selectIsland(IslandGroup island){
        selectedIslands.ifPresent(islandGroups -> islandGroups.add(island));
    }

    public void setSelectedIslands(List<IslandGroup> islands){
        selectedIslands = Optional.of(new ArrayList<>());
        for (IslandGroup island : islands) selectIsland(island);
    }

    public void selectEntranceStudent(int studentPos){
        selectedEntranceStudents.ifPresent(studentsPositions -> studentsPositions.add(studentPos));
    }

    public void setSelectedEntranceStudents(List<Integer> studentsPos){
        selectedEntranceStudents= Optional.of(new ArrayList<>());
        for (Integer studentPos : studentsPos) selectEntranceStudent(studentPos);
    }

    public void selectStudentType(StudentEnum studentType){
        selectedStudentTypes.ifPresent(students -> students.add(studentType));
    }

    public void setSelectedStudentTypes(List<StudentEnum> students){
        selectedStudentTypes = Optional.of(new ArrayList<>());
        for (StudentEnum studentType : students) selectStudentType(studentType);
    }

    public Optional<List<IslandGroup>> getSelectedIslands() {
        return selectedIslands;
    }

    public Optional<List<Integer>> getSelectedEntranceStudents() {
        return selectedEntranceStudents;
    }

    public Optional<List<StudentEnum>> getSelectedStudentTypes() {
        return selectedStudentTypes;
    }

    public void undoSelection(){
        selectedIslands = Optional.empty();
        selectedEntranceStudents = Optional.empty();
        selectedStudentTypes = Optional.empty();
    }
}
