package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;
import it.polimi.ingsw.model.ParameterHandler;

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
