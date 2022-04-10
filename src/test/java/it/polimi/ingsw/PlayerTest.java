package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    Player player;
    Wizard wizard;
    SimpleGame game;

    @BeforeEach
    public void initialise(){
        try {
            game=new SimpleGame(2);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
        player = new Player(game,PlayerEnum.PLAYER1,"mock",TeamEnum.WHITE,true);

        wizard = player.getWizard();
    }

    /**
     * Test if AssistantPlayed is updated correctly
     */
    @Test
    public void playAssistantTest(){

        Assistant assistant = wizard.getAssistant(2);
        player.playAssistant(assistant.id);

        assertEquals(player.getAssistantPlayed(),assistant,"Wrong Assistant was played");
    }

    /**
     * Tests if NoSuchAssistantException is thrown when given invalid Assistant ID
     */
    @Test
    public void playInvalidAssistantTest(){
        player.playAssistant(1000);
        assertNull(player.getAssistantPlayed(),"Played Assistant with invalid ID");
    }
}
