package it.polimi.ingsw;

import it.polimi.ingsw.model.StudentEnum;
import org.junit.jupiter.api.Test;


public class StudentEnumTest {

    /**
     * simple test for correctly behavior of StudentEnum
     */
    @Test
    public void print(){
        int y = 0;
        for(StudentEnum x: StudentEnum.values()){
            y = y + x.index;
        }
        assert (y == 15);
    }
}
