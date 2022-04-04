package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Centaur extends CharacterCard {

    public Centaur(){
        super(3,6);
    }

    /**
     * During this turn the towers' contribution is zero.
     * Set CountTowers true;
     * @param game
     */
    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setCountTowers(true);
    }

}
