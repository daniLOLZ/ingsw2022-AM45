package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.CharacterCard;

public class Mailman extends CharacterCard {

    private final int additionalSteps = 2;
    @Override
    public void activateEffect(AdvancedGame game) {
        game.setMNAdditionalSteps(additionalSteps);
    }
}
