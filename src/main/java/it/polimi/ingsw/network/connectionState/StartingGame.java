package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class StartingGame extends ConnectionState{

    public StartingGame(){

        super();
        allow(CommandEnum.SELECT_WIZARD);
        allow(CommandEnum.SELECT_TOWER_COLOR);
        allow(CommandEnum.GET_GAME_INITIALIZATION_STATUS);
    }
}
