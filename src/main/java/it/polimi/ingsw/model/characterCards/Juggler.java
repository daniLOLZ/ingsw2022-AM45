package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudentEnum;

public class Juggler extends InitialEffect{

    private final int startingStudents = 6;
    private final int maxTradeableStudents = 3;
    private int numTrades;

    public Juggler(){
        super(1,7);
    }

    @Override
    public void initialise(AdvancedGame game) {
        addAllStudents(game.getSack().drawNStudents(startingStudents));
    }

    /**
     * You can take maximum maxTradeableStudents from this card and exchange them with
     * currentPlayer students at Entrance
     * @param game
     */
    @Override
    public void activateEffect(AdvancedGame game) {
        super.activateEffect(game);
        numTrades = 0;
        game.setTradeableStudent(maxTradeableStudents);

    }

    /**
     * Take one student from Juggler and one student from Player's Entrance and exchange them
     * if the number of trades in this turn is grater than maxTradeableStudents do not
     * exchange them
     * @param studentToJuggler
     * @param index
     * @param player
     */
    public void tradeStudents(StudentEnum studentToJuggler, int index, Player player){

        if(numTrades > maxTradeableStudents)
            return;

        player.getBoard().addToEntrance(removeStudent(index));
        addStudent(studentToJuggler);
        numTrades++;
    }

}
