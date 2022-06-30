package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class BlockTileDrawer extends Drawer{

    private static final Image blockTile = new Image("assets/tiles/blockTile.png");

    private static final double blockTileSize = blockTile.getWidth();

    /**
     * Draws a block tile in the given position with the given scale
     * @param pos The position where the block tile must be drawn
     * @param scale The scale to apply to the view
     * @return The view containing the drawn block tile
     */
    public static ImageView drawBlockTile(Coord pos, double scale){

        return drawFromCenterInteractiveImage(blockTile, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    /**
     * Gets the size of a block tile image
     * @return The size of the block tile image
     */
    public static double getBlockTileSize() {
        return blockTileSize;
    }
}
