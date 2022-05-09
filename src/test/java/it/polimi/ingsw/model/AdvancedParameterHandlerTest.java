package it.polimi.ingsw.model;

import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdvancedParameterHandlerTest {

    /**
     * Tests whether the removal of coins from the game board happens correctly
     * There should never be a negative amount of coins on the table
     */
    @Test
    void removeCoinsTest(){
        AdvancedParameterHandler param = new AdvancedParameterHandler(20);
        param.removeCoins(5);
        assertEquals(param.getNumCoins(), 15);
        int numberRemoved;
        numberRemoved = param.removeCoins(16);
        assertEquals(numberRemoved, 15);
        assertEquals(param.getNumCoins(), 0);

    }
}
