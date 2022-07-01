package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.BoardHandlingToolbox;
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

import static it.polimi.ingsw.view.GUI.GUIApplication.*;

public class BoardDrawer extends Drawer{

    public static final int USER = 0, RIGHT = 1, TOP = 2, LEFT = 3;

    private static final double tableLength = 1623, tableWidth = 236;

    private static final Coord firstButtonPos = new Coord(-1080, -589);

    private static final List<Image> boards = new ArrayList<>();

    static {
        boards.add(new Image("assets/board/no_borders_user.png"));
        boards.add(new Image("assets/board/no_borders_left.png"));
        boards.add(new Image("assets/board/no_borders_top.png"));
        boards.add(new Image("assets/board/no_borders_right.png"));
    }

    private static final double boardWidth = boards.get(USER).getWidth(),
                                boardHeight = boards.get(USER).getHeight();

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
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(-277));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(-277));
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(-38));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(-38));
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(201));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(201));
        towerSlots.add(upLeftCorner.pureSumX(1136).pureSumY(440));
        towerSlots.add(upLeftCorner.pureSumX(1394).pureSumY(440));
    }

    private static final double hoverZoom = 1.4, woodenSize = 152, towerSize = 500;
    private static final double playerBoxWidth = 660, playerBoxHeight = 420, textSize = 1700;
    private static final double
            assistantWidth = 99 / 0.15,
            assistantHeight = assistantWidth * AssistantDrawer.getAssistantHeight() / AssistantDrawer.getAssistantWidth(),
            assistantLabelHeight = 160;

    private static final List<Coord> playerBoxSlots = new ArrayList<>();

    static {
        playerBoxSlots.add(upLeftCorner.pureSumX(-boardWidth / 2 - playerBoxWidth).pureSumY(-boardHeight / 2));
        playerBoxSlots.add(upLeftCorner.pureSumX(-boardHeight / 2).pureSumY(boardWidth / 2));
        playerBoxSlots.add(upLeftCorner.pureSumX(-boardWidth / 2 - playerBoxWidth).pureSumY(boardHeight / 2 - playerBoxHeight));
        playerBoxSlots.add(upLeftCorner.pureSumX(boardHeight / 2 - playerBoxWidth).pureSumY(-boardWidth / 2 - playerBoxHeight));
    }

    private static final List<Coord> lastAssistantPlayedSlots = new ArrayList<>();

    static {
        lastAssistantPlayedSlots.add(upLeftCorner.pureSumX(boardWidth / 2).pureSumY(-boardHeight / 2));
        lastAssistantPlayedSlots.add(upLeftCorner.pureSumX(-boardHeight / 2 - assistantWidth).pureSumY(-boardWidth / 2));
        lastAssistantPlayedSlots.add(lastAssistantPlayedSlots.get(USER));
        lastAssistantPlayedSlots.add(upLeftCorner.pureSumX(boardHeight / 2).pureSumY(-boardWidth / 2));
    }

    private static final double coinStep = 45, coinSize = 260;

    private static final List<Coord> firstCoinSlots = new ArrayList<>();

    static {
        firstCoinSlots.add(upLeftCorner.pureSumX(-boards.get(USER).getWidth() / 2 - playerBoxWidth / 2).pureSumY(-boards.get(USER).getHeight() / 2 + playerBoxHeight + coinStep + coinSize / 2));
        firstCoinSlots.add(upLeftCorner.pureSumX(-boards.get(RIGHT).getWidth() / 2 + playerBoxHeight + coinStep + coinSize / 2).pureSumY(-boards.get(RIGHT).getHeight() / 2 - playerBoxWidth / 2));
        firstCoinSlots.add(upLeftCorner.pureSumX(-boards.get(TOP).getWidth() / 2 - playerBoxWidth / 2).pureSumY(boards.get(TOP).getHeight() / 2 - playerBoxHeight - coinStep - coinSize / 2));
        firstCoinSlots.add(upLeftCorner.pureSumX(boards.get(LEFT).getWidth() / 2 - playerBoxHeight - coinStep - coinSize / 2).pureSumY(boards.get(RIGHT).getHeight() / 2 + playerBoxWidth / 2));
    }


    /**
     * Draws a Board with the given parameters.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param scale The scale to apply to the board view
     * @param orientation Determines where the player owning the board in drawing is placed around the imaginary table (also determines if the board is the user's one)
     * @param eventHandlers The Object containing all the Handlers to respond to the user actions
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos, double scale, int orientation, BoardHandlingToolbox eventHandlers){

        Image board = boards.get(orientation);

        int rotation;

        switch (orientation){
            case LEFT -> rotation = Coord.CLOCKWISE;
            case TOP -> rotation = Coord.UPSIDE_DOWN;
            case RIGHT -> rotation = Coord.COUNTERCLOCKWISE;
            default -> rotation = Coord.NO_ROTATION;
        }

        List<Node> toDraw = new ArrayList<>();

        List<EventHandler<MouseEvent>> entered = new ArrayList<>();
        List<EventHandler<MouseEvent>> exited = new ArrayList<>();

        AtomicReference<Double> actualBoardScale = new AtomicReference<>(scale);

        //draw the board
        ImageView boardView = drawFromCenterInteractiveImage(board, pos, actualBoardScale.get(), HandlingToolbox.NO_EFFECT);
        toDraw.add(boardView);

        //draw player info
        Rectangle playerBox = new Rectangle();
        Text playerInfo = new Text();

        toDraw.add(playerBox);
        toDraw.add(playerInfo);


        Coord boxSlot = playerBoxSlots.get(orientation);

        Coord boxLocation =
                pos
                        .pureSumX(boxSlot.x * actualBoardScale.get())
                        .pureSumY(boxSlot.y * actualBoardScale.get());


        playerBox.setFill(Color.WHITE);
        playerBox.setX(boxLocation.x);
        playerBox.setY(boxLocation.y);
        playerBox.setWidth(playerBoxWidth * actualBoardScale.get());
        playerBox.setHeight(playerBoxHeight * actualBoardScale.get());

        Coord finalBoxSlot = boxSlot.pureSumX(playerBoxWidth / 2).pureSumY(playerBoxHeight / 2);

        entered.add(getChildrenEnteredZoom(playerBox, finalBoxSlot, actualBoardScale.get(), hoverZoom, boardView));

        exited.add(getChildrenExitedZoom(playerBox, finalBoxSlot, actualBoardScale.get(), hoverZoom, boardView));


        Coord textSlot = boxSlot.pureSumY(playerBoxHeight / 2).pureSumY(- playerBoxHeight * 0.1).pureSumX(playerBoxWidth * 0.1);
        Coord textLocation = pos.pureSumX(textSlot.x * actualBoardScale.get()).pureSumY(textSlot.y * actualBoardScale.get());


        playerInfo.setFont(Font.font(playerInfo.getFont().getName(),actualBoardScale.get() * textSize / playerInfo.getFont().getSize()));
        playerInfo.setTextAlignment(TextAlignment.JUSTIFY);
        playerInfo.setX(textLocation.x);
        playerInfo.setY(textLocation.y);
        playerInfo.setWrappingWidth(playerBoxWidth * actualBoardScale.get());
        playerInfo.minHeight(playerBoxHeight * actualBoardScale.get());
        playerInfo.maxHeight(playerBoxHeight * actualBoardScale.get());
        playerInfo.setText(data.getPlayerId().name + "\n" + data.getNickname());

        entered.add(event -> {
            getChildrenEnteredZoom(playerInfo, new Coord(playerBox.getX() + playerBox.getWidth() / 10, playerBox.getY() + 3 * playerBox.getHeight() / 8) , actualBoardScale.get(), hoverZoom, boardView).handle(event);
        });

        exited.add(getChildrenExitedZoom(playerInfo, textLocation, actualBoardScale.get(), hoverZoom, boardView));

        //draw last assistant played
        Rectangle assistantLabel = new Rectangle();
        Text assistantText = new Text("Assistant played");

        toDraw.add(assistantLabel);
        toDraw.add(assistantText);

        Coord assistantLabelSlot = lastAssistantPlayedSlots.get(orientation);
        Coord assistantLabelLocation =
                pos
                        .pureSumX(assistantLabelSlot.x * scale)
                        .pureSumY(assistantLabelSlot.y * scale);

        assistantLabel.setFill(Color.WHITE);
        assistantLabel.setX(assistantLabelLocation.x);
        assistantLabel.setY(assistantLabelLocation.y);
        assistantLabel.setWidth(assistantWidth * actualBoardScale.get());
        assistantLabel.setHeight(assistantLabelHeight * actualBoardScale.get());

        Coord finalAssistantLabelSlot = assistantLabelSlot.pureSumX(assistantWidth / 2).pureSumY(assistantLabelHeight / 2);

        entered.add(getChildrenEnteredZoom(assistantLabel, finalAssistantLabelSlot, actualBoardScale.get(), hoverZoom, boardView));

        exited.add(getChildrenExitedZoom(assistantLabel, finalAssistantLabelSlot, actualBoardScale.get(), hoverZoom, boardView));


        Coord assistantTextSlot = assistantLabelSlot.pureSumY(assistantLabelHeight / 2);
        Coord assistantTextLocation = pos.pureSumX(assistantTextSlot.x * actualBoardScale.get()).pureSumY(assistantTextSlot.y * actualBoardScale.get());

        assistantText.setTextAlignment(TextAlignment.JUSTIFY);
        assistantText.setFont(Font.font(assistantText.getFont().getName(), textSize / 2 * actualBoardScale.get() / assistantText.getFont().getSize()));
        assistantText.setX(assistantTextLocation.x);
        assistantText.setY(assistantTextLocation.y);
        assistantText.setWrappingWidth(assistantWidth * actualBoardScale.get());
        assistantText.minHeight(assistantLabelHeight * actualBoardScale.get());
        assistantText.maxHeight(assistantLabelHeight * actualBoardScale.get());

        entered.add(event -> {
            getChildrenEnteredZoom(assistantText, new Coord(assistantLabel.getX() + assistantLabel.getWidth() / 10, assistantLabel.getY() + 3 * assistantLabel.getHeight() / 8), actualBoardScale.get(), hoverZoom, boardView).handle(event);
        });

        exited.add(getChildrenExitedZoom(assistantText, assistantTextLocation, actualBoardScale.get(), hoverZoom, boardView));

        Coord assistantSlot = assistantLabelSlot.pureSumX(assistantWidth / 2).pureSumY(assistantHeight / 2 + assistantLabelHeight);
        Coord assistantLocation = pos.pureSumX(assistantSlot.x * actualBoardScale.get()).pureSumY(assistantSlot.y * actualBoardScale.get());

        if (data.getAssistantPlayed() != null) {
            ImageView assistantView = AssistantDrawer.drawAssistant(data.getAssistantPlayed().id, assistantLocation, assistantWidth / AssistantDrawer.getAssistantWidth() * actualBoardScale.get());
            toDraw.add(assistantView);

            Coord finalAssistantSlot = assistantSlot.pureSumX(0);

            entered.add(getChildrenEnteredZoom(assistantView, finalAssistantSlot, actualBoardScale.get(), hoverZoom, boardView));

            exited.add(getChildrenExitedZoom(assistantView, finalAssistantSlot, actualBoardScale.get(), hoverZoom, boardView));
        }



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

            EventHandler<MouseEvent> onStudentClick = eventHandlers.getOnEntranceStudentClick(finalStudentIndex);

            ImageView entranceStudentView = StudentDrawer.drawStudent(
                    entranceStudent,
                    finalStudentPos,
                    woodenSize / StudentDrawer.getStudentSize() * actualBoardScale.get(),
                    onStudentClick);

            toDraw.add(entranceStudentView);
            studentIndex++;

            Coord finalStudentSlot = studentSlot.pureSumY(0);

            entered.add(getChildrenEnteredZoom(entranceStudentView, finalStudentSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
            exited.add(getChildrenExitedZoom(entranceStudentView, finalStudentSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
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

                entered.add(getChildrenEnteredZoom(diningView, finalDiningSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
                exited.add(getChildrenExitedZoom(diningView, finalDiningSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
            }
        }

        //make the tables clickable (for putting students in hall and color selection)
        if (orientation == USER){
            for (StudentEnum color : StudentEnum.getStudents()){

                Rectangle tableButton = new Rectangle();
                tableButton.setFill(Color.TRANSPARENT);
                tableButton.setStrokeWidth(tableButton.getStrokeWidth() * 3);
                tableButton.setStroke(Color.TRANSPARENT);

                Coord buttonSlot = firstButtonPos.pureSumY(tableWidth * color.index);

                tableButton.setX(pos.pureSumX(buttonSlot.x * actualBoardScale.get()).x);
                tableButton.setY(pos.pureSumY(buttonSlot.y * actualBoardScale.get()).y);

                tableButton.setWidth(tableLength * actualBoardScale.get());
                tableButton.setHeight(tableWidth * actualBoardScale.get());

                int table = color.index;

                EventHandler<MouseEvent> onTableClick = eventHandlers.getOnHallClick(table);

                tableButton.setOnMouseClicked(onTableClick);

                if (onTableClick == HandlingToolbox.NO_EFFECT) tableButton.setStroke(Color.WHITE);

                Coord finalButtonSlot = buttonSlot.pureSumX(tableLength / 2).pureSumY(tableWidth / 2);

                entered.add(getChildrenEnteredZoom(tableButton, finalButtonSlot, actualBoardScale.get(), hoverZoom, boardView));
                exited.add(getChildrenExitedZoom(tableButton, finalButtonSlot, actualBoardScale.get(), hoverZoom, boardView));

                toDraw.add(tableButton);

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

            entered.add(getChildrenEnteredZoom(profView, finalProfSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
            exited.add(getChildrenExitedZoom(profView, finalProfSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
        }

        //draw towers
        Iterator<Coord> currTowerSlot = towerSlots.iterator();

        List<ImageView> towerViews = new ArrayList<>();
        for (int tower = 0; tower < data.getNumTowers(); tower++) {
            Coord towerSlot = currTowerSlot.next();

            Coord towerLocation = pos.pureSumX(towerSlot.x * actualBoardScale.get()).pureSumY(towerSlot.y * actualBoardScale.get());
            Coord towerPos = towerLocation.pureRotate(pos, rotation);

            ImageView towerView = TowerDrawer.drawTower(data.getTowerColor(), towerPos, towerSize / TowerDrawer.getTowerSize() * actualBoardScale.get());
            towerViews.add(towerView);

            Coord finalTowerSlot;

            double towerBaseOffset = TowerDrawer.getTowerBaseOffset();

            switch (orientation){
                case USER -> finalTowerSlot = towerSlot.pureSumY(-towerBaseOffset);
                case RIGHT -> finalTowerSlot = towerSlot.pureSumX(-towerBaseOffset);
                case TOP -> finalTowerSlot = towerSlot.pureSumY(towerBaseOffset);
                case LEFT -> finalTowerSlot = towerSlot.pureSumX(towerBaseOffset);
                default -> finalTowerSlot = towerSlot.pureSumY(-towerBaseOffset);
            }

            finalTowerSlot.moveY(-towerBaseOffset);

            entered.add(getChildrenEnteredZoom(towerView, finalTowerSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
            exited.add(getChildrenExitedZoom(towerView, finalTowerSlot, actualBoardScale.get(), hoverZoom, boardView, rotation));
        }

        if (orientation == LEFT || orientation == TOP){
            List<ImageView> tempTowers = new ArrayList<>();

            for (int slot = towerViews.size() - 1; slot >= 0 ; slot--) {
                tempTowers.add(towerViews.get(slot));
            }

            towerViews = tempTowers;
        }

        toDraw.addAll(towerViews);

        //draw coins (if any)
        if (data.getNumCoins() != 0){

            int coinIndex;

            Coord coinSlot = firstCoinSlots.get(orientation).pureSumY(0);

            Coord moving = new Coord(0, coinStep).pureRotate(upLeftCorner, -45 * orientation);

            ImageView lastCoin = new ImageView();

            for (coinIndex = 0; coinIndex < data.getNumCoins(); coinIndex++) {

                coinSlot.moveCoord(moving);
                Coord coinLocation = pos.pureSumX(coinSlot.x * actualBoardScale.get()).pureSumY(coinSlot.y * actualBoardScale.get());
                ImageView coinView = CoinDrawer.drawCoin(coinLocation, coinSize / CoinDrawer.getCoinSize() * actualBoardScale.get());
                toDraw.add(coinView);

                Coord finalCoinSlot = coinSlot.pureSumY(0);

                entered.add(getChildrenEnteredZoom(coinView, finalCoinSlot, actualBoardScale.get(), hoverZoom, boardView));
                exited.add(getChildrenExitedZoom(coinView, finalCoinSlot, actualBoardScale.get(), hoverZoom, boardView));

                if (coinIndex == data.getNumCoins() - 1) lastCoin = coinView;
            }

            Text numCoins = new Text(String.valueOf(data.getNumCoins()));
            toDraw.add(numCoins);


            Coord numCoinsSlot = coinSlot.pureSumX(-coinSize * 1.1).pureSumY(coinSize * 0.9);
            Coord numCoinsLocation = pos.pureSumX(numCoinsSlot.x * actualBoardScale.get()).pureSumY(numCoinsSlot.y * actualBoardScale.get());
            Coord numCoinsPos = numCoinsLocation.pureRotate(pos, rotation);

            numCoins.setX(numCoinsPos.x);
            numCoins.setY(numCoinsPos.y);
            numCoins.setFont(Font.font("Sylfaen", textSize * actualBoardScale.get() / numCoins.getFont().getSize()));
            numCoins.setWrappingWidth(coinSize * actualBoardScale.get());
            numCoins.minHeight(coinSize * actualBoardScale.get());
            numCoins.maxHeight(coinSize * actualBoardScale.get());
            numCoins.setTextAlignment(TextAlignment.CENTER);

            Coord finalNumCoinSlot = numCoinsSlot.pureSumX(numCoins.getWrappingWidth() / 2).pureSumY(numCoins.minHeight(-1) / 2).pureRotate(pos, rotation);

            entered.add(getChildrenEnteredZoom(numCoins, new Coord(lastCoin.getX() - coinSize * 1.1 * scale, lastCoin.getY() - coinSize * 0.9 * scale), actualBoardScale.get(), hoverZoom, boardView));
            exited.add(getChildrenExitedZoom(numCoins, numCoinsPos, actualBoardScale.get(), hoverZoom, boardView));
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

        addHoveringEffects(boardView, pos, scale, zoomChildren, shrinkChildren, hoverZoom, toDraw.subList(1, toDraw.size()), rotation);

        return toDraw;
    }

    /**
     * Draws a Board with the given parameters.
     * This method only draw noninteractive nodes.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param scale The scale to apply to the board view
     * @param orientation Determines where the player owning the board in drawing is placed around the imaginary table (also determines if the board is the user's one)
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos, double scale, int orientation){
        return drawBoard(data, pos, scale, orientation, BoardHandlingToolbox.NONINTERACTIVE);
    }

    /**
     * Draws a Board with the given parameters.
     * This method implies that the board is the user's one.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param scale The scale to apply to the board view
     * @param eventHandlers The Object containing all the Handlers to respond to the user actions
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos, double scale, BoardHandlingToolbox eventHandlers){
        return drawBoard(data, pos, scale, USER, eventHandlers);
    }

    /**
     * Draws a Board with the given parameters.
     * This method implies that the board is the user's one and doesn't apply any scaling factor.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param eventHandlers The Object containing all the Handlers to respond to the user actions
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos, BoardHandlingToolbox eventHandlers){
        return drawBoard(data, pos, REAL_SIZE, eventHandlers);
    }

    /**
     * Draws a Board with the given parameters.
     * This method implies that the board is the user's one.
     * This method only draw noninteractive nodes.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos, double scale){
        return drawBoard(data, pos, scale, USER, BoardHandlingToolbox.NONINTERACTIVE);
    }

    /**
     * Draws a Board with the given parameters.
     * This method implies that the board is the user's one and doesn't apply any scaling factor.
     * This method only draw noninteractive nodes.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(AdvancedPlayerBean data, Coord pos){
        return drawBoard(data, pos, REAL_SIZE, BoardHandlingToolbox.NONINTERACTIVE);
    }

    /**
     * Draws a Board with the given parameters.
     * Version for not advanced games.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param scale The scale to apply to the board view
     * @param orientation Determines where the player owning the board in drawing is placed around the imaginary table (also determines if the board is the user's one)
     * @param eventHandlers The Object containing all the Handlers to respond to the user actions
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(PlayerBean data, Coord pos, double scale, int orientation, BoardHandlingToolbox eventHandlers){
        AdvancedPlayerBean adaptedData = AdvancedPlayerBean.getPromotedBean(data);
        return drawBoard(adaptedData, pos, scale, orientation, eventHandlers);
    }

    /**
     * Draws a Board with the given parameters.
     * Version for not advanced games.
     * This method implies that the board is the user's one.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param scale The scale to apply to the board view
     * @param eventHandlers The Object containing all the Handlers to respond to the user actions
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(PlayerBean data, Coord pos, double scale, BoardHandlingToolbox eventHandlers){
        return drawBoard(data, pos, scale, USER, eventHandlers);
    }

    /**
     * Draws a Board with the given parameters.
     * Version for not advanced games.
     * This method implies that the board is the user's one and doesn't apply any scaling factor.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param eventHandlers The Object containing all the Handlers to respond to the user actions
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(PlayerBean data, Coord pos, BoardHandlingToolbox eventHandlers){
        return drawBoard(data, pos, REAL_SIZE, eventHandlers);
    }

    /**
     * Draws a Board with the given parameters.
     * Version for not advanced games.
     * This method only draw noninteractive nodes.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param scale The scale to apply to the board view
     * @param orientation Determines where the player owning the board in drawing is placed around the imaginary table (also determines if the board is the user's one)
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(PlayerBean data, Coord pos, double scale, int orientation){
        AdvancedPlayerBean adaptedData = AdvancedPlayerBean.getPromotedBean(data);
        return drawBoard(adaptedData, pos, scale, orientation, BoardHandlingToolbox.NONINTERACTIVE);
    }

    /**
     * Draws a Board with the given parameters.
     * Version for not advanced games.
     * This method implies that the board is the user's one.
     * This method only draw noninteractive nodes.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @param scale The scale to apply to the board view
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(PlayerBean data, Coord pos, double scale){
        return drawBoard(data, pos, scale, USER, BoardHandlingToolbox.NONINTERACTIVE);
    }

    /**
     * Draws a Board with the given parameters.
     * Version for not advanced games.
     * This method implies that the board is the user's one and doesn't apply any scaling factor.
     * This method only draw noninteractive nodes.
     * @param data The Bean containing all relevant information about the board to draw
     * @param pos The position in which the board must be drawn
     * @return A list containing all the nodes that have been drawn
     */
    public static List<Node> drawBoard(PlayerBean data, Coord pos){
        return drawBoard(data, pos, REAL_SIZE, BoardHandlingToolbox.NONINTERACTIVE);
    }

    /**
     * Gets the board image width.
     * @return The board image width
     */
    public static double getBoardWidth() {
        return boardWidth;
    }
}
