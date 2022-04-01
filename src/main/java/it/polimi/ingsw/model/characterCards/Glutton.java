package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Glutton extends CharacterCard {

    public Glutton(){
        super(2,2);
    }

    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setDrawIsWin(true);
    }

}
