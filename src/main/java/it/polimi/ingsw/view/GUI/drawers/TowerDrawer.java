package it.polimi.ingsw.view.GUI.drawers;


import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class TowerDrawer extends Drawer{

    private static final List<Image> towers = new ArrayList<>();

    static {
        towers.add(new Image("assets/wooden/towers/white_tower.png"));
        towers.add(new Image("assets/wooden/towers/black_tower.png"));
        towers.add(new Image("assets/wooden/towers/grey_tower.png"));
    }

    private static final double towerSize = towers.get(0).getWidth();

    public static void drawTower(Group root, TeamEnum color, Coord pos, double scale){

        drawFromCenterInteractiveImage(root, towers.get(color.index), pos, scale, null);
    }

    public static double getTowerSize(){
        return towerSize;
    }
}
