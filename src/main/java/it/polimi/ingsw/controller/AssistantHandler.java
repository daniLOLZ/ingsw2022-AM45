package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.NoSuchAssistantException;
import it.polimi.ingsw.model.assistantCards.Wizard;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/*
 *ACTIONS:
 *
 *
 */

public class AssistantHandler {
    private final Controller controller;
    private List<Assistant> assistantsPlayed;
    private final List<Player> players;
    private List<Player> turnPlayer;

    public AssistantHandler(Controller controller){
        this.controller = controller;
        assistantsPlayed = new ArrayList<>();
        players = controller.simpleGame.getPlayers();
        turnPlayer = new ArrayList<>();
    }

    public void setAssistantsPlayed(List<Assistant> assistantsPlayed) {
        this.assistantsPlayed = assistantsPlayed;
    }

    public void setTurnPlayer(List<Player> turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    /**
     * play an assistant card if the chosen card is valid.
     * Update the lists assistantPlayed and turnPlayer.
     *
     * @param id > 0
     */
    public void playCard(int id){

        //CHECK IF CARD IS VALID
        if(!checkValidAssistant(id)){
            controller.simpleGame.getParameters().setErrorState("Assistant not valid");
        }

        //GET ASSISTANT CARD TO USE FOR SORT PLAYERS
        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        Wizard wizard = currentPlayer.getWizard();
        Assistant assistantPlayed;
        try {
            assistantPlayed = wizard.getAssistantByID(id);
            assistantsPlayed.add(assistantPlayed);
        } catch (NoSuchAssistantException e) {
            controller.simpleGame.getParameters().setErrorState("Assistant not in Wizard");
            return;
        }

        //PLAY CARD IN MODEL
        currentPlayer.playAssistant(id);


        //SORT PLAYERS
        if(turnPlayer.size() == 0){
            turnPlayer.add(currentPlayer);
            return;
        }

        int turnOthers;

        for(int position=0; position < turnPlayer.size(); position++){
            turnOthers = turnPlayer.get(position).getAssistantPlayed().getTurnOrder();
            if(assistantPlayed.getTurnOrder() < turnOthers)
                turnPlayer.add(position,currentPlayer);
        }
    }


    public void resetAssistantsPlayed(){
        assistantsPlayed.clear();
    }

    public void resetTurnPlayers(){
        turnPlayer.clear();
    }


    /**
     * check if a played card is Valid
     * @param id > 0
     * @return false if card is not present in Wizard
     * true if Current Player's cards are all equal to already played cards
     * false if Current Player has at least one different card from already played card but
     * He played a card equal to an already played card.
     */
    public boolean checkValidAssistant(int id){
        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        Wizard wizard = currentPlayer.getWizard();
        boolean idInWizard = wizard.contains(id);

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
     * @param id > 0
     * @return Assistant corresponding with chosen id
     * set error state otherwise
     */
    private Assistant getAssistant(int id){
        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        Wizard wizard = currentPlayer.getWizard();
        try {
            return wizard.getAssistantByID(id);
        } catch (NoSuchAssistantException e) {
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
        return assistantsPlayed.stream().anyMatch(card -> card.equals(assistant));
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
