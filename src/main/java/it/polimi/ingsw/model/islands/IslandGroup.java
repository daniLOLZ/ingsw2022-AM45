package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.ArrayList;
import java.util.List;

public class IslandGroup {
    protected ParameterHandler parameters;
    protected final int idGroup;
    protected final List<Island> islands;
    private IslandGroup nextIslandGroup;
    private IslandGroup prevIslandGroup;
    protected List<StudentEnum> students;
    protected TeamEnum towerColor;

    /**
     * default constructor
     * create a generic IslandGroup
     * useful for test
     */
    public IslandGroup(){
        idGroup = 0;
        islands = new ArrayList<>();
        islands.add(new Island(0));
        nextIslandGroup = null;
        prevIslandGroup = null;
        towerColor = TeamEnum.NOTEAM;
        parameters = new ParameterHandler(2);
    }

    public IslandGroup(int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor, ParameterHandler parameters) {
        this.idGroup = idGroup;
        this.islands = islands;
        this.nextIslandGroup = nextIslandGroup;
        this.prevIslandGroup = prevIslandGroup;
        this.students = students;
        this.towerColor = towerColor;
        this.parameters = parameters;
    }

    /**
     * Creates a copy of the island parameter
     * @param island the IslandGroup to create a new object out of
     */
    public IslandGroup(IslandGroup island){
        this.idGroup = island.idGroup;
        this.islands = island.islands;
        this.nextIslandGroup = island.nextIslandGroup;
        this.prevIslandGroup = island.prevIslandGroup;
        this.students = island.students;
        this.towerColor = island.towerColor;
        this.parameters = island.parameters;
    }

    // Maybe useless, semi-factory method for IslandGroup
    public static IslandGroup getIslandGroup(int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor, ParameterHandler parameters){
        return new IslandGroup(idGroup, islands, nextIslandGroup, prevIslandGroup, students, towerColor, parameters);
    }

    /**
     * Creates a collection of island groups with no useful data inside except for
     * the linkage between these island groups
     * @requires amount > 0
     * @param startingId Starting id for the IslandGroups
     * @param amount Amount of IslandGroups to have in the collection.
     *               if the amount is 1, the next and previous pointers will refer to
     *               the single island group in the collection
     * @return A collection of island groups, ordered cyclically
     */
    public static List<IslandGroup> getCollectionOfIslandGroup(ParameterHandler parameters, int startingId, int amount){

        List<IslandGroup> returnList = new ArrayList<>();
        for (int islandGroupId = startingId; islandGroupId < startingId+amount; islandGroupId++){
            List<Island> islList = new ArrayList<>();
            islList.add(new Island(islandGroupId));
            returnList.add(new IslandGroup(islandGroupId, islList, null, null, new ArrayList<>(), TeamEnum.NOTEAM, parameters));
        }

        IslandGroup first = returnList.get(0);
        IslandGroup last = returnList.get(amount-1);

        first.setPrevIslandGroup(last);
        last.setNextIslandGroup(first);

        for(int index = 1; index < amount; index++){
            returnList.get(index).setPrevIslandGroup(returnList.get(index-1));
            returnList.get(index-1).setNextIslandGroup(returnList.get(index));
        }

        return returnList;

    }

    protected void setNextIslandGroup(IslandGroup nextIslandGroup) {
        this.nextIslandGroup = nextIslandGroup;
    }

    protected void setPrevIslandGroup(IslandGroup prevIslandGroup) {
        this.prevIslandGroup = prevIslandGroup;
    }

    public IslandGroup getPrevIslandGroup() {
        return prevIslandGroup;
    }

    public IslandGroup getNextIslandGroup() {
        return nextIslandGroup;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public List<StudentEnum> getStudents() {
        return students;
    }

    public TeamEnum getTowerColor() {
        return towerColor;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public ParameterHandler getParameters() {
        return parameters;
    }

    /**
     * Evaluates which team has the most influence
     * If two or more teams have the same influence, returns TeamEnum.NOTEAM
     * @return the Team with the most influence on this IslandGroup
     */
    public TeamEnum evaluateMostInfluential(){
        int maximumInfluence = 0;
        TeamEnum mostInfluentialTeam = TeamEnum.NOTEAM;
        PlayerEnum professorOwner;
        TeamEnum owningTeam;

        for (TeamEnum currentTeam : TeamEnum.values()){

            // There's no point in checking NOTEAM's influence
            if(currentTeam.equals(TeamEnum.NOTEAM)){
                continue;
            }

            professorOwner = PlayerEnum.NOPLAYER;
            owningTeam = TeamEnum.NOTEAM;

            int currentInfluence = 0;

            // Check influence of towers
            if (towerColor.equals(currentTeam)){
                currentInfluence += numOfIslandsInGroup();
            }

            // Checks influence of students
            for(StudentEnum stud : students){
                professorOwner = parameters.getProfessors().get(stud.ordinal());

                // There's no point in adding influence to NOPLAYER     //redundant
                // if (professorOwner == PlayerEnum.NOPLAYER) continue;

                //Finds which team that owner is part of
                owningTeam = parameters.getPlayerTeamById(professorOwner);

                // adds one to the influence if it's the current team being checked
                if (owningTeam.equals(currentTeam)) currentInfluence++;
            }


            if (currentInfluence > maximumInfluence){
                maximumInfluence = currentInfluence;
                mostInfluentialTeam = currentTeam;
            }
            else if(currentInfluence == maximumInfluence){
                mostInfluentialTeam = TeamEnum.NOTEAM;
            }
        }
        return mostInfluentialTeam;
    }


    /**
     * Builds the towers according to the evaluation of the most influential Team
     * If the most influential team is NOTEAM or if it's the same team as the previous one, it does nothing.
     * @param team the team who will build their towers
     * @param players a copy of the collection of players in SimpleGame
     */
    public void build(TeamEnum team, List<Player> players){

        int oldTowers;
        TeamEnum previousTeam = towerColor;
        if(team.equals(TeamEnum.NOTEAM)) {
            return;
        }
        if(team.equals(previousTeam)){
            return;
        }

        for (Player p : players) {
            if (p.getTeamColor().equals(previousTeam) && p.isLeader()) {
                oldTowers = p.getBoard().getNumberOfTowers();
                p.getBoard().updateTowers(-numOfIslandsInGroup());
            }
            if (p.getTeamColor().equals(team) && p.isLeader()){
                oldTowers = p.getBoard().getNumberOfTowers();
                p.getBoard().updateTowers(numOfIslandsInGroup());
            }
        }
        towerColor = team;

    }

    /**
     * Merges this islandGroup with the neighboring islands and updates the IslandGroup collection in game
     * @param newId the id to assign the newly formed group
     * @param islandGroups the islandGroup list that the new island will need to check for possible mergings
     * @throws  UnmergeableException The island group can't be merged with any neighbors
     * @return the new islandGroup created by the merging
     */
    public IslandGroup mergeAdjacent(int newId, List<IslandGroup> islandGroups) throws UnmergeableException{

        //todo move to controller
        //Checks if the island has a tower built on top of it
        if(towerColor.equals(TeamEnum.NOTEAM)){
            throw new UnmergeableException();
        }

        //todo move to controller
        //Checking if it can merge with any island at all
        if(!towerColor.equals(nextIslandGroup.towerColor) && !towerColor.equals(prevIslandGroup.towerColor)){
            throw new UnmergeableException();
        }

        // Attributes of the new island group
        IslandGroup successor = this;
        IslandGroup predecessor = this;
        List<Island> mergedIslands = new ArrayList<>(this.islands);
        List<StudentEnum> mergedStudents = new ArrayList<>(this.students);


        // Finds what merging needs to happen (with the successor, the predecessor, or both),
        // then updates the temporary island group
        if (nextIslandGroup.towerColor.equals(towerColor)){
            successor = nextIslandGroup;
            mergedIslands.addAll(nextIslandGroup.islands);
            mergedStudents.addAll(nextIslandGroup.students);
            // Once we know the island must be merged, we remove it from the group
            islandGroups.remove(nextIslandGroup);
        }
        if(prevIslandGroup.towerColor.equals(towerColor)){
            predecessor = prevIslandGroup;
            mergedIslands.addAll(prevIslandGroup.islands);
            mergedStudents.addAll(prevIslandGroup.students);
            // Same as before
            islandGroups.remove(prevIslandGroup);
        }
        // Finally, remove this island
        islandGroups.remove(this);

        // prepare pointers
        IslandGroup nextPointer = successor.nextIslandGroup;
        IslandGroup previousPointer = predecessor.prevIslandGroup;


        IslandGroup mergedGroup = new IslandGroup(newId, mergedIslands, nextPointer, previousPointer, mergedStudents, towerColor, parameters);

        islandGroups.add(mergedGroup); // possibly unsafe handling of game attribute

        return mergedGroup;
    }

    /**
     * adds a student on this island
     * @param student the student to add onto the island
     */
    public void addStudent(StudentEnum student){
        students.add(student);
    }

    /**
     * Returns the amount of islands in this IslandGroup
     */
    public int numOfIslandsInGroup(){
        return islands.size();
    }

    /**
     * returns the amount of students of a given color on this IslandGroup
     * @param color the color of the student to count
     * @return the amount of students of type color
     */
    private int numberOfStudentsOfColor(StudentEnum color){
        return (int) students.stream()
                .filter(x -> x.equals(color))
                .count();
    }

    /**
     *
     * @return a Java Bean with all information about this Island group,
     * its id, islands' ids, students on this island group, if MN is present,
     * tower color
     */
    public GameElementBean toBean(){
        int idIslandGroup = idGroup;
        List<Integer> idIsland = new ArrayList<>();
        List<StudentEnum> studentsOnIsland = students;
        boolean isPresentMN = parameters.getIdIslandGroupMN() == idIslandGroup;
        TeamEnum tower = towerColor;
        for(Island island: islands)
            idIsland.add(island.getId());


        IslandGroupBean bean = new IslandGroupBean(idIslandGroup, idIsland,
                studentsOnIsland, isPresentMN, tower);
        return bean;
    }
}
