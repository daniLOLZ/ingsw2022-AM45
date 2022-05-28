package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.GUIApplication;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    private static final double defaultIslandScale = 0.1, hoverZoom = 2.4;
    private static final double islandEdge = 160.0 / 0.4 * defaultIslandScale;
    private static final double islandWidth = islandEdge * 2;
    private static final double islandHeight = Math.sqrt(3) * islandEdge;
    private static final int firstIslandId = 1;
    private static final int differentIslandTiles = 3;

    public static void drawAdvancedIslandGroup(Group root, AdvancedIslandGroupBean data, Coord pos){

        List<Integer> islands = data.getIdIslands();
        List<Coord> positions = getIslandSlots(islands.size(), pos);

        List<Double> actualScales = new ArrayList<>();
        AtomicReference<List<List<ImageView>>> children = new AtomicReference<>();
        children.set(new ArrayList<>());
        for (int ignored : islands) {
            actualScales.add(defaultIslandScale);
            children.get().add(new ArrayList<>());
        }

        //draw islands
        Iterator<Coord> drawingPosition = positions.iterator();
        for (int id: islands) {

            AtomicReference<ImageView> islandView = new AtomicReference<>();
            ObservableList<Node> oldLayout = root.getChildren();
            Coord drawingSlot = drawingPosition.next();
            islandView.set(drawFromCenterInteractiveImage(root, fromIdToImage(id), drawingSlot, defaultIslandScale, null));
            /*addHoveringEffects(islandView.get(), drawingSlot, defaultIslandScale,
                    event -> {
                        islandView.get().toFront();
                        actualScales.set(id - firstIslandId, defaultIslandScale * hoverZoom);
                        //positions.set(id - firstIslandId, new Coord(islandView.get().getX(), islandView.get().getY()));
                        for (ImageView view:
                                children.get().get(id - firstIslandId)) {
                            root.getChildren().remove(view);
                        }
                        children.get().set(id - firstIslandId, drawIslandChildEntities(root, data, islands, positions, actualScales));
                        for (ImageView child : children.get().get(id - firstIslandId)) child.toFront();
                    },
                    event -> {
                        actualScales.set(id - firstIslandId, defaultIslandScale);
                        //positions.set(id - firstIslandId, getIslandSlots(islands.size(), pos).get(id - firstIslandId));
                        for (ImageView view:
                                children.get().get(id - firstIslandId)) {
                            root.getChildren().remove(view);
                        }
                        children.get().set(id - firstIslandId,drawIslandChildEntities(root, data, islands, positions, actualScales));
                    },

                    hoverZoom);*/


            children.get().set(id - firstIslandId ,drawIslandChildEntities(root, data, islands, positions, actualScales));
            }


    }

    public static void drawIslandGroup(Group root, IslandGroupBean data, Coord pos){

        AdvancedIslandGroupBean adaptedData = (AdvancedIslandGroupBean) data;
        adaptedData.setNumBlockTiles(0);

        drawAdvancedIslandGroup(root, adaptedData, pos);
    }

    private static List<ImageView> drawIslandChildEntities(Group root, AdvancedIslandGroupBean data, List<Integer> islands, List<Coord> positions, List<Double> actualScales){

        //draw towers

        List<ImageView> children = new ArrayList<>();

        Iterator<Coord> drawingPosition = positions.iterator();
        if (data.getTowersColor() != TeamEnum.NOTEAM)
            for (int id: islands){
                Coord drawingSlot = drawingPosition.next();
                children.add(drawFromCenterInteractiveImage(
                        root,
                        towers.get(data.getTowersColor().index),
                        new Coord(towerSlot.x * actualScales.get(id - firstIslandId), towerSlot.y * actualScales.get(id - firstIslandId)).pureSum(drawingSlot),
                        specialWoodenSize / towers.get(0).getHeight() * actualScales.get(id - firstIslandId),
                        null));
            }

        //draw mother nature
        if (data.isPresentMN()){
            int MNPosition = (islands.size() + 1) / 2 - 1;
            children.add(drawFromCenterInteractiveImage(
                    root,
                    motherNature,
                    new Coord(MNSlot.x * actualScales.get(MNPosition), MNSlot.y * actualScales.get(MNPosition)).pureSum(positions.get(MNPosition)),
                    specialWoodenSize / motherNature.getWidth() * actualScales.get(MNPosition),
                    null));
        }

        //draw students
        Coord studentSlot = GUIApplication.upLeftCorner;
        Iterator<Coord> studentSlotIterator = studentsOnIslandsSlots.iterator();

        for (int studentIndex = 0; studentIndex < data.getStudentsOnIsland().size(); studentIndex++) {

            StudentEnum studentToDraw = data.getStudentsOnIsland().get(studentIndex);

            if (studentIndex % islands.size() == 0) studentSlot = studentSlotIterator.next();
                children.add(StudentDrawer.drawStudent(
                        root,
                        studentToDraw,
                        new Coord(studentSlot.x * actualScales.get(studentIndex % positions.size()), studentSlot.y * actualScales.get(studentIndex % positions.size())).pureSum(positions.get(studentIndex % positions.size())),
                        defaultWoodenSize / StudentDrawer.getStudentSize() * actualScales.get(studentIndex % positions.size())));
        }


        for (int currBlockTile = 0; currBlockTile < data.getNumBlockTiles(); currBlockTile++) {

            children.add(drawFromCenterInteractiveImage(
                    root,
                    blockTile,
                    positions.get(currBlockTile % islands.size()).pureSumX((MNSlot.x + 15 * currBlockTile) * actualScales.get(currBlockTile % islands.size())).pureSumY((MNSlot.y + 20 - 15 * currBlockTile) * actualScales.get(currBlockTile % islands.size())),
                    specialWoodenSize / blockTile.getWidth() * actualScales.get(currBlockTile % islands.size()),
                    null));

        }

        return children;
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
