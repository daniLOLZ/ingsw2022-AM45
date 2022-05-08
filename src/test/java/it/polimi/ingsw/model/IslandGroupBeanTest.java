package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.IslandGroupBean;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IslandGroupBeanTest {

    @Test
    public void testDrawCLI(){
        List<Integer> islands = new ArrayList<>();
        islands.add(2);
        islands.add(3);

        List<StudentEnum> studOnIsland = new ArrayList<>();
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.RED);
        IslandGroupBean bean = new IslandGroupBean(1,islands,studOnIsland,true,TeamEnum.NOTEAM);
        System.out.println(bean.drawCLI());

    }
}
