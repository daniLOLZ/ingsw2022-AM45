package it.polimi.ingsw.model;

import it.polimi.ingsw.model.game.AdvancedGame;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGameTest {

    AdvancedGame game;
    int players = 2;
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
        try {
            game = new AdvancedGame(players,selectedWizards,teamColors,nicknames,coins,CharacterCards);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkCorrectNumberCoins(){
        assertTrue(game.getAdvancedParameters().getNumCoins() >= 0);
    }

}
