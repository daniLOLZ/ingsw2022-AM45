package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

public class Mailman extends CharacterCard {
    private static final String name ="MAILMAN";
    private static final String description =
            "You may move Mother Nature " +
            "up to 2 additional Islands " +
            "than is indicated by the Assistant " +
            "card you've played.";



    public Mailman(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(1,4, parameters, advancedParameters,name, description);
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
