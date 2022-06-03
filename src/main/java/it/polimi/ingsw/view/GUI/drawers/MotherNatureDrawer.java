package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MotherNatureDrawer extends Drawer{

    private final static Image motherNature = new Image("assets/wooden/mother_nature.png");

    private final static double motherNatureSize = motherNature.getWidth();

    public static ImageView drawMotherNature(Coord pos, double scale){
        return drawFromCenterInteractiveImage(motherNature, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    public static double getMotherNatureSize() {
        return motherNatureSize;
    }
}
