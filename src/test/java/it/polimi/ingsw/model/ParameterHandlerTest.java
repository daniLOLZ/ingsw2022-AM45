package it.polimi.ingsw.model;

import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.game.SimpleGame;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ParameterHandlerTest {

    SimpleGame game;
    ParameterHandler params;

    @BeforeEach
    void createGame(){
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
            game = new SimpleGame(3,selectedWizards,teamColors,nicknames);
            game.initializeGame();
        } catch (IncorrectPlayersException e) {
            fail();
        }
        params = game.getParameters();
    }

    /**
     * Tests if the method correctly returns the professors owned by
     * a player
     */
    @Test
    void getProfessorsPlayerTest(){
        List<Player> players = game.getPlayers();
        players.get(0).getBoard().addToHall(StudentEnum.PINK);
        game.updateProfessor(StudentEnum.PINK);
        players.get(0).getBoard().addToHall(StudentEnum.BLUE);
        game.updateProfessor(StudentEnum.BLUE);
        assertTrue(params.getProfessorsByPlayer(players.get(0).getPlayerId()).containsAll(
                    Arrays.asList(StudentEnum.PINK, StudentEnum.BLUE)));
        assertTrue(params.getProfessorsByPlayer(players.get(1).getPlayerId()).isEmpty());
    }
}
