package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class FlagBearer extends CharacterCard {

    public FlagBearer(){
        super(3,3);
    }

    /**
     * Now you must calculate influence of a specific IslandGroup wit all consequences
     * Set IslandToEvaluate true
     * @param game
     */
    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setIslandToEvaluateDue(true);

    }
}
