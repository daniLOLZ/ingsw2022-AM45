package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public abstract class ActionPhaseTurn extends ConnectionState{

    public ActionPhaseTurn(){

        super();
        allow(CommandEnum.SELECT_CHARACTER);

    }
}
