package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class InLobby extends ConnectionState{

    public InLobby(){

        super();
        allow(CommandEnum.READY_TO_START);
        allow(CommandEnum.NOT_READY);
        allow(CommandEnum.LEAVE_LOBBY);
        allow(CommandEnum.START_GAME);
    }
}
