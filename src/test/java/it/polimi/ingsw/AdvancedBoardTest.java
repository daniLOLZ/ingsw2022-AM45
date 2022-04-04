package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedBoardTest {

    AdvancedBoard board = null;
    StudentEnum student = null;

    @BeforeEach
    public void initalise(){
        board = new AdvancedBoard(8, TeamEnum.WHITE);
        student = StudentEnum.GREEN;
    }

    /**
     * Tests if the board is correctly constructed
     */
    @Test
    public void advancedBoardTest(){
        for (int count = 0; count < 5; count++) {

            assertEquals(board.getCoinsOnBoard().get(count).pop(),3,"Stack not correctly initialised");
            assertEquals(board.getCoinsOnBoard().get(count).pop(),6,"Stack not correctly initialised");
            assertEquals(board.getCoinsOnBoard().get(count).pop(),9,"Stack not correctly initialised");
        }
    }

    /**
     * Tests whether method checkCoinSeat returns true when the player is expected to receive a coin
     */
    @Test
    public void checkCoinSeatTest(){
        int seat = 1;
        ArrayList<Integer> alreadyChecked = new ArrayList<>();
        boolean popped;
        for (StudentEnum studentEnum : StudentEnum.values()){
            if(studentEnum == StudentEnum.NOSTUDENT) continue;

            while (seat <= 10) {

                popped = board.checkCoinSeat(studentEnum, seat);
                if (!alreadyChecked.contains(seat) && (seat == 3 || seat == 6 || seat == 9)) assertTrue(popped,"False negative on " + seat);
                else assertFalse(popped,"False positive");

                if (!popped) seat++;
                else alreadyChecked.add(seat);
            }
        }
    }
}
