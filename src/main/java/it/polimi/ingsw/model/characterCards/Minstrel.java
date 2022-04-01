package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudentEnum;

public class Minstrel extends CharacterCard {

    private final int maxTradeableStudents = 2;
    private int trades;

    public Minstrel(){
        super(1,10);
    }

    @Override
    public void activateEffect(AdvancedGame game) {
        super.activateEffect(game);
        trades = 0;
        game.setTradeableStudent(maxTradeableStudents);
    }

    //TODO waiting Board implementation
    /*
    public void tradeStudents(Player player, StudentEnum colorEntrance, StudentEnum colorHall){
        if(trades > 2)
            return;

        Board board = player.getBoard();
        board.addToHall();
        trades++;
    }
    */
}
