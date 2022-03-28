package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IslandGroup {
    private final int idGroup;
    private Collection<Island> islands;
    private final IslandGroup nextIslandGroup;
    private final IslandGroup prevIslandGroup;
    private Collection<StudentEnum> students;
    private TeamEnum towerColor;

    public IslandGroup(int idGroup, Collection<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, Collection<StudentEnum> students, TeamEnum towerColor) {
        this.idGroup = idGroup;
        this.islands = islands;
        this.nextIslandGroup = nextIslandGroup;
        this.prevIslandGroup = prevIslandGroup;
        this.students = students;
        this.towerColor = towerColor;
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
            if (p.getTeamColor().equals(previousTeam) && p.isLeader() == true) {
                p.getBoard().updateTowers(numOfIslandsInGroup());
            }
            if (p.getTeamColor().equals(team) && p.isLeader() == true){
                p.getBoard().updateTowers(numOfIslandsInGroup());
            }
        }
        towerColor = team;

    }

    /**
     * Merges this islandGroup with the parameter
     * @exception  UnmergeableException The two island groups have different tower color or are not adjacent
     * @param islandGroup the islandGroup to merge this with
     * @return A new IslandGroup, consisting of the two previous groups merged together, retaining all relevant information
     */
    public IslandGroup mergeAdjacent(IslandGroup islandGroup, SimpleGame game) throws UnmergeableException{
        if(!this.towerColor.equals(islandGroup.towerColor)){
            throw new UnmergeableException();
        }

        // Finds which of the two island groups comes first
        // throws an exception in case the two aren't adjacent
        IslandGroup predecessor;
        IslandGroup successor;
        if(this.nextIslandGroup.equals(islandGroup) && islandGroup.prevIslandGroup.equals(this)){
            predecessor = this;
            successor = islandGroup;
        }
        else if(this.prevIslandGroup.equals(islandGroup) && islandGroup.nextIslandGroup.equals(this)){
            predecessor = islandGroup;
            successor = this;
        }
        else{
            throw new UnmergeableException();
        }

        // Prepare attributes of the new island group
            // prepare pointers
        IslandGroup nextPointer = successor.nextIslandGroup;
        IslandGroup previousPointer = predecessor.prevIslandGroup;
            // prepare islands forming the group
        Collection<Island> mergedIslands = new ArrayList<Island>();
        mergedIslands.addAll(this.islands);
        mergedIslands.addAll(islandGroup.islands);
            // prepare the new id
        int mergedId = game.getNewCurrentIslandId();
            // prepare the students on the island group
        Collection<StudentEnum> mergedStudents = new ArrayList<StudentEnum>();
        mergedStudents.addAll(this.students);
        mergedStudents.addAll(islandGroup.students);

        return new IslandGroup(mergedId, mergedIslands, nextPointer, previousPointer, mergedStudents, towerColor);
    }

    /**
     * adds a student on this island
     * @param student the student to add onto the island
     */
    public void addStudent(StudentEnum student){
        students.add(student);
    }

    public IslandGroup getNextIslandGroup() {
        return nextIslandGroup;
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
