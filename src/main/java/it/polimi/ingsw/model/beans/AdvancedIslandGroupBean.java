package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;

import java.util.List;

public class AdvancedIslandGroupBean extends IslandGroupBean{
    private Integer numBlockTiles;

    public AdvancedIslandGroupBean(int idIslandGroup, List<Integer> idIslands,
                                   List<StudentEnum> studentsOnIsland, boolean isPresentMN,
                                   TeamEnum towersColor, int numBlockTiles) {
        super(idIslandGroup, idIslands, studentsOnIsland, isPresentMN, towersColor);
        this.numBlockTiles = numBlockTiles;
    }

    public Integer getNumBlockTiles() {
        return numBlockTiles;
    }

    public void setNumBlockTiles(Integer numBlockTiles) {
        this.numBlockTiles = numBlockTiles;
    }
}
