package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.islands.IslandGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MotherNatureTest {

    int numIslands = 3;
    ParameterHandler parameters = new ParameterHandler(2);
    List<IslandGroup> islandGroups;
    MotherNature motherNature;

    @BeforeEach
    public void initialise(){
        islandGroups = IslandGroup.getCollectionOfIslandGroup(parameters,0,numIslands);
        motherNature = new MotherNature(islandGroups.get(0));
    }

    /**
     * Tests if MotherNature movement happens as intended
     */
    @Test
    public void moveTest(){

        motherNature.move(2);

        assertEquals(motherNature.getPosition(),islandGroups.get(2),"MotherNature moved on the wrong IslandGroup");
    }

    /**
     * Tests if MotherNature movement is cyclic
     */
    @Test
    public void movePacmanTest(){

        motherNature.move(3);

        assertEquals(motherNature.getPosition(),islandGroups.get(0),"MotherNature is not returned to the first IslandGroup when moved from the last");
    }
}
