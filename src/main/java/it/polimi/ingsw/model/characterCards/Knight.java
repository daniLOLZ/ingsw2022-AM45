package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.CharacterCard;

public class Knight extends CharacterCard {

    private final int addabelInfluent = 2;

    @Override
    public void activateEffect(AdvancedGame game) {
        game.setAdditionalInfluence(addabelInfluent);
    }
}
