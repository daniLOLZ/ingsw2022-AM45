package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.game.SimpleGame;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.FactoryPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CLITest {

    @Test
    public void showTestAdvanced(){
        CLI cli = new CLI();
        List<Integer> islands = new ArrayList<>();
        islands.add(2);
        islands.add(3);
        islands.add(4);
        islands.add(5);
        islands.add(6);
        islands.add(7);
        islands.add(8);

        ParameterHandler parameterHandler = new ParameterHandler(2);
        parameterHandler.addProfessor(PlayerEnum.PLAYER1, StudentEnum.RED);

        List<StudentEnum> studOnIsland = new ArrayList<>();
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.RED);
        AdvancedIslandGroupBean bean1 = new AdvancedIslandGroupBean(1,islands,studOnIsland,true, TeamEnum.NOTEAM, 0);
        AdvancedIslandGroupBean bean2 = new AdvancedIslandGroupBean(2,islands,studOnIsland,false, TeamEnum.BLACK,1);
        AdvancedIslandGroupBean bean3 = new AdvancedIslandGroupBean(3,islands,studOnIsland,false, TeamEnum.NOTEAM,0);
        AdvancedIslandGroupBean bean4 = new AdvancedIslandGroupBean(4,islands,studOnIsland,false, TeamEnum.NOTEAM,0);

        AdvancedPlayer player = new AdvancedPlayer(PlayerEnum.PLAYER1,"Franco", TeamEnum.WHITE,
                true,parameterHandler);
        player.addEntrance(StudentEnum.RED);
        player.selectStudentAtEntrance(0);
        player.moveFromEntranceToHall();
        player.addEntrance(StudentEnum.YELLOW);
        GameElementBean beanP = player.toBean();

        AdvancedGame game = null;
        try {
            game = new AdvancedGame(2,20,3);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
        for(Player playerX: game.getPlayers()){
            playerX.playAssistant(0);
        }
        game.startPhase(PlayerEnum.PLAYER1.index);
        GameElementBean beanG = game.toBean();
        Cloud cloud = new Cloud(1,3);
        List<StudentEnum> listStudent = new ArrayList<StudentEnum>();
        listStudent.add(StudentEnum.RED);
        listStudent.add(StudentEnum.BLUE);
        cloud.fill(listStudent);
        GameElementBean beanC = cloud.toBean();
        CharacterCard card;
        for(int i=0;i< 3;i++ ){
            card = game.getCharacterCard(i);
            cli.addBean(card.toBean());
        }


        cli.addBean(bean1);
        cli.addBean(bean2);
        cli.addBean(bean3);
        cli.addBean(bean4);
        cli.addBean(beanP);
        cli.addBean(beanG);
        cli.addBean(beanC);
        cli.addBean(beanC);
        cli.show();
    }

    @Test
    public void showTestASimple(){
        CLI cli = new CLI();
        List<Integer> islands = new ArrayList<>();
        islands.add(2);
        islands.add(3);
        islands.add(4);
        islands.add(5);
        islands.add(6);
        islands.add(7);
        islands.add(8);

        ParameterHandler parameterHandler = new ParameterHandler(2);
        parameterHandler.addProfessor(PlayerEnum.PLAYER1, StudentEnum.RED);

        List<StudentEnum> studOnIsland = new ArrayList<>();
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.BLUE);
        studOnIsland.add(StudentEnum.RED);
        IslandGroupBean bean1 = new IslandGroupBean(1,islands,studOnIsland,true, TeamEnum.NOTEAM);
        IslandGroupBean bean2 = new IslandGroupBean(2,islands,studOnIsland,false, TeamEnum.BLACK);
        IslandGroupBean bean3 = new IslandGroupBean(3,islands,studOnIsland,false, TeamEnum.NOTEAM);
        IslandGroupBean bean4 = new IslandGroupBean(4,islands,studOnIsland,false, TeamEnum.NOTEAM);

        Player player = FactoryPlayer.getPlayer("Franco", PlayerEnum.PLAYER1, TeamEnum.WHITE, FactoryWizard.getWizard(0),
                true, parameterHandler, false);
        player.addEntrance(StudentEnum.RED);
        player.selectStudentAtEntrance(0);
        player.moveFromEntranceToHall();
        player.addEntrance(StudentEnum.YELLOW);
        GameElementBean beanP = player.toBean();

        SimpleGame game = null;
        try {
            game = new SimpleGame(2);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
        for(Player playerX: game.getPlayers()){
            playerX.playAssistant(0);
        }
        game.startPhase(PlayerEnum.PLAYER1.index);
        GameElementBean beanG = game.toBean();


        cli.addBean(bean1);
        cli.addBean(bean2);
        cli.addBean(bean3);
        cli.addBean(bean4);
        cli.addBean(beanP);
        cli.addBean(beanG);
        cli.show();

    }

    /*
    @Test
    public void askCommand(){
        CLI cli = new CLI();
        String x = cli.askCommand();
        System.out.println(x);
    }

     */
}
