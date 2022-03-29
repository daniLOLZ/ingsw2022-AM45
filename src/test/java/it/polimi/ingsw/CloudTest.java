package it.polimi.ingsw;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.StudentEnum;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

public class CloudTest {

    @Test
    public void fillTest(){
        Cloud cloud = new Cloud(3,3);
        ArrayList<StudentEnum> list = new ArrayList<>();
        list.add(StudentEnum.BLUE);
        list.add(StudentEnum.RED);
        list.add(StudentEnum.GREEN);
        cloud.fill(list);
        assert (cloud.remove(0) == StudentEnum.BLUE);
        assert (cloud.remove(0) == StudentEnum.RED);
        assert (cloud.remove(0) == StudentEnum.GREEN);
    }

    @Test
    public void emptyTest(){
        Cloud cloud = new Cloud(3,3);
        ArrayList<StudentEnum> list = new ArrayList<>();
        list.add(StudentEnum.BLUE);
        list.add(StudentEnum.RED);
        list.add(StudentEnum.GREEN);
        cloud.fill(list);
        cloud.empty();
        assert (cloud.remove(0) == null);
        assert(cloud.isEmpty() == true);
    }
}
