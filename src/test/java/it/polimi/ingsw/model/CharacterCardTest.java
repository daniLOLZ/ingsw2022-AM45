package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import it.polimi.ingsw.model.board.AdvancedBoard;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characterCards.*;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardTest {


    AdvancedGame game;
    int players = 3;
    int CharacterCards = 3;
    int coins = 20;
    ParameterHandler parameter ;
    AdvancedParameterHandler advancedParameter;


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
            parameter = game.getParameters();
            advancedParameter = game.getAdvancedParameters();
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }



    /**
     * tests if FactoryCharacterCard create a card correctly, with right id and cost
     */
    @Test
    public void createCard(){

        CharacterCard card = FactoryCharacterCard.getSpecificCard(1,parameter, advancedParameter);
        CharacterCard priest = new Priest(parameter, advancedParameter);
        assertEquals(card, priest, "Wrong card");
        assertEquals(card.id, priest.id, "Wrong id");
        assertEquals(card.getCardCost(), priest.getCardCost(), "Wrong cost");

    }

    /**
     * tests if FactoryCharacterCard return 3 random and different cards
     */
    @Test
    public void randomCards(){
        List<CharacterCard> listCards = new ArrayList<>();
        CharacterCard card1 = FactoryCharacterCard.getCharacterCard(listCards,null, null);
        listCards.add(card1);
        CharacterCard card2 = FactoryCharacterCard.getCharacterCard(listCards,null, null);
        listCards.add(card2);
        CharacterCard card3 = FactoryCharacterCard.getCharacterCard(listCards,null, null);
        assertNotEquals(card1,card2, "Two equal cards");
        assertNotEquals(card2,card3,"Two equals cards");
        assertNotEquals(card1,card3,"Two equals cards");
    }

    /**
     * tests if after activate effect of a card, this one increment his cost by one
     */
    @Test
    public  void incrementCost(){

        CharacterCard card = FactoryCharacterCard.getCharacterCard(parameter ,advancedParameter);
        int costBefore = card.getCardCost();
        card.activateEffect();
        int costNow = card.getCardCost();
        assertEquals(costBefore + 1, costNow, "Cost updating not correct");
    }

    /**
     * tests if cost increment is applied one time
     */
    @Test
    public void oneTimeIncrementCost(){
        List<CharacterCard> cards = new ArrayList<>
                (FactoryCharacterCard.getAllCards(parameter, advancedParameter));
        int costBefore;
        int costNow;

        //TIME WHERE INCREMENT
        for(CharacterCard cardSel: cards){
            costBefore = cardSel.getCardCost();
            cardSel.activateEffect();
            costNow = cardSel.getCardCost();
            assertEquals(costBefore + 1, costNow, "Wrong cost with" + cardSel.id + "card");
        }

        //TIME WHERE DO NOT INCREMENT
        for(CharacterCard cardSel: cards){
            costBefore = cardSel.getCardCost();
            cardSel.activateEffect();
            costNow = cardSel.getCardCost();
            assertEquals(costBefore, costNow, "Wrong cost with" + cardSel.id + "card");
        }

    }

    /**
     * tests InitialEffect Cards, if after using their methods they still have a consistent state
     */
    @Test
    public void InitialEffectCards(){
        List<CharacterCard> cards = new ArrayList<>();
        AdvancedGame game = null;
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
        try {
            game = new AdvancedGame(3, selectedWizards,teamColors,nicknames, 20,3);
            game.initializeGame();
        }
        catch (IncorrectPlayersException e) {
            e.printStackTrace();
            return;
        }

        AdvancedPlayer player = new AdvancedPlayer(PlayerEnum.PLAYER1,"Bob",TeamEnum.WHITE, FactoryWizard.getWizard(0), true,game.getParameters());
        AdvancedBoard board = (AdvancedBoard) player.getBoard(); // It's gonna be advanced
        StudentEnum studentSel;
        IslandGroup island = new IslandGroup(0,null,null,
                null, new ArrayList<>(),TeamEnum.NOTEAM,game.getParameters());

        game.getParameters().setCurrentPlayer(player);
        game.getParameters().setCurrentPlayer(player);
        cards.addAll(FactoryCharacterCard.
                getInitialEffectCards(game.getParameters(), game.getAdvancedParameters()));


        for(CharacterCard card: cards)
            card.initialise(game);


        for(CharacterCard cardSel: cards){
            if(cardSel.id == 7){

                //JUGGLER SET TO TEST
                Juggler juggler = (Juggler) cardSel;
                juggler.removeAll();
                juggler.addStudent(StudentEnum.RED);
                studentSel = juggler.getStudents(0);
                board.addToEntrance(StudentEnum.BLUE);

                //JUGGLER METHOD
                juggler.tradeStudents(0, 0);

                //TESTS
                assertEquals(juggler.getStudents(0), StudentEnum.BLUE,
                        "Wrong student added to Juggler");
                assertEquals(studentSel,board.getAtEntrance(board.entranceSize() - 1),
                        "Wrong student at Entrance");
            }


            if(cardSel.id == 1){

                //PRIEST SET TO TEST
                Priest priest = (Priest) cardSel;
                priest.removeAll();
                priest.addStudent(StudentEnum.RED);
                studentSel = priest.getStudents(0);
                List<IslandGroup> islands = new ArrayList<>();
                islands.add(island);
                game.getParameters().setSelectedIslands(islands);

                //PRIEST METHOD
                priest.placeStudentOnIsland(game);

                //TESTS
                assertEquals(studentSel, island.getStudents().get(0), "Wrong student add to island");
                assertFalse(priest.isEmpty(), "Did not draw from sack");
            }

            if(cardSel.id == 11){

                //DAME SET TO TEST
                Dame dame = (Dame) cardSel;
                dame.removeAll();
                dame.addStudent(StudentEnum.RED);
                studentSel = dame.getStudents(0);
                int studentAtTable = board.getStudentsAtTable(studentSel);
                Integer pos = 0;
                List<Integer> positions = new ArrayList<>();
                positions.add(pos);
                game.getAdvancedParameters().setSelectedStudentsOnCard(positions);

                //DAME METHOD
                dame.placeStudentToHall(game.getSack());

                //TESTS
                assertEquals(studentAtTable + 1, board.getStudentsAtTable(studentSel).intValue(),
                        "Wrong number of students in Hall");
                assertFalse(dame.isEmpty(), "Did not draw from sack");
            }
        }

    }

    /**
     * Test Minstrel effect and how many times we can use it.
     */
    @Test
    public void MinstrelTest(){
        Minstrel minstrel = new Minstrel(parameter,advancedParameter);
        minstrel.initialise(game);
        Player player = game.getPlayers().get(0);

        //Remove one student from the entrance to make room for the test student
        player.getBoard().removeFromEntrance(8);

        //FIRST TRADE
        player.getBoard().addToEntrance(StudentEnum.BLUE);
        player.getBoard().addToHall(StudentEnum.RED);
        game.getParameters().setCurrentPlayer(player);
        minstrel.tradeStudents(8,StudentEnum.RED);

        assertEquals(0, player.getNumStudentAtTable(StudentEnum.RED));
        assertEquals(1, player.getNumStudentAtTable(StudentEnum.BLUE));
        assertEquals(StudentEnum.RED, player.getBoard().getAtEntrance(8));

        player.getBoard().removeFromEntrance(8); //clean entrance

        //SECOND TRADE
        player.getBoard().addToEntrance(StudentEnum.BLUE);
        player.getBoard().addToHall(StudentEnum.RED);
        game.getParameters().setCurrentPlayer(player);
        minstrel.tradeStudents(8,StudentEnum.RED);

        assertEquals(0, player.getNumStudentAtTable(StudentEnum.RED));
        assertEquals(2, player.getNumStudentAtTable(StudentEnum.BLUE));
        assertEquals(StudentEnum.RED, player.getBoard().getAtEntrance(8));

        player.getBoard().removeFromEntrance(8); //clean entrance


        //THIRD TRADE (not available, so no change is applied)
        player.getBoard().addToEntrance(StudentEnum.BLUE);          //remain at entrance
        player.getBoard().addToHall(StudentEnum.RED);               //remain at hall
        game.getParameters().setCurrentPlayer(player);
        minstrel.tradeStudents(8,StudentEnum.RED);

        assertEquals(1, player.getNumStudentAtTable(StudentEnum.RED));
        assertEquals(2, player.getNumStudentAtTable(StudentEnum.BLUE));
        assertEquals(StudentEnum.BLUE, player.getBoard().getAtEntrance(8));
    }

    @Test
    public void MinstrelTest1Color(){
        Minstrel minstrel = new Minstrel(parameter,advancedParameter);
        minstrel.initialise(game);
        Player player = game.getPlayers().get(0);
        game.getParameters().setCurrentPlayer(player);

        player.getBoard().addToHall(StudentEnum.RED);
        player.getBoard().addToHall(StudentEnum.RED);

        int position = 0;

        while(player.getBoard().entranceSize() > 0)
            player.getBoard().removeFromEntrance(position++);

        player.addEntrance(StudentEnum.BLUE);
        player.addEntrance(StudentEnum.BLUE);


        game.selectStudentAtEntrance(player,0);
        game.selectStudentAtEntrance(player,1);
        game.selectStudentType(StudentEnum.RED);
        minstrel.getRequirements().setSatisfied();
        minstrel.activateEffect();

        assertEquals(0, player.getBoard().getStudentsAtTable(StudentEnum.RED));
        assertEquals(2, player.getBoard().getStudentsAtTable(StudentEnum.BLUE));
        assertEquals(StudentEnum.RED, player.getStudentFromEntrance(0));
        assertEquals(StudentEnum.RED, player.getStudentFromEntrance(1));
    }

    /**
     * Test loanshark effect and if player with no enough student at hall loses all his students
     */
    @Test
    public void LoanSharkTest(){
        LoanShark loanShark = new LoanShark(parameter,advancedParameter);
        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);
        Player player3 = game.getPlayers().get(2);

        player1.getBoard().addToHall(StudentEnum.RED);
        player1.getBoard().addToHall(StudentEnum.RED);
        player1.getBoard().addToHall(StudentEnum.RED);
        player1.getBoard().addToHall(StudentEnum.RED);

        player2.getBoard().addToHall(StudentEnum.RED);
        player2.getBoard().addToHall(StudentEnum.RED);
        player2.getBoard().addToHall(StudentEnum.RED);

        player3.getBoard().addToHall(StudentEnum.RED);


        game.selectStudentType(StudentEnum.RED);
        loanShark.extortStudents(game);

        assertEquals(1, player1.getNumStudentAtTable(StudentEnum.RED));
        assertEquals(0, player2.getNumStudentAtTable(StudentEnum.RED));
        assertEquals(0, player3.getNumStudentAtTable(StudentEnum.RED));
    }

    /**
     * Test flagBearer effect
     */
    @Test
    public void FlagBearerTest(){
        FlagBearer flagBearer = new FlagBearer(parameter,advancedParameter);
        Player playerWhite = game.getPlayers().get(0);

        //Gain red prof
        playerWhite.getBoard().removeFromEntrance(playerWhite.getBoard().entranceSize() - 1);
        playerWhite.getBoard().addToEntrance(StudentEnum.RED);
        game.selectStudentAtEntrance(playerWhite,playerWhite.getBoard().entranceSize() - 1);
        game.moveFromEntranceToHall(playerWhite);

        //1 red student on island
        IslandGroup island = game.getIslandGroups().get(0);
        island.addStudent(StudentEnum.RED);
        game.selectIslandGroup(island.getIdGroup());

        //evaluate island with card effect and playerWhite wins
        flagBearer.evaluate(game);
        assertEquals(TeamEnum.WHITE,island.getTowerColor());
    }
}
