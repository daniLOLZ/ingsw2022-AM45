package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IslandGroup {
    private SimpleGame game;
    private final int idGroup;
    private List<Island> islands;
    private IslandGroup nextIslandGroup;
    private IslandGroup prevIslandGroup;
    private List<StudentEnum> students;
    private TeamEnum towerColor;

    public IslandGroup(SimpleGame game, int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor) {
        this.game = game;
        this.idGroup = idGroup;
        this.islands = islands;
        this.nextIslandGroup = nextIslandGroup;
        this.prevIslandGroup = prevIslandGroup;
        this.students = students;
        this.towerColor = towerColor;
    }

    public IslandGroup(IslandGroup island){
        this.game = island.game;
        this.idGroup = island.idGroup;
        this.islands = island.islands;
        this.nextIslandGroup = island.nextIslandGroup;
        this.prevIslandGroup = island.prevIslandGroup;
        this.students = island.students;
        this.towerColor = island.towerColor;
    }

    public static IslandGroup getIslandGroup(SimpleGame game, int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor){
        return new IslandGroup(game, idGroup, islands, nextIslandGroup, prevIslandGroup, students, towerColor);
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
    public static List<IslandGroup> getCollectionOfIslandGroup(SimpleGame game, int startingId, int amount){

        List<IslandGroup> returnList = new ArrayList<>();
        for (int islandGroupId = startingId; islandGroupId < startingId+amount; islandGroupId++){
            List<Island> islList = new ArrayList<>();
            islList.add(new Island(islandGroupId));
            returnList.add(new IslandGroup(game, islandGroupId, islList, null, null, new ArrayList<>(), TeamEnum.NOTEAM));
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

    private void setNextIslandGroup(IslandGroup nextIslandGroup) {
        this.nextIslandGroup = nextIslandGroup;
    }

    private void setPrevIslandGroup(IslandGroup prevIslandGroup) {
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

            /* Check influence of students
            for (StudentEnum s : StudentEnum.values()){
                professorOwner = game.getProfessors().get(s.ordinal());

                for (Player player : game.getPlayers()){
                    if(player.getPlayerId().equals(professorOwner)){ // if a player owns the current professor
                       if(currentTeam.equals(player.getTeamColor())) { // and if the player is of the current team that's being checked
                           currentInfluence += numberOfStudentsOfColor(s);
                       }
                    }
                }
            }
            */
            // Checks influence of students
            for(StudentEnum stud : students){
                professorOwner = game.getProfessors().get(stud.ordinal());

                //Finds which team that owner is part of
                for (Player player : game.getPlayers()){
                    if(player.getPlayerId().equals(professorOwner)){
                        owningTeam = player.getTeamColor();
                    }
                }

                // adds one to the influence if it's the current team being checked
                if (owningTeam.equals(currentTeam)) currentInfluence++;
            }


            if (currentInfluence > maximumInfluence){
                maximumInfluence = currentInfluence;
                mostInfluentialTeam = currentTeam;
            }
            if(currentInfluence == maximumInfluence){
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

        TeamEnum previousTeam = towerColor;
        if(team.equals(TeamEnum.NOTEAM)) {
            return;
        }
        if(team.equals(previousTeam)){
            return;
        }

        for (Player p : players) {
            if (p.getTeamColor().equals(previousTeam) && p.isLeader()) {
                p.getBoard().updateTowers(-numOfIslandsInGroup());
            }
            if (p.getTeamColor().equals(team) && p.isLeader()){
                p.getBoard().updateTowers(numOfIslandsInGroup());
            }
        }
        towerColor = team;

    }

    /**
     * Merges this islandGroup with the neighboring islands and updates the IslandGroup collection in game
     * @param newId the id to assign the newly formed group
     * @exception  UnmergeableException The island groups have different tower colors
     */
    public void mergeAdjacent(int newId) throws UnmergeableException{

        //Checks if the island has a tower built on top of it
        if(towerColor.equals(TeamEnum.NOTEAM)){
            throw new UnmergeableException();
        }

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
            game.getIslandGroups().remove(nextIslandGroup);
        }
        if(prevIslandGroup.towerColor.equals(towerColor)){
            predecessor = prevIslandGroup;
            mergedIslands.addAll(prevIslandGroup.islands);
            mergedStudents.addAll(prevIslandGroup.students);
            // Same as before
            game.getIslandGroups().remove(prevIslandGroup);
        }

        // prepare pointers
        IslandGroup nextPointer = successor.nextIslandGroup;
        IslandGroup previousPointer = predecessor.prevIslandGroup;

        IslandGroup mergedGroup = new IslandGroup(game, newId, mergedIslands, nextPointer, previousPointer, mergedStudents, towerColor);

        game.getIslandGroups().add(mergedGroup); // possibly unsafe handling of game attribute
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
    private int numOfIslandsInGroup(){
        return islands.size();
    }

    /**
     *  returns the amount of students of a given color on this IslandGroup
     * @param color the color of the student to count
     * @return the amount of students of type color
     */
    private int numberOfStudentsOfColor(StudentEnum color){
        return (int) students.stream()
                .filter(x -> x.equals(color))
                .count();
    }
}
