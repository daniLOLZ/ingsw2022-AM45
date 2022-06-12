package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.Dame;
import it.polimi.ingsw.model.characterCards.Juggler;
import it.polimi.ingsw.model.characterCards.Priest;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.game.SimpleGame;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.FactoryPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.CommandEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CLITest {


    AdvancedGame game;
    SimpleGame gameSimple;
    int players = 4;
    int coins = 20;
    int CharacterCards = 3;
    VirtualView virtualView;
    VirtualView virtualViewSimple;

    @BeforeEach
    public void initialize(){
        final List<Integer> selectedWizards = new ArrayList<>();
        selectedWizards.add(0);
        selectedWizards.add(10);
        selectedWizards.add(20);
        selectedWizards.add(30);
        final List<TeamEnum> teamColors = new ArrayList<>();
        teamColors.add(TeamEnum.WHITE);
        teamColors.add(TeamEnum.WHITE);
        teamColors.add(TeamEnum.BLACK);
        teamColors.add(TeamEnum.BLACK);
        //teamColors.add(TeamEnum.GREY);
        final List<String> nicknames = new ArrayList<>();
        nicknames.add("Franco");
        nicknames.add("Mario");
        nicknames.add("Alice");
        nicknames.add("Ben");
        virtualView = new VirtualView();
        virtualViewSimple = new VirtualView();


        try {
            game = new AdvancedGame(players,selectedWizards,teamColors,nicknames,
                    coins,CharacterCards, virtualView);
            gameSimple = new SimpleGame(players,selectedWizards,teamColors,nicknames,virtualViewSimple);
            gameSimple.initializeGame();
            game.initializeGame();
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        CLI cli = new CLI();

        game.fillClouds();
        AdvancedPlayer player = (AdvancedPlayer)game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(2);
        game.getFromCloud(player,0);
        game.getFromCloud(player2,1);
        player.addEntrance(StudentEnum.RED);
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToIsland(player,0);
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToHall(player);
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToHall(player);
        game.playAssistant(player, 1);
        game.playAssistant(player, 2);
        game.playAssistant(player, 3);
        game.playAssistant(player, 4);
        cli.printGameInterface(virtualView.renderAdvancedView());

        System.out.println("\n\n\n\n");
        cli.printGameInterface(virtualViewSimple.renderSimpleView());

    }



}
