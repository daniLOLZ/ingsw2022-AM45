package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.CommandEnum;

public abstract class ActionPhaseTurn extends ConnectionState{

    public ActionPhaseTurn(GameRuleEnum rule){

        super();
        if(GameRuleEnum.isAdvanced(rule.id)){
            allow(CommandEnum.SELECT_CHARACTER);
        }

    }
}
