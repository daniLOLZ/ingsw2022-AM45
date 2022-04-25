package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryPlayerTest {

    ParameterHandler parameters = null;
    //TODO nickname testing will be added when nickname handling is available


    /**
     * Test creation of single Player
     */
    @Test
    void getPlayerTest(){

        parameters = new ParameterHandler(2);

        Player player = FactoryPlayer.getPlayer("mock",PlayerEnum.PLAYER1,TeamEnum.WHITE,true,parameters,false);

        assertEquals(player.getNickname(),"mock","Wrong nickname assigned");
        assertEquals(player.getPlayerId(),PlayerEnum.PLAYER1,"Wrong PlayerId assigned");
        assertEquals(player.getTeamColor(),TeamEnum.WHITE,"assigned to wrong Team");
        assertTrue(player.isLeader(),"Wrong leader value");
        assertFalse(player instanceof AdvancedPlayer,"Returned wrong type of Player");

        assertFalse(FactoryPlayer.validNickname("mock"),"Nickname is not in already-used-nickname list");

        //FactoryPlayer is static
        //and needs resetting after tests
        FactoryPlayer.removeNickname("mock");
    }

    /**
     * Test creation of single AdvancedPlayer
     */
    @Test
    void getAdvancedPlayerTest(){

        parameters = new ParameterHandler(2);

        Player player = FactoryPlayer.getPlayer("mock",PlayerEnum.PLAYER1,TeamEnum.WHITE,true,parameters,true);
        assertEquals(player.getNickname(),"mock","Wrong nickname assigned");
        assertEquals(player.getPlayerId(),PlayerEnum.PLAYER1,"Wrong PlayerId assigned");
        assertEquals(player.getTeamColor(),TeamEnum.WHITE,"assigned to wrong Team");
        assertTrue(player.isLeader(),"Wrong leader value");
        assertTrue(player instanceof AdvancedPlayer,"Returned wrong type of Player");

        assertFalse(FactoryPlayer.validNickname("mock"),"Nickname is not in already-used-nickname list");

        //FactoryPlayer is static
        //and needs resetting after tests
        FactoryPlayer.removeNickname("mock");
    }

    /**
     * Tests creation of Players for a 2-player game
     */
    @Test
    public void get2PlayersTest() {

        parameters = new ParameterHandler(2);
        List<Player> players = FactoryPlayer.getNPlayers(2,parameters);

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

        parameters = new ParameterHandler(3);
        List<Player> players = FactoryPlayer.getNPlayers(3,parameters);

        assertEquals(players.get(0).getPlayerId(),PlayerEnum.PLAYER1,"Wrong PlayerID assigned");
        assertEquals(players.get(1).getPlayerId(),PlayerEnum.PLAYER2,"Wrong PlayerID assigned");
        assertEquals(players.get(2).getPlayerId(),PlayerEnum.PLAYER3,"Wrong PlayerID assigned");

        assertEquals(players.get(0).getTeamColor(),TeamEnum.WHITE,"Wrong Color assigned");
        assertEquals(players.get(1).getTeamColor(),TeamEnum.GREY,"Wrong Color assigned");
        assertEquals(players.get(2).getTeamColor(),TeamEnum.BLACK,"Wrong Color assigned");

        for (Player player: players) {
            assertTrue(player.isLeader(),"Wrong Leader assignment");
        }
    }

    /**
     * Tests creation of Players for a 4-player game
     */
    @Test
    public void get4PlayersTest(){

        parameters = new  ParameterHandler(4);
        List<Player> players = FactoryPlayer.getNPlayers(4,parameters);

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
