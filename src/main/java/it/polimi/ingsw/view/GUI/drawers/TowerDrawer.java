package it.polimi.ingsw.view.GUI.drawers;


import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class TowerDrawer extends Drawer {

    private static final List<Image> towers = new ArrayList<>();

    static {
        towers.add(new Image("assets/wooden/towers/white_tower.png"));
        towers.add(new Image("assets/wooden/towers/black_tower.png"));
        towers.add(new Image("assets/wooden/towers/grey_tower.png"));
    }

    private static final double towerSize = towers.get(0).getWidth();
    private static final double towerBaseOffset = 200;

    /**
     * Draws a tower of the given color at the given position with the given scaling factor.
     *
     * @param color The Team color of the tower to draw
     * @param pos   The position in which the tower should be drawn
     * @param scale The scaling factor to apply to the view
     * @return The view containing rhe drawn tower
     */
    public static ImageView drawTower(TeamEnum color, Coord pos, double scale) {

        if (color.equals(TeamEnum.NOTEAM)) return new ImageView();

        return drawFromCenterInteractiveImage(towers.get(color.index), pos.pureSumY(-towerBaseOffset * scale), scale, HandlingToolbox.NO_EFFECT);
    }

    /**
     * Gets the size of the tower image.
     * @return The size of the tower
     */
    public static double getTowerSize() {
        return towerSize;
    }

    /**
     * Gets the value of towerBaseOffset property.
     * @return The distance between the center of the tower image and the base of the tower
     */
    public static double getTowerBaseOffset() {
        return towerBaseOffset;
    }
}