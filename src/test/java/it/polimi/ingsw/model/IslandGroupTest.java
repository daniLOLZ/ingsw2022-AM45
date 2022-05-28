package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.game.GameHelper;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.game.SimpleGame;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.islands.UnmergeableException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;



public class IslandGroupTest {


    SimpleGame game;
    ParameterHandler parameters;
    List<IslandGroup> group;
    int numIslands = 12;
    int islandGroupId = 0;

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
            game = new SimpleGame(2,selectedWizards,teamColors,nicknames, virtualView);
            game.initializeGame();
        }
        catch (IncorrectPlayersException e){
            e.printStackTrace();
        }

        parameters = game.getParameters();

        // group = IslandGroup.getCollectionOfIslandGroup(game.getParameters(),islandGroupId, numIslands);
        group = game.getIslandGroups();
        islandGroupId += numIslands;

    }

    /**
     * Does a round trip of the island groups and checks that all pointers are consistent.
     */
    @Test
    public void properInitializationOfPointers(){

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
     * Tests merging exception with newly initialized island groups with no towers build
     * (run in @Before)
     */
    @Test
    public void UnmergeableExceptionNoTeamTest(){
        islandGroupId += 1;
        assertThrows(UnmergeableException.class, () -> group.get(1).mergeAdjacent(islandGroupId, game.getIslandGroups()));
    }

    /**
     * Tests merging exception when no neighboring towers of the same color are present
     * (run in @Before)
     */
    @Test
    public void UnmergeableExceptionNoNeighborsTest(){
        group.get(1).build(TeamEnum.WHITE, game.getPlayers());

        islandGroupId += 1;
        assertThrows(UnmergeableException.class, () -> group.get(1).mergeAdjacent(islandGroupId, game.getIslandGroups()));
    }


    /**
     * Tests whether a simple merge between three islandGroups succeeds
     * and keeps the internal state of the group consistent
     */
    @Test
    public void mergeTest(){
        IslandGroup g1 = group.get(1);
        IslandGroup g2 = group.get(2);
        IslandGroup g3 = group.get(3);

        g1.addStudent(StudentEnum.PINK);
        g2.addStudent(StudentEnum.RED);
        g3.addStudent(StudentEnum.GREEN);

        IslandGroup gMerged = null;

        List<Player> players = new ArrayList<>();
        players.add(new Player(PlayerEnum.PLAYER1, "pp1", TeamEnum.WHITE, true, parameters));
        players.add(new Player(PlayerEnum.PLAYER2, "pp2", TeamEnum.BLACK, true, parameters));

        g1.build(TeamEnum.WHITE, players);
        g2.build(TeamEnum.WHITE, players);
        g3.build(TeamEnum.WHITE, players);

        try {
            group.get(2).mergeAdjacent(islandGroupId+1, game.getIslandGroups());
            islandGroupId++;
        }
        catch(UnmergeableException e){
            e.printStackTrace();
        }

        for(IslandGroup is : group){
            if(is.getIdGroup() == islandGroupId){
                gMerged = is;
                break;
            }
        }

        List<StudentEnum> totalStudents = new ArrayList<>();
        totalStudents.addAll(g1.getStudents());
        totalStudents.addAll(g2.getStudents());
        totalStudents.addAll(g3.getStudents());
        List<Island> totalIslands = new ArrayList<>();
        totalIslands.addAll(g1.getIslands());
        totalIslands.addAll(g2.getIslands());
        totalIslands.addAll(g3.getIslands());

        assertTrue(totalStudents.containsAll(gMerged.getStudents()), "Students anomaly");
        assertTrue(totalIslands.containsAll(gMerged.getIslands()), "Islands anomaly");
        assertEquals(gMerged.getNextIslandGroup(), g3.getNextIslandGroup());
        assertEquals(gMerged.getPrevIslandGroup(), g1.getPrevIslandGroup());
        assertEquals(gMerged.getTowerColor(), g1.getTowerColor());
    }

    /**
     * Tests a simple evaluation of the most influential team
     */
    @Test
    public void evaluationTest(){
        while(! group.get(0).getStudents().isEmpty())
            group.get(0).getStudents().remove(0);
        group.get(0).addStudent(StudentEnum.GREEN);
        group.get(0).addStudent(StudentEnum.GREEN);
        group.get(0).addStudent(StudentEnum.RED);
        parameters.getProfessors().set(StudentEnum.GREEN.index, PlayerEnum.PLAYER1); // Player 1 controls the green professor
        parameters.getProfessors().set(StudentEnum.RED.index, PlayerEnum.PLAYER2); // Player 2 controls the red professor
        //Player 1's team should have more influence
        assertTrue(group.get(0).evaluateMostInfluential().equals(
                GameHelper.getPlayerById(game.getPlayers(), PlayerEnum.PLAYER1).getTeamColor())
        );

    }
}
