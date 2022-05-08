package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.ArrayList;
import java.util.List;

public class AdvancedIslandGroup extends IslandGroup {

    private AdvancedParameterHandler advancedParameters;
    private List<BlockTile> blockTiles;

    /**
     * default constructor
     * useful for test
     */
    public AdvancedIslandGroup(AdvancedParameterHandler advancedParameters){
        super();
        blockTiles = new ArrayList<>();
       this.advancedParameters = advancedParameters;
    }

    /**
     * Constructor of an advancedIslandGroup starting from a base IslandGroup
     * @param island the islandGroup to base this object off of
     * @param blockTiles an array of block tiles for this new object
     * @param advancedParameters advanced parameters
     */
    public AdvancedIslandGroup(IslandGroup island, List<BlockTile> blockTiles, AdvancedParameterHandler advancedParameters){
        super(island);
        this.blockTiles = new ArrayList<>(blockTiles);
        this.advancedParameters = advancedParameters;
    }
    public AdvancedIslandGroup(int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor, List<BlockTile> blockTiles, ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(idGroup, islands, nextIslandGroup, prevIslandGroup, students, towerColor, parameters);
        this.blockTiles = new ArrayList<>(blockTiles);
        this.advancedParameters = advancedParameters;
    }

    /**
     * Creates and returns a List of AdvancedIslands
     * @param startingId Starting id for the IslandGroups
     * @param amount Amount of IslandGroups to have in the collection.
     *               if the amount is 1, the next and previous pointers will refer to
     *               the single island group in the collection
     * @return a collection of AdvancedIslandGroup
     */
    public static List<IslandGroup> getCollectionAdvancedIslandGroup(AdvancedParameterHandler advancedParameters,ParameterHandler parameters, int startingId, int amount){
        List<IslandGroup> simpleIslands = getCollectionOfIslandGroup(parameters ,startingId, amount);
        List<IslandGroup> advancedIslands = new ArrayList<>();
        AdvancedIslandGroup decoratedIsland;
        for(IslandGroup s_group : simpleIslands){
            decoratedIsland = new AdvancedIslandGroup(s_group, new ArrayList<BlockTile>(), advancedParameters);
            advancedIslands.add(decoratedIsland);
        }
        // We fix the islands' pointers to point to the islands in the newly created collection
        // Possibly duplicate code
        IslandGroup first = advancedIslands.get(0);
        IslandGroup last = advancedIslands.get(amount-1);

        first.setPrevIslandGroup(last);
        last.setNextIslandGroup(first);

        for(int index = 1; index < advancedIslands.size(); index++){
            advancedIslands.get(index).setPrevIslandGroup(advancedIslands.get(index-1));
            advancedIslands.get(index-1).setNextIslandGroup(advancedIslands.get(index));
        }
        return advancedIslands;
    }
    @Override
    public void mergeAdjacent(int newId, List<IslandGroup> islandGroups) throws UnmergeableException{
        //Checks if the island has a tower built on top of it
        if(getTowerColor().equals(TeamEnum.NOTEAM)){
            throw new UnmergeableException();
        }

        //Checking if it can merge with any island at all
        if(!getTowerColor().equals(getNextIslandGroup().getTowerColor()) && !getTowerColor().equals(getPrevIslandGroup().getTowerColor())){
            throw new UnmergeableException();
        }

        // Attributes of the new island group
        IslandGroup successor = this;
        IslandGroup predecessor = this;
        List<Island> mergedIslands = new ArrayList<>(this.getIslands());
        List<StudentEnum> mergedStudents = new ArrayList<>(this.getStudents());
        List<BlockTile> mergedBlocks = new ArrayList<>(this.blockTiles);


        // Finds what merging needs to happen (with the successor, the predecessor, or both),
        // then updates the temporary island group
        if (getNextIslandGroup().getTowerColor().equals(getTowerColor())){
            successor = getNextIslandGroup();
            mergedIslands.addAll(getNextIslandGroup().getIslands());
            mergedStudents.addAll(getNextIslandGroup().getStudents());
            AdvancedIslandGroup groupTemp = (AdvancedIslandGroup) getNextIslandGroup();
            mergedBlocks.addAll(groupTemp.blockTiles);
            // Once we know the island must be merged, we remove it from the group
            islandGroups.remove(getNextIslandGroup());
        }
        if(getPrevIslandGroup().getTowerColor().equals(getTowerColor())){
            predecessor = getPrevIslandGroup();
            mergedIslands.addAll(getPrevIslandGroup().getIslands());
            mergedStudents.addAll(getPrevIslandGroup().getStudents());
            AdvancedIslandGroup groupTemp = (AdvancedIslandGroup) getPrevIslandGroup();
            mergedBlocks.addAll(groupTemp.blockTiles);
            // Same as before
            islandGroups.remove(getPrevIslandGroup());
        }
        // Finally, remove this island
        islandGroups.remove(this);

        // prepare pointers
        IslandGroup nextPointer = successor.getNextIslandGroup();
        IslandGroup previousPointer = predecessor.getPrevIslandGroup();


        AdvancedIslandGroup mergedGroup = new AdvancedIslandGroup(newId, mergedIslands, nextPointer, previousPointer, mergedStudents, getTowerColor(), mergedBlocks, getParameters(), advancedParameters);

        islandGroups.add(mergedGroup); // possibly unsafe handling of game attribute
    }

    /**
     * Evaluates the most influential team on this IslandGroup considering possible effects
     * of active character cards
     * @return the most influential team on this island
     */
    @Override
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

            // Check influence given by the knight
            if(currentTeam.equals(
                    getParameters().getPlayerTeamById(getParameters().getCurrentPlayer().getPlayerId()))){
                currentInfluence += advancedParameters.getAdditionalInfluence();
            }

            // Check influence of towers
            if(advancedParameters.isCountTowers()) {
                if (getTowerColor().equals(currentTeam)) {
                    currentInfluence += numOfIslandsInGroup();
                }
            }

            // Checks influence of students
            for(StudentEnum stud : getStudents()){
                if(!advancedParameters.getIgnoredStudentType().equals(stud)) {
                    professorOwner = getParameters().getProfessors().get(stud.ordinal());

                    // There's no point in adding influence to NOPLAYER     //redundant
                    // if (professorOwner == PlayerEnum.NOPLAYER) continue;

                    //Finds which team that owner is part of
                    owningTeam = getParameters().getPlayerTeamById(professorOwner);

                    // adds one to the influence if it's the current team being checked
                    if (owningTeam.equals(currentTeam)) currentInfluence++;
                }
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
     * Assigns this IslandGroup with an additional BlockTile
     * @param tile the BlockTile given to this IslandGroup
     */
    public void block(BlockTile tile){
        blockTiles.add(tile);
    }

    /**
     *  Removes a block tile from the group's collection and frees it for
     *  a successive utilization
     */
    public void unblock(){
        if(!blockTiles.isEmpty()){
            BlockTile tileToRemove = blockTiles.remove(0);
            tileToRemove.setAssigned(false);
        }
    }

    public AdvancedParameterHandler getAdvancedParameters() {
        return advancedParameters;
    }

    public BlockTile getBlockTileById(int id) {
        return blockTiles.get(id);
    }

    public List<BlockTile> getBlockTiles(){
        return blockTiles;
    }

    public int getNumBlockTiles(){
        return blockTiles.size();
    }

    /**
     *
     * @return a Java Bean with all information about this Island group,
     *         its id, islands' ids, students on this island group, if MN is present,
     *         tower color and num of block tiles on this island group
     */
    @Override
    public GameElementBean toBean() {
        int idIslandGroup = idGroup;
        List<Integer> idIsland = new ArrayList<>();
        List<StudentEnum> studentsOnIsland = students;
        boolean isPresentMN = parameters.getIdIslandGroupMN() == idIslandGroup;
        TeamEnum tower = towerColor;
        int numBlockTiles = blockTiles.size();
        for(Island island: islands)
            idIsland.add(island.getId());


        AdvancedIslandGroupBean bean = new AdvancedIslandGroupBean(idIslandGroup, idIsland,
                studentsOnIsland, isPresentMN, tower, numBlockTiles);
        return  bean;
    }
}
