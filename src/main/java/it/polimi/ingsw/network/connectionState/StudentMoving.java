package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class StudentMoving extends ActionPhaseTurn {

    public StudentMoving(){

        super();
        allow(CommandEnum.PUT_IN_HALL);
        allow(CommandEnum.PUT_IN_ISLAND);
        allow(CommandEnum.DESELECT_STUDENT);

    }
}
