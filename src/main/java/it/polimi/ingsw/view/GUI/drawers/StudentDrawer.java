package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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

    public static void drawStudent(GraphicsContext gc, StudentEnum studentType, Coord pos, double scale){

        drawFromCenterImage(gc, students.get(studentType.index), pos, scale);
    }

    public static double getStudentSize(){
        return studentSize;
    }
}
