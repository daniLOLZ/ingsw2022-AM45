package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;

public class Centaur extends CharacterCard {

    public Centaur(AdvancedParameterHandler advancedParameters){
        super(3,6, advancedParameters);
    }

    /**
     * During this turn the towers' contribution is zero.
     * Set CountTowers true;
     */
    @Override
    public void activateEffect() {

        super.activateEffect();
        super.getAdvancedParameters().setCountTowers(false);
    }

}
