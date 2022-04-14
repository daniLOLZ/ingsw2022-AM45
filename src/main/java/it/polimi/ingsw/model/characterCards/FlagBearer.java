package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;

public class FlagBearer extends CharacterCard {

    public FlagBearer(AdvancedParameterHandler advancedParameters){
        super(3,3, advancedParameters);
    }

    /**
     * Now you must calculate influence of a specific IslandGroup wit all consequences
     * Set IslandToEvaluate true
     * @param game
     */
    //@Override
    //TODO I'm not touching this but it needs to be changed
    public void activateEffect(AdvancedGame game) {

        super.activateEffect();
        game.setIslandToEvaluateDue(true);

    }
}
