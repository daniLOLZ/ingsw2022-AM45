package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class StudentChoosing extends ActionPhaseTurn {

    public StudentChoosing() {

        super();
        allow(CommandEnum.SELECT_STUDENT);

    }

}
