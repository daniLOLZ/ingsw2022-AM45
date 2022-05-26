package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static it.polimi.ingsw.view.GUI.GUIApplication.upLeftCorner;

public class BoardDrawer extends Drawer{

    private static final Image board = new Image("assets/board/with_borders.png");

    private static final List<Coord> atEntranceSlots = new ArrayList<>();

    static {
        atEntranceSlots.add(upLeftCorner.pureSumX(-1295).pureSumY(-474));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1493).pureSumY(-235));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1295).pureSumY(-235));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1493).pureSumY(0));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1295).pureSumY(0));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1493).pureSumY(241));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1295).pureSumY(241));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1493).pureSumY(478));
        atEntranceSlots.add(upLeftCorner.pureSumX(-1295).pureSumY(478));
    }

    private static final List<Coord> firstSeat = new ArrayList<>();

    static {
        firstSeat.add(upLeftCorner.pureSumX(-985).pureSumY(-474));
        firstSeat.add(upLeftCorner.pureSumX(-985).pureSumY(-235));
        firstSeat.add(upLeftCorner.pureSumX(-985).pureSumY(0));
        firstSeat.add(upLeftCorner.pureSumX(-985).pureSumY(241));
        firstSeat.add(upLeftCorner.pureSumX(-985).pureSumY(478));
    }

    private static final double seatStep = 160;

    private static final List<Coord> profSeats = new ArrayList<>();

    static {
        profSeats.add(upLeftCorner.pureSumX(766).pureSumY(-474));
        profSeats.add(upLeftCorner.pureSumX(766).pureSumY(-235));
        profSeats.add(upLeftCorner.pureSumX(766).pureSumY(0));
        profSeats.add(upLeftCorner.pureSumX(766).pureSumY(241));
        profSeats.add(upLeftCorner.pureSumX(766).pureSumY(478));
    }

    private static final List<Coord> towerSlots = new ArrayList<>();

    static {
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(-477));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(-477));
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(-238));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(-238));
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(1));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(1));
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(240));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(240));
    }

    private static final double defaultBoardScale = 0.15, woodenSize = 152, towerSize = 500;

    public static void drawBoard(GraphicsContext gc, PlayerBean data, Coord pos){

        //draw the board
        drawFromCenterImage(gc, board, pos,defaultBoardScale);

        //draw students at entrance
        Iterator<Coord> entranceSlot = atEntranceSlots.iterator();

        for (StudentEnum entranceStudent:
             data.getStudentsAtEntrance()) {

            Coord studentPos = entranceSlot.next();

            Platform.runLater(() -> StudentDrawer.drawStudent(gc, entranceStudent, pos.pureSumX(studentPos.x * defaultBoardScale).pureSumY(studentPos.y * defaultBoardScale), woodenSize / StudentDrawer.getStudentSize() * defaultBoardScale));
        }

        //draw students in the dining hall
        Iterator<Integer> studentsAtTable = data.getStudentsPerTable().iterator();
        List<List<Coord>> tablesSeats = new ArrayList<>();

        for (int table = 0; table < StudentEnum.getNumStudentTypes(); table++) {
            int finalTable = table;
            int students = studentsAtTable.next();
            List<Coord> tableSeats = new ArrayList<>();
            tablesSeats.add(tableSeats);
            for (int student = 0; student < students; student++) {
                tablesSeats.get(table).add(firstSeat.get(table).pureSumX(student * seatStep));
                int finalTable1 = table;
                int finalStudent = student;
                Platform.runLater(() -> StudentDrawer.drawStudent(gc, StudentEnum.getColorById(finalTable), pos.pureSumX(tablesSeats.get(finalTable1).get(finalStudent).x * defaultBoardScale).pureSumY(tablesSeats.get(finalTable1).get(finalStudent).y * defaultBoardScale), woodenSize / StudentDrawer.getStudentSize() * defaultBoardScale));
            }
        }

        //draw professors
        for (StudentEnum prof:
             data.getProfessors()) {
            Platform.runLater(() -> ProfessorDrawer.drawProfessor(gc, prof, pos.pureSumX(profSeats.get(prof.index).x * defaultBoardScale).pureSumY(profSeats.get(prof.index).y * defaultBoardScale), woodenSize / ProfessorDrawer.getProfessorSize() * defaultBoardScale));
        }

        //draw towers
        Iterator<Coord> towerSlot = towerSlots.iterator();
        for (int tower = 0; tower < data.getNumTowers(); tower++) {
            Coord slot = towerSlot.next();
            Platform.runLater(() -> TowerDrawer.drawTower(gc, data.getTowerColor(), pos.pureSumX(slot.x * defaultBoardScale).pureSumY(slot.y * defaultBoardScale), towerSize / TowerDrawer.getTowerSize() * defaultBoardScale));
        }
    }
}
