package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.islands.UnmergeableException;
import it.polimi.ingsw.model.player.Player;

public class AdvancedIslandHandler extends IslandHandler{

    public AdvancedIslandHandler(Controller controller){
        super(controller);
    }

    @Override
    public void moveMN(int steps) {
        if(!checkCorrectSteps(steps)){
            controller.simpleGame.getParameters().setErrorState("INCORRECT STEPS");
            return;
        }

        int idIsland = controller.advancedGame.moveMN(steps);               //Move MN
        if(controller.advancedGame.isBlocked(idIsland)){                    //is island blocked?
            controller.advancedGame.unblockIsland(idIsland);                //unblock it and return
        }

        else {
            try {
                controller.advancedGame.evaluateIsland(idIsland);             //island is not blocked so evaluate
            } catch (UnmergeableException e) {
                //Non so cosa dovrei fare
            }
        }
    }

    @Override
    protected boolean checkCorrectSteps(int steps) {
        if(controller.advancedGame.getAdvancedParameters().getMNAdditionalSteps() == 0)
            return super.checkCorrectSteps(steps);

        if(steps == 0)
            return  false;

        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        int maxSteps = currentPlayer.getAssistantPlayed().motherNatureSteps +
                controller.advancedGame.getAdvancedParameters().getMNAdditionalSteps();
        return steps <= maxSteps;

    }
}
