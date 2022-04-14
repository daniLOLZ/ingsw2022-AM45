package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;

public class Knight extends CharacterCard {

    private final int addableInfluence = 2;

    public Knight(AdvancedParameterHandler advancedParameters){
        super(2,8, advancedParameters);
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
