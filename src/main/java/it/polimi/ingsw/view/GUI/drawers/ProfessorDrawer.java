package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    /**
     * Draws the specified professor at the given position with the given scaling factor.
     * @param studentType The type (color) of professor to draw
     * @param pos The position in which the professor should be drawn
     * @param scale The scaling factor to apply to the view
     * @return The view containing the drawn professor
     */
    public static ImageView drawProfessor(StudentEnum studentType, Coord pos, double scale){

        return drawFromCenterInteractiveImage(professors.get(studentType.index), pos, scale, null);
    }

    /**
     * Gets the professor image size.
     * @return The professor image size.
     */
    public static double getProfessorSize(){
        return professorSize;
    }
}

