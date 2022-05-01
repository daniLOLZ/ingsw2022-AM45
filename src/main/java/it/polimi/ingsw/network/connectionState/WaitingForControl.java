package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class WaitingForControl extends ConnectionState{

    public WaitingForControl(){

        super();
        allow(CommandEnum.ASK_FOR_CONTROL);
    }
}
