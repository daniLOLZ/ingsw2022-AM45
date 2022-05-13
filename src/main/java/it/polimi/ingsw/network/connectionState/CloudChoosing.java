package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class CloudChoosing extends ActionPhaseTurn{

    public CloudChoosing(){

        super();
        allow(CommandEnum.CHOOSE_CLOUD);

    }

}
