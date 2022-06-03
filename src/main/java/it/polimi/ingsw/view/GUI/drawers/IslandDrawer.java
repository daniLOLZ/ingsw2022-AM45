package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.GUIApplication;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
                               towerSlot = GUIApplication.upLeftCorner.pureSumX(268).pureSumY(-230);

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

    private static final int firstIslandId = 1;
    private static final int differentIslandTiles = 3;

    public static List<Node> drawIsland(int id, List<StudentEnum> students, TeamEnum towerColor, boolean isPresentMotherNature, int numBlockTile, Coord pos, double scale){

        List<Node> toDraw = new ArrayList<>();

        //draw island
        toDraw.add(drawFromCenterInteractiveImage(islands.get((id - firstIslandId) % differentIslandTiles), pos, scale, HandlingToolbox.NO_EFFECT));

        //draw students
        if (students.size() <= studentsOnIslandSlots.size()) {
            int studentIndex = 0;

            for (StudentEnum student:
                 students) {
                toDraw.add(
                        StudentDrawer.drawStudent(
                                student,
                                pos.pureSumX(studentsOnIslandSlots.get(studentIndex).x * scale).pureSumY(studentsOnIslandSlots.get(studentIndex).y * scale),
                                defaultWoodenSize / StudentDrawer.getStudentSize() * scale));
                studentIndex++;
            }
        }

        //draw tower
        toDraw.add(TowerDrawer.drawTower(towerColor, pos.pureSumX(towerSlot.x * scale).pureSumY(towerSlot.y * scale), specialWoodenSize / TowerDrawer.getTowerSize() * scale));

        //draw mother nature (if present)
        if (isPresentMotherNature) toDraw.add(MotherNatureDrawer.drawMotherNature(pos.pureSumX(MNSlot.x * scale).pureSumY(MNSlot.y), specialWoodenSize / MotherNatureDrawer.getMotherNatureSize() * scale));

        //draw block tiles
        for (int blockTile = 0; blockTile < numBlockTile; blockTile++) {
            toDraw.add(BlockTileDrawer.drawBlockTile(pos.pureSumX((MNSlot.x + 15 * blockTile) * scale).pureSumY((MNSlot.y + 20 + 15 * blockTile) * scale), specialWoodenSize /BlockTileDrawer.getBlockTileSize() * scale));
        }

        addHoveringEffects( (ImageView) toDraw.get(0), pos, scale, HandlingToolbox.NO_EFFECT, HandlingToolbox.NO_EFFECT, hoverZoom, toDraw.subList(1, toDraw.size()));

        return toDraw;
    }

    public static List<Node> drawIsland(int id, List<StudentEnum> students, TeamEnum towerColor, boolean isPresentMotherNature, Coord pos, double scale){
        return drawIsland(id, students, towerColor, isPresentMotherNature, 0, pos, scale);
    }

    public static double getIslandSize() {
        return islandSize;
    }
}

