package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StudentEnumTest {



    /**
     * Tests the id to student color mapping
     */
    @Test
    void idToStudentTest(){
        assertEquals(StudentEnum.getColorById(1), StudentEnum.RED);
    }

}
