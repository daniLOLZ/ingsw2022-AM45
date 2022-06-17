package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.beans.GameBoardBean;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.islands.UnmergeableException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.view.VirtualView;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleGameTest {

    SimpleGame game;
    ParameterHandler parameters;

    @BeforeEach
    public void createNewGame(){
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
        VirtualView virtualView = new VirtualView();
        try {
            game = new SimpleGame(3,selectedWizards,teamColors,nicknames,virtualView);
            game.initializeGame();
            game.initialiseSelection();
        }
        catch (IncorrectPlayersException e){
            e.printStackTrace();
        }
        parameters = game.getParameters();

    }

    @Test
    public void wrongNumberOfPlayers(){
        SimpleGame game2;
        assertThrows(IncorrectPlayersException.class, () -> new SimpleGame(5,null,null,null,null));
        assertThrows(IncorrectPlayersException.class, () -> new SimpleGame(-1,null,null,null,null));
        assertThrows(IncorrectPlayersException.class, () -> new SimpleGame(0,null,null,null,null));
    }

    @Test
    public void checkInitialized(){
        //assertFalse(game.isHasBeenInitialized());
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
        assertEquals(parameters.getProfessors().get(StudentEnum.PINK.index),
                game.getPlayers().get(0).getPlayerId());

        game.getPlayers().get(1).getBoard().addToHall(StudentEnum.PINK); // We give PLAYER 2 a pink student
        game.updateProfessor(StudentEnum.PINK); //The professor should still be PLAYER 1's
        assertNotEquals(parameters.getProfessors().get(StudentEnum.PINK.index),
                game.getPlayers().get(1).getPlayerId());

        game.getPlayers().get(1).getBoard().addToHall(StudentEnum.PINK); // We give PLAYER 2 ANOTHER pink student
        game.updateProfessor(StudentEnum.PINK); //The professor should switch sides
        assertEquals(parameters.getProfessors().get(StudentEnum.PINK.index),
                game.getPlayers().get(1).getPlayerId());

    }

    /**
     * Checks whether the sorting of the players happens successfully
     * based on the assistants played
     */
    @Test
    public void checkPlayerOrderAllDifferentCards() {
        if (game.getErrorState() == null) {
            GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1).playAssistant(5); //Should have turn order 5
            GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2).playAssistant(3); //Should have turn order 3
            GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3).playAssistant(2); //Should have turn order 2
            game.sortPlayers();
            //The order should be 3 > 2 > 1
            assertTrue(game.getPlayers().get(0).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3))
                    && game.getPlayers().get(1).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2))
                    && game.getPlayers().get(2).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1)));
        }
    }

    /**
     * Checks whether the ordering of the player works successfully
     * when two players play the same valued assistant
     */
    @Test
    public void checkPlayerOrderTwoEqualCards(){
        if(game.getErrorState() == null){
        //We begin with the case of all different cards, to set the player array
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1).playAssistant(5); //Should have turn order 5
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2).playAssistant(3); //Should have turn order 3
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3).playAssistant(2); //Should have turn order 2
        game.sortPlayers();
        // Now the order should be 3>2>1

        //Then we see what happens when two players play the same value card
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1).playAssistant(8); //Should have turn order 8
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2).playAssistant(4); //Should have turn order 4
        GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3).playAssistant(8); //Should have turn order 8
        // Now the order should be 2>3>1
        game.sortPlayers();

        assertTrue(game.getPlayers().get(0).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER2))
                && game.getPlayers().get(1).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER3))
                && game.getPlayers().get(2).equals(GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1)));
        }
    }

    /**
     * Test if after filling clouds, these are not empty
     */
    @Test
    public void fillCloudsTest(){
        game.fillClouds();
        for(int i=0; i< game.cloudsNumber(); i++)
            Assertions.assertFalse(game.cloudIsEmpty(i));
    }

    /**
     * Test if getFromCloud add correctly the student at the player entrance and
     * remove them from cloud
     */
    @Test
    public void getFromCloudTest(){
        game.fillClouds();
        Player player = game.getPlayers().get(0);
        while(player.getBoard().entranceSize() != 0)
            player.getBoard().removeFromEntrance(0);

        game.getFromCloud(player,0);

        assertTrue(player.getBoard().entranceSize() > 0);
        assertTrue(game.cloudIsEmpty(0));
    }

    @Test
    public void noMoreAssistantTest(){
        boolean noMoreAssistant = game.noMoreAssistant();
        Player player = game.getPlayers().get(0);

        assertFalse(noMoreAssistant);

        game.playAssistant(player,1);
        game.playAssistant(player,2);
        game.playAssistant(player,3);
        game.playAssistant(player,4);
        game.playAssistant(player,5);
        game.playAssistant(player,6);
        game.playAssistant(player,7);
        game.playAssistant(player,8);
        game.playAssistant(player,9);
        game.playAssistant(player,10);

        noMoreAssistant = game.noMoreAssistant();
        assertTrue(noMoreAssistant);



    }

    /**
     * Test if noMoreTowers return the team with no tower
     */
    @Test
    public void noMoreTowersTest(){
        TeamEnum noMoreTowers = game.noMoreTowers();
        assertEquals(TeamEnum.NOTEAM, noMoreTowers);

        Player player = game.getPlayers().get(0);
        int numTowers = player.getNumTowers();
        player.getBoard().updateTowers(-numTowers);
        noMoreTowers = game.noMoreTowers();
        assertEquals(player.getTeamColor(), noMoreTowers);

        //RESET
        player.getBoard().updateTowers(numTowers);

    }

    /**
     * Test if islandShortage return true if there are 3 island groups or less
     */
    @Test
    public void islandShortageTest(){
        boolean shortage = game.islandShortage();
        assertFalse(shortage);

        List<IslandGroup> mem = new ArrayList<>();
        while(game.getIslandGroups().size() > 3){
            mem.add(game.getIslandGroups().remove(0));
        }

        shortage = game.islandShortage();
        assertTrue(shortage);

        //RESET
        game.getIslandGroups().addAll(mem);
    }


    /**
     * Test if number of professors per team is correct
     */
    @Test
    public void professorPerTeamTest(){
        Map map = game.professorsPerTeam();

        assertEquals(0, map.get(TeamEnum.WHITE));
        assertEquals(0, map.get(TeamEnum.BLACK));
        assertEquals(0, map.get(TeamEnum.GREY));

        Player player = game.getPlayers().get(1);               //BLACK TEAM
        game.getParameters().setCurrentPlayer(player);
        player.getBoard().addToEntrance(StudentEnum.RED);
        game.selectStudentAtEntrance(player,0);
        game.moveFromEntranceToHall(player);                    //BLACK TEAM GAIN 1 PROF

        map = game.professorsPerTeam();

        assertEquals(0, map.get(TeamEnum.WHITE));
        assertEquals(1, map.get(TeamEnum.BLACK));
        assertEquals(0, map.get(TeamEnum.GREY));
    }

    /**
     * Test behavior of startPlanning phase
     */
    @Test
    public void startPlanningPhaseTest(){
        game.startPlanningPhase(0);
        assertEquals(PhaseEnum.PLANNING, game.getParameters().getCurrentPhase());
        assertEquals(game.getPlayers().get(0), game.getParameters().getCurrentPlayer());
    }

    /**
     * Test behavior of startAction phase
     */
    @Test
    public void startActionPhaseTest(){
        game.startActionPhase(0);
        assertEquals(PhaseEnum.ACTION, game.getParameters().getCurrentPhase());
        assertEquals(game.getPlayers().get(0), game.getParameters().getCurrentPlayer());
    }

    /**
     *Test for correct behavior of moveFromEntranceToHall.
     * Professor update and students moved
     */
    @Test
    public void moveFromEntranceToHallTest(){
        Player player = game.getPlayers().get(0);

        //Remove one student from the entrance to make room for the test student
        player.getBoard().removeFromEntrance(8);

        int previousSize = player.getBoard().entranceSize();
        int previousTableStudents = player.getNumStudentAtTable(StudentEnum.GREEN);

        player.getBoard().addToEntrance(StudentEnum.GREEN);
        game.selectStudentAtEntrance(player, 8);
        game.moveFromEntranceToHall(player);

        assertEquals(previousTableStudents + 1, player.getNumStudentAtTable(StudentEnum.GREEN));
        assertEquals(previousSize, player.getBoard().entranceSize());
        assertEquals(player.getPlayerId(), game.getParameters().getProfessors().get(StudentEnum.GREEN.index));
    }

    /**
     * Test for correct behavior of moveFromEntranceToIsland.
     * Students moved
     */
    @Test
    public void moveFromEntranceToIsland(){
        Player player = game.getPlayers().get(0);
        IslandGroup island = game.getIslandGroups().get(0);

        //Remove one student from the entrance to make room for the test student
        player.getBoard().removeFromEntrance(8);

        int entranceSize = player.getBoard().entranceSize();
        player.getBoard().addToEntrance(StudentEnum.BLUE);
        game.selectEntranceStudent(8);
        game.moveFromEntranceToIsland(player,island.getIdGroup());
        assertTrue(island.getStudents().contains(StudentEnum.BLUE));
        assertEquals(entranceSize, player.getBoard().entranceSize());
    }

    /**
     * Test for check validity of id received as input
     */
    @Test
    public void checkValidID(){

        assertFalse(game.checkValidIdIsland(5000));
        assertFalse(game.checkValidIdIsland(-1));
        assertFalse(game.checkValidIdCloud(5000));
        assertFalse(game.checkValidIdCloud(-1));
        assertTrue(game.checkValidIdIsland(game.getIslandGroups().get(0).getIdGroup()));
        boolean emptyCloud = game.cloudIsEmpty(0);
        assertTrue(game.checkValidIdCloud(0) || emptyCloud);
    }

    /**
     * Test if playerWithMoreStudent return the player with more students of chosen color
     */
    @Test
    public void moreStudentAtTable(){

        //ALL PLAYERS WITH 0 YELLOW STUDENTS
        PlayerEnum playerId = game.playerWithMoreStudent(StudentEnum.YELLOW);
        assertEquals(PlayerEnum.NOPLAYER, playerId);

        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);

        //PLAYER1 GET 1 YELLOW STUDENT AND NOW HE IS THE PLAYER WITH MORE YELLOW STUDENTS
        player1.getBoard().addToHall(StudentEnum.YELLOW);
        playerId = game.playerWithMoreStudent(StudentEnum.YELLOW);

        assertEquals(player1.getPlayerId(), playerId);

        //PLAYER2 GET 2 YELLOW STUDENT AND NOW HE IS THE PLAYER WITH MORE YELLOW STUDENTS

        player2.getBoard().addToHall(StudentEnum.YELLOW);
        player2.getBoard().addToHall(StudentEnum.YELLOW);

        playerId = game.playerWithMoreStudent(StudentEnum.YELLOW);

        assertEquals(player2.getPlayerId(), playerId);
    }

    /**
     * Test behavior of moveMN
     */
    @Test
    public void MoveMNTest(){
        int idIsland = game.moveMN(2);
        for(IslandGroup island: game.getIslandGroups())
            if(idIsland == island.getIdGroup())
                assertEquals(island.getIdGroup(), game.getIdIslandMN());
    }

    /**
     * Test behavior of evaluate.
     * Merge, Mn position and built tower
     */
    @Test
    public void evaluateTest()  {
        IslandGroup island = game.getIslandGroups().get(2);
        int previousNumberIsland = game.getIslandGroups().size();

        //PUT MN ON ISLAND
        while(game.getIdIslandMN() != island.getIdGroup())
            game.moveMN(1);

        Player player = game.getPlayers().get(2);               //GREY

        //Remove one student from the entrance to make room for the test student
        player.getBoard().removeFromEntrance(8);

        player.getBoard().addToEntrance(StudentEnum.PINK);
        game.selectEntranceStudent(8);
        game.moveFromEntranceToHall(player);
        island.addStudent(StudentEnum.PINK);

        //NO MERGE
        try {
            game.evaluateIsland(island.getIdGroup());
        } catch (UnmergeableException e) {
            //Non una vera eccezione
        }

        assertEquals(TeamEnum.GREY, island.getTowerColor());

        //MERGE 2 ISLANDS
        IslandGroup island2 = island.getNextIslandGroup();
        player.getBoard().addToEntrance(StudentEnum.PINK);
        game.selectEntranceStudent(8);
        game.moveFromEntranceToHall(player);
        island2.addStudent(StudentEnum.PINK);


        try {
            game.evaluateIsland(island2.getIdGroup());
        } catch (UnmergeableException e) {
            //Non una vera eccezione
        }

        assertFalse(game.getIslandGroups().contains(island));
        assertFalse(game.getIslandGroups().contains(island2));
        assertEquals(previousNumberIsland - 1, game.getIslandGroups().size());
        assertFalse(game.getIdIslandMN() == island.getIdGroup());


        int newIslandId = game.getIdIslandMN();
        IslandGroup newIsland = null;
        for(IslandGroup isla: game.getIslandGroups())
            if(isla.getIdGroup() == newIslandId)
                newIsland = isla;

        assertEquals(2, newIsland.getIslands().size());
        assertEquals(island.getStudents().size() + island2.getStudents().size(),
                newIsland.getStudents().size());

        assertEquals(island.getTowerColor(), newIsland.getTowerColor());


    }

    /**
     * Bean test
     */
    @Test
    public void toBeanTest(){
        GameBoardBean bean = (GameBoardBean) game.toBean();


        assertNotNull(bean);


        List<Integer> cardsAssistant = bean.getIdAssistantsPlayed();
        List<Integer> idPlayer = bean.getIdPlayers();
        for(Player player: game.getPlayers()){
            if(player.getAssistantPlayed() != null)
                assertTrue(cardsAssistant.contains(player.getAssistantPlayed().id));
            assertTrue(idPlayer.contains(player.getPlayerId().index));
        }

        List<Integer> islands = bean.getIdIslandGroups();
        for(IslandGroup island: game.getIslandGroups())
            assertTrue(islands.contains(island.getIdGroup()));

        assertEquals(game.getParameters().getCurrentPlayer().getPlayerId().index,bean.getCurrentPlayerId());
        assertEquals(game.getParameters().getTurn(),bean.getTurn());
        assertEquals(game.getParameters().getCurrentPhase(), bean.getPhase());
    }

    /**
     * Test if assistant card is played correctly
     */
    @Test
    public void PlayAssistant(){
        Player player3 = game.getPlayers().get(2);
        int size = player3.getWizard().size();
        game.playAssistant(player3, 1);
        assertEquals(21,player3.getAssistantPlayed().id);
        assertEquals(size - 1,player3.getWizard().size());
    }

    @Test
    public void playedAssistantsTest(){
        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);
        Player player3 = game.getPlayers().get(2);

        //FIRST TIME OF PLANNING PHASE WHERE ASSISTANTS ARE PLAYED
        game.playAssistant(player1,1);
        game.playAssistant(player2,1);
        game.playAssistant(player3,1);

        List<Assistant> assistantsPlayed = game.playedAssistants();
        List<Integer> assistantPlayedId = assistantsPlayed.stream().
                mapToInt(x -> x.id).
                collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        assertEquals(3, assistantPlayedId.size());
        assertTrue(assistantPlayedId.contains(1));
        assertTrue(assistantPlayedId.contains(11));
        assertTrue(assistantPlayedId.contains(21));
        assertFalse(assistantPlayedId.contains(22));


        //SECOND TIME OF PLANNING PHASE WHERE ASSISTANTS ARE PLAYED
        game.playAssistant(player1,2);
        game.playAssistant(player2,2);
        game.playAssistant(player3,2);

        assistantsPlayed = game.playedAssistants();
        assistantPlayedId = assistantsPlayed.stream().
                                     mapToInt(x -> x.id).
                                        collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        assertEquals(3, assistantPlayedId.size());
        assertTrue(assistantPlayedId.contains(2));
        assertTrue(assistantPlayedId.contains(12));
        assertTrue(assistantPlayedId.contains(22));
        assertFalse(assistantPlayedId.contains(1));
    }

    @Test
    public void emptySackTest(){
        assertFalse(game.emptySack());

        while(!game.getSack().isEmpty()){
            game.getSack().drawNStudents(1);
        }

        assertTrue(game.emptySack());
    }

    /**
     * Test if select and deselect correctly
     */
    @Test
    public void selectionTest(){
        IslandGroup island = game.getIslandGroups().get(0);
        Player player = game.getPlayers().get(0);
        player.getBoard().addToEntrance(StudentEnum.RED);
        game.selectIslandGroup(island.getIdGroup());
        game.selectStudentAtEntrance(player,0);
        game.selectStudentType(StudentEnum.PINK);

        assertEquals(island, game.getParameters().getSelectedIslands().get().get(0));
        assertEquals(0,game.getParameters().getSelectedEntranceStudents().get().get(0));
        assertEquals(StudentEnum.PINK,game.getParameters().getSelectedStudentTypes().get().get(0));

        game.deselectAllEntranceStudents();
        game.deselectAllIslandGroup();
        game.deselectAllStudentTypes();

        assertTrue(game.getParameters().getSelectedIslands().get().isEmpty());
        assertTrue(game.getParameters().getSelectedEntranceStudents().get().isEmpty());
        assertTrue(game.getParameters().getSelectedStudentTypes().get().isEmpty());

    }



}
