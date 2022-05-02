package it.polimi.ingsw;

import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.FactoryAssistant;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class FactoryAssistantTest {

    /**
     * tests if file json is correctly read by FactoryAssistant.getAssistant() and
     * if this one create the right card
     */
    @Test
    public void getAssistant(){

        Assistant assistant = FactoryAssistant.getAssistant(18);
        assertEquals(4,assistant.motherNatureSteps, "Wrong steps");
        assertEquals(8,assistant.turnOrder, "Wrong turnOrder");

        assistant = FactoryAssistant.getAssistant(1);
        assertEquals(1,assistant.motherNatureSteps, "Wrong steps");
        assertEquals(1,assistant.turnOrder, "Wrong turnOrder");

        assistant = FactoryAssistant.getAssistant(40);
        assertEquals(5,assistant.motherNatureSteps, "Wrong steps");
        assertEquals(10,assistant.turnOrder, "Wrong turnOrder");


    }

    /**
     * test values of all Assistant Cards
     */
    @Test
    public void correctAssistants(){
        int steps[] = {0,1,1,2,2,3,3,4,4,5,5};
        int turn[] = {0,1,2,3,4,5,6,7,8,9,10};
        Assistant card;

        for(int id=1;id<=40; id++) {
            int position = id % FactoryWizard.numOfCardsPerWizard;
            if (position == 0)
                position = FactoryWizard.numOfCardsPerWizard;


            card = FactoryAssistant.getAssistant(id);
            assertEquals(steps[position], card.motherNatureSteps, "Wrong steps with " + id + "card");
            assertEquals(turn[position], card.turnOrder, "Wrong turn with " + id + "card");
        }

    }

    /**
     * tests if wrong id parameter is managed well
     */
    @Test
    public void wrongId(){

            Assistant assistant = FactoryAssistant.getAssistant(-1);
            assertEquals(FactoryAssistant.standardAssistant,assistant,"Wrong card returned");
            assistant = FactoryAssistant.getAssistant(FactoryAssistant.numCards +1);
            assertEquals(FactoryAssistant.standardAssistant,assistant,"Wrong card returned");

    }
}
