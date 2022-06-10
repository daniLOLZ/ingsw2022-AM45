package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.AdvancedGameBoardBean;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.Glutton;
import it.polimi.ingsw.model.characterCards.Herbalist;
import it.polimi.ingsw.model.game.AdvancedGame;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGameTest {

    AdvancedGame game;
    int players = 3;
    int CharacterCards = 3;
    int coins = 20;

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

        VirtualView virtualView = new VirtualView();
        try {
            game = new AdvancedGame(players,selectedWizards,teamColors,nicknames,
                    coins,CharacterCards, virtualView);
            game.initializeGame();
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check correct number of coins
     */
    @Test
    public void checkCorrectNumberCoins(){
        assertEquals(game.getAdvancedParameters().getNumCoins(), coins);
    }

    /**
     * Test teh correct existence of character cards
     */
    @Test
    public void checkCharacterCards(){
        for(int i=0; i< CharacterCards; i++){
            CharacterCard card = game.getCharacterCard(i);
            assertNotNull(card);
        }
    }

    /**
     * Test for correct behavior of method spend coin
     */
    @Test
    public void spendCoinTest(){

        //ENOUGH COIN
        int previousCoins = game.getAdvancedParameters().getNumCoins();
        AdvancedPlayer player = (AdvancedPlayer) game.getPlayers().get(0);
        int previousPlayerCoin = player.getNumCoins();
        boolean enoughCoin = game.spendCoin(player, 1);
        int nowCoins = game.getAdvancedParameters().getNumCoins();
        int nowPlayerCoins = player.getNumCoins();

        assertEquals(previousCoins + 1, nowCoins,"Game coins incorrect");
        assertEquals(previousPlayerCoin - 1, nowPlayerCoins,"Player coins incorrect");
        assertTrue(enoughCoin);

        player.addCoin();                            //To return in previous state
        game.getAdvancedParameters().removeCoin();  //To return in previous state

        //NOT ENOUGH COIN
        enoughCoin = game.spendCoin(player, 500);
        nowCoins = game.getAdvancedParameters().getNumCoins();
        nowPlayerCoins = player.getNumCoins();

        assertEquals(previousCoins , nowCoins,"Game coins incorrect");
        assertEquals(previousPlayerCoin , nowPlayerCoins,"Player coins incorrect");
        assertFalse(enoughCoin);


    }

    /**
     * Test if moveFromEntranceToHall override correctly the previous method adding coin
     * to player when he deserves it
     */
    @Test
    public void moveFromEntranceToHallTest(){
        AdvancedPlayer player = (AdvancedPlayer) game.getPlayers().get(0);
        int previousCoin = game.getAdvancedParameters().getNumCoins();
        int previousPlayerCoin = player.getNumCoins();

        //ADD 3 STUDENT WITH SAME COLOR AT PLAYER ENTRANCE
        while(player.getBoard().entranceSize() != 0)
            player.getBoard().removeFromEntrance(0);

        player.getBoard().addToEntrance(StudentEnum.RED);
        player.getBoard().addToEntrance(StudentEnum.RED);
        player.getBoard().addToEntrance(StudentEnum.RED);


        //PUT 3 STUDENT WITH SAME COLOR AND PLAYER GETS A COIN
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToHall(player);
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToHall(player);
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToHall(player);
        int nowCoin = game.getAdvancedParameters().getNumCoins();
        int nowPlayerCoin = player.getNumCoins();

        assertEquals(previousCoin - 1, nowCoin, "Error with game coin");
        assertEquals(previousPlayerCoin + 1, nowPlayerCoin, "Error with player coin");

        //RESET
        player.useCoin();
        game.getAdvancedParameters().addCoins(1);
        player.getBoard().removeNStudentsFromHall(StudentEnum.RED,3);

    }

    /**
     * Test the  correct behavior of updateProfessor.
     * When glutton effect is on and when it is off.
     */
    @Test
    public void updateProfessors(){
        Glutton glutton = new Glutton(game.getParameters(), game.getAdvancedParameters());
        AdvancedPlayer player1 = (AdvancedPlayer) game.getPlayers().get(0);
        AdvancedPlayer player2 = (AdvancedPlayer) game.getPlayers().get(1);

        //WITH GLUTTON
        player1.getBoard().addToEntrance(StudentEnum.RED);
        player2.getBoard().addToEntrance(StudentEnum.RED);

        //Player1 move a red student into Hall and gain red professor
        game.getParameters().setCurrentPlayer(player1);
        game.selectStudentAtEntrance(player1,0);
        game.moveFromEntranceToHall(player1);
        PlayerEnum hasRedProf = game.getParameters().getProfessors().get(StudentEnum.RED.index);
        assertEquals(player1.getPlayerId(), hasRedProf, "Error getting professor");

        //GLUTTON EFFECT (now draw win professor)
        glutton.activateEffect();
        assertTrue(game.getAdvancedParameters().isDrawIsWin());


        //Player2 move a red student into Hall and gain red professor cause glutton effect
        game.getParameters().setCurrentPlayer(player2);
        game.selectStudentAtEntrance(player2,0);
        game.moveFromEntranceToHall(player2);
        hasRedProf = game.getParameters().getProfessors().get(StudentEnum.RED.index);
        assertEquals(player2.getPlayerId(), hasRedProf, "Error getting professor");

        //RESET
        game.getParameters().getProfessors().add(StudentEnum.RED.index, PlayerEnum.NOPLAYER);
        game.getAdvancedParameters().setDrawIsWin(false);
        player1.getBoard().removeNStudentsFromHall(StudentEnum.RED,1);
        player2.getBoard().removeNStudentsFromHall(StudentEnum.RED,1);



        //WITHOUT GLUTTON
        player1.getBoard().addToEntrance(StudentEnum.RED);
        player2.getBoard().addToEntrance(StudentEnum.RED);

        //Player1 move a red student into Hall and gain red professor
        game.getParameters().setCurrentPlayer(player1);
        game.selectStudentAtEntrance(player1,0);
        game.moveFromEntranceToHall(player1);
        hasRedProf = game.getParameters().getProfessors().get(StudentEnum.RED.index);
        assertEquals(player1.getPlayerId(), hasRedProf, "Error getting professor");


        //NO GLUTTON EFFECT(draw is not enough to win a professor)

        //Player2 move a red student into Hall, but he draws with player1 so player1 still
        //has red professor
        game.getParameters().setCurrentPlayer(player2);
        game.selectStudentAtEntrance(player2,0);
        game.moveFromEntranceToHall(player2);
        hasRedProf = game.getParameters().getProfessors().get(StudentEnum.RED.index);
        assertEquals(player1.getPlayerId(), hasRedProf, "Error getting professor");


        //RESET
        game.getParameters().getProfessors().add(StudentEnum.RED.index, PlayerEnum.NOPLAYER);
        game.getAdvancedParameters().setDrawIsWin(false);
        player1.getBoard().removeNStudentsFromHall(StudentEnum.RED,1);
        player2.getBoard().removeNStudentsFromHall(StudentEnum.RED,1);
    }

    /**
     * Test the  correct behavior of canUseThisCard.
     * If player has enough coins return true, false otherwise
     */
    @Test
    public void canUseCharacterCardTest(){
        AdvancedPlayer player = (AdvancedPlayer) game.getPlayers().get(0);
        int playerCoin = player.getNumCoins();
        CharacterCard card = game.getCharacterCard(0);
        int cardCost = card.getCardCost();

        //ENOUGH COIN
        while(playerCoin < cardCost){
            player.addCoin();
            playerCoin = player.getNumCoins();
        }

        boolean canUseCard = game.canUseCharacterCard(player, card.id);
        assertTrue(canUseCard,"Coin should be enough to use the card");

        //NOT ENOUGH COIN
        player.addCoin();
        playerCoin = player.getNumCoins();

        while (playerCoin >= cardCost){
            player.useCoin();
            playerCoin = player.getNumCoins();
        }

        canUseCard = game.canUseCharacterCard(player, card.id);
        assertFalse(canUseCard,"Coin should not be enough to use the card ");
    }

    /**
     * Test the  correct behavior of isBlocked.
     */
    @Test
    public void isBlockedTest(){
        Herbalist herbalist = new Herbalist(game.getParameters(), game.getAdvancedParameters());
        AdvancedIslandGroup island = (AdvancedIslandGroup) game.getIslandGroups().get(0);
        game.getParameters().setErrorState("");
        boolean isBlocked;

        //WRONG ID
        isBlocked = game.isBlocked(-1);
        assertFalse(isBlocked);
        assertEquals("WRONG ID ISLAND GROUP", game.getParameters().getErrorState());

        game.getParameters().setErrorState("");


        //NOT BLOCKED
        isBlocked = game.isBlocked(island.getIdGroup());
        assertFalse(isBlocked);
        assertEquals("", game.getParameters().getErrorState());

        //BLOCKED
        game.selectIslandGroup(island.getIdGroup());
        herbalist.getRequirements().setSatisfied();
        herbalist.activateEffect();
        herbalist.blockIsland();
        isBlocked = game.isBlocked(island.getIdGroup());
        assertTrue(isBlocked);
        assertEquals("", game.getParameters().getErrorState());

        //Reset
        island.unblock();



    }

    /**
     * Test the  correct behavior of unblock.
     */
    @Test
    public void unblockTest(){
        AdvancedIslandGroup island = (AdvancedIslandGroup) game.getIslandGroups().get(0);
        Herbalist herbalist = new Herbalist(game.getParameters(), game.getAdvancedParameters());

        game.selectIslandGroup(island.getIdGroup());
        herbalist.getRequirements().setSatisfied();
        herbalist.activateEffect();
        herbalist.blockIsland();

        assertEquals(1,island.getNumBlockTiles());

        game.unblockIsland(island.getIdGroup());

        assertEquals(0,island.getNumBlockTiles());
    }

    /**
     * Test if bean is correctly created
     */
    @Test
    public void toBeanTest(){
        AdvancedGameBoardBean bean = (AdvancedGameBoardBean) game.toBean();

        assertNotNull(bean);
        List<Integer> cards = bean.getIdCharacterCards();
        for(int i=0; i< CharacterCards; i++)
            assertTrue(cards.contains(game.getCharacterCard(i).id));

        List<Integer> cardsAssistant = bean.getIdAssistantsPlayed();
        List<Integer> idPlayer = bean.getIdPlayers();
        for(Player player: game.getPlayers()){
            if(player.getAssistantPlayed() != null)
                assertTrue(cardsAssistant.contains(player.getAssistantPlayed().id));
            assertTrue(idPlayer.contains(player.getPlayerId().index));
        }

        List<Integer> islands = bean.getIdIslandGroups();
        for(IslandGroup island: game.getIslandGroups())
            assertTrue(islands.contains(island.getIdGroup()));

        assertEquals(game.getAdvancedParameters().getNumCoins(),bean.getNumGameCoins());
        assertEquals(game.getParameters().getCurrentPlayer().getPlayerId().index,bean.getCurrentPlayerId());
        assertEquals(game.getParameters().getTurn(),bean.getTurn());
        assertEquals(game.getParameters().getCurrentPhase(), bean.getPhase());
    }

    /**
     * Tests if select the correct position on card
     */
    @Test
    public void selectStudentsOnCard(){
        game.selectStudentOnCard(0);
        assertEquals(0,game.getAdvancedParameters().getSelectedStudentsOnCard().get().get(0));
    }




}
