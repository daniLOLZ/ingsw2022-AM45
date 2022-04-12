package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WizardTest {

    Wizard wizard = null;
    Assistant assistant = null;

    @BeforeEach
    public void initialise()  {
        try {
            wizard = FactoryWizard.getWizard();
        } catch (IOException e) {
            e.printStackTrace();
            List<Assistant> list = new ArrayList<>();
            list.add(new Assistant(1,1,1));
            list.add(new Assistant(2,1,2));
            list.add(new Assistant(3,2,3));
            list.add(new Assistant(4,2,4));
            list.add(new Assistant(5,3,5));
            list.add(new Assistant(6,3,6));
            list.add(new Assistant(7,4,7));
            list.add(new Assistant(8,4,8));
            list.add(new Assistant(9,5,9));
            list.add(new Assistant(10,5,10));
            wizard = new Wizard(0,list);
        } catch (ParseException e) {
            e.printStackTrace();
            List<Assistant> list = new ArrayList<>();
            list.add(new Assistant(1,1,1));
            list.add(new Assistant(2,1,2));
            list.add(new Assistant(3,2,3));
            list.add(new Assistant(4,2,4));
            list.add(new Assistant(5,3,5));
            list.add(new Assistant(6,3,6));
            list.add(new Assistant(7,4,7));
            list.add(new Assistant(8,4,8));
            list.add(new Assistant(9,5,9));
            list.add(new Assistant(10,5,10));
            wizard = new Wizard(0,list);
        }
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
