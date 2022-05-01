package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class EndTurn extends ConnectionState{

    public EndTurn(){

        super();
        allow(CommandEnum.CHOOSE_CLOUD);
    }
}
