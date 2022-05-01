package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class LookingForLobby extends ConnectionState{

    public LookingForLobby(){

        super();
        allow(CommandEnum.PLAY_GAME);
    }
}
