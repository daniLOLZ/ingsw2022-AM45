package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.AdvancedIslandGroup;
import it.polimi.ingsw.model.BlockTile;
import it.polimi.ingsw.model.characterCards.Herbalist;
import org.junit.jupiter.api.Test;

public class HerbalistTest {
    /**
     * Tests if herbalist block correctly a islandGroup
     */
    @Test
    public void blockTest(){
        Herbalist herbalist = new Herbalist(null);
        AdvancedIslandGroup island = new AdvancedIslandGroup(null);
        int numBlock = island.getNumBlockTiles();
        herbalist.blockIsland(island);
        assertEquals(numBlock + 1, island.getNumBlockTiles(), "Block tile is not added to Island" );
        assertTrue(island.getBlockTiles(0).isAssigned(), "Wrong setting assigned in Block Tile");

    }

    /**
     * tests if island correct unblock
     */
    @Test
    public void unblockTest(){
        Herbalist herbalist = new Herbalist(null);
        AdvancedIslandGroup island = new AdvancedIslandGroup(null);
        int numBlock = island.getNumBlockTiles();
        herbalist.blockIsland(island);
        BlockTile tile = island.getBlockTiles(0);
        island.unblock();
        assertEquals(numBlock, island.getNumBlockTiles(), "Block tile is nor removed from island");
        assertTrue(!tile.isAssigned(), "Wrong setting assigned in Block Tile");
    }
}
