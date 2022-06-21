package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.view.GUI.Coord;

import it.polimi.ingsw.view.GUI.handlingToolbox.IslandHandlingToolbox;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IslandGroupDrawer extends Drawer{


    private static final double islandEdge = 400;
    private static final double islandWidth = islandEdge * 2;
    private static final double islandHeight = Math.sqrt(3) * islandEdge;


    public static List<Node> drawIslandGroup(AdvancedIslandGroupBean data, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        List<Node> toDraw = new ArrayList<>();

        int numIslands = data.getIdIslands().size();

        List<Coord> islandsSlots = getIslandSlots(numIslands, pos, scale);

        Iterator<Coord> slot = islandsSlots.iterator();

        int numBlockTiles = data.getNumBlockTiles();
        int blockTilesPerIsland = getNumBlockTilePerIsland(numBlockTiles, numIslands);
        int blockTilesToDraw = blockTilesPerIsland;

        boolean drawMN;

        int index = 0;

        for (Integer island:
             data.getIdIslands()) {
            if (numBlockTiles < blockTilesPerIsland) blockTilesToDraw = numBlockTiles;

            drawMN = data.isPresentMN() &&
                    index == numIslands / 2 + numIslands % 2 - 1;

            List<StudentEnum> studentsToDraw = new ArrayList<>();

            for (int student = index; student < data.getStudentsOnIsland().size(); student += numIslands) {
                studentsToDraw.add(data.getStudentsOnIsland().get(student));
            }

            toDraw.addAll(IslandDrawer.drawIsland(
                    island,
                    studentsToDraw,
                    data.getTowersColor(),
                    drawMN,
                    blockTilesToDraw,
                    slot.next(),
                    scale,
                    onClick));

            numBlockTiles -= blockTilesToDraw;
            index++;
        }

        return toDraw;
    }

    public static List<Node> drawIslandGroup(IslandGroupBean data, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        AdvancedIslandGroupBean adaptedData = AdvancedIslandGroupBean.getPromotedBean(data);
        adaptedData.setNumBlockTiles(0);

        return drawIslandGroup(adaptedData, pos, scale, onClick);
    }

    private static List<Coord> getIslandSlots(int numIsland, Coord centerPos, double scale) {

        List<Coord> positions = new ArrayList<>();

        double islandWidth = IslandGroupDrawer.islandWidth * scale;
        double islandHeight = IslandGroupDrawer.islandHeight * scale;

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
                positions.add(centerPos.pureSumY(0));
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
                positions.add(centerPos.pureSumY(0));
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

    private static int getNumBlockTilePerIsland(int numBlockTile, int numIslands){

        if (numBlockTile <= numIslands) return 1;

        else {
            int additionalTiles;
            if (numBlockTile % numIslands == 0) additionalTiles = 0;
            else additionalTiles = 1;

            return numBlockTile / numIslands + additionalTiles;
        }

    }
}
