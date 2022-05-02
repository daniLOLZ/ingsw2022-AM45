package it.polimi.ingsw.controller;

public class TurnHandler {
    protected Controller controller;
    private int currentTurn;
    private final int numPlayers;
    int playersPlayedInThisTurn;

    public TurnHandler(Controller controller){
        this.controller = controller;
        currentTurn = 0;
        playersPlayedInThisTurn = 0;
        int players = controller.gameRule.id;
        if(players > 10)
            numPlayers = players / 10;
        else
            numPlayers = players;
    }

    /**
     * starts a new turn.
     * increment turn number and set the playerPlayedInThisTurn to zero.
     */
    public void newTurn(){
        playersPlayedInThisTurn = 0;
        currentTurn++;
        controller.simpleGame.getParameters().setTurn(currentTurn);
    }

    /**
     * a player ends his phases.
     * increment playersPlayedInThisTurn.
     * This method will be called after getFromCloud
     */
    public void endPlayerPhases(){
        playersPlayedInThisTurn++;
    }

    /**
     *
     * @return true if all players have played their phases
     */
    public boolean endTurn(){
        return playersPlayedInThisTurn == numPlayers;
    }

    /**
     * check if the turn is going to start is the last one
     * and modify the model with that value
     */
    public void isLastTurn(){
        boolean sackEmpty = controller.simpleGame.emptySack();
        boolean noAssistants = controller.simpleGame.noMoreAssistant();
        controller.simpleGame.setLastTurn(sackEmpty || noAssistants);
    }

}
