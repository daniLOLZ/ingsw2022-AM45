package it.polimi.ingsw.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientMainTest {

    @Test
    public void parametersCongruence(){
        assertEquals(ClientMain.allowedParameters.size(), ClientMain.parameterRequiresInput.size());
    }

}
