package it.polimi.ingsw.network;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandEnumTest {

    /**
     * Test if the field returning method gives the correct fields,
     * stripping the unnecessary ones
     */
    @Test
    public void returnNeededFields(){
        List<NetworkFieldEnum> list = new ArrayList<>();
        assertEquals(CommandEnum.getFieldsNeeded(CommandEnum.QUIT), list);
        list.add(NetworkFieldEnum.CHOSEN_ENTRANCE_STUDENT);
        assertEquals(CommandEnum.getFieldsNeeded(CommandEnum.SELECT_STUDENT), list);
    }
}
