package it.polimi.ingsw.model;

import java.util.Collection;

public class Wizard {
    private final int idWizard;
    private Collection<Assistant> assistants;

    public Wizard(int idWizard, Collection<Assistant> assistants){
        this.idWizard = idWizard;
        this.assistants = assistants;
    }

    /**
     * Choose an Assistant card from the deck, remove it from deck and return it to the Player
     * @param id The id of the selected Assistant card
     * @return the selected Assistant card
     * @exception NoSuchAssistantException The Assistant card is not in the deck
     */
    public Assistant playCard(int id) throws NoSuchAssistantException {
        for (Assistant assistant : assistants) {
            if (assistant.getId() == id){ //look for the Assistant card
                assistants.remove(assistant);
                return assistant;
            }
        }
        throw new NoSuchAssistantException();
    }

    public boolean isEmpty(){
        return assistants.isEmpty();
    }
}
