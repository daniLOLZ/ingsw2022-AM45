package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

public class Centaur extends CharacterCard {

    public Centaur(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(3,6, parameters, advancedParameters);
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
