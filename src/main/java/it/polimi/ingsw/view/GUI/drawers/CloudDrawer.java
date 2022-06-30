package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.CloudHandlingToolbox;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
    private static final double hoverZoom = 2.4;

    /**
     * Draws a cloud with the given parameters.
     * @param data The Bean containing all relevant information about the cloud to draw
     * @param pos The position in which the cloud should be drawn
     * @param scale The scaling factor to apply to the view
     * @param onClick The action to perform when the user clicks on the cloud
     * @return The list of all drawn nodes
     */
    public static List<Node> drawCloud(CloudBean data, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        List<Node> toDraw = new ArrayList<>();

        List<StudentEnum> students = data.getStudents();
        List<Coord> slots = getStudentsSlot(students.size());

        List<EventHandler<MouseEvent>> entered = new ArrayList<>();
        List<EventHandler<MouseEvent>> exited = new ArrayList<>();


        //draw cloud
        EventHandler<MouseEvent> actualEffectOnClick;

        if (data.getStudents().isEmpty()) actualEffectOnClick = HandlingToolbox.NO_EFFECT;
        else actualEffectOnClick = onClick;

        ImageView cloudView = drawFromCenterInteractiveImage(clouds.get(data.getIdCloud()), pos, scale, actualEffectOnClick);
        toDraw.add(cloudView);

        //draw students
        int index = 0;
        for (StudentEnum student: students) {

            Coord studentSlot = slots.get(index);

            ImageView studentView = StudentDrawer.drawStudent(
                    student,
                    pos.pureSumX(studentSlot.x * scale).pureSumY(studentSlot.y * scale),
                    studentSize / StudentDrawer.getStudentSize() * scale,
                    actualEffectOnClick);

            toDraw.add(studentView);

            entered.add(getChildrenEnteredZoom(studentView, studentSlot, scale, hoverZoom, cloudView));
            exited.add(getChildrenExitedZoom(studentView, studentSlot, scale, hoverZoom, cloudView));

            index++;
        }

        EventHandler<MouseEvent> zoomChildren = event -> {
            for (EventHandler<MouseEvent> handler: entered) {
                handler.handle(event);
            }
        };

        EventHandler<MouseEvent> shrinkChildren = event -> {
            for (EventHandler<MouseEvent> handler: exited) {
                handler.handle(event);
            }
        };

        addHoveringEffects(cloudView, pos, scale, zoomChildren, shrinkChildren, hoverZoom, toDraw.subList(1, toDraw.size()));

        return toDraw;
    }

    /**
     * Returns a list containing all the coordinates in which the students on the cloud should be placed (relative to the center of the cloud and not scaled).
     * @param amount The amount of slots to generate
     * @return The list of all the slots available for the students
     */
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

    /**
     * Gets the size of the cloud image.
     * @return The size of the cloud image
     */
    public static double getCloudSize(){
        return cloudSize;
    }
}
