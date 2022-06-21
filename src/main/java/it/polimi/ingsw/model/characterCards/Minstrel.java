package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.FullEntranceException;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.player.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Minstrel extends CharacterCard {
    private static final String name ="MINSTREL";
    private static final String description ="You may exchange up to 2 Students\n" +
                                         "\t|\tbetween your Entrance and your\n" +
                                         "\t|\tDining Room";

    private final int maxTradeableStudents = 2;
    private int trades;

    public Minstrel(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(1,10,parameters, advancedParameters,name,description);
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

        Collections.sort(indexEntrance);
        Collections.reverse(indexEntrance);

        if(indexHall.size() == 1){
            while(indexHall.size() != indexEntrance.size())
                indexHall.add(StudentEnum.getColorById(indexHall.get(0).index));
        }
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
        if(trades >= maxTradeableStudents)
            return;

        Player player = parameters.getCurrentPlayer();

        Board board = player.getBoard();
        board.setSelectedEntranceStudentPos(indexStudentEntrance);
        board.moveFromEntranceToHall();
        try {
            board.moveFromHallToEntrance(colorHall);
            trades++;
        } catch (FullEntranceException e) {
            e.printStackTrace();
            parameters.setErrorState("ENTRANCE FULL, OPERATION FAILED");
        }

    }

}
