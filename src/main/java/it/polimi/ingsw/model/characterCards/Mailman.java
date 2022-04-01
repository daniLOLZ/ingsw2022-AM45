package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Mailman extends CharacterCard {

    private final int additionalSteps = 2;

    public Mailman(){
        super(1,4);
    }
    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setMNAdditionalSteps(additionalSteps);
    }
}
