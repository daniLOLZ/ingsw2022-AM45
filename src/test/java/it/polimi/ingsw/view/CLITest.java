package it.polimi.ingsw.view;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CLITest {

    @Test
    public void showTest(){
        CLI cli = new CLI();
        List<Integer> islands = new ArrayList<>();
        islands.add(2);
        islands.add(3);
        islands.add(4);
        islands.add(5);
        islands.add(6);
        islands.add(7);
        islands.add(8);

        List<StudentEnum> studOnIsland = new ArrayList<>();
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.RED);
        IslandGroupBean bean1 = new IslandGroupBean(1,islands,studOnIsland,true, TeamEnum.NOTEAM);
        IslandGroupBean bean2 = new IslandGroupBean(2,islands,studOnIsland,false, TeamEnum.BLACK);
        IslandGroupBean bean3 = new IslandGroupBean(3,islands,studOnIsland,false, TeamEnum.NOTEAM);
        IslandGroupBean bean4 = new IslandGroupBean(4,islands,studOnIsland,false, TeamEnum.NOTEAM);
        cli.addBean(bean1);
        cli.addBean(bean2);
        cli.addBean(bean3);
        cli.addBean(bean4);
        cli.show();
    }
}
