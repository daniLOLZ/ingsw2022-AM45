package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.beans.CharacterCardBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
import it.polimi.ingsw.model.islands.BlockTile;

import java.util.ArrayList;
import java.util.List;

public class Herbalist extends CharacterCard {
    private static final String name = "HERBALIST";
    private static final String description = "Place a No Entry tile on an Island\n" +
                                          "\t|\tof your choice. The first time Mother\n" +
                                          "\t|\tNature ends her movement there,\n" +
                                          "\t|\tput the No Entry tile back onto this\n" +
                                          "\t|\tcard, DO NOT calculate influence on that Island,\n" +
                                          "\t|\tor place any Towers";


    private int numBlockTiles;
    private final List<BlockTile> blockTiles;

    public Herbalist(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,5, parameters, advancedParameters, name, description);
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

                //RESTORE CORRECT NUMBER OF FREE TILES
                numBlockTiles = (int) blockTiles.stream().
                        filter((tileX)-> ! tileX.isAssigned()).
                        count();

                alert();

                return;
            }
        }


    }

    @Override
    public GameElementBean toBean() {
        CharacterCardBean bean = (CharacterCardBean) super.toBean();
        bean.setNumBlocks(numBlockTiles);
        return  bean;
    }
}
