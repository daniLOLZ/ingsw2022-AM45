package it.polimi.ingsw;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.FactoryWizard;
import it.polimi.ingsw.model.Wizard;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FactoryWizardTest {

    /**
     * tests if FactoryWizard create two deck of cards Assistant
     * with equal cards but different id
     */
    @Test
    public void getWizard()  {
        Wizard wizard = null;

        wizard = FactoryWizard.getWizard(0);

        Wizard wizard1 = FactoryWizard.getWizard(10);
        assertEquals(10, wizard.size(), "Wrong number of cards");
        assertEquals(10, wizard1.size(), "Wrong number of cards");


        for(int i=0; i<10;i++){
            assertEquals(wizard.getAssistant(i),wizard1.getAssistant(i),"Wrong Card fit");
            assertNotEquals(wizard.getAssistant(i).id, wizard1.getAssistant(i).id, "Two cards with same id but they " +
                    "belong to 2 different wizard");
        }
    }

    /**
     * tests if all Assitants cards were created correctly
     */
    @Test
    public void getAllWizard()  {
        List<Wizard> list = new ArrayList<>();
        Wizard wizardTest = null;

        wizardTest = FactoryWizard.getWizard();
        list.addAll(FactoryWizard.getAllWizards());
        for(Wizard wizard : list){
            for(int i=0;i<FactoryWizard.numOfCardsPerWizard;i++){
                assertEquals(wizardTest.getAssistant(i),wizard.getAssistant(i),
                        "Different expected card's values");
                assertTrue((wizard.getIdWizard()==0) || (wizard.getIdWizard() > 0 &&
                        wizard.getAssistant(i).id != wizardTest.getAssistant(i).id),
                        "Wrong id");
            }
        }

    }

    /**
     * tests if wrong id parameter is managed well
     */
    @Test
    public void wrongId() {
        Wizard wizard = FactoryWizard.getWizard(3);
        assertEquals(0, wizard.getIdWizard(), "Wrong wizard deck gift as answer " +
                "to a wrong id request");
        wizard = FactoryWizard.getWizard(39);
        assertEquals(0, wizard.getIdWizard(), "Wrong wizard deck gift as answer " +
                "to a wrong id request");

    }
}
