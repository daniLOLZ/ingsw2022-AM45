package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class ProfessorDrawer extends Drawer{

    private static final List<Image> professors = new ArrayList<>();

    static {
        professors.add(StudentEnum.GREEN.index, new Image("assets/wooden/professors/greenProf.png"));
        professors.add(StudentEnum.RED.index, new Image("assets/wooden/professors/redProf.png"));
        professors.add(StudentEnum.YELLOW.index, new Image("assets/wooden/professors/yellowProf.png"));
        professors.add(StudentEnum.PINK.index, new Image("assets/wooden/professors/pinkProf.png"));
        professors.add(StudentEnum.BLUE.index, new Image("assets/wooden/professors/blueProf.png"));
    }

    private static final double professorSize = professors.get(0).getWidth();

    public static void drawProfessor(GraphicsContext gc, StudentEnum studentType, Coord pos, double scale){

        drawFromCenterImage(gc, professors.get(studentType.index), pos, scale);
    }

    public static double getProfessorSize(){
        return professorSize;
    }
}

