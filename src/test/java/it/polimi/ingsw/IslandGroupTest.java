package it.polimi.ingsw;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandGroup;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class IslandGroupTest {

    List<IslandGroup> group;
    int numIslands = 10; //random amount of islands

    @Before
    public void initializeIslandGroup(){

        group = new ArrayList<>();

        for (int i = 0; i < numIslands; i++){
            List<Island> islList = new ArrayList<>();
            islList.add(new Island(i));
            group.add(new IslandGroup(i, islList, null, null, new ArrayList<>(), TeamEnum.WHITE));
        }
        group.get(0).setNextIslandGroup(group.get(1));
        group.get(0).setPrevIslandGroup(group.get(numIslands-1));
        group.get(numIslands-1).setNextIslandGroup(group.get(0));
        group.get(numIslands-1).setPrevIslandGroup(group.get(numIslands-2));
        for(int i = 1; i < numIslands-1;i++){
            group.get(i).setPrevIslandGroup(group.get(i-1));
            group.get(i).setNextIslandGroup(group.get(i+1));
        }
    }

    @Test
    public void properInitializationOfPointers(){

        IslandGroup firstGroup = group.get(0);
        IslandGroup curIsland = group.get(0);
        for(int i=0;i<numIslands; i++){
            curIsland = curIsland.getNextIslandGroup();
        }
        //After a full visit, we should end up at the exact same group
        assert(firstGroup.equals(curIsland)) : "The two groups aren't the same";
    }

    @Test
    public void mergingTest(){
        //TODO
    }

}
