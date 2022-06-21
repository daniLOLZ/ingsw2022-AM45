package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EriantysTest {


    /**
     * Checks whether the parameters stored in the Eriantys class are of the same size as the list
     * that stores if the input for each parameter is required
     */
    @Test
    public void parametersCongruence(){
        assertEquals(Eriantys.allowedParameters.size(), Eriantys.parameterRequiresInput.size());
    }


}
