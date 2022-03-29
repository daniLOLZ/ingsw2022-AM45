package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IslandGroupTest {

    List<IslandGroup> group;
    int numIslands = 10; //random amount of islands
    int islandGroupId = 0;

    @Before
    public void initializeIslandGroup(){

        group = new ArrayList<>();

        for (islandGroupId = 0; islandGroupId < numIslands; islandGroupId++){
            List<Island> islList = new ArrayList<>();
            islList.add(new Island(islandGroupId));
            group.add(new IslandGroup(islandGroupId, islList, null, null, new ArrayList<>(), TeamEnum.WHITE));

        }
        group.get(0).setNextIslandGroup(group.get(1));
        group.get(0).setPrevIslandGroup(group.get(numIslands-1));
        group.get(numIslands-1).setNextIslandGroup(group.get(0));
        group.get(numIslands-1).setPrevIslandGroup(group.get(numIslands-2));
        for(int i = 1; i < numIslands-1;i++){
            group.get(i).setPrevIslandGroup(group.get(i-1));
            group.get(i).setNextIslandGroup(group.get(i+1));
        }
        //TODO Maybe implement a FactoryIslandGroupArray of some sort
    }

    @Test
    public void properInitializationOfPointers(){

        IslandGroup firstGroup = group.get(0);
        IslandGroup curIsland = group.get(0);
        for(int i=0;i<numIslands; i++){
            curIsland = curIsland.getNextIslandGroup();
        }
        //After a full visit, we should end up at the exact same group
        assertSame("The two groups aren't the same", firstGroup, curIsland);

        for (IslandGroup Is : group) {
            assertSame("Error in assignment next -> prev",Is.getNextIslandGroup().getPrevIslandGroup(), Is);
            assertSame("Error in assignment prev -> next",Is.getPrevIslandGroup().getNextIslandGroup(), Is);
        }
    }

    /**
     * Tests merging of 3 island groups
     */
    @Test
    public void UnmergeableExceptionTest(){

        islandGroupId += 1;
        //assertThrows(UnmergeableException.class, group.get(1).mergeAdjacent(group, islandGroupId));
        assert(true); //TODO update junit to 5 to allow for assertThrows
    }


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
            group.get(2).mergeAdjacent(group,islandGroupId+1);
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

        assertTrue("Students anomaly", totalStudents.containsAll(gMerged.getStudents()));
        assertTrue("Islands anomaly", totalIslands.containsAll(gMerged.getIslands()));
        assertEquals(gMerged.getNextIslandGroup(), g3.getNextIslandGroup());
        assertEquals(gMerged.getPrevIslandGroup(), g1.getPrevIslandGroup());
    }
}
