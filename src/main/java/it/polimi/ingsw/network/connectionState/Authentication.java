package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class Authentication extends ConnectionState{

    public Authentication(){

        super();
        allow(CommandEnum.CONNECTION_REQUEST);
    }
}
