package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryPlayerTest {

    //TODO nickname testing will be added when nickname handling is available

    /**
     * Tests creation of Players for a 2-player game
     */
    @Test
    public void get2PlayersTest() {

        List<Player> players = FactoryPlayer.getNPlayers(null, 2);

        assertEquals(players.get(0).getPlayerId(), PlayerEnum.PLAYER1, "Wrong PlayerID assigned");
        assertEquals(players.get(1).getPlayerId(), PlayerEnum.PLAYER2, "Wrong PlayerID assigned");

        assertEquals(players.get(0).getTeamColor(), TeamEnum.WHITE, "Wrong Color assigned");
        assertEquals(players.get(1).getTeamColor(), TeamEnum.BLACK, "Wrong Color assigned");

        for (Player player : players) {
            assertTrue(player.isLeader(), "Wrong Leader assignment");
        }
    }

    /**
     * Tests creation of Players for a 3-player game
     */
    @Test
    public void get3PlayersTest(){

        List<Player> players = FactoryPlayer.getNPlayers(null,3);

        assertEquals(players.get(0).getPlayerId(),PlayerEnum.PLAYER1,"Wrong PlayerID assigned");
        assertEquals(players.get(1).getPlayerId(),PlayerEnum.PLAYER2,"Wrong PlayerID assigned");
        assertEquals(players.get(2).getPlayerId(),PlayerEnum.PLAYER3,"Wrong PlayerID assigned");

        assertEquals(players.get(0).getTeamColor(),TeamEnum.WHITE,"Wrong Color assigned");
        assertEquals(players.get(1).getTeamColor(),TeamEnum.BLACK,"Wrong Color assigned");
        assertEquals(players.get(2).getTeamColor(),TeamEnum.GREY,"Wrong Color assigned");

        for (Player player: players) {
            assertTrue(player.isLeader(),"Wrong Leader assignment");
        }
    }

    /**
     * Tests creation of Players for a 4-player game
     */
    @Test
    public void get4PlayersTest(){

        List<Player> players = FactoryPlayer.getNPlayers(null,4);

        assertEquals(players.get(0).getPlayerId(),PlayerEnum.PLAYER1,"Wrong PlayerID assigned");
        assertEquals(players.get(1).getPlayerId(),PlayerEnum.PLAYER2,"Wrong PlayerID assigned");
        assertEquals(players.get(2).getPlayerId(),PlayerEnum.PLAYER3,"Wrong PlayerID assigned");
        assertEquals(players.get(3).getPlayerId(),PlayerEnum.PLAYER4,"Wrong PlayerID assigned");

        assertEquals(players.get(0).getTeamColor(), TeamEnum.WHITE, "Wrong Color assigned");
        assertEquals(players.get(1).getTeamColor(), TeamEnum.WHITE, "Wrong Color assigned");
        assertEquals(players.get(2).getTeamColor(), TeamEnum.BLACK, "Wrong Color assigned");
        assertEquals(players.get(3).getTeamColor(), TeamEnum.BLACK, "Wrong Color assigned");

        for (Player player: players) {
            assertEquals(player.isLeader(),player.getPlayerId().ordinal() % 2 == 0,"Wrong Leader assignment");
        }
    }
}