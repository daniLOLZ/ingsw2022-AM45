package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.Centaur;
import it.polimi.ingsw.model.characterCards.Fungalmancer;
import it.polimi.ingsw.model.characterCards.Herbalist;
import it.polimi.ingsw.model.characterCards.Knight;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
import it.polimi.ingsw.model.islands.BlockTile;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.islands.UnmergeableException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AdvancedIslandGroupTest {

    AdvancedGame game;
    ParameterHandler parameters;
    AdvancedParameterHandler advancedParameters;
    List<IslandGroup> group;
    int numIslands = 12;
    int numCoins = 20;
    int numCharacters = 3;
    int islandGroupId = 0;

    /**
     * Creates the base elements to test the class, like the game
     * hosting the AdvancedIslandGroups
     */
    @BeforeEach
    public void initializeIslandGroup(){

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
        try{
            game = new AdvancedGame(3,selectedWizards,teamColors,nicknames, numCoins, numCharacters, virtualView);
            game.initializeGame();
        }
        catch (IncorrectPlayersException e){
            e.printStackTrace();
        }

        parameters = game.getParameters();
        advancedParameters = game.getAdvancedParameters();

        group = game.getIslandGroups();
        islandGroupId += numIslands;

    }

    @Test
    public void checkAttributesNotNull(){

        AdvancedIslandGroup island = (AdvancedIslandGroup) group.get(0);
        assertNotNull(island.getNextIslandGroup(), "nextIslandGrouop null");
        assertNotNull(island.getPrevIslandGroup(), "prevIslandGroup null");
        assertNotNull(island.getTowerColor(), "towerColor null");
        assertNotNull(island.getParameters(), "parameters null");
        assertNotNull(island.getBlockTiles(), "blockTiles null");
        assertNotNull(island.getAdvancedParameters(), "advancedParameters null");

    }

    /**
     * Similar test to the superclass, different code is being run because
     * of different implementation
     */
    @Test
    public void checkCorrectAssignmentOfPointers(){
        IslandGroup firstGroup = group.get(0);
        IslandGroup curIsland = group.get(0);
        for(int i=0;i<numIslands; i++){
            curIsland = curIsland.getNextIslandGroup();
        }
        //After a full visit, we should end up at the exact same group
        assertSame(firstGroup, curIsland, "The two groups aren't the same");

        for (IslandGroup Is : group) {
            assertSame(Is.getNextIslandGroup().getPrevIslandGroup(), Is, "Error in assignment next -> prev");
            assertSame(Is.getPrevIslandGroup().getNextIslandGroup(), Is, "Error in assignment prev -> next");
        }
    }

    /**
     * Tests whether the merging of blockTiles happens correctly
     */
    @Test
    public void MergingOfBlockTiles(){

        Herbalist herb = new Herbalist(parameters, advancedParameters);
        List<IslandGroup> selectedList = new ArrayList<>();
        selectedList.add(group.get(1));
        parameters.setSelectedIslands(selectedList);
        herb.blockIsland(); // I add a block to island 1
        herb.blockIsland(); // I add a second block to island 1
        selectedList.remove(group.get(1));
        selectedList.add(group.get(3));
        parameters.setSelectedIslands(selectedList);
        herb.blockIsland(); // I add a block to island 3

        group.get(1).build(TeamEnum.WHITE, game.getPlayers());
        group.get(2).build(TeamEnum.WHITE, game.getPlayers());
        group.get(3).build(TeamEnum.WHITE, game.getPlayers());

        try {
            group.get(2).mergeAdjacent(islandGroupId+1, group);
        }
        catch (UnmergeableException e){
            e.printStackTrace();
        }
        islandGroupId++;

        AdvancedIslandGroup mergedIsland = new AdvancedIslandGroup(advancedParameters);
        for(IslandGroup island : group){
            if(island.getIdGroup() == islandGroupId){
                mergedIsland = (AdvancedIslandGroup) island;
            }
        }

        AdvancedIslandGroup advancedIsland1 = (AdvancedIslandGroup) group.get(1);
        AdvancedIslandGroup advancedIsland3 = (AdvancedIslandGroup) group.get(3);

        List<BlockTile> tilesOf1and3 = advancedIsland1.getBlockTiles();
        tilesOf1and3.addAll(advancedIsland3.getBlockTiles());

        assertEquals(mergedIsland.getNumBlockTiles(), 3, "Wrong number of tiles in merged group");
        assertTrue(mergedIsland.getBlockTiles().containsAll(tilesOf1and3), "Different tiles in new islandGroup");

    }

    @Test
    public void EvaluateWithoutTowers(){
        Centaur centaur = new Centaur(parameters, advancedParameters);
        Player player0 = game.getPlayers().get(0);
        Player player1 = game.getPlayers().get(1);

        IslandGroup island = group.get(0);
        island.build(player0.getTeamColor(), game.getPlayers());
        island.addStudent(StudentEnum.RED);
        island.addStudent(StudentEnum.GREEN);
        parameters.setCurrentPlayer(player0);

        player0.getBoard().addToHall(StudentEnum.RED);
        player1.getBoard().addToHall(StudentEnum.GREEN);

        game.updateProfessor(StudentEnum.RED); //RED professor should now belong to player 0
        game.updateProfessor(StudentEnum.GREEN); //GREEN professor should now belong to player 1

        assertEquals(island.evaluateMostInfluential(), game.getPlayers().get(0).getTeamColor(), "problem before activating centaur");

        centaur.activateEffect();

        assertEquals(island.evaluateMostInfluential(), TeamEnum.NOTEAM, "problem after activating centaur");
    }

    @Test
    public void checkAdditionalInfluence(){
        Knight knight = new Knight(parameters, advancedParameters);
        Player player0 = game.getPlayers().get(0);
        Player player1 = game.getPlayers().get(1);

        IslandGroup island = group.get(0);
        island.build(player0.getTeamColor(), game.getPlayers());
        island.addStudent(StudentEnum.RED);
        island.addStudent(StudentEnum.GREEN);
        parameters.setCurrentPlayer(player0);

        player1.getBoard().addToHall(StudentEnum.RED);
        player1.getBoard().addToHall(StudentEnum.GREEN);

        game.updateProfessor(StudentEnum.RED);
        game.updateProfessor(StudentEnum.GREEN); //RED and GREEN professors should now belong to player 1
        // Now player0 has 1 influence from the tower, player1 has 2 influence from the red and green students

        assertEquals(island.evaluateMostInfluential(), player1.getTeamColor());

        knight.activateEffect(); // Now player0 has 3 ingluence, including the 2 bonus points from the character

        assertEquals(island.evaluateMostInfluential(), player0.getTeamColor());
    }

    @Test
    public void ignoredStudentTypeTest(){
        Fungalmancer fungalmancer = new Fungalmancer(parameters, advancedParameters);
        Player player0 = game.getPlayers().get(0);
        Player player1 = game.getPlayers().get(1);

        parameters.setCurrentPlayer(player0);

        IslandGroup island = group.get(0);

        island.addStudent(StudentEnum.RED);
        island.addStudent(StudentEnum.RED);
        island.addStudent(StudentEnum.RED);
        island.addStudent(StudentEnum.GREEN);
        island.addStudent(StudentEnum.GREEN);
        island.addStudent(StudentEnum.BLUE); // On the island: 3 Red, 2 Green, 1 Blue

        player0.getBoard().addToHall(StudentEnum.RED);
        player0.getBoard().addToHall(StudentEnum.BLUE);
        player1.getBoard().addToHall(StudentEnum.GREEN);

        game.updateProfessor(StudentEnum.RED);
        game.updateProfessor(StudentEnum.GREEN);
        game.updateProfessor(StudentEnum.BLUE);

        // Currently player0 wins 4 to 2
        assertEquals(island.evaluateMostInfluential(), player0.getTeamColor());

        advancedParameters.ignoreStudent(StudentEnum.RED);
        fungalmancer.activateEffect();

        //Now player0 should lose 1 to 2
        assertEquals(island.evaluateMostInfluential(), player1.getTeamColor());
    }
}


