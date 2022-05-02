package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.characterCards.Herbalist;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
import it.polimi.ingsw.model.islands.BlockTile;
import it.polimi.ingsw.model.islands.IslandGroup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HerbalistTest {

    ParameterHandler parameter = new ParameterHandler(2);
    AdvancedParameterHandler advancedParameter = new AdvancedParameterHandler(20);
    /**
     * Tests if herbalist block correctly a islandGroup
     */
    @Test
    public void blockTest(){
        Herbalist herbalist = new Herbalist(parameter,advancedParameter);
        AdvancedIslandGroup island = new AdvancedIslandGroup(advancedParameter);
        List<IslandGroup> islands = new ArrayList<>();
        islands.add(island);
        parameter.setSelectedIslands(islands);
        int numBlock = island.getNumBlockTiles();
        herbalist.blockIsland();
        assertEquals(numBlock + 1, island.getNumBlockTiles(), "Block tile is not added to Island" );
        assertTrue(island.getBlockTileById(0).isAssigned(), "Wrong setting assigned in Block Tile");

    }

    /**
     * tests if island correct unblock
     */
    @Test
    public void unblockTest(){
        Herbalist herbalist = new Herbalist(parameter,advancedParameter);
        AdvancedIslandGroup island = new AdvancedIslandGroup(advancedParameter);
        List<IslandGroup> islands = new ArrayList<>();
        islands.add(island);
        parameter.setSelectedIslands(islands);
        int numBlock = island.getNumBlockTiles();
        herbalist.blockIsland();
        BlockTile tile = island.getBlockTileById(0);
        island.unblock();
        assertEquals(numBlock, island.getNumBlockTiles(), "Block tile is nor removed from island");
        assertFalse(tile.isAssigned(), "Wrong setting assigned in Block Tile");
    }
}
