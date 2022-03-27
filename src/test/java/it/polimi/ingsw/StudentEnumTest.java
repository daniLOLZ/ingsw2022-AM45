package it.polimi.ingsw;

import it.polimi.ingsw.model.StudentEnum;
import org.junit.Test;

public class StudentEnumTest {

    @Test
    public void print(){
        int y = 0;
        for(StudentEnum x: StudentEnum.values()){
            y = y + x.index;
        }

        assert (y == 15);
    }
}
