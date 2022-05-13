package it.polimi.ingsw.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerMainTest {

    @Test
    public void parametersCongruence(){
        assertEquals(ServerMain.allowedParameters.size(), ServerMain.parameterRequiresInput.size());
    }
}
