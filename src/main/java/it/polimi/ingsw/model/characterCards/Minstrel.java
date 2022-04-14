package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

public class Minstrel extends CharacterCard {

    private final int maxTradeableStudents = 2;
    private int trades;

    public Minstrel(AdvancedParameterHandler advancedParameters){
        super(1,10,advancedParameters);
    }


    /**
     * You can exchange  maxTradeableStudents students from Hall to Entrance or vice-versa
     * @param game
     */
    //@Override
    //TODO I'm not touching this but it needs to be changed
    public void activateEffect(AdvancedGame game) {
        super.activateEffect();
        trades = 0;
        game.setTradeableStudent(maxTradeableStudents);
    }


    /**
     * Exchange one student at Entrance with one at Hall.
     * Increment trades
     * If trades are grater than maxTradeableStudents return
     * @param player
     * @param indexStudentEntrance >= 0
     * @param colorHall != NOSTUDENT
     */
    public void tradeStudents(Player player, int indexStudentEntrance, StudentEnum colorHall){
        if(trades > maxTradeableStudents)
            return;

        Board board = player.getBoard();
        board.setSelectedEntranceStudentPos(indexStudentEntrance);
        board.moveFromEntranceToHall();
        try {
            board.moveFromHallToEntrance(colorHall);
        } catch (FullEntranceException e) {
            e.printStackTrace();
            return;
        }
        trades++;
    }

}
