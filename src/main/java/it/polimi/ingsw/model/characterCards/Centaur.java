package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

public class Centaur extends CharacterCard {
    private static final String name = "CENTAUR";
    private static final String description =
            "When resolving a Conquering on an Island, " +
            "Towers do not count towards influence.";

    public Centaur(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(3,6, parameters, advancedParameters, name, description);
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
