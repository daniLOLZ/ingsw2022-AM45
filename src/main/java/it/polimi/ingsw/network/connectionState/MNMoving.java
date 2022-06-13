package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class MNMoving extends ActionPhaseTurn {

    public MNMoving() {

        super();
        allow(CommandEnum.MOVE_MN);

    }
}
