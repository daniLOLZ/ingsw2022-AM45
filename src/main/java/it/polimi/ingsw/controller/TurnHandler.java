package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.PhaseEnum;

public class TurnHandler {
    protected Controller controller;
    private int currentTurn;
    private PhaseEnum currentPhase;
    private final int numPlayers;
    int playersPlayedInThisTurn;

    public TurnHandler(Controller controller){
        this.controller = controller;
        currentPhase = PhaseEnum.PLANNING;
        currentTurn = 1;
        playersPlayedInThisTurn = 0;
        GameRuleEnum gameRule = controller.gameRule;
        numPlayers = GameRuleEnum.getNumPlayers(gameRule.id);
    }

    /**
     * starts a new turn.
     * increment turn number and set the playerPlayedInThisTurn to zero.
     */
    @Deprecated
    public void newTurn(){
        playersPlayedInThisTurn = 0;
        currentPhase = PhaseEnum.PLANNING;
        currentTurn++;
        controller.simpleGame.getParameters().setTurn(currentTurn);
    }

    /**
     * Reinitializes the board and all the users' selections for the next turn
     */
    public void initializeNewTurn(){
       controller.simpleGame.initialiseSelection();
       controller.boardHandler.refillClouds();
    }

    /**
     * Changes the turn phase to the next one (plan -> action; action -> plan)
     * starts a new turn if the current phase is an action phase
     * prepares the phase for the first player by calling startPlanningPhase or startActionPhase
     */
    public void nextPhase(){
        playersPlayedInThisTurn = 0;
        if (currentPhase.equals(PhaseEnum.ACTION)){
            currentTurn++;
            controller.simpleGame.getParameters().setTurn(currentTurn);
        }
        //Switches phase
        currentPhase = (currentPhase.equals(PhaseEnum.PLANNING)) ? PhaseEnum.ACTION : PhaseEnum.PLANNING;

        //Prepares the phase for the first player
        if(currentPhase.equals(PhaseEnum.PLANNING)){
            startPlanningPhase();
        }
        else startActionPhase();
    }

    /**
     * a player ends his phases.
     * increment playersPlayedInThisTurn.
     * This method will be called after getFromCloud
     */
    public void endPlayerPhase(){
        playersPlayedInThisTurn++;
    }

    /**
     * Start a new player's turn, now player
     * in position 'playersPlayedInThisTurn' can play
     */
    /*
    public void startPlayerPhase(){
        controller.simpleGame.startPhase(playersPlayedInThisTurn);
    }
    */
    //There's no difference between planning and action? it looks like one must come after the
    // other for the same player

    /**
     * Start a new player's phase, now player
     * in position 'playersPlayedInThisTurn' can play their planning phase
     */
    public void startPlanningPhase(){
        controller.simpleGame.startPlanningPhase(playersPlayedInThisTurn);
    }

    /**
     * The player in position 'playersPlayedInThisTurn' can now play
     * their action phase
     */
    public void startActionPhase(){
        controller.simpleGame.startActionPhase(playersPlayedInThisTurn);
        controller.boardHandler.resetStudentsMoved();
    }

    /**
     * Checks whether the player asking for control really has control
     * @param idUser the id of the user asking to take control
     * @param gamePhase the phase of the game they're asking for
     * @return true if the user can play their round in the phase specified
     */
    public boolean askForControl(Integer idUser, PhaseEnum gamePhase){
        if( controller.playerNumbers.indexOf(idUser) ==
                controller.simpleGame.getParameters().getCurrentPlayer().getPlayerId().index &&
            gamePhase.equals(controller.simpleGame.getParameters().getCurrentPhase())){
            return true;
        }
        return false;
    }

    /**
     *
     * @return true if all players have played their phase
     */
    public boolean isPhaseOver(){
        return playersPlayedInThisTurn == numPlayers;
    }

    /**
     * check if the turn is going to start is the last one
     * and modify the model with that value
     */
    public void checkLastTurn(){
        boolean sackEmpty = controller.simpleGame.emptySack();
        boolean noAssistants = controller.simpleGame.noMoreAssistant();
        controller.simpleGame.setLastTurn(sackEmpty || noAssistants);
    }

}
