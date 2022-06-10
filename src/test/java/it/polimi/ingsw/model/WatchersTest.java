package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.islands.UnmergeableException;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class WatchersTest {

    AdvancedGame game;
    int players = 3;
    int coins = 20;
    int CharacterCards = 3;
    VirtualView virtualView;

    @BeforeEach
    public void initialize(){
        final List<Integer> selectedWizards = new ArrayList<>();
        selectedWizards.add(0);
        selectedWizards.add(10);
        selectedWizards.add(20);
        final List<TeamEnum> teamColors = new ArrayList<>();
        teamColors.add(TeamEnum.WHITE);
        teamColors.add(TeamEnum.BLACK);
        teamColors.add(TeamEnum.GREY);
        final List<String> nicknames = new ArrayList<>();
        nicknames.add("Franco");
        nicknames.add("Mario");
        nicknames.add("Alice");
        virtualView = new VirtualView();


        try {
            game = new AdvancedGame(players,selectedWizards,teamColors,nicknames,
                    coins,CharacterCards, virtualView);
            game.initializeGame();
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void initialStateTest(){
        AdvancedGameBoardBean gameBean = virtualView.getAdvancedGameBean();
        List<AdvancedIslandGroupBean> islandsBean = virtualView.getAdvancedIslandBean();
        List<AdvancedPlayerBean> playersBean = virtualView.getAdvancedPlayerBean();
        List<CloudBean> cloudsBean = virtualView.getCloudBean();
        List<CharacterCardBean> cardBean = virtualView.getCharacterBean();

        assertNotNull(gameBean);
        assertNotNull(islandsBean);
        assertNotNull(playersBean);
        assertNotNull(cloudsBean);
        assertNotNull(cardBean);

        assertFalse(islandsBean.isEmpty());
        assertFalse(playersBean.isEmpty());
        assertFalse(cloudsBean.isEmpty());
        assertFalse(cardBean.isEmpty());

        assertEquals(1,gameBean.getTurn());
        assertEquals(PhaseEnum.PLANNING,gameBean.getPhase());
        assertEquals(3, cardBean.size());

        for(CharacterCardBean bean: cardBean){
            assertTrue(bean.getId() > 0);
            assertTrue(gameBean.getIdCharacterCards().contains(bean.getId()));
        }

        for(AdvancedIslandGroupBean bean: islandsBean){
            assertTrue(bean.getIdIslandGroup() >= 0);
            assertTrue(gameBean.getIdIslandGroups().contains(bean.getIdIslandGroup()));
        }

        for(AdvancedPlayerBean bean: playersBean){
            assertTrue(bean.getPlayerId().index >= 0);
            assertTrue(gameBean.getIdPlayers().contains(bean.getPlayerId().index));
        }

    }

    @Test
    public void playAssistants(){
        AdvancedGameBoardBean gameBean = virtualView.getAdvancedGameBean();
        CLI cli = new CLI();

        cli.addBean(gameBean);
        //cli.show();

        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);
        Player player3 = game.getPlayers().get(2);

        assertEquals(PhaseEnum.PLANNING, gameBean.getPhase());
        for(Integer id: gameBean.getIdAssistantsPlayed()){
            assertEquals(0, id);
        }

        game.playAssistant(player1, 2);                     //TURN VALUE: 2
        game.playAssistant(player2, 14);                    //TURN VALUE: 4
        game.playAssistant(player3, 21);                    //TURN VALUE: 1

        gameBean = virtualView.getAdvancedGameBean();

        cli.addBean(gameBean);
        //cli.show();

        assertTrue(gameBean.getIdAssistantsPlayed().contains(2));
        assertTrue(gameBean.getIdAssistantsPlayed().contains(14));
        assertTrue(gameBean.getIdAssistantsPlayed().contains(21));

        game.sortPlayers();

        gameBean = virtualView.getAdvancedGameBean();

        cli.addBean(gameBean);
        //cli.show();


        assertEquals(player3.getPlayerId().index, gameBean.getIdPlayers().get(0));
        assertEquals(player1.getPlayerId().index, gameBean.getIdPlayers().get(1));
        assertEquals(player2.getPlayerId().index, gameBean.getIdPlayers().get(2));
    }


    @Test
    public void moveStudentsFromEntranceToHall(){
        AdvancedPlayer player = (AdvancedPlayer) game.getPlayers().get(0);
        AdvancedPlayerBean playerBean = virtualView.getAdvancedPlayerBean().get(0);
        CloudBean cloudBean = virtualView.getCloudBean().get(0);
        CLI cli = new CLI();
        cli.addBean(playerBean);
        cli.addBean(cloudBean);
        //cli.show();

        if(game.cloudIsEmpty(0))
            game.fillClouds();

        cloudBean = virtualView.getCloudBean().get(0);

        assertFalse(cloudBean.getStudents().isEmpty());

        cli.addBean(playerBean);
        cli.addBean(cloudBean);
        //cli.show();

        game.getFromCloud(player,0);
        cloudBean = virtualView.getCloudBean().get(0);
        playerBean = virtualView.getAdvancedPlayerBean().get(0);


        assertTrue(cloudBean.getStudents().isEmpty());
        assertFalse(playerBean.getStudentsAtEntrance().isEmpty());

        cli.addBean(playerBean);
        cli.addBean(cloudBean);
        //cli.show();

        int sizeBeforeMove = playerBean.getStudentsAtEntrance().size();
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToHall(player);
        playerBean = virtualView.getAdvancedPlayerBean().get(0);


        assertEquals(sizeBeforeMove - 1, playerBean.getStudentsAtEntrance().size());

        cli.addBean(playerBean);
        cli.addBean(cloudBean);
        //cli.show();


    }

    @Test
    public void moveMN(){
        Player player1 = game.getPlayers().get(0);
        PlayerBean playerBean = virtualView.getAdvancedPlayerBean().get(0); //Player have 10 assistants
        IslandGroup islandMN = null;
        game.moveMN(1);
        for(IslandGroup isla: game.getIslandGroups())
            if(isla.getIdGroup() == game.getIdIslandMN())
                islandMN = isla;


        //PLAYER 1 PLAY AN ASSISTANT, MOVE 3 STUDENTS AND MOVE MN
        player1.getBoard().addToEntrance(StudentEnum.RED);
        game.selectStudentAtEntrance(player1,0);
        game.moveFromEntranceToIsland(player1,islandMN.
                getNextIslandGroup().
                getNextIslandGroup().getIdGroup());                 //Player moves 1 red students
                                                                    // into future MN island

        player1.getBoard().addToEntrance((StudentEnum.RED));
        game.selectStudentAtEntrance(player1, 0);
        game.moveFromEntranceToHall(player1);                       //Player moves 1 red students at table and
                                                                    //gains red professor

        player1.getBoard().addToEntrance((StudentEnum.RED));
        game.selectStudentAtEntrance(player1, 0);
        game.moveFromEntranceToHall(player1);                       //Player moves 1 red students at table

        IslandGroupBean islandBean = null;
        for(IslandGroupBean bean: virtualView.getAdvancedIslandBean())
            if(bean.getIdIslandGroup() == islandMN.getIdGroup())
                islandBean = bean;

        CLI cli = new CLI();
        cli.addBean(islandBean);
        cli.addBean(playerBean);

        cli.show();

        game.playAssistant(player1, 3);                         //Assistant = (turn:3 , MN_steps: 2)
        game.moveMN(2);

        playerBean = virtualView.getAdvancedPlayerBean().get(0);            //Player have 9 assistants
        IslandGroupBean prevIslandBean = null;
        for(IslandGroupBean bean: virtualView.getAdvancedIslandBean())      //Prev island hasn't MN anymore
            if(bean.getIdIslandGroup() == islandMN.getIdGroup())
                prevIslandBean = bean;

        for(IslandGroup isla: game.getIslandGroups())               //Look for new position of MN
            if(isla.getIdGroup() == game.getIdIslandMN())
                islandMN = isla;

        for(IslandGroupBean bean: virtualView.getAdvancedIslandBean())      //new island has MN
            if(bean.getIdIslandGroup() == islandMN.getIdGroup())
                islandBean = bean;

        cli.addBean(islandBean);
        cli.addBean(prevIslandBean);
        cli.addBean(playerBean);

        cli.show();

        //PLAYER 1 BUILDS 1 TOWER ON MN ISLAND
        try {
            game.evaluateIsland(islandBean.getIdIslandGroup());

        } catch (UnmergeableException e) {
            //nulla
        }

        playerBean = virtualView.getAdvancedPlayerBean().get(0);            //Player have 9 assistants
        for(IslandGroupBean bean: virtualView.getAdvancedIslandBean())      //Now Mn island has towers
            if(bean.getIdIslandGroup() == islandMN.getIdGroup())
                islandBean = bean;

        cli.addBean(playerBean);
        cli.addBean(islandBean);

        cli.show();

        //PLAYER 1 PLAY AN ASSISTANT, PUT A RED STUDENT ON AN ISLAND AND MOVE MN ON THAT ISLAND
        game.playAssistant(player1, 1);                         //Assistant = (turn:1 , MN_steps: 1)
        player1.getBoard().addToEntrance(StudentEnum.RED);
        game.selectStudentAtEntrance(player1,0);
        game.moveFromEntranceToIsland(player1,islandMN.getNextIslandGroup().getIdGroup());
        game.moveMN(1);

        //PLAYER 1 BUILDS A TOWER ON THIS ISLAND AND THE ISLANDS MERGE THEMSELVES
        try {
            game.evaluateIsland(islandMN.getNextIslandGroup().getIdGroup());
        } catch (UnmergeableException e) {
            //nulla
        }

        for(IslandGroup isla: game.getIslandGroups())               //Look for new position of MN
            if(isla.getIdGroup() == game.getIdIslandMN())
                islandMN = isla;

        playerBean = virtualView.getAdvancedPlayerBean().get(0);            //Player have 8 assistants
        for(IslandGroupBean bean: virtualView.getAdvancedIslandBean())      //Now Mn island has towers
            if(bean.getIdIslandGroup() == islandMN.getIdGroup())
                islandBean = bean;

        assertEquals(4, playerBean.getNumTowers());
        assertEquals(2, islandBean.getIdIslands().size());
        assertEquals(8, playerBean.getIdAssistants().size());
        cli.addBean(playerBean);
        cli.addBean(islandBean);
        cli.addBean(game.toBean());
        for(IslandGroup i : game.getIslandGroups())
            cli.addBean(i.toBean());
        cli.show();

    }



}
