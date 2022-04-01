package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Centaur extends CharacterCard {

    public Centaur(){
        super(3,6);
    }

    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setCountTowers(false);
    }

}
