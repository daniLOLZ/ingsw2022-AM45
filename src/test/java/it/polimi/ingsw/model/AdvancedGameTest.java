package it.polimi.ingsw.model;

import it.polimi.ingsw.model.game.AdvancedGame;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
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

    @Test
    public void checkCorrectNumberCoins(){
        assertTrue(game.getAdvancedParameters().getNumCoins() >= 0);
    }

}
