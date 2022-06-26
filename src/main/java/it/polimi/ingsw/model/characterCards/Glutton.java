package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

public class Glutton extends CharacterCard {
    private static final String name ="GLUTTON";
    private static final String description=
            "During this turn, you take control of any number of Professors " +
            "even if you have the same number of students as the player who currently controls them.";
    public Glutton(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,2, parameters, advancedParameters, name, description);
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
