package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorStateTest {

    /**
     * Test the correct creation of ErrorState
     */
    @Test
    public void errorStateTest(){
        ErrorState error = new ErrorState("ERROR");
        assertEquals("ERROR",error.getErrorMessage());
    }
}
