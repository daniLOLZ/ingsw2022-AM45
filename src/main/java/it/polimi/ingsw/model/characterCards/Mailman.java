package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedParameterHandler;
import it.polimi.ingsw.model.ParameterHandler;

public class Mailman extends CharacterCard {



    public Mailman(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(1,4, parameters, advancedParameters);
    }

    /**
     * During this round you can add additionSteps at maximum steps that Mother
     * Nature can do.
     * Set MNAdditionalSteps with additionalSteps
     */
    @Override
    public void activateEffect() {
        final int additionalSteps = 2;
        super.activateEffect();
        super.getAdvancedParameters().addMNSteps(additionalSteps);
    }
}
