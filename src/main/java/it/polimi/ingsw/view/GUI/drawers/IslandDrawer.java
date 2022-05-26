package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.GUIApplication;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IslandDrawer extends Drawer{


    private static final List<Image> islandTiles = new ArrayList<>();
    static {
        islandTiles.add(new Image("assets/tiles/island1.png"));
        islandTiles.add(new Image("assets/tiles/island2.png"));
        islandTiles.add(new Image("assets/tiles/island3.png"));
    }

    private static final double defaultWoodenSize = 130;
    private static final double specialWoodenSize = 220;
    private static final double islandTileEdge = islandTiles.get(0).getWidth();

    private static final List<Coord> studentsOnIslandsSlots = new ArrayList<>();
    private static final Coord MNSlot = GUIApplication.upLeftCorner.pureSumX(145).pureSumY(158),
                               towerSlot = GUIApplication.upLeftCorner.pureSumX(268).pureSumY(-230);

    static {
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-186).pureSumY(-305));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-2).pureSumY(-293));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-293).pureSumY(-176));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-120).pureSumY(-177));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(48).pureSumY(-137));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-316).pureSumY(-25));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-163).pureSumY(-25));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-19).pureSumY(-7));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(188).pureSumY(-45));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-279).pureSumY(123));
        studentsOnIslandsSlots.add(GUIApplication.upLeftCorner.pureSumX(-103).pureSumY(123));

    }

    private static final List<Image> towers = new ArrayList<>();

    static {
        towers.add(TeamEnum.WHITE.index, new Image("assets/wooden/towers/white_tower.png"));
        towers.add(TeamEnum.BLACK.index, new Image("assets/wooden/towers/black_tower.png"));
        towers.add(TeamEnum.GREY.index, new Image("assets/wooden/towers/grey_tower.png"));
    }

    private final static Image motherNature = new Image("assets/wooden/mother_nature.png");

    private static final Image blockTile = new Image("assets/tiles/blockTile.png");

    private static final double defaultIslandScale = 0.2;
    private static final double islandEdge = 160.0 / 0.4 * defaultIslandScale;
    private static final double islandWidth = islandEdge * 2;
    private static final double islandHeight = Math.sqrt(3) * islandEdge;
    private static final int firstIslandId = 1;
    private static final int differentIslandTiles = 3;

    public static void drawIslandGroup(GraphicsContext gc, IslandGroupBean data, Coord pos){

        List<Integer> islands = data.getIdIslands();
        List<Coord> positions = getIslandSlots(islands.size(), pos);

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

        Coord studentSlot = GUIApplication.upLeftCorner;
        Iterator<Coord> studentSlotIterator = studentsOnIslandsSlots.iterator();

        for (int studentIndex = 0; studentIndex < data.getStudentsOnIsland().size(); studentIndex++) {

            StudentEnum studentToDraw = data.getStudentsOnIsland().get(studentIndex);

            if (studentIndex % islands.size() == 0) studentSlot = studentSlotIterator.next();

            Coord finalStudentSlot = studentSlot;
            int finalStudentIndex = studentIndex;
            Platform.runLater(() -> StudentDrawer.drawStudent(gc, studentToDraw, new Coord(finalStudentSlot.x * defaultIslandScale, finalStudentSlot.y * defaultIslandScale).pureSum(positions.get(finalStudentIndex % positions.size())), defaultWoodenSize / StudentDrawer.getStudentSize() * defaultIslandScale));
        }
    }

    public static void drawAdvancedIslandGroup(GraphicsContext gc, AdvancedIslandGroupBean data, Coord pos){

        drawIslandGroup(gc, data, pos);

        int numIslands = data.getIdIslands().size();
        List<Coord> positions = getIslandSlots(numIslands, pos);
        for (int currBlockTile = 0; currBlockTile < data.getNumBlockTiles(); currBlockTile++) {

            int finalCurrBlockTile = currBlockTile;
            Platform.runLater(() -> drawFromCenterImage(gc, blockTile, positions.get(finalCurrBlockTile % numIslands).pureSumX((MNSlot.x + 15 * finalCurrBlockTile) * defaultIslandScale).pureSumY((MNSlot.y + 20 - 15 * finalCurrBlockTile) * defaultIslandScale), specialWoodenSize / blockTile.getWidth() * defaultIslandScale));

        }

    }

    private static Image fromIdToImage(int id){
        return islandTiles.get((id - firstIslandId) % differentIslandTiles);
    }

    private static List<Coord> getIslandSlots(int numIsland, Coord centerPos) {

        List<Coord> positions = new ArrayList<>();

        switch (numIsland) {

            case 1 -> positions.add(centerPos);
            case 2 -> {
                positions.add(centerPos.pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumY(islandHeight / 2));
            }
            case 3 -> {
                positions.add(centerPos.pureSumX(-islandWidth / 4).pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(islandWidth / 2));
                positions.add(centerPos.pureSumX(-islandWidth / 4).pureSumY(islandHeight / 2));
            }
            case 4 -> {
                positions.add(centerPos.pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4));
                positions.add(centerPos.pureSumY(islandHeight / 2));
            }
            case 5 -> {
                positions.add(centerPos.pureSumX(-islandWidth / 4).pureSumY(-islandHeight));
                positions.add(centerPos.pureSumX(islandWidth / 2).pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(-islandWidth / 4));
                positions.add(centerPos.pureSumX(islandWidth / 2).pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumX(-islandWidth / 4).pureSumY(islandHeight));
            }
            case 6 -> {
                positions.add(centerPos.pureSumX(islandWidth / 2).pureSumY(-islandHeight));
                positions.add(centerPos.pureSumX(-islandWidth / 4).pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(-islandWidth));
                positions.add(centerPos.pureSumX(islandWidth / 2));
                positions.add(centerPos.pureSumX(-islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumX(islandWidth / 2).pureSumY(islandHeight));
            }
            case 7 -> {
                positions.add(centerPos.pureSumY(-islandHeight));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(-islandHeight / 2));
                positions.add(centerPos);
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumY(islandHeight));
            }
            case 8 -> {
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(-islandHeight));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(-islandHeight));
                positions.add(centerPos.pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4));
                positions.add(centerPos.pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight));
            }
            case 9 -> {
                positions.add(centerPos.pureSumY(-islandHeight));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 2));
                positions.add(centerPos);
                positions.add(centerPos.pureSumX(3 * islandWidth / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumY(islandHeight));
            }
            case 10 -> {
                positions.add(centerPos.pureSumY(-3 * islandHeight / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(-islandHeight));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(-islandHeight));
                positions.add(centerPos.pureSumY(-islandHeight / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4));
                positions.add(centerPos.pureSumY(islandHeight / 2));
                positions.add(centerPos.pureSumX(-3 * islandWidth / 4).pureSumY(islandHeight));
                positions.add(centerPos.pureSumX(3 * islandWidth / 4).pureSumY(islandHeight));
                positions.add(centerPos.pureSumY(3 * islandHeight / 2));
            }
        }

        return positions;
    }

}
