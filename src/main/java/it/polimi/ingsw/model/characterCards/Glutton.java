package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Glutton extends CharacterCard {

    public Glutton(){
        super(2,2);
    }

    /**
     * During this turn Current player gain the professor even if he has equal number of students at Hall
     * Set DrawIsWin true
     * @param game
     */
    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setDrawIsWin(true);
    }

}
