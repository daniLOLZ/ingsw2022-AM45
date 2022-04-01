package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.CharacterCard;

public class Centaur extends CharacterCard {

    @Override
    public void activateEffect(AdvancedGame game) {
        game.setCountTowers(false);
    }

}
