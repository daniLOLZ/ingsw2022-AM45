package it.polimi.ingsw;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.FactoryWizard;
import it.polimi.ingsw.model.Wizard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FactoryWizardTest {

    @Test
    public void getWizard(){
        Wizard wizard = FactoryWizard.getWizard(0);
        Wizard wizard1 = FactoryWizard.getWizard(10);
        assertEquals(10, wizard.size(), "Wrong number of cards");
        assertEquals(10, wizard1.size(), "Wrong number of cards");

        for(int i=0; i<10;i++){
            assertEquals(wizard.getAssistant(i),wizard1.getAssistant(i),"Wrong Card fit");
        }
    }
}
