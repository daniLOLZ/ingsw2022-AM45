package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class StudentDrawer extends Drawer{

    private static final List<Image> students = new ArrayList<>();

    static {
        students.add(StudentEnum.GREEN.index, new Image("assets/wooden/students/greenStud.png"));
        students.add(StudentEnum.RED.index, new Image("assets/wooden/students/redStud.png"));
        students.add(StudentEnum.YELLOW.index, new Image("assets/wooden/students/yellowStud.png"));
        students.add(StudentEnum.PINK.index, new Image("assets/wooden/students/pinkStud.png"));
        students.add(StudentEnum.BLUE.index, new Image("assets/wooden/students/blueStud.png"));
    }

    private static final double studentSize = students.get(0).getWidth();

    /**
     * Draws a student of the desired color with the given parameters.
     * @param studentType The type (color) of the student to draw
     * @param pos The position in which the student should be drawn
     * @param scale The scaling factor to apply to the view
     * @param onClick The action to perform when the user clicks on the student
     * @return The view containing the drawn student
     */
    public static ImageView drawStudent(StudentEnum studentType, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        if (studentType == StudentEnum.NOSTUDENT) return new ImageView();

        return drawFromCenterInteractiveImage(students.get(studentType.index), pos, scale, onClick);
    }

    /**
     * Draws a student of the desired color with the given parameters.
     * The student draw will be noninteractive.
     * @param studentType The type (color) of the student to draw
     * @param pos The position in which the student should be drawn
     * @param scale The scaling factor to apply to the view
     * @return The view containing the drawn student
     */
    public static ImageView drawStudent(StudentEnum studentType, Coord pos, double scale){

        return drawStudent(studentType, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    /**
     * Gets the size of the student image.
     * @return The size of the student image
     */
    public static double getStudentSize(){
        return studentSize;
    }
}
