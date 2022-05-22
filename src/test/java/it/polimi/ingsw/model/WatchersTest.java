package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
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
        assertEquals(PhaseEnum.PLANNING.name,gameBean.getPhase());
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

        assertEquals(PhaseEnum.PLANNING.name, gameBean.getPhase());
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




}
