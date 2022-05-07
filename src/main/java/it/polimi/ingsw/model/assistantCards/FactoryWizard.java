package it.polimi.ingsw.model.assistantCards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactoryWizard {

    public static final int numOfCardsPerWizard = 10;
    public static final int[] idWizard = {0,10,20,30};

    /**
     * create and return Wizard's deck corresponding with chosen id
     * @param wizard == 0 ||
     *                 == 10 ||
     *                 == 20 ||
     *                 == 30
     * @return Wizard deck(with numOfCardsPerWizard Assistant cards)
     * with chosen id if this one is an available parameter
     * return Wizard with id == 0 otherwise
     */
    public static Wizard getWizard(int wizard)  {
        final int defaultWizard = idWizard[0];

        final int wizardToMatch = wizard;
        if(Arrays.stream(idWizard).noneMatch(id -> id == wizardToMatch))
            wizard = defaultWizard;

        List<Assistant> assistants = new ArrayList<>();

        for(int cards = 1; cards <= numOfCardsPerWizard; cards++){
            assistants.add(FactoryAssistant.getAssistant(cards + wizard));
        }

        return new Wizard(wizard,assistants);
    }

    public static Wizard getWizard()  {
        return getWizard(0);
    }

    public static List<Wizard> getAllWizards()  {
        List<Wizard> listToReturn = new ArrayList<>();
        for(int i: idWizard)
            listToReturn.add(getWizard(i));
        return listToReturn;
    }

    /**
     *
     * @param id  > 0
     * @return true if idWizard contains id
     */
    public static boolean checkCorrectId(int id){
        for (int j : idWizard) {
            if (j == id)
                return true;
        }

        return false;
    }
}
