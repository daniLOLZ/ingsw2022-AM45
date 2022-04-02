package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class FactoryWizard {

    public static final int numOfCardsPerWizard = 10;

    public static Wizard getWizard(int idWizard){

        if(idWizard !=0 && idWizard != 10 && idWizard != 20 && idWizard != 30)
            idWizard = 0;

        List<Assistant> assistants = new ArrayList<>();

        for(int cards = 0; cards < 10; cards++){
            assistants.add(FactoryAssistant.getAssistant(cards + idWizard));
        }

        return new Wizard(idWizard,assistants);
    }
}
