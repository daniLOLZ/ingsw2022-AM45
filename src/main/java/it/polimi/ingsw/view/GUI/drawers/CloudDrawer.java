package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CloudDrawer extends Drawer{

    private static final List<Image> clouds = new ArrayList<>();

    static {
        clouds.add(new Image("assets/tiles/cloud_card_1.png"));
        clouds.add(new Image("assets/tiles/cloud_card_2.png"));
        clouds.add(new Image("assets/tiles/cloud_card_3.png"));
        clouds.add(new Image("assets/tiles/cloud_card_4.png"));
    }

    public static ImageView drawCloud(Group root, int id, Coord pos, double scale){
        //TODO
        return null;
    }

    private static List<Coord> getStudentsSlot(int amount, Coord pos){
        List<Coord> slots = new ArrayList<>();
        //TODO

        return slots;
    }
}
