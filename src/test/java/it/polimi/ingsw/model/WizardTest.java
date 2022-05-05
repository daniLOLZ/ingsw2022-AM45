package it.polimi.ingsw.model;

import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import it.polimi.ingsw.model.assistantCards.NoSuchAssistantException;
import it.polimi.ingsw.model.assistantCards.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WizardTest {

    Wizard wizard = null;
    Assistant assistant = null;

    @BeforeEach
    public void initialise()  {

        wizard = FactoryWizard.getWizard();
        assistant = wizard.getAssistant(0);
    }

    /**
     * Tests if assistant is correctly played
     */
    @Test
    public void playCardTest(){
        try {
            wizard.playCard(assistant.getId());
        } catch (NoSuchAssistantException e) {
            e.printStackTrace();
        }

        assertEquals(wizard.size(), FactoryWizard.numOfCardsPerWizard - 1,"Number of Assistants in deck did not decrease");

        for (int index = 0; index < wizard.size(); index++) {

            assertNotSame(wizard.getAssistant(index), assistant,"Assistant was not removed from the deck");
        }
    }

    /**
     * Tests if NoSuchAssistant is thrown correctly
     */
    @Test
    public void playNonPresentCardTest(){

        assertThrows(NoSuchAssistantException.class, () -> wizard.playCard(1000),"Method did not signal invalid AssistantId");
        assertEquals(wizard.size(),FactoryWizard.numOfCardsPerWizard,"Deck was altered");
    }
}
