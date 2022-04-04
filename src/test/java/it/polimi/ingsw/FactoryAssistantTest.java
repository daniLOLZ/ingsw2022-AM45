package it.polimi.ingsw;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.FactoryAssistant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FactoryAssistantTest {

    /**
     * tests if file json is correctly read by FactoryAssistant.getAssistant() and
     * if this one create the right card
     */
    @Test
    public void getAssistant(){
        Assistant assistant = FactoryAssistant.getAssistant(1);
        assertEquals(1,assistant.motherNatureSteps, "Wrong steps");
        assertEquals(2,assistant.turnOrder, "Wrong turnOrder");
    }
}
