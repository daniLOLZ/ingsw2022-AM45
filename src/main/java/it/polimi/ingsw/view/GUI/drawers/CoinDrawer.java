package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CoinDrawer extends Drawer{

    private static final Image coin = new Image("assets/coin.png");
    private static final double coinSize = coin.getWidth();

    /**
     * Draws a coin in the given position with the given scaling factor.
     * @param pos The position in which the coin should be drawn
     * @param scale The scaling factor to apply to the view
     * @return The view containing the drawn coin
     */
    public static ImageView drawCoin(Coord pos, double scale){

       return drawFromCenterInteractiveImage(coin, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    /**
     * Gets the coin image size.
     * @return The coin image size
     */
    public static double getCoinSize() {
        return coinSize;
    }
}
