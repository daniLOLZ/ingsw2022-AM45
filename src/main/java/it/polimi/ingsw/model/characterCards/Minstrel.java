package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.Iterator;
import java.util.List;

public class Minstrel extends CharacterCard {

    private final int maxTradeableStudents = 2;
    private int trades;

    public Minstrel(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(1,10,parameters, advancedParameters);
        requirements = new Requirements(0,maxTradeableStudents,maxTradeableStudents,0);
    }


    /**
     * You can exchange  maxTradeableStudents students from Hall to Entrance or vice-versa
     *
     */
    @Override
    public void activateEffect() {
        super.activateEffect();
        trades = 0;
        List<Integer> indexEntrance;
        List<StudentEnum> indexHall;

        //CHECK IF USER CHOOSE STUDENTS AT ENTRANCE
        if(parameters.getSelectedEntranceStudents().isEmpty()){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentAtEntrance");
            return;
        }

        //CHECK IF USER CHOOSE STUDENTS AT HALL
        if(parameters.getSelectedStudentTypes().isEmpty()){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentType");
            return;
        }

        //GET AND CHECK IF LISTS OF CHOSEN STUDENTS ARE RIGHT
        indexEntrance = parameters.getSelectedEntranceStudents().get();
        indexHall = parameters.getSelectedStudentTypes().get();
        Iterator<Integer> iteratorEntrance = indexEntrance.iterator();
        Iterator<StudentEnum> iteratorHall = indexHall.iterator();

        if(indexEntrance.size() != indexHall.size() || indexEntrance.size() > maxTradeableStudents){
            parameters.setErrorState("WRONG NUMBER OF CHOSEN STUDENTS ");
            return;
        }

        //MAKE THE EXCHANGE
        while(iteratorEntrance.hasNext() && iteratorHall.hasNext() && trades < maxTradeableStudents){
            tradeStudents(iteratorEntrance.next(), iteratorHall.next());
        }


    }


    /**
     * Exchange one student at Entrance with one at Hall.
     * Increment trades
     * If trades are grater than maxTradeableStudents return
     * @param indexStudentEntrance >= 0
     * @param colorHall != NOSTUDENT
     */
    public void tradeStudents( int indexStudentEntrance, StudentEnum colorHall){
        if(trades > maxTradeableStudents)
            return;

        Player player = parameters.getCurrentPlayer();

        Board board = player.getBoard();
        board.setSelectedEntranceStudentPos(indexStudentEntrance);
        board.moveFromEntranceToHall();
        try {
            board.moveFromHallToEntrance(colorHall);
        } catch (FullEntranceException e) {
            e.printStackTrace();
            parameters.setErrorState("ENTRANCE FULL, OPERATION FAILED");
            return;
        }
        trades++;
    }

}
