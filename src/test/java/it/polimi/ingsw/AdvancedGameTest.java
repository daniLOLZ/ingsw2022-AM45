package it.polimi.ingsw;

import it.polimi.ingsw.model.AdvancedGame;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.IncorrectPlayersException;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.characterCards.Glutton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdvancedGameTest {

    AdvancedGame game;
    int players = 2;
    int CharacterCards = 3;
    int coins = 20;

    @BeforeEach
    public void initialize(){
        try {
            game = new AdvancedGame(players,coins,CharacterCards);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if reset correctly
     */
    @Test
    public void resetTest(){
        game.setTradeableStudent(3);
        game.setDrawIsWin(true);
        game.setIslandToEvaluateDue(true);
        game.setAdditionalInfluence(2);
        game.setIgnoredStudent(true);
        game.setChosenStudentType(StudentEnum.RED);

        game.resetCardEffect();

        assertNull(game.getChosenIsland(),"Chosen Island was not reset");
        assertFalse(game.isDrawIsWin(),"DrawIsWin was not reset");
        assertEquals(0,game.getMNAdditionalSteps(),"MNAdditionalSteps was not reset");
        assertFalse(game.isCountTowers(),"CountTowers was not reset");
        assertFalse(game.isIgnoredStudent(),"IgnoredStudent was not reset");
        assertEquals(0,game.getIdCharacterCardActive(),"Id Character Card active was not reset");
        assertFalse(game.isIslandToEvaluateDue(),"IslandToEvalueteDue was not reset");
        assertEquals(0,game.getAdditionalInfluence(),"Additional Influence was not reset");
        assertEquals(0,game.getTradeableStudent(),"Tradeable student was not reset");
        assertEquals(StudentEnum.NOSTUDENT,game.getChosenStudentType(),"Chosen student type was not reset");
    }

    /**
     * Tests if game can play a Character Card and if, after
     * playing a CharacterCard, game update correctly his state
     */
    @Test
    public void playCharacterCardTest(){
        game.playCharacterCard(0);
        assertTrue(game.getIdCharacterCardActive() != 0, "Wrong id Character Card active");
    }


}
