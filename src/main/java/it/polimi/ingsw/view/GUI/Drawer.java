package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Drawer {

    public static final double WINDOW_WIDTH = 1520, WINDOW_HEIGHT = 780;

    public static final double
            left    = 0,
            right   = WINDOW_WIDTH,
            up      = 0,
            down    = WINDOW_HEIGHT,
            centerX = WINDOW_WIDTH/2,
            centerY = WINDOW_HEIGHT/2;


    public static final Coord
                        upLeftCorner    = new Coord(left,up),
                        upRightCorner   = new Coord(right,up),
                        upCenter        = new Coord(centerX,up),
                        centerRight     = new Coord(right,centerY),
                        centerLeft      = new Coord(left,centerY),
                        center          = new Coord(centerX,centerY),
                        downLeftCorner  = new Coord(left,down),
                        downRightCorner = new Coord(right,down),
                        downCenter      = new Coord(centerX,down);

    private static final List<Image> islandTiles = new ArrayList<>();
    static {
        islandTiles.add(new Image("assets/tiles/island1.png"));
        islandTiles.add(new Image("assets/tiles/island2.png"));
        islandTiles.add(new Image("assets/tiles/island3.png"));
    }

    private static final double REAL_SIZE = 1, defaultWoodenSize = 130, specialWoodenSize = 220, islandTileEdge = islandTiles.get(0).getWidth();

    private static final List<Coord> studentsOnIslandsSlots = new ArrayList<>();
    private static final Coord MNSlot = upLeftCorner.pureSumX(145).pureSumY(158),
                               towerSlot = upLeftCorner.pureSumX(268).pureSumY(-230);

    static {
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-186).pureSumY(-305));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-2).pureSumY(-293));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-293).pureSumY(-176));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-120).pureSumY(-177));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(48).pureSumY(-137));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-316).pureSumY(-25));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-163).pureSumY(-25));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-19).pureSumY(-7));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(188).pureSumY(-45));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-279).pureSumY(123));
        studentsOnIslandsSlots.add(upLeftCorner.pureSumX(-103).pureSumY(123));

    }

    private static final List<Image> towers = new ArrayList<>();

    static {
        towers.add(TeamEnum.WHITE.index, new Image("assets/wooden/towers/white_tower.png"));
        towers.add(TeamEnum.BLACK.index, new Image("assets/wooden/towers/black_tower.png"));
        towers.add(TeamEnum.GREY.index, new Image("assets/wooden/towers/grey_tower.png"));
    }

    private final static Image motherNature = new Image("assets/wooden/mother_nature.png");

    private static final List<Image> students = new ArrayList<>();

    static {
        students.add(StudentEnum.GREEN.index, new Image("assets/wooden/students/greenStud.png"));
        students.add(StudentEnum.RED.index, new Image("assets/wooden/students/redStud.png"));
        students.add(StudentEnum.YELLOW.index, new Image("assets/wooden/students/yellowStud.png"));
        students.add(StudentEnum.PINK.index, new Image("assets/wooden/students/pinkStud.png"));
        students.add(StudentEnum.BLUE.index, new Image("assets/wooden/students/blueStud.png"));
    }

    private static final double defaultIslandScale = 0.2;
    private static final double islandEdge = 160.0 / 0.4 * defaultIslandScale;
    private static final double islandWidth = islandEdge * 2;
    private static final double islandHeight = Math.sqrt(3) * islandEdge;
    private static final int firstIslandId = 1;
    private static final int differentIslandTiles = 3;

    /**
     * Draws an image scaled by the given scaling factor.
     * If alignment coordinates are inside the scene, the drawn image is always totally inside the scene (unless it's too big)
     * @param graphicsContext The graphics context required to draw the image
     * @param image The image to draw
     * @param alignment The point the image should be placed
     * @param scale The scaling factor applied to the image
     */
    public static void drawImage (GraphicsContext graphicsContext, Image image, Coord alignment, double scale){

        double imageWidth = image.getWidth()*scale, imageHeight = image.getHeight()*scale;

        double windowWidth = graphicsContext.getCanvas().getWidth();
        double windowHeight = graphicsContext.getCanvas().getHeight();

        Coord pos = new Coord(alignment.x/windowWidth, alignment.y/windowHeight),
                anchor = new Coord(pos.x * imageWidth, pos.y * imageHeight);

        Platform.runLater(() -> graphicsContext.drawImage(image, alignment.x - anchor.x, alignment.y - anchor.y, imageWidth, imageHeight));
    }

    public static void drawImage (GraphicsContext graphicsContext, Image image, Coord alignment){
        drawImage(graphicsContext, image, alignment, REAL_SIZE);
    }

    /**
     * Draws an image scaled by the given scaling factor, positioning its center at the give coordinates.
     * @param graphicsContext The graphics context required to draw the image
     * @param image The image to draw
     * @param pos The point the image's center should be placed
     * @param scale The scaling factor applied to the image
     */
    public static void drawFromCenterImage (GraphicsContext graphicsContext, Image image, Coord pos, double scale){

        double imageWidth = image.getWidth()*scale, imageHeight = image.getHeight()*scale;
        Coord imageCenter = new Coord(imageWidth/2, imageHeight/2);

        Platform.runLater(() -> graphicsContext.drawImage(image,pos.x - imageCenter.x,pos.y - imageCenter.y, imageWidth, imageHeight));
    }

    public static void drawFromCenterImage (GraphicsContext graphicsContext, Image image, Coord pos){
        drawFromCenterImage(graphicsContext, image, pos, REAL_SIZE);
    }

    public static void drawIslandGroup(GraphicsContext gc, IslandGroupBean data, Coord pos){

        List<Integer> islands = data.getIdIslands();
        List<Coord> positions = new ArrayList<>();

        switch (islands.size()) {

            case 1 -> positions.add(pos);
            case 2 -> {
                positions.add(pos.pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumY(islandHeight / 2));
            }
            case 3 -> {
                positions.add(pos.pureSumX(- islandWidth / 4).pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(islandWidth / 2));
                positions.add(pos.pureSumX(- islandWidth / 4).pureSumY(islandHeight /2));
            }
            case 4 -> {
                positions.add(pos.pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(- 3 * islandWidth / 4));
                positions.add(pos.pureSumX(3 * islandWidth / 4));
                positions.add(pos.pureSumY(islandHeight / 2));
            }
            case 5 -> {
                positions.add(pos.pureSumX(- islandWidth / 4).pureSumY(- islandHeight));
                positions.add(pos.pureSumX(islandWidth / 2).pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(- islandWidth / 4));
                positions.add(pos.pureSumX(islandWidth / 2).pureSumY(islandHeight / 2));
                positions.add(pos.pureSumX(- islandWidth / 4).pureSumY(islandHeight));
            }
            case 6 -> {
                positions.add(pos.pureSumX(islandWidth / 2).pureSumY(- islandHeight));
                positions.add(pos.pureSumX(- islandWidth / 4).pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(- islandWidth));
                positions.add(pos.pureSumX(islandWidth / 2));
                positions.add(pos.pureSumX(- islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(pos.pureSumX(islandWidth / 2).pureSumY(islandHeight));
            }
            case 7 -> {
                positions.add(pos.pureSumY(- islandHeight));
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(- islandHeight / 2));
                positions.add(pos);
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(pos.pureSumY(islandHeight));
            }
            case 8 -> {
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(- islandHeight));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(- islandHeight));
                positions.add(pos.pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(-3 * islandWidth / 4));
                positions.add(pos.pureSumX(3 * islandWidth / 4));
                positions.add(pos.pureSumY(islandHeight / 2));
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight));
            }
            case 9 -> {
                positions.add(pos.pureSumY(- islandHeight));
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(-3 * islandWidth / 2));
                positions.add(pos);
                positions.add(pos.pureSumX(3 * islandWidth / 2));
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(pos.pureSumY(islandHeight));
            }
            case 10 -> {
                positions.add(pos.pureSumY(- 3 * islandHeight / 2));
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(- islandHeight));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(- islandHeight));
                positions.add(pos.pureSumY(- islandHeight / 2));
                positions.add(pos.pureSumX(-3 * islandWidth / 4));
                positions.add(pos.pureSumX(3 * islandWidth / 4));
                positions.add(pos.pureSumY(islandHeight / 2));
                positions.add(pos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight));
                positions.add(pos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight));
                positions.add(pos.pureSumY(3 * islandHeight / 2));
            }
        }

        Iterator<Coord> drawingPosition = positions.iterator();
        for (int id: islands) {
            Coord drawingSlot = drawingPosition.next();
            Platform.runLater(() -> drawFromCenterImage(gc, fromIdToImage(id), drawingSlot, defaultIslandScale));

            if (data.getTowersColor() != TeamEnum.NOTEAM)
                Platform.runLater(() -> drawFromCenterImage(gc,
                                                            towers.get(data.getTowersColor().index),
                                                            new Coord(towerSlot.x * defaultIslandScale, towerSlot.y * defaultIslandScale).pureSum(drawingSlot),
                                                            specialWoodenSize / towers.get(0).getHeight() * defaultIslandScale));

            }

        if (data.isPresentMN()){
            int MNPosition = (islands.size() + 1) / 2 - 1;
            Platform.runLater(() -> drawFromCenterImage(gc, motherNature, new Coord(MNSlot.x * defaultIslandScale, MNSlot.y * defaultIslandScale).pureSum(positions.get(MNPosition)), specialWoodenSize / motherNature.getWidth() * defaultIslandScale));
        }

        Coord studentSlot = upLeftCorner;
        Iterator<Coord> studentSlotIterator = studentsOnIslandsSlots.iterator();

        for (int studentIndex = 0; studentIndex < data.getStudentsOnIsland().size(); studentIndex++) {

            StudentEnum studentToDraw = data.getStudentsOnIsland().get(studentIndex);

            if (studentIndex % islands.size() == 0) studentSlot = studentSlotIterator.next();

            Coord finalStudentSlot = studentSlot;
            int finalStudentIndex = studentIndex;
            Platform.runLater(() -> drawFromCenterImage(gc, students.get(studentToDraw.index), new Coord(finalStudentSlot.x * defaultIslandScale, finalStudentSlot.y * defaultIslandScale).pureSum(positions.get(finalStudentIndex % positions.size())), defaultWoodenSize / students.get(0).getHeight() * defaultIslandScale));
        }

        //TODO draw block tile
    }

    private static Image fromIdToImage(int id){
        return islandTiles.get((id - firstIslandId) % differentIslandTiles);
    }

    //<editor-fold desc="Decorative Methods">

    public static void showMenuBackground(Region region){
        Image background = new Image("assets/background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT,0.5,true,Side.TOP,0.5,true),
                new BackgroundSize(WINDOW_WIDTH, WINDOW_WIDTH, false, false, false, false));

        region.setBackground(new Background(backgroundImage));
    }

    public static void drawLogo(GraphicsContext graphicsContext){
        Image profsLogo = new Image("assets/decorations/login_screen/professors.png");
        Image textLogo = new Image("assets/decorations/login_screen/eriantys_text_logo.png");
        drawImage(graphicsContext,
                profsLogo,
                new Coord(centerX, centerY * 0.1),
                0.6);
        drawImage(graphicsContext,
                textLogo,
                new Coord(centerX, centerY * 0.35),
                0.15);
    }

    public static void showDecorativeIslands(GraphicsContext graphicsContext){

        Image island1 = new Image("assets/tiles/island1.png");
        Image island2 = new Image("assets/tiles/island2.png");
        Image island3 = new Image("assets/tiles/island3.png");

        Image cloud = new Image("assets/tiles/cloud_card.png");

        double scale = 0.2;


        drawImage(graphicsContext, cloud, upLeftCorner, scale);
        drawImage(graphicsContext, island1, upLeftCorner, scale);
        drawImage(graphicsContext, cloud, upRightCorner, scale);
        drawImage(graphicsContext, island1, upRightCorner, scale);

        drawImage(graphicsContext, cloud, centerLeft, scale);
        drawImage(graphicsContext, island2, centerLeft, scale);
        drawImage(graphicsContext, cloud, centerRight, scale);
        drawImage(graphicsContext, island2, centerRight, scale);

        drawImage(graphicsContext, cloud, downLeftCorner, scale);
        drawImage(graphicsContext, island3, downLeftCorner, scale);
        drawImage(graphicsContext, cloud, downRightCorner, scale);
        drawImage(graphicsContext, island3, downRightCorner, scale);
    }

    //</editor-fold>
}
