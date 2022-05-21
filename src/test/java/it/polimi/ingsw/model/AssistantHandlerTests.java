package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.AssistantHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.FactoryAssistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.assistantCards.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssistantHandlerTests {

    final Controller controller = new Controller();
    AssistantHandler assistantHandler;
    List<Assistant> playedCards = new ArrayList<>();
    Player currentPlayer;
    Player player1;
    Player player2;
    Wizard currentWizard;
    List<Player> playerList;
    final int assistantPlayer1ID = 2;

    @BeforeEach
    public void initialise(){
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
        controller.createSimpleGame(3,selectedWizards,teamColors,nicknames);
        controller.getSimpleGame().initializeGame();
        player1 = controller.getSimpleGame().getPlayers().get(0);
        player2 = controller.getSimpleGame().getPlayers().get(1);
        controller.getSimpleGame().getParameters().setCurrentPlayer(player2);
        currentPlayer = controller.getSimpleGame().getParameters().getCurrentPlayer();
        currentWizard = currentPlayer.getWizard();
        playerList = controller.getSimpleGame().getPlayers();
        assistantHandler = new AssistantHandler(controller);

    }


    /**
     * Check if Handler recognize as invalid card the Player 2 (Current Player)  Card,
     * Because the Player 2 Card is equal to already played card
     *//*
    @Test
    public void InvalidCardAlreadyPlayed(){
        assertFalse(assistantHandler.checkValidAssistant(assistantPlayer1ID + currentWizard.getIdWizard()),
                "Invalid card is not recognized");
    }
    */

    /**
     * Player 2 play a card played by player 1 but this time this action is valid
     * because player2 has not valid choice
     */
    @Test
    public void AllEquals(){
        //PLAYER2 PLAYED ALL CARDS EXCEPT THE 10
        player2.playAssistant(1 + player2.getWizard().getIdWizard());
        player2.playAssistant(2 + player2.getWizard().getIdWizard());
        player2.playAssistant(3 + player2.getWizard().getIdWizard());
        player2.playAssistant(4 + player2.getWizard().getIdWizard());
        player2.playAssistant(5 + player2.getWizard().getIdWizard());
        player2.playAssistant(6 + player2.getWizard().getIdWizard());
        player2.playAssistant(7 + player2.getWizard().getIdWizard());
        player2.playAssistant(8 + player2.getWizard().getIdWizard());
        player2.playAssistant(9 + player2.getWizard().getIdWizard());

        //PLAYER1 PLAY HIS 10 CARD AS FIRST PLAYER
        playedCards.clear();
        playedCards.add(FactoryAssistant.getAssistant(10));


        //PLAYER2 PLAY HIS 10 CARD BUT THIS CARD IS VALID BECAUSE HE HAS NOT VALID CHOICE
        assertTrue(assistantHandler.checkValidAssistant(10 + currentWizard.getIdWizard()),
                "All equals was not recognized");

    }
}
