package it.polimi.ingsw;

import it.polimi.ingsw.model.Sack;
import it.polimi.ingsw.model.StudentEnum;
import org.junit.Test;

import java.util.ArrayList;

public class SackTest {

    @Test
    public void simpleTest(){
        Sack sack = new Sack(1);
        int studentA = sack.drawNStudents(1).get(0).index;
        int studentB = sack.drawNStudents(1).get(0).index;

        assert (!sack.isEmpty());
        assert (studentA != StudentEnum.NOSTUDENT.index);
        assert (studentB != StudentEnum.NOSTUDENT.index);
        assert (studentA != studentB);
    }

    @Test
    public void thereIsnoNOSTUDENTS(){
        Sack sack = new Sack(10);
        int x ;
        while(!sack.isEmpty()){
            x= sack.drawNStudents(1).get(0).index;
            assert (x != StudentEnum.NOSTUDENT.index);
        }
    }

    @Test
    public void equalNumberStudents(){
        int num = 10;
        Sack sack = new Sack(num);
        int occurrence[] = new int[5];
        int x ;
        while(!sack.isEmpty()){
            x= sack.drawNStudents(1).get(0).index;
            occurrence[x]++;
        }

        assert (occurrence[0] == num);
        assert (occurrence[1] == num);
        assert (occurrence[2] == num);
        assert (occurrence[3] == num);
        assert (occurrence[4] == num);
    }
}
