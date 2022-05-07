package it.polimi.ingsw.model.assistantCards;

import java.util.ArrayList;
import java.util.List;

public class Wizard {
    private final int idWizard;                 //ID    0  10  20  30
    private List<Assistant> assistants;

    public Wizard(int idWizard, List<Assistant> assistants){
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
        Assistant assistant = getAssistantByID(id);
        assistants.remove(assistant);
        return assistant;
    }

    public boolean isEmpty(){
        return assistants.isEmpty();
    }

    public int size(){
        return assistants.size();
    }

    public Assistant getAssistant(int index){
        return assistants.get(index);
    }

    public Assistant getAssistantByID(int id) throws NoSuchAssistantException{
        for (Assistant assistant : assistants) {
            if (assistant.getId() == id){ //look for the Assistant card
                assistants.remove(assistant);
                return assistant;
            }
        }
        throw new NoSuchAssistantException();
    }

    public int getIdWizard(){return idWizard;}

    /**
     *
     * @param id > 0
     * @return true <=> a Card with id == param is contained in this deck
     */
    public boolean contains(int id){
        return assistants.stream().anyMatch(card -> card.id == id);
    }

    /**
     *
     * @return a list with the remained Assistants' ids
     */
    public List<Integer> getRemainedAssistants(){
        List<Integer> idList = new ArrayList<>();
        for(Assistant assistant: assistants)
            idList.add(assistant.id);
        return idList;
    }

}
