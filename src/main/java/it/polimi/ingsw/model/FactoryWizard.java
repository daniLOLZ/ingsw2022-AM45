package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class FactoryWizard {

    public static final int numOfCardsPerWizard = 10;
    public static final int idWizard[] = {0,10,20,30};

    /**
     * create and return Wizard's deck corresponding with chosen id
     * @param idWizard == 0 ||
     *                 == 10 ||
     *                 == 20 ||
     *                 == 30
     * @return
     */
    public static Wizard getWizard(int idWizard){

        if(idWizard !=0 && idWizard != 10 && idWizard != 20 && idWizard != 30)
            idWizard = 0;

        List<Assistant> assistants = new ArrayList<>();

        for(int cards = 0; cards < 10; cards++){
            assistants.add(FactoryAssistant.getAssistant(cards + idWizard));
        }

        return new Wizard(idWizard,assistants);
    }

    public static Wizard getWizard(){
        return getWizard(0);
    }

    public static List<Wizard> getAllWizards(){
        List<Wizard> listToReturn = new ArrayList<>();
        for(int i: idWizard)
            listToReturn.add(getWizard(i));
        return listToReturn;
    }
}
