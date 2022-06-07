package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

import static it.polimi.ingsw.view.GUI.GUIApplication.WINDOW_HEIGHT;
import static it.polimi.ingsw.view.GUI.GUIApplication.upLeftCorner;

public class BoardDrawer extends Drawer{

    private static final Image board = new Image("assets/board/with_borders.png");

    private static final double boardWidth = board.getWidth();

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

    private static final double hoverZoom = 1.4, woodenSize = 152, towerSize = 500;
    private static final double playerBoxWidth = 700, playerBoxHeight = 420, textSize = 1700;
    private static final double
            assistantWidth = 100 / 0.15,
            assistantHeight = assistantWidth * AssistantDrawer.getAssistantHeight() / AssistantDrawer.getAssistantWidth(),
            assistantLabelHeight = 160;

    private static final double coinStep = 45, coinSize = 260;

    private static final Coord firstCoinSlot = upLeftCorner.pureSumX(-board.getWidth() / 2 - playerBoxWidth / 2).pureSumY(-board.getHeight() / 2 + playerBoxHeight + coinStep + coinSize / 2);


    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos, double scale, int rotation){

        List<Node> toDraw = new ArrayList<>();

        List<EventHandler<MouseEvent>> entered = new ArrayList<>();
        List<EventHandler<MouseEvent>> exited = new ArrayList<>();

        AtomicReference<Double> actualBoardScale = new AtomicReference<>(scale);

        //draw the board
        ImageView boardView = drawFromCenterInteractiveImage(board, pos,actualBoardScale.get(), HandlingToolbox.NO_EFFECT);
        toDraw.add(boardView);
        boardView.getTransforms().add(new Rotate(90 * rotation, pos.x, pos.y));


        //draw player info
        Rectangle playerBox = new Rectangle();
        Text playerInfo = new Text();

        toDraw.add(playerBox);
        toDraw.add(playerInfo);


        Coord boxSlot = new Coord(-board.getWidth()/2 - playerBoxWidth, -board.getHeight()/2);

        Coord boxLocation =
                pos
                        .pureSumX(boxSlot.x * actualBoardScale.get())
                        .pureSumY(boxSlot.y * actualBoardScale.get());

        Coord actualBoxSlot = boxSlot.pureRotate(pos, rotation);
        Coord actualBoxLocation = boxLocation.pureRotate(pos, rotation);


        playerBox.setFill(Color.WHITE);
        playerBox.setX(actualBoxLocation.x);
        playerBox.setY(actualBoxLocation.y);
        playerBox.setWidth(playerBoxWidth * actualBoardScale.get());
        playerBox.setHeight(playerBoxHeight * actualBoardScale.get());
        playerBox.getTransforms().add(new Rotate(90 * rotation, actualBoxLocation.x, actualBoxLocation.y));

        Coord finalBoxSlot = boxSlot.pureSumX(playerBoxWidth / 2).pureSumY(playerBoxHeight / 2).pureRotate(pos, rotation);

        entered.add(getChildrenEnteredZoom(playerBox, finalBoxSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));

        exited.add(getChildrenExitedZoom(playerBox, finalBoxSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));


        Coord textSlot = boxSlot.pureSumY(playerBoxHeight / 2 - WINDOW_HEIGHT / 13);
        Coord textLocation = pos.pureSumX(textSlot.x * actualBoardScale.get()).pureSumY(textSlot.y * actualBoardScale.get());

        Coord actualTextSlot = textSlot.pureRotate(pos, rotation);
        Coord actualTextLocation = textLocation.pureRotate(pos, rotation);


        playerInfo.setTextAlignment(TextAlignment.CENTER);
        playerInfo.setFont(Font.font(playerInfo.getFont().getName(),actualBoardScale.get() * textSize / playerInfo.getFont().getSize()));
        playerInfo.setX(actualTextLocation.x);
        playerInfo.setY(actualTextLocation.y);
        playerInfo.setWrappingWidth(playerBoxWidth * actualBoardScale.get());
        playerInfo.minHeight(playerBoxHeight * actualBoardScale.get());
        playerInfo.maxHeight(playerBoxHeight * actualBoardScale.get());
        playerInfo.getTransforms().add(new Rotate(90 * rotation, actualTextLocation.x, actualTextLocation.y));
        playerInfo.setText(data.getPlayerId().name + "\n" + data.getNickname());

        Coord finalTextSlot = textSlot.pureSumX(playerBoxWidth / 2).pureSumY(playerBoxHeight / 2).pureRotate(pos, rotation);

        entered.add(getChildrenEnteredZoom(playerInfo, finalTextSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));

        exited.add(getChildrenExitedZoom(playerInfo, finalTextSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));

        //draw last assistant played
        Rectangle assistantLabel = new Rectangle();
        Text assistantText = new Text("Assistant played");

        toDraw.add(assistantLabel);
        toDraw.add(assistantText);

        Coord assistantLabelSlot = new Coord(board.getWidth() / 2, -board.getHeight() / 2);
        Coord assistantLabelLocation =
                pos
                        .pureSumX(assistantLabelSlot.x * actualBoardScale.get())
                        .pureSumY(assistantLabelSlot.y * actualBoardScale.get());

        Coord labelPos = assistantLabelLocation.pureRotate(pos, rotation);

        assistantLabel.setFill(Color.WHITE);
        assistantLabel.setX(labelPos.x);
        assistantLabel.setY(labelPos.y);
        assistantLabel.setWidth(assistantWidth * actualBoardScale.get());
        assistantLabel.setHeight(assistantLabelHeight * actualBoardScale.get());
        assistantLabel.getTransforms().add(new Rotate(90 * rotation, labelPos.x, labelPos.y));

        Coord finalAssistantLabelSlot = assistantLabelSlot.pureSumX(assistantWidth / 2).pureSumY(assistantLabelHeight / 2);

        entered.add(getChildrenEnteredZoom(assistantLabel, finalAssistantLabelSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));

        exited.add(getChildrenExitedZoom(assistantLabel, finalAssistantLabelSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));


        Coord assistantTextSlot = assistantLabelSlot.pureSumY(assistantLabelHeight / 2);
        Coord assistantTextLocation = pos.pureSumX(assistantTextSlot.x * actualBoardScale.get()).pureSumY(assistantTextSlot.y * actualBoardScale.get());
        Coord assistantTextPos = assistantTextLocation.pureRotate(pos, rotation);

        assistantText.setTextAlignment(TextAlignment.CENTER);
        assistantText.setFont(Font.font(assistantText.getFont().getName(), textSize / 2 * actualBoardScale.get() / assistantText.getFont().getSize()));
        assistantText.setX(assistantTextPos.x);
        assistantText.setY(assistantTextPos.y);
        assistantText.setWrappingWidth(assistantWidth * actualBoardScale.get());
        assistantText.minHeight(assistantLabelHeight * actualBoardScale.get());
        assistantText.maxHeight(assistantLabelHeight * actualBoardScale.get());
        assistantText.getTransforms().add(new Rotate(90 * rotation, assistantTextPos.x, assistantTextPos.y));

        Coord finalAssistantTextSlot = assistantTextSlot.pureSumX(assistantWidth / 2).pureSumY(assistantLabelHeight / 2);

        entered.add(getChildrenEnteredZoom(assistantText, finalAssistantTextSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));

        exited.add(getChildrenExitedZoom(assistantText, finalAssistantTextSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));

        Coord assistantSlot = assistantTextSlot.pureSumX(assistantWidth / 2).pureSumY(assistantHeight / 2 + assistantLabelHeight / 2);
        Coord assistantLocation = pos.pureSumX(assistantSlot.x * actualBoardScale.get()).pureSumY(assistantSlot.y * actualBoardScale.get());
        Coord assistantPos = assistantLocation.pureRotate(pos, rotation);

        //TODO change id into last played assistant once the feature is available
        ImageView assistantView = AssistantDrawer.drawAssistant(5, assistantPos, assistantWidth / AssistantDrawer.getAssistantWidth() * actualBoardScale.get());
        toDraw.add(assistantView);

        assistantView.getTransforms().add(new Rotate(90 * rotation, assistantPos.x, assistantPos.y));

        Coord finalAssistantSlot = assistantSlot.pureSumY(0);

        entered.add(getChildrenEnteredZoom(assistantView, finalAssistantSlot, actualBoardScale.get(), hoverZoom, boardView));

        exited.add(getChildrenExitedZoom(assistantView, finalAssistantSlot, actualBoardScale.get(), hoverZoom, boardView));

        //draw students at entrance
        Iterator<Coord> entranceSlot = atEntranceSlots.iterator();

        int studentIndex = 0;

        for (StudentEnum entranceStudent:
                data.getStudentsAtEntrance()) {

            Coord studentSlot = entranceSlot.next();
            Coord studentPos =
                    pos
                            .pureSumX(studentSlot.x * actualBoardScale.get())
                            .pureSumY(studentSlot.y * actualBoardScale.get())
                            .pureRotate(pos, rotation);
            Coord finalStudentPos = studentPos.pureSumY(0);
            int finalStudentIndex = studentIndex;

            ImageView entranceStudentView = StudentDrawer.drawStudent(
                    entranceStudent,
                    finalStudentPos,
                    woodenSize / StudentDrawer.getStudentSize() * actualBoardScale.get(),
                    event -> System.out.println("Clicked on student #" + finalStudentIndex));

            toDraw.add(entranceStudentView);
            studentIndex++;

            Coord finalStudentSlot = studentSlot.pureSumY(0);

            entered.add(getChildrenEnteredZoom(entranceStudentView, finalStudentSlot, actualBoardScale.get(), hoverZoom, boardView));
            exited.add(getChildrenExitedZoom(entranceStudentView, finalStudentSlot, actualBoardScale.get(), hoverZoom, boardView));
        }

        //draw students in the dining hall
        Iterator<Integer> studentsAtTable = data.getStudentsPerTable().iterator();

        for (int table = 0; table < StudentEnum.getNumStudentTypes(); table++) {
            int students = studentsAtTable.next();
            for (int student = 0; student < students; student++) {

                Coord diningSlot = firstSeat.get(table).pureSumX(student * seatStep);
                Coord diningLocation = pos.pureSumX(diningSlot.x * actualBoardScale.get()).pureSumY(diningSlot.y * actualBoardScale.get());
                Coord diningPos = diningLocation.pureRotate(pos, rotation);
                ImageView diningView = StudentDrawer.drawStudent(
                        StudentEnum.getColorById(table),
                        diningPos,
                        woodenSize / StudentDrawer.getStudentSize() * actualBoardScale.get());
                toDraw.add(diningView);

                Coord finalDiningSlot = diningSlot.pureSumY(0);

                entered.add(getChildrenEnteredZoom(diningView, finalDiningSlot, actualBoardScale.get(), hoverZoom, boardView));
                exited.add(getChildrenExitedZoom(diningView, finalDiningSlot, actualBoardScale.get(), hoverZoom, boardView));
            }
        }

        //draw professors
        for (StudentEnum prof:
                data.getProfessors()) {

            Coord profSlot = profSeats.get(prof.index);
            Coord profLocation = pos.pureSumX(profSlot.x * actualBoardScale.get()).pureSumY(profSlot.y * actualBoardScale.get());
            Coord profPos = profLocation.pureRotate(pos, rotation);

            ImageView profView = ProfessorDrawer.drawProfessor(prof, profPos, woodenSize / ProfessorDrawer.getProfessorSize() * actualBoardScale.get());
            toDraw.add(profView);

            Coord finalProfSlot = profSlot.pureSumY(0);

            entered.add(getChildrenEnteredZoom(profView, finalProfSlot, actualBoardScale.get(), hoverZoom, boardView));
            exited.add(getChildrenExitedZoom(profView, finalProfSlot, actualBoardScale.get(), hoverZoom, boardView));
        }

        //draw towers
        Iterator<Coord> currTowerSlot = towerSlots.iterator();
        for (int tower = 0; tower < data.getNumTowers(); tower++) {
            Coord towerSlot = currTowerSlot.next();
            Coord towerLocation = pos.pureSumX(towerSlot.x * actualBoardScale.get()).pureSumY(towerSlot.y * actualBoardScale.get());
            Coord towerPos = towerLocation.pureRotate(pos, rotation);

            ImageView towerView = TowerDrawer.drawTower(data.getTowerColor(), towerPos, towerSize / TowerDrawer.getTowerSize() * actualBoardScale.get());
            toDraw.add(towerView);

            Coord finalTowerSlot = towerSlot.pureSumY(0);

            entered.add(getChildrenEnteredZoom(towerView, finalTowerSlot, actualBoardScale.get(), hoverZoom, boardView));
            exited.add(getChildrenExitedZoom(towerView, finalTowerSlot, actualBoardScale.get(), hoverZoom, boardView));
        }

        //draw coins (if any)
        if (data.getNumCoins() != 0){

            int coinIndex;

            Coord coinSlot = firstCoinSlot.pureSumY(0);

            for (coinIndex = 0; coinIndex < data.getNumCoins(); coinIndex++) {

                coinSlot.moveY(coinStep);
                Coord coinLocation = pos.pureSumX(coinSlot.x * actualBoardScale.get()).pureSumY(coinSlot.y * actualBoardScale.get());
                Coord coinPos = coinLocation.pureRotate(pos, rotation);
                ImageView coinView = CoinDrawer.drawCoin(coinPos, coinSize / CoinDrawer.getCoinSize() * actualBoardScale.get());
                toDraw.add(coinView);
                coinView.getTransforms().add(new Rotate(90 * rotation, coinPos.x, coinPos.y));

                Coord finalCoinSlot = coinSlot.pureSumY(0);

                entered.add(getChildrenEnteredZoom(coinView, finalCoinSlot, actualBoardScale.get(), hoverZoom, boardView));
                exited.add(getChildrenExitedZoom(coinView, finalCoinSlot, actualBoardScale.get(), hoverZoom, boardView));
            }

            Text numCoins = new Text(String.valueOf(data.getNumCoins()));
            toDraw.add(numCoins);


            Coord numCoinsSlot = coinSlot.pureSumX(-coinSize * 1.1).pureSumY(coinSize * 0.9);
            Coord numCoinsLocation = pos.pureSumX(numCoinsSlot.x * actualBoardScale.get()).pureSumY(numCoinsSlot.y * actualBoardScale.get());
            Coord numCoinsPos = numCoinsLocation.pureRotate(pos, rotation);

            numCoins.setX(numCoinsPos.x);
            numCoins.setY(numCoinsPos.y);
            numCoins.setFont(Font.font("Sylfaen", textSize * actualBoardScale.get() / numCoins.getFont().getSize()));
            numCoins.setTextAlignment(TextAlignment.CENTER);

            entered.add(getChildrenEnteredZoom(numCoins, numCoinsSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
            exited.add(getChildrenExitedZoom(numCoins, numCoinsSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
        }

        EventHandler<MouseEvent> zoomChildren = event -> {
            for (EventHandler<MouseEvent> handler:
                 entered) {
                handler.handle(event);
            }
        };

        EventHandler<MouseEvent> shrinkChildren = event -> {
            for (EventHandler<MouseEvent> handler:
                    exited) {
                handler.handle(event);
            }
        };

        addHoveringEffects(boardView, pos, scale, zoomChildren, shrinkChildren, hoverZoom, toDraw.subList(1, toDraw.size()));

        return toDraw;
    }

    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos, double scale){
        return drawBoard(data, pos, scale, Coord.NO_ROTATION);
    }

    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos){
        return drawBoard(data, pos, REAL_SIZE);
    }

    public static List<Node> drawBoard(PlayerBean data, Coord pos, double scale, int rotation){
        AdvancedPlayerBean adaptedData = (AdvancedPlayerBean) data;
        ((AdvancedPlayerBean) data).setNumCoins(0);
        return drawBoard(adaptedData, pos, scale, rotation);
    }

    public static List<Node> drawBoard(PlayerBean data, Coord pos, double scale){
        return drawBoard(data, pos, scale, Coord.NO_ROTATION);
    }

    public static List<Node> drawBoard(PlayerBean data, Coord pos){
        return drawBoard(data, pos, REAL_SIZE);
    }

    public static double getBoardWidth() {
        return boardWidth;
    }
}
