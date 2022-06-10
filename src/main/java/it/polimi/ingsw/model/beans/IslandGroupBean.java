package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.network.BeanEnum;

import java.util.List;
import java.util.Scanner;

public class IslandGroupBean extends GameElementBean {
    protected int idIslandGroup;
    protected int idPrevIslandGroup;
    protected int idNextIslandGroup;
    protected List<Integer> idIslands;
    protected List<StudentEnum> studentsOnIsland;
    protected boolean isPresentMN;
    protected TeamEnum towersColor;

    @Deprecated
    public IslandGroupBean(int idIslandGroup, List<Integer> idIslands,
                           List<StudentEnum> studentsOnIsland, boolean isPresentMN,
                           TeamEnum towersColor){

        final int mediumPriority = 4;
        priority = mediumPriority;
        this.idIslandGroup = idIslandGroup;
        this.idIslands = idIslands;
        this.studentsOnIsland = studentsOnIsland;
        this.isPresentMN = isPresentMN;
        this.towersColor = towersColor;
    }

    public IslandGroupBean(int idIslandGroup, List<Integer> idIslands,
                           List<StudentEnum> studentsOnIsland, boolean isPresentMN,
                           TeamEnum towersColor, int prev, int next){

        final int mediumPriority = 4;
        priority = mediumPriority;
        this.idIslandGroup = idIslandGroup;
        this.idIslands = idIslands;
        this.studentsOnIsland = studentsOnIsland;
        this.isPresentMN = isPresentMN;
        this.towersColor = towersColor;
        this.idPrevIslandGroup = prev;
        this.idNextIslandGroup = next;
    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.ISLANDGROUP_BEAN;
    }

    public int getIdPrevIslandGroup() {
        return idPrevIslandGroup;
    }

    public int getIdNextIslandGroup() {
        return idNextIslandGroup;
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

    public void setIdNextIslandGroup(int idNextIslandGroup) {
        this.idNextIslandGroup = idNextIslandGroup;
    }

    public void setIdPrevIslandGroup(int idPrevIslandGroup) {
        this.idPrevIslandGroup = idPrevIslandGroup;
    }

    public boolean isPresentMN() {
        return isPresentMN;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        String Mn;
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
        toReturn.append("\t|\tStudents: ").append(studentsOnIsland).append("\n");
        toReturn.append("\t|\tIs present Mother Nature: ").append(Mn).append("\n");
        toReturn.append("\t|\tTower color: ").append(towersColor).append("\n");
        toReturn.append("\t|\tNumber of towers: ").append(towers).append("\n");
        toReturn.append("\t____________________________________\t\n");

         return setTab(toReturn.toString());

    }




}
