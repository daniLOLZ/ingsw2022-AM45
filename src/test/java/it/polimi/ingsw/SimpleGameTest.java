package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleGameTest {

    SimpleGame game;

    @BeforeEach
    public void createNewGame(){
        try {
            game = new SimpleGame(3);
        }
        catch (IncorrectPlayersException e){
            e.printStackTrace();
        }
        //game = new SimpleGame(3)
        //game = new SimpleGame(4)
    }

    @Test
    public void wrongNumberOfPlayers(){
        SimpleGame game2;
        assertThrows(IncorrectPlayersException.class, () -> new SimpleGame(5));
        assertThrows(IncorrectPlayersException.class, () -> new SimpleGame(-1));
        assertThrows(IncorrectPlayersException.class, () -> new SimpleGame(0));
    }

    @Test
    public void checkInitialized(){
        assertFalse(game.isHasBeenInitialized());
        game.initializeGame();
        assertTrue(game.isHasBeenInitialized());
    }

    /**
     * Checks whether a professor switches sides correctly
     * both when there is an equal or different amount of students at a table
     */
    @Test
    public void checkOwnershipChangeOfProfessor(){
        game.initializeGame();
        game.getPlayers().get(0).getBoard().addToHall(StudentEnum.PINK); // Add a pink student to PLAYER 1 board
        game.updateProfessor(StudentEnum.PINK); //The pink professor should now belong to PLAYER1
        assertTrue(game.getProfessors().get(StudentEnum.PINK.index).equals(game.getPlayers().get(0).getPlayerId()));

        game.getPlayers().get(1).getBoard().addToHall(StudentEnum.PINK); // We give PLAYER 2 a pink student
        game.updateProfessor(StudentEnum.PINK); //The professor should still be PLAYER 1's
        assertFalse(game.getProfessors().get(StudentEnum.PINK.index).equals(game.getPlayers().get(1).getPlayerId()));

        game.getPlayers().get(1).getBoard().addToHall(StudentEnum.PINK); // We give PLAYER 2 ANOTHER pink student
        game.updateProfessor(StudentEnum.PINK); //The professor should switch sides
        assertTrue(game.getProfessors().get(StudentEnum.PINK.index).equals(game.getPlayers().get(1).getPlayerId()));

    }

    /**
     * Checks whether the sorting of the players happens successfully
     * based on the assistants played
     */
    @Test
    public void checkPlayerOrderAllDifferentCards(){
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1).playAssistant(5); //Should have turn order 5
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2).playAssistant(13); //Should have turn order 3
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3).playAssistant(22); //Should have turn order 2
        game.sortPlayers();
        //The order should be 3 > 2 > 1
        assertTrue(game.getPlayers().get(0).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3))
                    && game.getPlayers().get(1).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2))
                    && game.getPlayers().get(2).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1)));
    }

    /**
     * Checks whether the ordering of the player works successfully
     * when two players play the same valued assistant
     */
    @Test
    public void checkPlayerOrderTwoEqualCards(){
        //We begin with the case of all different cards, to set the player array
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1).playAssistant(5); //Should have turn order 5
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2).playAssistant(13); //Should have turn order 3
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3).playAssistant(22); //Should have turn order 2
        game.sortPlayers();
        // Now the order should be 3>2>1

        //Then we see what happens when two players play the same value card
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1).playAssistant(8); //Should have turn order 8
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2).playAssistant(14); //Should have turn order 4
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3).playAssistant(28); //Should have turn order 8
        // Now the order should be 2>3>1
        game.sortPlayers();

        assertTrue(game.getPlayers().get(0).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2))
                && game.getPlayers().get(1).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3))
                && game.getPlayers().get(2).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1)));
    }



}
