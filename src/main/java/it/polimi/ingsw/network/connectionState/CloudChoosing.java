package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.CommandEnum;

public class CloudChoosing extends ActionPhaseTurn{

    public CloudChoosing(GameRuleEnum rule){

        super(rule);
        allow(CommandEnum.CHOOSE_CLOUD);

    }

}
