package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;

import java.util.List;
import java.util.Scanner;

public class IslandGroupBean extends GameElementBean {
    private int idIslandGroup;
    private List<Integer> idIslands;
    private List<StudentEnum> studentsOnIsland;
    private boolean isPresentMN;
    private TeamEnum towersColor;

    public IslandGroupBean(int idIslandGroup, List<Integer> idIslands,
                           List<StudentEnum> studentsOnIsland, boolean isPresentMN,
                           TeamEnum towersColor){

        int mediumPriority = 2;
        priority = mediumPriority;
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

    @Override
    public String drawCLI() {
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
        toReturn.append("\t|\tStudents: ").append(studentsOnIsland).append("\n");
        toReturn.append("\t|\tIs present Mother Nature: ").append(Mn).append("\n");
        toReturn.append("\t|\tTower color: ").append(towersColor).append("\n");
        toReturn.append("\t|\tNumber of towers: ").append(towers).append("\n");
        toReturn.append("\t____________________________________\t\n");

         return setTab(toReturn.toString());

    }

    private String setTab(String string){
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter("\n");
        StringBuilder builder = new StringBuilder();
        String border = "\t____________________________________\t.\n";
        while(scanner.hasNext()){
            String x = scanner.next();
            while(x.length() < border.length())
                x = x + " ";

            builder.append(x).append("\n");
        }
        return  builder.toString();
    }


}
