package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Herbalist extends CharacterCard {


    private int numBlockTiles;
    private final List<BlockTile> blockTiles;

    public Herbalist(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,5, parameters, advancedParameters);
        final int maxBlockTiles = 4;
        numBlockTiles = maxBlockTiles;
        blockTiles = new ArrayList<>();

        for(int tile= 0; tile < maxBlockTiles; tile++)
            blockTiles.add(new BlockTile());

        requirements = new Requirements(1,0,0,0);
    }

    /**
     *You can put one block Tile on a choosen Island
     * You need to call blockIsland with island to block
     */
    @Override
    public void activateEffect() {
        super.activateEffect();
    }

    /**
     * restore correct number of free tiles counting BlockTiles' isAssigned field as true
     * if there is at least one free tile put one Blocktile on chosenIsland, adding it to
     * chosenIsland's list of BlockTiles
     * set isAssigned true
     */
    public void blockIsland(){

        if(parameters.getSelectedIslands().isEmpty()){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedIslands");
            return;
        }

        //TAKE CHOSEN ISLAND FROM PARAMETERS UPDATED BY USER, SELECTED-ISLANDS
        //RETURN ISLAND GROUP SO I NEED CAST
        AdvancedIslandGroup chosenIsland = (AdvancedIslandGroup) parameters.getSelectedIslands().get().get(0);

        //RESTORE CORRECT NUMBER OF FREE TILES
        numBlockTiles = (int) blockTiles.stream().
                filter((tile)-> ! tile.isAssigned()).
                count();

        //NO MORE BLOCKTILES
        if(numBlockTiles <= 0)
            return;


        //ASSIGN BLOCKTILE TO CHOSEN ISLAND
        BlockTile tileToUse;
        for(BlockTile tile: blockTiles){
            if(!tile.isAssigned()){
                tileToUse = tile;
                chosenIsland.block(tileToUse);
                tile.setAssigned(true);
                return;
            }
        }
    }
}
