package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.CommandEnum;

public class EndTurn extends ActionPhaseTurn{

    public EndTurn(GameRuleEnum rule){

        super(rule);
        allow(CommandEnum.END_TURN);

    }
}
