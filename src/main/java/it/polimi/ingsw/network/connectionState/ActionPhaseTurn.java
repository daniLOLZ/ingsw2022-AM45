package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class ActionPhaseTurn extends ConnectionState{

    public ActionPhaseTurn(){

        super();
        allow(CommandEnum.SELECT_STUDENT);
        allow(CommandEnum.PUT_IN_HALL);
        allow(CommandEnum.PUT_IN_ISLAND);
        allow(CommandEnum.DESELECT_STUDENT);
        allow(CommandEnum.MOVE_MN_TO_ISLAND);
        allow(CommandEnum.CHOOSE_CLOUD);
        allow(CommandEnum.SELECT_CHARACTER);
    }
}
