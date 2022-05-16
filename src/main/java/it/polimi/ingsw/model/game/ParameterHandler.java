package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.beans.ErrorBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.observer.ErrorWatcher;
import it.polimi.ingsw.view.observer.SimpleGameWatcher;
import it.polimi.ingsw.view.observer.Watcher;

import java.util.*;

public class ParameterHandler extends DrawableObject {

    //STATIC PARAMETER FOR DIFFERENT GAME TYPE
    private static final int studentsPerCloud2or4Players = 3;
    private static final int studentsPerCloud3Players = 4;
    private static final int maxStudentsAtEntrance2or4Players = 7;
    private static final int maxStudentsAtEntrance3Players = 9;
    private static final int numTowers2or4Players = 8;
    private static final int numTowers3Players = 6;

    //STATIC PARAMETERS
    private final int studentsPerCloud;
    private final int maxStudentsAtEntrance;
    private final int numTowers;
    private  List<Watcher> watcherList;
    private Map<PlayerEnum, TeamEnum> playersAllegiance;

    //DYNAMIC PARAMETERS
    private Player currentPlayer;
    private int idIslandGroupMN;
    private PhaseEnum currentPhase;
    private List<PlayerEnum> professors;
    private int turn;

    //SELECTION PARAMETERS
    private Optional<List<IslandGroup>> selectedIslands;
    private Optional<List<Integer>> selectedEntranceStudents;
    private Optional<List<StudentEnum>> selectedStudentTypes;

    //ERROR PARAMETER
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
        professors = new ArrayList<>();
        for (StudentEnum studEnum : StudentEnum.values()){
            professors.add(PlayerEnum.NOPLAYER);
        }

        undoSelection();

    }

    /**
     * Version with observer pattern
     * @param numPlayers
     * @param virtualView
     */
    public ParameterHandler(int numPlayers, VirtualView virtualView){

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
        professors = new ArrayList<>();
        for (StudentEnum studEnum : StudentEnum.values()){
            professors.add(PlayerEnum.NOPLAYER);
        }

        undoSelection();

        watcherList = new ArrayList<>();
        ErrorWatcher watcher = new ErrorWatcher(this, virtualView);
        watcherList.add(watcher);
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

    public void setIdIslandGroupMN(int idIslandGroupMN) {
        this.idIslandGroupMN = idIslandGroupMN;
    }

    public int getIdIslandGroupMN() {
        return idIslandGroupMN;
    }

    /**
     *
     * @param playerId != NO Player
     * @return a list with chosen player's professors
     */
    public List<StudentEnum> getProfessorsByPlayer(PlayerEnum playerId){
        List<StudentEnum> professorList = new ArrayList<>();
        for(int color=0; color < professors.size(); color++){
            if(professors.get(color) == playerId)
                professorList.add(StudentEnum.getColorById(color));
        }
        return professorList;
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

    public PhaseEnum getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(PhaseEnum currentPhase) {
        this.currentPhase = currentPhase;
    }

    public String getErrorState(){
        return errorMessage;
    }

    public void setErrorState(String error){
        errorMessage = error;
        //alert();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<PlayerEnum> getProfessors() {
        return professors;
    }

    public void addProfessor(PlayerEnum player, StudentEnum color){
        if(color == StudentEnum.NOSTUDENT || player == PlayerEnum.NOPLAYER)
            return;
        professors.add(color.index,player);
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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public GameElementBean toBean() {
        return new ErrorBean(errorMessage);
    }
}
