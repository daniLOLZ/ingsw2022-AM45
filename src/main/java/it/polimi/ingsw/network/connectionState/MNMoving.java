package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.CommandEnum;

public class MNMoving extends ActionPhaseTurn {

    public MNMoving(GameRuleEnum rule) {

        super(rule);
        allow(CommandEnum.MOVE_MN);

    }
}
