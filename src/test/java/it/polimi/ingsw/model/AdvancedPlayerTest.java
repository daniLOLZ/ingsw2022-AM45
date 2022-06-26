package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.view.VirtualView;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class AdvancedPlayerTest {

    AdvancedPlayer player;
    AdvancedGame game;

    @BeforeEach
    public void initializePlayers(){final List<Integer> selectedWizards = new ArrayList<>();
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
            game = new AdvancedGame(3,selectedWizards,teamColors,
                    nicknames,20,3, virtualView);
            game.initializeGame();
            player = (AdvancedPlayer) game.getPlayers().get(0);

        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test coin methods
     */
    @Test
    public void coinTest(){
        assertEquals(1,player.getNumCoins());

        player.addCoin();
        assertEquals(2,player.getNumCoins());

        player.useCoin();
        assertEquals(1,player.getNumCoins());
    }



}
