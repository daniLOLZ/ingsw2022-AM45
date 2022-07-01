package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.GUIApplication;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class IslandDrawer extends Drawer{

    private static final List<Image> islands = new ArrayList<>();
    static {
        islands.add(new Image("assets/tiles/island1.png"));
        islands.add(new Image("assets/tiles/island2.png"));
        islands.add(new Image("assets/tiles/island3.png"));
    }
    private static final double islandSize = islands.get(0).getWidth();

    private static final Coord MNSlot = GUIApplication.upLeftCorner.pureSumX(145).pureSumY(158),
                               towerSlot = GUIApplication.upLeftCorner.pureSumX(268).pureSumY(-30);

    private static final double blockTileOffset = 20, blockTileGap = 15;

    private static final double defaultWoodenSize = 130;
    private static final double specialWoodenSize = 220;

    private static final List<Coord> studentsOnIslandSlots = new ArrayList<>();

    static {
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-186).pureSumY(-305));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-2).pureSumY(-293));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-293).pureSumY(-176));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-120).pureSumY(-177));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(48).pureSumY(-137));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-316).pureSumY(-25));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-163).pureSumY(-25));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-19).pureSumY(-7));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(188).pureSumY(-45));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-279).pureSumY(123));
        studentsOnIslandSlots.add(GUIApplication.upLeftCorner.pureSumX(-103).pureSumY(123));

    }

    private static final double hoverZoom = 1.8;

    private static final int firstIslandId = 0;//todo check if this is correct
    private static final int differentIslandTiles = 3;

    /**
     * Draws an island with the given parameters.
     * @param id The id of the island to draw (will determine its appearance)
     * @param students The list of the students on the island
     * @param towerColor The color of the team controlling the island (NOTEAM if none is controlling it)
     * @param isPresentMotherNature true if mother nature shall be drawn on this island
     * @param numBlockTile The number of block tiles that should be drawn on this island
     * @param pos The position in which the island should be drawn
     * @param scale The scaling factor to apply to the view
     * @param onClick The action to perform when the player clicks on the island
     * @return The list of all drawn nodes
     */
    public static List<Node> drawIsland(int id, List<StudentEnum> students, TeamEnum towerColor, boolean isPresentMotherNature, int numBlockTile, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        List<Node> toDraw = new ArrayList<>();

        List<EventHandler<MouseEvent>> entered = new ArrayList<>();
        List<EventHandler<MouseEvent>> exited = new ArrayList<>();

        //draw island
        ImageView islandView = drawFromCenterInteractiveImage(islands.get((id - firstIslandId) % differentIslandTiles), pos, scale, onClick);
        toDraw.add(islandView);

        if (onClick == HandlingToolbox.NO_EFFECT) addLighting(islandView, Color.GRAY);

        //draw students
        if (students.size() <= studentsOnIslandSlots.size()) {
            int studentIndex = 0;

            for (StudentEnum student:
                 students) {

                ImageView studentView = StudentDrawer.drawStudent(
                        student,
                        pos.pureSumX(studentsOnIslandSlots.get(studentIndex % studentsOnIslandSlots.size()).x * scale).pureSumY((studentsOnIslandSlots.get(studentIndex % studentsOnIslandSlots.size()).y - defaultWoodenSize * scale / StudentDrawer.getStudentSize() / 3 * studentIndex) * scale),
                        defaultWoodenSize / StudentDrawer.getStudentSize() * scale,
                        onClick);

                toDraw.add(studentView);

                entered.add(getChildrenEnteredZoom(studentView, studentsOnIslandSlots.get(studentIndex), scale, hoverZoom, islandView));

                exited.add(getChildrenExitedZoom(studentView, studentsOnIslandSlots.get(studentIndex), scale, hoverZoom, islandView));

                studentIndex++;
            }
        }

        //draw tower
        ImageView towerView = TowerDrawer.drawTower(towerColor, pos.pureSumX(towerSlot.x * scale).pureSumY(towerSlot.y * scale), specialWoodenSize / TowerDrawer.getTowerSize() * scale);
        toDraw.add(towerView);

        towerView.setOnMouseClicked(onClick);

        entered.add(getChildrenEnteredZoom(towerView, towerSlot, scale, hoverZoom, islandView));
        exited.add(getChildrenExitedZoom(towerView, towerSlot, scale, hoverZoom, islandView));

        //draw block tiles

        List<Coord> blockTilesSlots = getBlockTileSlots(numBlockTile);

        for (Coord blockTileSlot : blockTilesSlots) {

            ImageView blockTileView = BlockTileDrawer.drawBlockTile(pos.pureSumX(blockTileSlot.x * scale).pureSumY(blockTileSlot.y * scale), specialWoodenSize / BlockTileDrawer.getBlockTileSize() * scale);
            toDraw.add(blockTileView);

            blockTileView.setOnMouseClicked(onClick);

            entered.add(getChildrenEnteredZoom(blockTileView, blockTileSlot, scale, hoverZoom, islandView));
            exited.add(getChildrenExitedZoom(blockTileView, blockTileSlot, scale, hoverZoom, islandView));
        }

        //draw mother nature (if present)
        if (isPresentMotherNature) {
            ImageView motherNatureView = MotherNatureDrawer.drawMotherNature(pos.pureSumX(MNSlot.x * scale).pureSumY(MNSlot.y * scale), specialWoodenSize / MotherNatureDrawer.getMotherNatureSize() * scale);
            toDraw.add(motherNatureView);

            motherNatureView.setOnMouseClicked(onClick);

            entered.add(getChildrenEnteredZoom(motherNatureView, MNSlot, scale, hoverZoom, islandView));
            exited.add(getChildrenExitedZoom(motherNatureView, MNSlot, scale, hoverZoom, islandView));
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

        addHoveringEffects( (ImageView) toDraw.get(0), pos, scale, zoomChildren, shrinkChildren, hoverZoom, toDraw.subList(1, toDraw.size()));

        return toDraw;
    }

    public static List<Node> drawIsland(int id, List<StudentEnum> students, TeamEnum towerColor, boolean isPresentMotherNature, Coord pos, double scale, EventHandler<MouseEvent> onClick){
        return drawIsland(id, students, towerColor, isPresentMotherNature, 0, pos, scale, onClick);
    }

    /**
     * Gets the size of the island image.
     * @return The size of the island image
     */
    public static double getIslandSize() {
        return islandSize;
    }

    /**
     * Returns a list containing the coordinates where the desired number of block tiles should be drawn.
     * @param amount The amount of block tiles to be drawn
     * @return The list of all the coordinates available for drawing the block tiles
     */
    private static List<Coord> getBlockTileSlots(int amount){

        Coord start = GUIApplication.upLeftCorner;

        List<Coord> slots = new ArrayList<>();

        for (int blockTile = 0; blockTile < amount; blockTile++) {

            slots.add(start.pureSumX(MNSlot.x + blockTileGap * blockTile).pureSumY(MNSlot.y + blockTileOffset + blockTileGap * blockTile));
        }

        return slots;
    }
}

