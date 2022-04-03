package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;



public class IslandGroupTest {


    SimpleGame game;
    List<IslandGroup> group;
    int numIslands = 10; //random amount of islands
    int islandGroupId = 0;

    @BeforeEach
    public void initializeIslandGroup(){

        try{
            game = new SimpleGame(2);
        }
        catch (IncorrectPlayersException e){
            e.printStackTrace();
        }
        group = IslandGroup.getCollectionOfIslandGroup(game,0, 10);

    }

    /**
     * Does a round trip of the island groups and checks that all pointers are consistent.
     */
    @Test
    public void properInitializationOfPointers(){

        IslandGroup firstGroup = group.get(0);
        IslandGroup curIsland = group.get(0);
        for(int i=0;i<numIslands; i++){
            curIsland = curIsland.getNextIslandGroup();
        }
        //After a full visit, we should end up at the exact same group
        assertSame(firstGroup, curIsland, "The two groups aren't the same");

        for (IslandGroup Is : group) {
            assertSame(Is.getNextIslandGroup().getPrevIslandGroup(), Is, "Error in assignment next -> prev");
            assertSame(Is.getPrevIslandGroup().getNextIslandGroup(), Is, "Error in assignment prev -> next");
        }
    }

    /**
     * Tests merging exception with newly initialized island groups
     * (run in @Before)
     */
    @Test
    public void UnmergeableExceptionTest(){

        islandGroupId += 1;
        assertThrows(UnmergeableException.class, () -> group.get(1).mergeAdjacent(islandGroupId));

    }



}
