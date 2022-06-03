package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class BlockTileDrawer extends Drawer{

    private static final Image blockTile = new Image("assets/tiles/blockTile.png");

    private static final double blockTileSize = blockTile.getWidth();

    public static ImageView drawBlockTile(Coord pos, double scale){

        return drawFromCenterInteractiveImage(blockTile, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    public static double getBlockTileSize() {
        return blockTileSize;
    }
}
