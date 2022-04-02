package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardTest {

    @Test
    public void createCard(){

        CharacterCard card = FactoryCharacterCard.getSpecificCard(1);
        CharacterCard priest = new Priest();
        assertEquals(card, priest, "Wrong card");
        assertEquals(card.id, priest.id, "Wrong id");
        assertEquals(card.getCardCost(), priest.getCardCost(), "Wrong cost");

    }

    @Test
    public void randomCards(){
        List<CharacterCard> listCards = new ArrayList<>();
        CharacterCard card1 = FactoryCharacterCard.getCharacterCard(listCards);
        listCards.add(card1);
        CharacterCard card2 = FactoryCharacterCard.getCharacterCard(listCards);
        listCards.add(card2);
        CharacterCard card3 = FactoryCharacterCard.getCharacterCard(listCards);
        assertNotEquals(card1,card2, "Two equal cards");
        assertNotEquals(card2,card3,"Two equals cards");
        assertNotEquals(card1,card3,"Two equals cards");
    }

    @Test
    public  void incrementCost(){
        CharacterCard card = FactoryCharacterCard.getCharacterCard();
        AdvancedGame game = new AdvancedGame(2, 20,3);
        int costBefore = card.getCardCost();
        card.activateEffect(game);
        int costNow = card.getCardCost();
        assertEquals(costBefore + 1, costNow, "Cost updating not correct");
    }

    @Test
    public void oneTimeIncrementCost(){
        AdvancedGame game = new AdvancedGame(2,20,3);
        List<CharacterCard> cards = new ArrayList<>();
        cards.addAll(FactoryCharacterCard.getAllCards());
        int costBefore;
        int costNow;

        //TIME WHERE INCREMENT
        for(CharacterCard cardSel: cards){
            costBefore = cardSel.getCardCost();
            cardSel.activateEffect(game);
            costNow = cardSel.getCardCost();
            assertEquals(costBefore + 1, costNow, "Wrong cost with" + cardSel.id + "card");
        }

        //TIME WHERE DO NOT INCREMENT
        for(CharacterCard cardSel: cards){
            costBefore = cardSel.getCardCost();
            cardSel.activateEffect(game);
            costNow = cardSel.getCardCost();
            assertEquals(costBefore, costNow, "Wrong cost with" + cardSel.id + "card");
        }

    }

    @Test
    public void InitialEffectCards(){
        List<CharacterCard> cards = new ArrayList<>();
        cards.addAll(FactoryCharacterCard.getInitialEffectCards());
        AdvancedGame game = new AdvancedGame(2,20,3);
        game.setCurrentPlayer(new Player(game,PlayerEnum.PLAYER1,"Bob", TeamEnum.BLACK, true));
        StudentEnum studentSel;
        Board board = game.getCurrentPlayer().getBoard();
        IslandGroup island = new IslandGroup(game,0,null,null,
                                            null, new ArrayList<>(),TeamEnum.NOTEAM);


        for(CharacterCard cardSel: cards){
            if(cardSel.id == 7){

                //JUGGLER SET TO TEST
                Juggler juggler = (Juggler) cardSel;
                juggler.removeAll();
                juggler.addStudent(StudentEnum.RED);
                studentSel = juggler.getStudents(0);

                //JUGGLER METHOD
                juggler.tradeStudents(StudentEnum.BLUE, 0, game.getCurrentPlayer());

                //TESTS
                assertEquals(juggler.getStudents(0), StudentEnum.BLUE,
                        "Wrong student added to Juggler");
                assertEquals(studentSel,board.getAtEntrance(board.EntranceSize() - 1),
                        "Wrong student at Entrance");
            }


            if(cardSel.id == 1){

                //PRIEST SET TO TEST
                Priest priest = (Priest) cardSel;
                priest.removeAll();
                priest.addStudent(StudentEnum.RED);
                studentSel = priest.getStudents(0);

                //PRIEST METHOD
                priest.placeStudentOnIsland(game,island,0);

                //TESTS
                assertEquals(studentSel, island.getStudents().get(0), "Wrong student add to island");
                assertTrue(!priest.isEmpty(),"Did not draw from sack");
            }

            if(cardSel.id == 11){

                //DAME SET TO TEST
                Dame dame = (Dame) cardSel;
                dame.removeAll();
                dame.addStudent(StudentEnum.RED);
                studentSel = dame.getStudents(0);
                int studentAtTable = board.getStudentsPerTable(studentSel).intValue();

                //DAME METHOD
                dame.placeStudentToHall(game.getCurrentPlayer(),0,game.getSack());

                //TESTS
                assertEquals(studentAtTable + 1, board.getStudentsPerTable(studentSel).intValue(),
                        "Wrong number of students in Hall");
                assertTrue(!dame.isEmpty(), "Did not draw from sack");
            }
        }

    }
}
