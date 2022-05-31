package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRuleEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActiveLobbiesTest {

    /**
     * Tests whether a lobby with no participants gets destroyed
     */
    @Test
    public void lobbyFullyDestroyedTest(){
        GameRuleEnum rule = GameRuleEnum.ADVANCED_3;
        Lobby lobby = ActiveLobbies.assignLobby(rule);
        lobby.addPlayer(0);
        lobby.removePlayer(0);
        assertFalse(ActiveLobbies.getLobbies().contains(lobby));
    }
}
