package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;
import it.polimi.ingsw.model.ParameterHandler;

public class Mailman extends CharacterCard {

    private final int additionalSteps = 2;

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

        super.activateEffect();
        super.getAdvancedParameters().addMNSteps(additionalSteps);
    }
}
