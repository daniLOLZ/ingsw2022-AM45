package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    Player player;
    Wizard wizard;
    ParameterHandler parameters;

    @BeforeEach
    public void initialise(){
        parameters = new ParameterHandler(2);
        player = new Player(PlayerEnum.PLAYER1,"mock",TeamEnum.WHITE,true, parameters);

        wizard = player.getWizard();

    }

    /**
     * Test if AssistantPlayed is updated correctly
     */
    @Test
    public void playAssistantTest(){

        if(parameters.getErrorState() == null){
        Assistant assistant = wizard.getAssistant(2);
        player.playAssistant(assistant.id);

        assertEquals(player.getAssistantPlayed(),assistant,"Wrong Assistant was played");
        }
    }

    /**
     * Tests if NoSuchAssistantException is thrown when given invalid Assistant ID
     */
    @Test
    public void playInvalidAssistantTest(){
        if(parameters.getErrorState() == null){
        player.playAssistant(1000);
        assertNull(player.getAssistantPlayed(),"Played Assistant with invalid ID");
        }
    }
}
