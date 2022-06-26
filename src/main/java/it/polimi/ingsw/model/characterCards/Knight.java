package it.polimi.ingsw.model.characterCards;


import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

public class Knight extends CharacterCard {
    private static final String name ="KNIGHT";
    private static final String description =
            "During the influence calculation this turn, " +
            "you count as having 2 more influence.";



    public Knight(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,8, parameters, advancedParameters,name,description);
    }

    /**
     * During this round currentPlayer sum addableInfluence to the
     * Influence count result
     * Set additionalInfluence with addableInfluence
     */
    @Override
    public void activateEffect() {
        final int addableInfluence = 2;
        super.activateEffect();
        super.getAdvancedParameters().addInfluence(addableInfluence);
    }
}
