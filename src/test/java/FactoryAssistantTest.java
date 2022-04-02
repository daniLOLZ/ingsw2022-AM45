import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.FactoryAssistant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FactoryAssistantTest {

    @Test
    public void getAssistant(){
        Assistant assistant = FactoryAssistant.getAssistant(1);
        assertEquals(1,assistant.motherNatureSteps, "Wrong steps");
        assertEquals(2,assistant.turnOrder, "Wrong turnOrder");
    }
}
