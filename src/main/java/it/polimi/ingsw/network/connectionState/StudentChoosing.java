package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.CommandEnum;

public class StudentChoosing extends ActionPhaseTurn {

    public StudentChoosing(GameRuleEnum rule) {

        super(rule);
        allow(CommandEnum.SELECT_STUDENT);

    }

}
