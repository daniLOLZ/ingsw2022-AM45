package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.CommandEnum;

public class StudentMoving extends ActionPhaseTurn {

    public StudentMoving(GameRuleEnum rule){

        //We disable selecting a character card during this inbetween phase, giving the constructor
        // a simple rule instead of whatever the actual rule might be
        super(GameRuleEnum.SIMPLE_2);
        allow(CommandEnum.PUT_IN_HALL);
        allow(CommandEnum.PUT_IN_ISLAND);
        allow(CommandEnum.DESELECT_STUDENT);

    }
}
