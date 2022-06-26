package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.characterCards.*;
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
        //selectedWizards.add(30);
        final List<TeamEnum> teamColors = new ArrayList<>();
        teamColors.add(TeamEnum.WHITE);
        //teamColors.add(TeamEnum.WHITE);
        teamColors.add(TeamEnum.BLACK);
        //teamColors.add(TeamEnum.BLACK);
        teamColors.add(TeamEnum.GREY);
        final List<String> nicknames = new ArrayList<>();
        final List<String> nicknames2 = new ArrayList<>();
        nicknames.add("Franco");
        nicknames.add("Mario");
        nicknames.add("Alice");
        //nicknames.add("Ben");
        nicknames2.add("Tizia");
        nicknames2.add("Caia");
        nicknames2.add("Sempronia");
        //nicknames2.add("Flavia");
        virtualView = new VirtualView();
        virtualViewSimple = new VirtualView();


        try {
            game = new AdvancedGame(players -1 ,selectedWizards,teamColors,nicknames,
                    coins,CharacterCards, virtualView);
            gameSimple = new SimpleGame(players-1,selectedWizards,teamColors,nicknames2,virtualViewSimple);
            gameSimple.initializeGame();
            game.initializeGame();
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the correct CLI visualization
     */
    @Test
    public void test(){
        CLI cli = new CLI();

        Player player1 = (Player)game.getPlayers().get(0);
        Player player2 = (Player)game.getPlayers().get(1);
        Player player3 = (Player)game.getPlayers().get(2);
        //Player player4 = (Player)game.getPlayers().get(3);
        Herbalist y;
        for(int i=0; i< 3 ; i++){
            CharacterCard x = game.getCharacterCard(i);

            if(x.equals(new Herbalist(game.getParameters(),game.getAdvancedParameters()))){
                y = (Herbalist) x;
                game.selectIslandGroup(0);
                y.blockIsland();
            }
        }


        //game.selectStudentAtEntrance(player1,0);
        //game.moveFromEntranceToHall(player1);

        //game.getFromCloud(player1,1);



        game.playAssistant(player1, 4);
        game.playAssistant(player2, 3);
        game.playAssistant(player3, 2);
        //game.playAssistant(player4, 1);

        game.sortPlayers();






        gameSimple.getPlayers().get(1).getBoard().removeFromEntrance(2);
        gameSimple.getPlayers().get(1).getBoard().removeFromEntrance(5);
        gameSimple.getPlayers().get(1).getBoard().removeFromEntrance(6);




        cli.printGameInterface(virtualView.renderAdvancedView());
        cli.printInterface();

        System.out.println("\n\n\n\n");


        cli.printGameInterface(virtualViewSimple.renderSimpleView());
        cli.printInterface();

    }




}
