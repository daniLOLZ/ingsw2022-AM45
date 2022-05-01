package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class PlanningPhaseTurn extends ConnectionState{

    public PlanningPhaseTurn(){

        super();
        allow(CommandEnum.CHOOSE_ASSISTANT);
    }
}
