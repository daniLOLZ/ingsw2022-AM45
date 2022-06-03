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

    public static ImageView drawStudent(StudentEnum studentType, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        return drawFromCenterInteractiveImage(students.get(studentType.index), pos, scale, onClick);
    }

    public static ImageView drawStudent(StudentEnum studentType, Coord pos, double scale){

        return drawFromCenterInteractiveImage(students.get(studentType.index), pos, scale, HandlingToolbox.NO_EFFECT);
    }

    public static double getStudentSize(){
        return studentSize;
    }
}
