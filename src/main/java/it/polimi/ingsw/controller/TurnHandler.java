package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.player.Player;

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
     * Takes all actions necessary to begin the next turn: <br>
     * increments the turn counter, refills the clouds,
     * reinitializes the board and all the users' selections
     */
    public void initializeNewTurn(){
        currentTurn++;
        controller.simpleGame.getParameters().setTurn(currentTurn);
        controller.simpleGame.initialiseSelection();
        controller.boardHandler.refillClouds();
        playersPlayedInThisTurn = 0;
    }

    /**
     * Changes the turn phase to the next one (plan -> action; action -> plan)
     * starts a new turn if the current phase is an action phase
     * prepares the phase for the first player by calling startPlanningPhase or startActionPhase
     */
    public void nextPhase(){

        playersPlayedInThisTurn = 0;

        // If the last action phase is ending, we need to take certain turn ending actions
        if (currentPhase.equals(PhaseEnum.ACTION)){
            initializeNewTurn();
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
     * a player ends his PlanningPhase.
     * increment playersPlayedInThisTurn.
     * This method will be called after chooseAssistant
     */
    public void endPlayerPhase(){
        playersPlayedInThisTurn++;
        if(playersPlayedInThisTurn != numPlayers) {
            if (currentPhase.equals(PhaseEnum.PLANNING)) {
                startPlanningPhase();
            }
        }
        else startActionPhase();
    }

    /**
     * Start a new player's PlanningPhase, now player
     * in position 'playersPlayedInThisTurn' can play their planning phase
     */
    private void startPlanningPhase(){
        controller.simpleGame.startPlanningPhase(playersPlayedInThisTurn);
    }

    /**
     * The player in position 'playersPlayedInThisTurn' can now play
     * their action phase
     */
    private void startActionPhase(){
        controller.simpleGame.startActionPhase(playersPlayedInThisTurn);
        controller.boardHandler.resetStudentsMoved();
    }

    /**
     * Checks whether the player asking for control really has control
     * @param idUser the id of the user asking to take control
     * @param gamePhase the phase of the game they're asking for
     * @return true if the user can play their round in the phase specified
     */
    @Deprecated
    public boolean askForControl(Integer idUser, PhaseEnum gamePhase){
        if( controller.playerNumbers.indexOf(idUser) ==
                controller.simpleGame.getParameters().getCurrentPlayer().getPlayerId().index &&
            gamePhase.equals(controller.simpleGame.getParameters().getCurrentPhase())){
            return true;
        }
        return false;
    }

    public PhaseEnum getCurrentPhase(){
        return controller.simpleGame.getParameters().getCurrentPhase();
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

    /**
     * Update new current player following SimpleGame.players order.
     * If all players have played their turn, change phase and if this was the planning
     * phase then sort the players
     */
    public void nextCurrentPlayer(){
        int current = controller.simpleGame.getParameters().getCurrentPlayer().getTurn() - 1; //PlayerTurn = Player position in players + 1
        current++;

        if(current == numPlayers){
            if(controller.simpleGame.getParameters().getCurrentPhase() == PhaseEnum.PLANNING)
                controller.simpleGame.sortPlayers();

            nextPhase();
            return;
        }

        Player player = controller.simpleGame.getPlayers().get(current);
        controller.simpleGame.getParameters().setCurrentPlayer(player);
    }

}
