package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.FactoryAssistant;
import it.polimi.ingsw.model.assistantCards.NoSuchAssistantException;
import it.polimi.ingsw.model.assistantCards.Wizard;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 *ACTIONS:
 *
 *
 */

public class AssistantHandler {
    private final Controller controller;
    protected List<Assistant> assistantsPlayed;


    public AssistantHandler(Controller controller){
        this.controller = controller;
        assistantsPlayed = new ArrayList<>();

    }

    public void setAssistantsPlayed(Assistant assistant) {
        this.assistantsPlayed.add(assistant);
    }

    /**
     * play an assistant card if the chosen card is valid.
     * Update the lists assistantPlayed and turnPlayer.
     * @param id the id of the assistant to play (1 <= id <= 10)
     * @return true if the assistant was played successfully
     */
    public boolean playCard(int id){

        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        if(currentPlayer.getTurn() == 1)
            assistantsPlayed.clear();


        if(checkValidAssistant(id)){
            controller.simpleGame.playAssistant(currentPlayer, id);
            assistantsPlayed.add(FactoryAssistant.getAssistant(id));
        }
        else{
            controller.simpleGame.getParameters().setErrorState("INVALID ASSISTANT");
            return false;
        }
        return true;
    }




    /**
     * check if a played card is Valid
     * @param id (1 <= id <= 10)
     * @return false if card is not present in Wizard
     * true if Current Player's cards are all equal to already played cards
     * false if Current Player has at least one different card from already played card but
     * He played a card equal to an already played card.
     */
    public boolean checkValidAssistant(int id){
        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        Wizard wizard = currentPlayer.getWizard();
        boolean idInWizard = wizard.contains(id); // todo i am going insane

        if(!idInWizard){
            return false;
        }

        final Assistant currentAssistant = getAssistant(id);
        if(currentAssistant == null)
            return false;

        boolean allEquals = allEquals();
        if(allEquals)
            return true;

        boolean alreadyPlayed = isPlayed(currentAssistant);
        return !alreadyPlayed;
    }

    /**
     *
     * @param id (1 <= id <= 10)
     * @return Assistant corresponding with chosen id
     * set error state otherwise
     */
    private Assistant getAssistant(int id){
        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        Wizard wizard = currentPlayer.getWizard();
        try {
            return wizard.getAssistantByID(id);
        }
        catch (NoSuchAssistantException e) {
            controller.simpleGame.getParameters().setErrorState("Assistant not in Wizard");
            return null;
        }
    }

    /**
     *
     * @param assistant!=null
     * @return true if and only if played card is equal to at least one card of already played cards
     */
    private boolean isPlayed(final Assistant assistant){
        if(assistantsPlayed.isEmpty())
            return  false;

        return assistantsPlayed.stream()
                .filter(Objects::nonNull)
                .anyMatch(card -> card.equals(assistant));
    }

    /**
     *
     * @return true if and only if the last Current Player's cards are equal to
     * already played cards
     */
    private boolean allEquals(){
        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        Wizard currentWizard = currentPlayer.getWizard();
        List<Assistant> assistantList = new ArrayList<>();

        //SUPPORT ASSISTANTS LIST
        for(int position = 0; position < currentWizard.size(); position++)
            assistantList.add(currentWizard.getAssistant(position));

        //CHECK AND REMOVE EQUAL ASSISTANTS BETWEEN ASSISTANTS PLAYED IN THIS TURN
        // AND CURRENT PLAYER DECK
        for(Assistant assistant: assistantsPlayed)
            assistantList.remove(assistant);

        //IF THERE ARE NO MORE DIFFERENT CARDS
        return assistantList.size() == 0;

    }



}
