package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedIslandGroup;
import it.polimi.ingsw.model.BlockTile;

import java.util.ArrayList;
import java.util.List;

public class Herbalist extends CharacterCard {

    private final int maxBlockTiles = 4;
    private int numBlockTiles;
    private List<BlockTile> blockTiles;

    public Herbalist(){
        super(2,5);
        numBlockTiles = maxBlockTiles;
        blockTiles = new ArrayList<>();

        for(int tile= 0; tile < maxBlockTiles; tile++)
            blockTiles.add(new BlockTile());
    }

    /**
     *
     * @param game
     *
     */
    @Override
    public void activateEffect(AdvancedGame game) {
        super.activateEffect(game);
    }

    public void blockIsland(AdvancedIslandGroup choosenIsland){

        //RESTORE CORRECT NUMBER OF FREE TILES
        numBlockTiles = (int) blockTiles.stream().
                filter((tile)-> ! tile.isAssigned()).
                count();

        //NO MORE BLOCKTILES
        if(numBlockTiles <= 0)
            return;


        //ASSIGN BLOCKTILE TO CHOOSEN ISLAND
        BlockTile tileToUse;
        for(BlockTile tile: blockTiles){
            if(!tile.isAssigned()){
                tileToUse = tile;
                choosenIsland.block(tileToUse);
                return;
            }
        }
    }
}
