package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

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
    private static final double playerBoxWidth = 700, playerBoxHeight = 420;
    private static final double
            assistantWidth = 100 / defaultBoardScale,
            assistantHeight = assistantWidth * AssistantDrawer.getAssistantHeight() / AssistantDrawer.getAssistantWidth(),
            assistantLabelHeight = 160;

    public static void drawBoard(Group root, PlayerBean data, Coord pos, double scale, int rotation){

        AtomicReference<Double> actualBoardScale = new AtomicReference<>(defaultBoardScale * scale);
        Coord actualPos = pos;

        //draw the board
        ImageView boardView = drawFromCenterInteractiveImage(root, board, pos,scale * defaultBoardScale, null);
        boardView.getTransforms().add(new Rotate(90 * rotation, actualPos.x, actualPos.y));

        /*addHoveringEffects(boardView, pos, defaultBoardScale,
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
                hoverZoom);*/

        drawBoardChildEntities(root, data, actualPos, actualBoardScale, rotation);
    }

    public static void drawBoard(Group root, PlayerBean data, Coord pos, double scale){
        drawBoard(root, data, pos, scale, Coord.NO_ROTATION);
    }

    public static void drawBoard(Group root, PlayerBean data, Coord pos){
        drawBoard(root, data, pos, REAL_SIZE, Coord.NO_ROTATION);
    }

    private static void drawBoardChildEntities(Group root, PlayerBean data, Coord actualPos, AtomicReference<Double> actualBoardScale, int rotation){

        //draw player info
        Rectangle playerBox = new Rectangle();
        Text playerInfo = new Text();
        root.getChildren().addAll(playerBox, playerInfo);
        Coord boxLocation =
                actualPos
                .pureSumX((-board.getWidth()/2 - playerBoxWidth) * actualBoardScale.get())
                .pureSumY(-board.getHeight()/2 * actualBoardScale.get());

        Coord actualBoxLocation = boxLocation.pureRotate(actualPos, rotation);

        playerBox.setFill(Color.WHITE);
        playerBox.setX(actualBoxLocation.x);
        playerBox.setY(actualBoxLocation.y);
        playerBox.setWidth(playerBoxWidth * actualBoardScale.get());
        playerBox.setHeight(playerBoxHeight * actualBoardScale.get());
        playerBox.getTransforms().add(new Rotate(90 * rotation, actualBoxLocation.x, actualBoxLocation.y));

        boxLocation.moveY(playerBoxHeight / 2 * actualBoardScale.get());
        actualBoxLocation = boxLocation.pureRotate(actualPos, rotation);

        playerInfo.setTextAlignment(TextAlignment.CENTER);
        playerInfo.setFont(Font.font(playerInfo.getFont().getName(), playerInfo.getFont().getSize() * actualBoardScale.get() / defaultBoardScale));
        playerInfo.setX(actualBoxLocation.x);
        playerInfo.setY(actualBoxLocation.y);
        playerInfo.setWrappingWidth(playerBoxWidth * actualBoardScale.get());
        playerInfo.getTransforms().add(new Rotate(90 * rotation, actualBoxLocation.x, actualBoxLocation.y));
        playerInfo.setText(data.getPlayerId().name + "\n" + data.getNickname());

        //draw last assistant played
        Rectangle assistantLabel = new Rectangle();
        Text assistantText = new Text("Assistant played");
        root.getChildren().addAll(assistantLabel, assistantText);
        Coord assistantSlot =
                actualPos
                .pureSumX(board.getWidth() * actualBoardScale.get() / 2)
                .pureSumY(-board.getHeight() * actualBoardScale.get() / 2);

        Coord labelPos = assistantSlot.pureRotate(actualPos, rotation);

        assistantLabel.setFill(Color.WHITE);
        assistantLabel.setX(labelPos.x);
        assistantLabel.setY(labelPos.y);
        assistantLabel.setWidth(assistantWidth * actualBoardScale.get());
        assistantLabel.setHeight(assistantLabelHeight * actualBoardScale.get());
        assistantLabel.getTransforms().add(new Rotate(90 * rotation, labelPos.x, labelPos.y));

        assistantSlot.moveY(assistantLabelHeight / 2 * actualBoardScale.get());
        labelPos = assistantSlot.pureSumY(assistantLabelHeight / 4 * actualBoardScale.get()).pureRotate(actualPos, rotation);
        //TODO doesn't work (for some reason)
        assistantText.setTextAlignment(TextAlignment.CENTER);
        assistantText.setFont(Font.font(assistantText.getFont().getName(), assistantText.getFont().getSize() * actualBoardScale.get() / defaultBoardScale));
        assistantText.setX(labelPos.x);
        assistantText.setY(labelPos.y);
        assistantText.setWrappingWidth(assistantWidth * actualBoardScale.get());
        //assistantText.getTransforms().add(new Rotate(90 * rotation, labelPos.x, labelPos.y));

        assistantSlot.moveX(assistantWidth / 2 * actualBoardScale.get());
        assistantSlot.moveY(assistantHeight / 2 * actualBoardScale.get() + assistantLabelHeight / 2 * actualBoardScale.get());
        assistantSlot.rotate(actualPos, rotation);

        //TODO change id into last played assistant once the feature is available
        ImageView assistantView = AssistantDrawer.drawAssistant(root, 5, assistantSlot, assistantWidth / AssistantDrawer.getAssistantWidth() * actualBoardScale.get());

        assistantText.getTransforms().add(new Rotate(90 * rotation, assistantSlot.x, assistantSlot.y));

        //draw students at entrance
        Iterator<Coord> entranceSlot = atEntranceSlots.iterator();

        int studentIndex = 0;

        for (StudentEnum entranceStudent:
                data.getStudentsAtEntrance()) {

            Coord studentPos = entranceSlot.next();
            studentPos =
                    actualPos
                    .pureSumX(studentPos.x * actualBoardScale.get())
                    .pureSumY(studentPos.y * actualBoardScale.get())
                    .pureRotate(actualPos, rotation);
            Coord finalStudentPos = studentPos;
            int finalStudentIndex = studentIndex;
            StudentDrawer.drawStudent(
                    root,
                    entranceStudent,
                    finalStudentPos,
                    woodenSize / StudentDrawer.getStudentSize() * actualBoardScale.get(),
                    event -> System.out.println("Clicked on student #" + finalStudentIndex));
            studentIndex++;
        }

        //draw students in the dining hall
        Iterator<Integer> studentsAtTable = data.getStudentsPerTable().iterator();

        for (int table = 0; table < StudentEnum.getNumStudentTypes(); table++) {
            int students = studentsAtTable.next();
            for (int student = 0; student < students; student++) {
                Coord diningSeat =
                        actualPos
                        .pureSumX(firstSeat.get(table).pureSumX(student * seatStep).x * actualBoardScale.get())
                        .pureSumY(firstSeat.get(table).pureSumX(student * seatStep).y * actualBoardScale.get())
                        .pureRotate(actualPos, rotation);
                StudentDrawer.drawStudent(
                        root,
                        StudentEnum.getColorById(table),
                        diningSeat,
                        woodenSize / StudentDrawer.getStudentSize() * actualBoardScale.get());
            }
        }

        //draw professors
        for (StudentEnum prof:
                data.getProfessors()) {
            Coord profSeat =
                    actualPos
                    .pureSumX(profSeats.get(prof.index).x * actualBoardScale.get())
                    .pureSumY(profSeats.get(prof.index).y * actualBoardScale.get())
                    .pureRotate(actualPos, rotation);
            ProfessorDrawer.drawProfessor(root, prof, profSeat, woodenSize / ProfessorDrawer.getProfessorSize() * actualBoardScale.get());
        }

        //draw towers
        Iterator<Coord> towerSlot = towerSlots.iterator();
        for (int tower = 0; tower < data.getNumTowers(); tower++) {
            Coord slot = towerSlot.next();
            slot =
                    actualPos
                    .pureSumX(slot.x * actualBoardScale.get())
                    .pureSumY(slot.y * actualBoardScale.get())
                    .pureRotate(actualPos, rotation);
            Coord finalSlot = slot;
            TowerDrawer.drawTower(root, data.getTowerColor(), finalSlot, towerSize / TowerDrawer.getTowerSize() * actualBoardScale.get());
        }
    }
}
