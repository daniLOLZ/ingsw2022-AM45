package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;
import it.polimi.ingsw.model.ParameterHandler;

public class Knight extends CharacterCard {

    private final int addableInfluence = 2;

    public Knight(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,8, parameters, advancedParameters);
    }

    /**
     * During this round currentPlayer sum addableInfluence to the
     * Influence count result
     * Set additionalInfluence with addableInfluence
     */
    @Override
    public void activateEffect() {

        super.activateEffect();
        super.getAdvancedParameters().addInfluence(addableInfluence);
    }
}
