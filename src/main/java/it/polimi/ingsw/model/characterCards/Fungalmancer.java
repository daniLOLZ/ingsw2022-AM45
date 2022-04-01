package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Fungalmancer extends CharacterCard {

    public Fungalmancer(){
        super(3,9);
    }

    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);

        game.setIgnoredStudent(true);
    }
}
