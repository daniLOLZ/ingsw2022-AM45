package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.player.PlayerEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class BeanTest {

    @Test
    public void CloudBeanTest(){
        List<StudentEnum> list = new ArrayList<>();
        list.add(StudentEnum.RED);
        CloudBean cloudBean = new CloudBean(0,list);


        assertEquals(0, cloudBean.getIdCloud());
        assertEquals(StudentEnum.RED,cloudBean.getStudents().get(0));

        List<StudentEnum> list2 = new ArrayList<>();
        list2.add(StudentEnum.YELLOW);
        cloudBean.setStudents(list2);
        cloudBean.setIdCloud(1);

        assertEquals(1, cloudBean.getIdCloud());
        assertEquals(StudentEnum.YELLOW,cloudBean.getStudents().get(0));

        String string = cloudBean.drawCLI();
        assertNotNull(string);

    }

    @Test
    public void IslandGroupBean(){
        List<Integer> islands = new ArrayList<>();
        islands.add(1);
        List<StudentEnum> list = new ArrayList<>();
        list.add(StudentEnum.RED);
        IslandGroupBean bean = new IslandGroupBean(0,islands,list,false,TeamEnum.NOTEAM);

        assertEquals(0, bean.getIdIslandGroup());
        assertEquals(StudentEnum.RED, bean.getStudentsOnIsland().get(0));
        assertEquals(1, bean.getIdIslands().get(0));
        assertEquals(TeamEnum.NOTEAM,bean.getTowersColor());
        assertFalse(bean.isPresentMN());


        List<Integer> islands2 = new ArrayList<>();
        islands2.add(12);
        List<StudentEnum> list2 = new ArrayList<>();
        list2.add(StudentEnum.BLUE);

        bean.setIdIslandGroup(10);
        bean.setIdIslands(islands2);
        bean.setPresentMN(true);
        bean.setTowersColor(TeamEnum.WHITE);
        bean.setStudentsOnIsland(list2);

        assertEquals(10, bean.getIdIslandGroup());
        assertEquals(StudentEnum.BLUE, bean.getStudentsOnIsland().get(0));
        assertEquals(12, bean.getIdIslands().get(0));
        assertEquals(TeamEnum.WHITE,bean.getTowersColor());
        assertTrue(bean.isPresentMN());

        String string = bean.drawCLI();
        assertNotNull(string);
    }

    @Test
    public void advancedIslandGroupTest(){
        List<Integer> islands = new ArrayList<>();
        islands.add(1);
        List<StudentEnum> list = new ArrayList<>();
        list.add(StudentEnum.RED);
        AdvancedIslandGroupBean bean = new AdvancedIslandGroupBean(0,
                islands,list,false,TeamEnum.NOTEAM,3);

        assertEquals(3, bean.getNumBlockTiles());

        bean.setNumBlockTiles(2);

        assertEquals(2, bean.getNumBlockTiles());

        String string = bean.drawCLI();
        assertNotNull(string);

    }

    @Test
    public void CharacterCardBeanTest(){
        List<StudentEnum> list2 = new ArrayList<>();
        list2.add(StudentEnum.BLUE);
        CharacterCardBean bean = new CharacterCardBean(1,"Priest","NO",list2,3);

        assertEquals(3,bean.getCost());
        assertEquals(1,bean.getId());
        assertEquals("Priest",bean.getName());
        assertEquals("NO",bean.getDescription());

        String string = bean.drawCLI();
        assertNotNull(string);
    }

    @Test
    public void GameBoardBeanTest(){
        List<Integer> islands2 = new ArrayList<>();
        islands2.add(12);
        List<Integer> assistant = new ArrayList<>();
        assistant.add(1);
        List<Integer> players= new ArrayList<>();
        players.add(10);

        GameBoardBean bean = new GameBoardBean(islands2,assistant,players,12,2,"ACTION");

        assertEquals(12, bean.getIdIslandGroups().get(0));
        assertEquals(1, bean.getIdAssistantsPlayed().get(0));
        assertEquals(10, bean.getIdPlayers().get(0));
        assertEquals(12, bean.getCurrentPlayerId());
        assertEquals(2, bean.getTurn());
        assertEquals("ACTION", bean.getPhase());


        List<Integer> players2= new ArrayList<>();
        players2.add(10);
        List<Integer> islands = new ArrayList<>();
        islands.add(100);
        List<Integer> assistant2 = new ArrayList<>();
        assistant2.add(22);
        bean.setCurrentPlayerId(1);
        bean.setIdAssistantsPlayed(assistant2);
        bean.setIdIslandGroups(islands);
        bean.setIdPlayers(players2);
        bean.setTurn(22);
        bean.setPhase("PLANNING");

        assertEquals(100, bean.getIdIslandGroups().get(0));
        assertEquals(22, bean.getIdAssistantsPlayed().get(0));
        assertEquals(10, bean.getIdPlayers().get(0));
        assertEquals(1, bean.getCurrentPlayerId());
        assertEquals(22, bean.getTurn());
        assertEquals("PLANNING", bean.getPhase());

        String string = bean.drawCLI();
        assertNotNull(string);
    }

    @Test
    public void AdvancedGameBoardTest(){
        List<Integer> islands2 = new ArrayList<>();
        islands2.add(12);
        List<Integer> assistant = new ArrayList<>();
        assistant.add(1);
        List<Integer> players= new ArrayList<>();
        players.add(10);
        List<Integer> cards= new ArrayList<>();
        cards.add(111);

        AdvancedGameBoardBean bean = new AdvancedGameBoardBean(islands2,
                assistant,players,12,2,"ACTION",33,cards);

        assertEquals(12, bean.getIdIslandGroups().get(0));
        assertEquals(1, bean.getIdAssistantsPlayed().get(0));
        assertEquals(10, bean.getIdPlayers().get(0));
        assertEquals(12, bean.getCurrentPlayerId());
        assertEquals(2, bean.getTurn());
        assertEquals("ACTION", bean.getPhase());
        assertEquals(33,bean.getNumGameCoins());
        assertEquals(111,bean.getIdCharacterCards().get(0));


        List<Integer> players2= new ArrayList<>();
        players2.add(10);
        List<Integer> islands = new ArrayList<>();
        islands.add(100);
        List<Integer> assistant2 = new ArrayList<>();
        assistant2.add(22);
        List<Integer> cards2= new ArrayList<>();
        cards2.add(101);
        bean.setCurrentPlayerId(1);
        bean.setIdAssistantsPlayed(assistant2);
        bean.setIdIslandGroups(islands);
        bean.setIdPlayers(players2);
        bean.setTurn(22);
        bean.setPhase("PLANNING");
        bean.setNumGameCoins(1);
        bean.setIdCharacterCards(cards2);

        assertEquals(100, bean.getIdIslandGroups().get(0));
        assertEquals(22, bean.getIdAssistantsPlayed().get(0));
        assertEquals(10, bean.getIdPlayers().get(0));
        assertEquals(1, bean.getCurrentPlayerId());
        assertEquals(22, bean.getTurn());
        assertEquals("PLANNING", bean.getPhase());
        assertEquals(1,bean.getNumGameCoins());
        assertEquals(101,bean.getIdCharacterCards().get(0));

        String string = bean.drawCLI();
        assertNotNull(string);

    }

    @Test
    public void playerBeanTest(){
        List<StudentEnum> list2 = new ArrayList<>();
        list2.add(StudentEnum.BLUE);
        List<StudentEnum> prof = new ArrayList<>();
        prof.add(StudentEnum.BLUE);
        List<Integer> table = new ArrayList<>();
        table.add(12);
        List<Integer> assistant = new ArrayList<>();
        assistant.add(42);
        PlayerBean bean = new PlayerBean("FRANCO", PlayerEnum.PLAYER1,false,
                TeamEnum.WHITE,6,list2,table,prof,assistant);

        assertEquals(42,bean.getIdAssistants().get(0));
        assertEquals(6,bean.getNumTowers());
        assertEquals(12,bean.getStudentsPerTable().get(0));
        assertEquals(PlayerEnum.PLAYER1,bean.getPlayerId());
        assertEquals("FRANCO",bean.getNickname());
        assertEquals(StudentEnum.BLUE,bean.getProfessors().get(0));
        assertEquals(StudentEnum.BLUE,bean.getStudentsAtEntrance().get(0));
        assertEquals(TeamEnum.WHITE,bean.getTowerColor());
        assertFalse(bean.isLeader());

        List<StudentEnum> list = new ArrayList<>();
        list.add(StudentEnum.BLUE);
        List<StudentEnum> prof2 = new ArrayList<>();
        prof2.add(StudentEnum.BLUE);
        List<Integer> table2 = new ArrayList<>();
        table2.add(12);
        table2.add(12);
        table2.add(12);
        table2.add(12);
        table2.add(12);
        List<Integer> assistant2 = new ArrayList<>();
        assistant2.add(42);

        bean.setLeader(true);
        bean.setNickname("MARIO");
        bean.setPlayerId(PlayerEnum.PLAYER2);
        bean.setNumTowers(7);
        bean.setTowerColor(TeamEnum.BLACK);
        bean.setStudentsPerTable(table2);
        bean.setStudentsAtEntrance(list);
        bean.setIdAssistants(assistant2);

        assertEquals(42,bean.getIdAssistants().get(0));
        assertEquals(7,bean.getNumTowers());
        assertEquals(12,bean.getStudentsPerTable().get(0));
        assertEquals(PlayerEnum.PLAYER2,bean.getPlayerId());
        assertEquals("MARIO",bean.getNickname());
        assertEquals(StudentEnum.BLUE,bean.getProfessors().get(0));
        assertEquals(StudentEnum.BLUE,bean.getStudentsAtEntrance().get(0));
        assertEquals(TeamEnum.BLACK,bean.getTowerColor());
        assertTrue(bean.isLeader());

        String string = bean.drawCLI();
        assertNotNull(string);
    }

    @Test
    public void advancedPlayerBeanTest(){
        List<StudentEnum> list2 = new ArrayList<>();
        list2.add(StudentEnum.BLUE);
        List<StudentEnum> prof = new ArrayList<>();
        prof.add(StudentEnum.BLUE);
        List<Integer> table = new ArrayList<>();
        table.add(12);
        table.add(12);
        table.add(12);
        table.add(12);
        table.add(12);
        List<Integer> assistant = new ArrayList<>();
        assistant.add(42);
        AdvancedPlayerBean bean = new AdvancedPlayerBean("FRANCO", PlayerEnum.PLAYER1,false,
                TeamEnum.WHITE,6,list2,table,prof,assistant,20);

        assertEquals(20, bean.getNumCoins());

        bean.setNumCoins(1);

        assertEquals(1, bean.getNumCoins());

        String string = bean.drawCLI();
        assertNotNull(string);

    }

    @Test
    public void errorBeanTest(){
        ErrorBean bean = new ErrorBean("ERROR");

        assertEquals("ERROR", bean.getError());

        bean.setError("NO ERROR");

        assertEquals("NO ERROR", bean.getError());

        String string = bean.drawCLI();
        assertNotNull(string);
    }
}
