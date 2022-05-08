package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.List;


public class AdvancedPlayerBean extends PlayerBean{

    private Integer numCoins;
    public AdvancedPlayerBean(String nickname, PlayerEnum playerId, boolean leader,
                              TeamEnum towerColor, int numTowers, List<StudentEnum> studentsAtEntrance,
                              List<Integer> studentsPerTable, List<StudentEnum> professors,
                              List<Integer> idAssistants, int numCoins) {
        super(nickname, playerId, leader, towerColor, numTowers, studentsAtEntrance, studentsPerTable, professors, idAssistants);
        this.numCoins = numCoins;
    }

    public Integer getNumCoins() {
        return numCoins;
    }

    public void setNumCoins(Integer numCoins) {
        this.numCoins = numCoins;
    }
}
