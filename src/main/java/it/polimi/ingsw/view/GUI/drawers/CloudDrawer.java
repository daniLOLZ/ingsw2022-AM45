package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.GUI.GUIApplication.upLeftCorner;

public class CloudDrawer extends Drawer{

    private static final List<Image> clouds = new ArrayList<>();

    static {
        clouds.add(new Image("assets/tiles/cloud_card_1.png"));
        clouds.add(new Image("assets/tiles/cloud_card_2.png"));
        clouds.add(new Image("assets/tiles/cloud_card_3.png"));
        clouds.add(new Image("assets/tiles/cloud_card_4.png"));
    }

    private static final double studentSize = 420;
    private static final double cloudSize = clouds.get(0).getWidth();

    public static List<Node> drawCloud(CloudBean data, Coord pos, double scale){

        List<Node> toDraw = new ArrayList<>();

        List<StudentEnum> students = data.getStudents();
        List<Coord> slots = getStudentsSlot(students.size());

        //draw cloud
        toDraw.add(drawFromCenterInteractiveImage(clouds.get(data.getIdCloud()), pos, scale, HandlingToolbox.NO_EFFECT));

        int index = 0;
        //draw students
        for (StudentEnum student: students) {
            toDraw.add(StudentDrawer.drawStudent(
                    student,
                    pos.pureSumX(slots.get(index).x * scale).pureSumY(slots.get(index).y * scale),
                    studentSize / StudentDrawer.getStudentSize() * scale));
            index++;
        }

        return toDraw;
    }

    private static List<Coord> getStudentsSlot(int amount){
        List<Coord> slots = new ArrayList<>();

        if (amount == 3){
            slots.add(upLeftCorner.pureSumX(-290).pureSumY(-52));
            slots.add(upLeftCorner.pureSumX(217).pureSumY(-215));
            slots.add(upLeftCorner.pureSumX(102).pureSumY(293));
        }

        else if (amount == 4){
            slots.add(upLeftCorner.pureSumX(-270).pureSumY(-274));
            slots.add(upLeftCorner.pureSumX(221).pureSumY(-197));
            slots.add(upLeftCorner.pureSumX(-244).pureSumY(208));
            slots.add(upLeftCorner.pureSumX(254).pureSumY(277));
        }

        return slots;
    }

    public static double getCloudSize(){
        return cloudSize;
    }
}
