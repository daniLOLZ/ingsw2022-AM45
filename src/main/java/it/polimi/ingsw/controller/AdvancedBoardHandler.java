package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.StudentEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvancedBoardHandler extends BoardHandler{


    public AdvancedBoardHandler(Controller controller){
        super(controller);
    }

    /**
     * move a student from chosen position at currentPlayer's entrance
     * to hall and assign a coin to currentPlayer if he deserves it.
     * @param position > 0
     */
    @Override
    public void moveFromEntranceToHall(int position) {
        getCurrentPlayer();
        controller.advancedGame.moveFromEntranceToHall(currentPlayer);
    }
}
