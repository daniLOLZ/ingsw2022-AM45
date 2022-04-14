package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;
import it.polimi.ingsw.model.ParameterHandler;

public class Glutton extends CharacterCard {

    public Glutton(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,2, parameters, advancedParameters);
    }

    /**
     * During this turn Current player gain the professor even if he has equal number of students at Hall
     * Set DrawIsWin true
     */
    @Override
    public void activateEffect() {

        super.activateEffect();
        super.getAdvancedParameters().setDrawIsWin(true);
    }

}
