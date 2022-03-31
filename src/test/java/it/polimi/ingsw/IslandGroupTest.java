package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
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

    /**
     * Tests whether a simple merge between three islandGroups succeeds
     * and keeps the internal state of the group consistent
     */
    @Test public void mergeTest(){
        IslandGroup g1 = group.get(1);
        IslandGroup g2 = group.get(2);
        IslandGroup g3 = group.get(3);

        g1.addStudent(StudentEnum.PINK);
        g2.addStudent(StudentEnum.RED);
        g3.addStudent(StudentEnum.GREEN);

        IslandGroup gMerged = null;

        List<Player> players = new ArrayList<>();
        players.add(new Player(PlayerEnum.PLAYER1, "pp1", TeamEnum.WHITE, true));
        players.add(new Player(PlayerEnum.PLAYER2, "pp2", TeamEnum.BLACK, true));

        g1.build(TeamEnum.WHITE, players);
        g2.build(TeamEnum.WHITE, players);
        g3.build(TeamEnum.WHITE, players);

        try {
            group.get(2).mergeAdjacent(islandGroupId+1);
            islandGroupId++;
        }
        catch(UnmergeableException e){
            e.printStackTrace();
        }

        for(IslandGroup is : group){
            if(is.getIdGroup() == islandGroupId){
                gMerged = is;
                break;
            }
        }

        List<StudentEnum> totalStudents = new ArrayList<>();
        totalStudents.addAll(g1.getStudents());
        totalStudents.addAll(g2.getStudents());
        totalStudents.addAll(g3.getStudents());
        List<Island> totalIslands = new ArrayList<>();
        totalIslands.addAll(g1.getIslands());
        totalIslands.addAll(g2.getIslands());
        totalIslands.addAll(g3.getIslands());

        assertTrue(totalStudents.containsAll(gMerged.getStudents()), "Students anomaly");
        assertTrue(totalIslands.containsAll(gMerged.getIslands()), "Islands anomaly");
        assertEquals(gMerged.getNextIslandGroup(), g3.getNextIslandGroup());
        assertEquals(gMerged.getPrevIslandGroup(), g1.getPrevIslandGroup());
        assertEquals(gMerged.getTowerColor(), g1.getTowerColor());
    }
}
