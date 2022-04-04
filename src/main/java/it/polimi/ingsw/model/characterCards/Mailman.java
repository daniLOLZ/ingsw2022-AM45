package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Mailman extends CharacterCard {

    private final int additionalSteps = 2;

    public Mailman(){
        super(1,4);
    }

    /**
     * During this round you can add additionSteps at maximum steps that Mother
     * Nature can do.
     * Set MNAdditionalSteps with additionalSteps
     * @param game
     */
    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setMNAdditionalSteps(additionalSteps);
    }
}
