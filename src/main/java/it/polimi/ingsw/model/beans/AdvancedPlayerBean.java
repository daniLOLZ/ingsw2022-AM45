package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.BeanEnum;

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

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.ADVANCED_PLAYER_BEAN;
    }

    public Integer getNumCoins() {
        return numCoins;
    }

    public void setNumCoins(Integer numCoins) {
        this.numCoins = numCoins;
    }

    @Override
    public String toString(){
        StringBuilder toReturn = new StringBuilder();

        toReturn.append("\t________________________________________________________\t\n");
        toReturn.append("\t|Nickname : ").append(nickname).append("\n");
        toReturn.append("\t|\tPlayer id: ").append(playerId).append("\n");
        toReturn.append("\t|\tTower color: ").append(towerColor).append("\n");
        toReturn.append("\t|\tNumber of Towers: ").append(numTowers).append("\n");
        toReturn.append("\t|\tCoins: ").append(numCoins).append("\n");
        toReturn.append("\t|\tEntrance: ").append(studentsAtEntrance).append("\n");
        toReturn.append("\t|\t").
                append(StudentEnum.RED).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.RED.index)).append("\n");

        toReturn.append("\t|\t").
                append(StudentEnum.GREEN).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.GREEN.index)).append("\n");

        toReturn.append("\t|\t").
                append(StudentEnum.BLUE).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.BLUE.index)).append("\n");

        toReturn.append("\t|\t").
                append(StudentEnum.YELLOW).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.YELLOW.index)).append("\n");

        toReturn.append("\t|\t").
                append(StudentEnum.PINK).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.PINK.index)).append("\n");
        toReturn.append("\t|\tProfessors: ").append(professors).append("\n");
        toReturn.append("\t|\tAssistants: ").append(idAssistants).append("\n");
        toReturn.append("\t________________________________________________________\t\n");
        String border = 	"AAAAAAAA___________________________________________________\t\n";

        return setTab(toReturn.toString(), border.length() );
    }
}
