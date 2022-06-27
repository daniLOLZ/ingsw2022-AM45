package it.polimi.ingsw;

import it.polimi.ingsw.network.client.PingHandler;
import it.polimi.ingsw.network.server.PongHandler;
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

    /**
     * Checks whether the duration of the ping is congruent to the one specified in the protocol
     * Ignore this test while debugging.
     */
    @Test
    public void correctPingDuration(){
        assertEquals(5, PingHandler.PING_TIMEOUT_SECONDS);
        assertEquals(2000, PingHandler.waitBetweenPingsMilliseconds);
        assertEquals(5, PongHandler.PONG_TIMEOUT_SECONDS);
    }

}
