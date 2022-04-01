package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Knight extends CharacterCard {

    private final int addableInfluent = 2;

    public Knight(){
        super(2,8);
    }

    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setAdditionalInfluence(addableInfluent);
    }
}
