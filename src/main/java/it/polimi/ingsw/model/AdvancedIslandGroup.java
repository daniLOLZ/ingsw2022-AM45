package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class AdvancedIslandGroup extends IslandGroup {

    private List<BlockTile> blockTiles;

    /**
     * default constructor
     * useful for test
     */
    public AdvancedIslandGroup(){
        super();
        blockTiles = new ArrayList<>();
    }

    public AdvancedIslandGroup(IslandGroup island, ArrayList<BlockTile> blockTiles){
        super(island);
        this.blockTiles = new ArrayList<>(blockTiles);
    }
    public AdvancedIslandGroup(int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor, ArrayList<BlockTile> blockTiles, ParameterHandler parameters){
        super(idGroup, islands, nextIslandGroup, prevIslandGroup, students, towerColor, parameters);
        this.blockTiles = new ArrayList<>(blockTiles);
    }

    /**
     *
     * @param startingId Starting id for the IslandGroups
     * @param amount Amount of IslandGroups to have in the collection.
     *               if the amount is 1, the next and previous pointers will refer to
     *               the single island group in the collection
     * @return a collection of AdvancedIslandGroup
     */
    public List<AdvancedIslandGroup> getCollectionAdvancedIslandGroup(ParameterHandler parameters, int startingId, int amount){
        List<IslandGroup> simpleIslands = getCollectionOfIslandGroup(parameters ,startingId, amount);
        List<AdvancedIslandGroup> advancedIslands = new ArrayList<>();
        AdvancedIslandGroup decoratedIsland;
        for(IslandGroup s_group : simpleIslands){
            decoratedIsland = new AdvancedIslandGroup(s_group, new ArrayList<BlockTile>());
            advancedIslands.add(decoratedIsland);
        }
        return advancedIslands;
    }
    // TODO check stability of factory methods
    @Override
    public void mergeAdjacent(int newId, List<IslandGroup> islandGroups){
        //TODO
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

    public BlockTile getBlockTiles(int id) {
        return blockTiles.get(id);
    }

    public int getNumBlockTiles(){
        return blockTiles.size();
    }
}
