package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.game.SimpleGame;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.islands.IslandGroup;

import java.util.ArrayList;
import java.util.List;

public class BeanCreator {
    private final SimpleGame game;

    public BeanCreator(SimpleGame game){
        this.game = game;
    }

    /**
     *
     * @param islandGroup != null
     * @return a generic bean with islandGroup's information:
     * idIslandGroup, idIsland list, studentOnIsland list, if MN is present,
     * tower color
     */
    public GameElementBean getIslandGroupBean(IslandGroup islandGroup){

        GameElementBean beanToReturn;
        int idIslandGroup;
        List<Integer> idIslands = new ArrayList<>();
        List<StudentEnum> studentsOnIsland;
        boolean isPresentMN;
        TeamEnum towersColor;


        idIslandGroup = islandGroup.getIdGroup();
        studentsOnIsland = islandGroup.getStudents();
        isPresentMN = game.getIdIslandMN() == idIslandGroup;
        towersColor = islandGroup.getTowerColor();

        for(Island island: islandGroup.getIslands()){
            idIslands.add(island.getId());
        }

        beanToReturn = new IslandGroupBean(idIslandGroup,idIslands,studentsOnIsland,isPresentMN,towersColor);


        return beanToReturn;
    }
}
