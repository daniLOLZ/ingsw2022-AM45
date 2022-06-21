package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.islands.UnmergeableException;
import it.polimi.ingsw.model.player.Player;

public class IslandHandler {
    protected final Controller controller;

    public IslandHandler(Controller controller){
        this.controller = controller;
    }

    /**
     * Move Mother Nature and put her on island group distant
     * steps by current position of Mother Nature.
     * Mother Nature follows the order of the island group, based
     * on links between them.
     * When reach the correct island group evaluate the influence,
     * if a team wins its towers will be built.
     * Check if there are island groups that can be merged, in
     * this case merge them.
     * Update Mother Nature position and update parameters.
     * @param steps > 0
     */
    public boolean moveMN(int steps){
        if(!checkCorrectSteps(steps)){
            controller.simpleGame.getParameters().setErrorState("INCORRECT STEPS");
            return false;
        }

        int idIsland = controller.simpleGame.moveMN(steps);

        try {
            controller.simpleGame.evaluateIsland(idIsland);
        } catch (UnmergeableException e) {
            //Non so cosa dovrei fare
        }
        finally {
            return true;
        }
    }

    /**
     * evaluate the chosen island group influence,
     * if a team wins its towers will be built.
     * Check if there are island groups that can be merged, in
     * this case merge them.
     * Update Mother Nature position and update parameters.
     * This method is used by Flag-Bearer's effect in Character Card Handler
     * @param idIsland > 0
     */
    protected void evaluateIsland(int idIsland){
        try {
            controller.simpleGame.evaluateIsland(idIsland);
        } catch (UnmergeableException e) {
            //non so cosa dovrei fare
        }
    }

    /**
     *
     * @param steps > 0
     * @return false if player choose an invalid number of steps.
     * Zero or a number greater than played Assistant card's  steps.
     */
    protected boolean checkCorrectSteps(int steps){
        if(steps <= 0)
            return  false;

        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        int maxSteps = currentPlayer.getAssistantPlayed().motherNatureSteps;
        return steps <= maxSteps;
    }


}
