package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.BeanEnum;

import java.util.ArrayList;
import java.util.List;


public class AdvancedPlayerBean extends PlayerBean{

    private Integer numCoins;
    public AdvancedPlayerBean(String nickname, PlayerEnum playerId, boolean leader,
                              TeamEnum towerColor, int numTowers, List<StudentEnum> studentsAtEntrance,
                              List<Integer> studentsPerTable, List<StudentEnum> professors,
                              List<Assistant> Assistants, int numCoins, Assistant assistantPlayed, int turn) {
        super(nickname, playerId, leader, towerColor, numTowers, studentsAtEntrance,
                studentsPerTable, professors, Assistants, assistantPlayed, turn );
        this.numCoins = numCoins;
    }


    public AdvancedPlayerBean(String nickname, PlayerEnum playerId, boolean leader,
                              TeamEnum towerColor, int numTowers, List<StudentEnum> studentsAtEntrance,
                              List<Integer> studentsPerTable, List<StudentEnum> professors,
                              List<Integer> Assistants, int numCoins, int turn) {
        super(nickname, playerId, leader, towerColor, numTowers, studentsAtEntrance,
                studentsPerTable, professors, Assistants, turn);
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

        toReturn.append("    ________________________________________________________    \n");
        toReturn.append("    |Nickname : ").append(nickname).append("\n");
        toReturn.append("    |    Player id: ").append(playerId).append("\n");
        toReturn.append("    |    Tower color: ").append(towerColor).append("\n");
        toReturn.append("    |    Number of Towers: ").append(numTowers).append("\n");
        toReturn.append("    |    Coins: ").append(numCoins).append("\n");
        toReturn.append("    |    Entrance: ").append(studentsAtEntrance).append("\n");
        toReturn.append("    |    ").
                append(StudentEnum.RED).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.RED.index)).append("\n");

        toReturn.append("    |    ").
                append(StudentEnum.GREEN).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.GREEN.index)).append("\n");

        toReturn.append("    |    ").
                append(StudentEnum.BLUE).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.BLUE.index)).append("\n");

        toReturn.append("    |    ").
                append(StudentEnum.YELLOW).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.YELLOW.index)).append("\n");

        toReturn.append("    |    ").
                append(StudentEnum.PINK).append(" Table: ").
                append(studentsPerTable.get(StudentEnum.PINK.index)).append("\n");
        toReturn.append("    |    Professors: ").append(professors).append("\n");

        toReturn.append("    |    Assistant Played: ").append(assistantPlayed).append("\n");

        if(assistants.size() > 4){
            List<Assistant> list1 = new ArrayList<>();
            for(int i=0; i<4; i++)
                list1.add(assistants.get(i));

            List<Assistant> list2 = new ArrayList<>();
            for(int i = 4; i< assistants.size(); i++)
                list2.add(assistants.get(i));

            toReturn.append("    |    Assistants: ").append(list1).append("\n");
            toReturn.append("    |    ").append(list2).append("\n");

        }
        else
            toReturn.append("    |    Assistants: ").append(assistants).append("\n");

        toReturn.append("    ________________________________________________________    \n");
        String border = "    ________________________________________________________    ";

        return setTab(toReturn.toString(), border.length() );
    }
}
