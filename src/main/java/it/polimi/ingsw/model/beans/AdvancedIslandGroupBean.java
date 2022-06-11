package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.network.BeanEnum;

import java.util.List;

public class AdvancedIslandGroupBean extends IslandGroupBean{
    private Integer numBlockTiles;

    @Deprecated
    public AdvancedIslandGroupBean(int idIslandGroup, List<Integer> idIslands,
                                   List<StudentEnum> studentsOnIsland, boolean isPresentMN,
                                   TeamEnum towersColor, int numBlockTiles) {
        super(idIslandGroup, idIslands, studentsOnIsland, isPresentMN, towersColor);
        this.numBlockTiles = numBlockTiles;
    }

    public AdvancedIslandGroupBean(int idIslandGroup, List<Integer> idIslands,
                                   List<StudentEnum> studentsOnIsland, boolean isPresentMN,
                                   TeamEnum towersColor, int numBlockTiles, int prev, int next) {
        super(idIslandGroup, idIslands, studentsOnIsland, isPresentMN, towersColor,prev,next);
        this.numBlockTiles = numBlockTiles;
    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.ADVANCED_ISLANDGROUP_BEAN;
    }

    public Integer getNumBlockTiles() {
        return numBlockTiles;
    }

    public void setNumBlockTiles(Integer numBlockTiles) {
        this.numBlockTiles = numBlockTiles;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        String Mn;
        String colorOffset = "\t";
        if(isPresentMN)
            Mn = "YES";
        else
            Mn = "NO";

        int towers;
        if(towersColor == TeamEnum.NOTEAM)
            towers = 0;
        else
            towers = idIslands.size();

        toReturn.append("\t____________________________________\t\n");
        toReturn.append("\t|Island Group : ").append(idIslandGroup).append("\n");
        toReturn.append("\t|\tNumber of islands: ").append(idIslands.size()).append("\n");
        toReturn.append("\t|\tIslands: ").append(idIslands).append("\n");
        toReturn.append("\t|\tPrev-Island: ").append(idPrevIslandGroup).append("\n");
        toReturn.append("\t|\tNext-Island: ").append(idNextIslandGroup).append("\n");
        toReturn.append("\t|\tStudents: ").append(studentsOnIsland).append(colorOffset).
                append("\n");
        toReturn.append("\t|\tIs present Mother Nature: ").append(Mn).append("\n");
        toReturn.append("\t|\tTower color: ").append(towersColor).append("\n");
        toReturn.append("\t|\tNumber of towers: ").append(towers).append("\n");
        toReturn.append("\t|\tNumber of BlockTiles: ").append(numBlockTiles).append("\n");
        toReturn.append("\t____________________________________\t\n");

        String border= "____________________________________";

        return setTab(toReturn.toString(),border.length());
    }
}
