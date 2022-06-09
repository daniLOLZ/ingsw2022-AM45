package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

public class EndTurn extends ActionPhaseTurn{

    public EndTurn(){

        super();
        allow(CommandEnum.END_TURN);

    }
}
