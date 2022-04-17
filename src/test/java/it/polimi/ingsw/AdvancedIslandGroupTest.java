package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.FactoryCharacterCard;
import it.polimi.ingsw.model.characterCards.Herbalist;
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

    @BeforeEach
    public void initializeIslandGroup(){

        try{
            game = new AdvancedGame(2, numCoins, numCharacters);
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
}
