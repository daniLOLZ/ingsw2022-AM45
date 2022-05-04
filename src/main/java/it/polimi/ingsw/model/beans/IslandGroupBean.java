package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;

import java.util.List;

public class IslandGroupBean extends GameElementBean {
    private int idIslandGroup;
    private List<Integer> idIslands;
    private List<StudentEnum> studentsOnIsland;
    private boolean isPresentMN;
    private TeamEnum towersColor;

    public IslandGroupBean(int idIslandGroup, List<Integer> idIslands,
                           List<StudentEnum> studentsOnIsland, boolean isPresentMN,
                           TeamEnum towersColor){

        this.idIslandGroup = idIslandGroup;
        this.idIslands = idIslands;
        this.studentsOnIsland = studentsOnIsland;
        this.isPresentMN = isPresentMN;
        this.towersColor = towersColor;
    }

    public int getIdIslandGroup() {
        return idIslandGroup;
    }

    public List<Integer> getIdIslands() {
        return idIslands;
    }

    public List<StudentEnum> getStudentsOnIsland() {
        return studentsOnIsland;
    }

    public TeamEnum getTowersColor() {
        return towersColor;
    }

    public void setIdIslandGroup(int idIslandGroup) {
        this.idIslandGroup = idIslandGroup;
    }

    public void setIdIslands(List<Integer> idIslands) {
        this.idIslands = idIslands;
    }

    public void setPresentMN(boolean presentMN) {
        isPresentMN = presentMN;
    }

    public void setStudentsOnIsland(List<StudentEnum> studentsOnIsland) {
        this.studentsOnIsland = studentsOnIsland;
    }

    public void setTowersColor(TeamEnum towersColor) {
        this.towersColor = towersColor;
    }

    public boolean isPresentMN() {
        return isPresentMN;
    }
}
