package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MotherNatureDrawer extends Drawer{

    private final static Image motherNature = new Image("assets/wooden/mother_nature.png");

    private final static double motherNatureSize = motherNature.getWidth();

    /**
     * Draws mother nature at the given position with the given scaling factor.
     * @param pos The position in which mother nature should be drawn
     * @param scale The scaling factor to apply to the view
     * @return The view containing mother nature
     */
    public static ImageView drawMotherNature(Coord pos, double scale){
        return drawFromCenterInteractiveImage(motherNature, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    /**
     * Gets mother nature image size
     * @return Mother nature image size
     */
    public static double getMotherNatureSize() {
        return motherNatureSize;
    }
}
