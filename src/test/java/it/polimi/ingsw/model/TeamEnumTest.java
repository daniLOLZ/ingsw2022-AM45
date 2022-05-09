package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeamEnumTest {
    /**
     * Tests whether the number of teams returned by the class is the expected 3
     * (black, white, grey)
     */
    @Test
    void numTeamTest() {
        assertEquals(TeamEnum.getNumTeams(), 3);
    }

    /**
     * Tests whether the teams returned are all the teams existing in the game
     */
    @Test
    void getTeamsTest(){
        List<TeamEnum> returnedTeams = new ArrayList<>(TeamEnum.getTeams());
        List<TeamEnum> actualTeams = Arrays.asList(TeamEnum.WHITE, TeamEnum.BLACK, TeamEnum.GREY);
        assertTrue(actualTeams.containsAll(returnedTeams));
    }

    /**
     * Tests whether the method is able to correctly cast
     * from an object that contains a string to a TeamEnum of the carrect type
     */
    @Test
    void objectEnumTest(){
        Object objectTeam = "BLACK";
        assertEquals(TeamEnum.fromObjectToEnum(objectTeam), TeamEnum.BLACK);
    }
}
