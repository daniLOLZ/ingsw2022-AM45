package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.PlayerEnum;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerEnumTest {

    /**
     * Tests whether all player enums are returned correctly (Players 1 to 4)
     */
    @Test
    void getPlayersTest(){
        List<PlayerEnum> actualPlayers = Arrays.asList(PlayerEnum.PLAYER1, PlayerEnum.PLAYER2, PlayerEnum.PLAYER3, PlayerEnum.PLAYER4);
        assertTrue(actualPlayers.containsAll(PlayerEnum.getPlayers()));
    }
}
