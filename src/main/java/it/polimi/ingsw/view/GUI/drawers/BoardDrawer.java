package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    private static final double defaultBoardScale = 0.15, hoverZoom = 1.4, woodenSize = 152, towerSize = 500;

    public static void drawBoard(Group root, PlayerBean data, Coord pos){

        AtomicReference<Double> actualBoardScale = new AtomicReference<>(defaultBoardScale);
        Coord actualPos = pos;

        //draw the board
        ImageView boardView = drawFromCenterInteractiveImage(root, board, pos,defaultBoardScale, null);

        addHoveringEffects(boardView, pos, defaultBoardScale,
                event -> {
            actualBoardScale.set(defaultBoardScale * hoverZoom);
            actualPos.x = boardView.getX();
            actualPos.y = boardView.getY();
            drawBoardChildEntities(root, data, actualPos, actualBoardScale);
        },
                event -> {
            actualBoardScale.set(defaultBoardScale);
            actualPos.x = pos.x;
            actualPos.y = pos.y;
            drawBoardChildEntities(root, data, actualPos, actualBoardScale);
        },
                hoverZoom);

        drawBoardChildEntities(root, data, actualPos, actualBoardScale);
    }

    private static void drawBoardChildEntities(Group root, PlayerBean data, Coord actualPos, AtomicReference<Double> actualBoardScale){
        //draw students at entrance
        Iterator<Coord> entranceSlot = atEntranceSlots.iterator();

        int studentIndex = 0;

        for (StudentEnum entranceStudent:
                data.getStudentsAtEntrance()) {

            Coord studentPos = entranceSlot.next();

            Platform.runLater(() -> StudentDrawer.drawStudent(
                    root,
                    entranceStudent,
                    actualPos.pureSumX(studentPos.x * actualBoardScale.get()).pureSumY(studentPos.y * actualBoardScale.get()),
                    woodenSize / StudentDrawer.getStudentSize() * actualBoardScale.get(),
                    event -> System.out.println("Clicked on student # " + studentIndex)));
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
                Platform.runLater(() -> StudentDrawer.drawStudent(
                        root,
                        StudentEnum.getColorById(finalTable),
                        actualPos.pureSumX(tablesSeats.get(finalTable1).get(finalStudent).x * actualBoardScale.get()).pureSumY(tablesSeats.get(finalTable1).get(finalStudent).y * actualBoardScale.get()),
                        woodenSize / StudentDrawer.getStudentSize() * actualBoardScale.get()));
            }
        }

        //draw professors
        for (StudentEnum prof:
                data.getProfessors()) {
            Platform.runLater(() -> ProfessorDrawer.drawProfessor(root, prof, actualPos.pureSumX(profSeats.get(prof.index).x * actualBoardScale.get()).pureSumY(profSeats.get(prof.index).y * actualBoardScale.get()), woodenSize / ProfessorDrawer.getProfessorSize() * actualBoardScale.get()));
        }

        //draw towers
        Iterator<Coord> towerSlot = towerSlots.iterator();
        for (int tower = 0; tower < data.getNumTowers(); tower++) {
            Coord slot = towerSlot.next();
            Platform.runLater(() -> TowerDrawer.drawTower(root, data.getTowerColor(), actualPos.pureSumX(slot.x * actualBoardScale.get()).pureSumY(slot.y * actualBoardScale.get()), towerSize / TowerDrawer.getTowerSize() * actualBoardScale.get()));
        }
    }
}
