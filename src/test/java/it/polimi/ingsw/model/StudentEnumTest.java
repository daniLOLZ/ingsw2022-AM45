package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StudentEnumTest {

    /**
     * simple test for correctly behavior of StudentEnum
     */
    @Test
    public void print(){
        int y = 0;
        for(StudentEnum x: StudentEnum.values()){
            y = y + x.index;
        }
        assert (y == 15);
    }

    /**
     * Tests the id to student color mapping
     */
    @Test
    void idToStudentTest(){
        assertEquals(StudentEnum.getColorById(1), StudentEnum.RED);
    }

}
