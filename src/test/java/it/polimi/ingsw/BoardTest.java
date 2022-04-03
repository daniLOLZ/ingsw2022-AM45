package it.polimi.ingsw;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {


    /**
     * Tests if updateTowers returns the correct boolean value
     */
    @Test
    public void updateTowersTest(){
        Board board = new Board(8,TeamEnum.WHITE);
        assertFalse(board.updateTowers(-2),"Returned true when not out of towers");
        assertTrue(board.updateTowers(-6),"Returned false when out of towers");
    }


    /**
     * Tests if the selected student is correctly moved from the Entrance to the right table
     */
    @Test
    public void moveFromEntranceToHallTest(){

        Board board = new Board(8,TeamEnum.WHITE);
        StudentEnum student = StudentEnum.GREEN;

        board.addToEntrance(student); //this method shouldn't be public
        board.setSelectedEntranceStudentPos(0);
        assertEquals(board.moveFromEntranceToHall(),student,"Not corresponding Student types");
        assertEquals(board.entranceSize(),0,"Student is still at the Entrance");
        assertEquals(board.getStudentsAtTable(student),1,"Student was not moved to the correct table");

        for (StudentEnum table: StudentEnum.values()){

            if (table == student || table == StudentEnum.NOSTUDENT) continue;

            assertEquals(board.getStudentsAtTable(table),0,"Student was probably moved on the wrong table");
        }
    }
}
