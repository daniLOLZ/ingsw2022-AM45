package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IslandGroup {
    private final int idGroup;
    private List<Island> islands;
    private IslandGroup nextIslandGroup;
    private IslandGroup prevIslandGroup;
    private List<StudentEnum> students;
    private TeamEnum towerColor;

    public IslandGroup(int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor) {
        this.idGroup = idGroup;
        this.islands = islands;
        this.nextIslandGroup = nextIslandGroup;
        this.prevIslandGroup = prevIslandGroup;
        this.students = students;
        this.towerColor = towerColor;
    }

    public void setNextIslandGroup(IslandGroup nextIslandGroup) {
        this.nextIslandGroup = nextIslandGroup;
    }

    public void setPrevIslandGroup(IslandGroup prevIslandGroup) {
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
     * @param professors Copy of the SimpleGame.professors collection
     * @return the Team with the most influence on this IslandGroup
     */
    public TeamEnum evaluateMostInfluential(List<PlayerEnum> professors, List<Player> players){
        int maximumInfluence = 0;
        TeamEnum mostInfluentialTeam = TeamEnum.NOTEAM;

        for (TeamEnum t : TeamEnum.values()){
            // There's no point in checking NOTEAM's influence
            if(t.equals(TeamEnum.NOTEAM)){
                break;
            }

            int currentInfluence = 0;
            TeamEnum currentTeam = TeamEnum.NOTEAM;

            // Check influence of towers
            if (towerColor.equals(t)){
                currentInfluence += numOfIslandsInGroup();
            }

            // Check influence of students
            for (StudentEnum s : StudentEnum.values()){
                PlayerEnum professorOwner = professors.get(s.ordinal());
                for (Player player : players){
                    if(player.getPlayerId().equals(professorOwner)){ // if a player owns the current professor
                       if(t.equals(player.getTeamColor())){ // and if the player is of the current team that's being checked
                            currentInfluence += numberOfStudentsOfColor(s);
                       }
                       break;
                    }
                }
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
     * Merges this islandGroup with the neighboring islands
     * @param groups the List of IslandGroups to modify
     * @param newId the id to assign the newly formed group
     * @exception  UnmergeableException The island groups have different tower colors
     */
    public void mergeAdjacent(List<IslandGroup> groups, int newId) throws UnmergeableException{

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
            groups.remove(nextIslandGroup);
        }
        if(prevIslandGroup.towerColor.equals(towerColor)){
            predecessor = prevIslandGroup;
            mergedIslands.addAll(prevIslandGroup.islands);
            mergedStudents.addAll(prevIslandGroup.students);
            // Same as before
            groups.remove(prevIslandGroup);
        }

        // prepare pointers
        IslandGroup nextPointer = successor.nextIslandGroup;
        IslandGroup previousPointer = predecessor.prevIslandGroup;

        IslandGroup mergedGroup = new IslandGroup(newId, mergedIslands, nextPointer, previousPointer, mergedStudents, towerColor);

        groups.add(mergedGroup);
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

    public TeamEnum evaluate2(List<PlayerEnum> professors, List<Player> players){

        int teamScore[] = new int[TeamEnum.values().length];        //table with points per Team

        for(int index=0; index < TeamEnum.values().length; index++)
            teamScore[index] = 0;

        int mostInfluentialTeam = TeamEnum.NOTEAM.index;
        int maxInfluence = 0;
        int teamHavingStudent;

        //COUNT TOWERS POINTS
        if(towerColor != TeamEnum.NOTEAM)
            teamScore[towerColor.index] = teamScore[towerColor.index] + islands.size();

        //COUNT STUDENTS POINTS
        for(StudentEnum student: students){
            teamHavingStudent = professors.get(student.index).index;  //team who have prof of the current student
            teamScore[teamHavingStudent] ++;                          //increment point of team who 'have' current student
        }

        //EVALUATE MOST INFLUENT
        for(int team=1; team < TeamEnum.values().length; team++){

            //SUPERIOR INFLUENT CASE
            if(teamScore[team] > maxInfluence){
                maxInfluence = teamScore[team];
                mostInfluentialTeam = team;
            }

            //EQUAL INFLUENT CASE
            if(teamScore[team] == maxInfluence){
                mostInfluentialTeam = TeamEnum.NOTEAM.index;
            }
        }

        //RETURN TEAM
        for(TeamEnum team: TeamEnum.values())
            if(mostInfluentialTeam == team.index)
                return team;

        //RETURN FOR SAFE
        return TeamEnum.NOTEAM;

    }
}
