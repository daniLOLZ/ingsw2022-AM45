package it.polimi.ingsw;

import it.polimi.ingsw.model.AdvancedSack;
import it.polimi.ingsw.model.StudentEnum;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;

public class AdvancedSackTest {

    /**
     * tests if AddStudent add correctly one student,
     * with correct increment of size and
     * correct increment of added student's color occurrences
     */
    @Test
    public void addStudentTest(){
        AdvancedSack sack = new AdvancedSack(10);
        int sizeBefore = sack.size();
        int sizeThen;
        ArrayList<StudentEnum> list = new ArrayList<>();
        list.add(StudentEnum.GREEN);
        sack.addStudents(list);
        sizeThen = sack.size();
        assert (sizeBefore == (sizeThen - 1));  //correct increment of size


        int occurrence[] = new int[5];
        int x ;
        while(!sack.isEmpty()){
            x= sack.drawNStudents(1).get(0).index;
            occurrence[x]++;
        }

        //correct increment of occurrences
        assert (occurrence[StudentEnum.GREEN.index] == occurrence[StudentEnum.BLUE.index] +1);
    }
}
