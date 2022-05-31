package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CoinDrawer extends Drawer{

    private static final Image coin = new Image("assets/coin.png");
    private static final double coinSize = coin.getWidth();

    public static ImageView drawCoin(Group root, Coord pos, double scale){

       return drawFromCenterInteractiveImage(root, coin, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    public static double getCoinSize() {
        return coinSize;
    }
}
