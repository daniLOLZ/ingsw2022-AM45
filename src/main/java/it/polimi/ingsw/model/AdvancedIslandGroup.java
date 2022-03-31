package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class AdvancedIslandGroup extends IslandGroup {

    private List<BlockTile> blockTiles;

    public AdvancedIslandGroup(IslandGroup island, ArrayList<BlockTile> blockTiles){
        super(island);
        this.blockTiles = new ArrayList<>(blockTiles);
    }
    public AdvancedIslandGroup(SimpleGame game, int idGroup, List<Island> islands, IslandGroup nextIslandGroup, IslandGroup prevIslandGroup, List<StudentEnum> students, TeamEnum towerColor, ArrayList<BlockTile> blockTiles){
        super(game, idGroup, islands, nextIslandGroup, prevIslandGroup, students, towerColor);
        this.blockTiles = new ArrayList<>(blockTiles);
    }
    public List<AdvancedIslandGroup> getCollectionAdvancedIslandGroup(AdvancedGame game, int startingId, int amount){
        List<IslandGroup> simpleIslands = getCollectionOfIslandGroup(game, startingId, amount);
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
    public void mergeAdjacent(int newId){
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

}
