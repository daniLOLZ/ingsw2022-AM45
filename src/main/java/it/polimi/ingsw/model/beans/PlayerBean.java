package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.List;

public class PlayerBean extends GameElementBean{
    private String nickname;
    private PlayerEnum playerId;
    private boolean leader;
    private TeamEnum towerColor;
    private int numTowers;
    private List<StudentEnum> studentsAtEntrance;
    private List<Integer> studentsPerTable;
    private List<Integer> idAssistants;
    private List<StudentEnum> professors;


    public PlayerBean(String nickname, PlayerEnum playerId, boolean leader, TeamEnum towerColor,
                      int numTowers, List<StudentEnum> studentsAtEntrance,
                      List<Integer> studentsPerTable, List<StudentEnum> professors, List<Integer> idAssistants){
        int lowPriority = 3;
        priority = lowPriority;
        this.nickname = nickname;
        this.playerId = playerId;
        this.leader = leader;
        this.towerColor = towerColor;
        this.numTowers = numTowers;
        this.studentsAtEntrance = studentsAtEntrance;
        this.studentsPerTable = studentsPerTable;
        this.professors = professors;
        this.idAssistants = idAssistants;

    }

    public String getNickname() {
        return nickname;
    }

    public int getNumTowers() {
        return numTowers;
    }

    public List<Integer> getIdAssistants() {
        return idAssistants;
    }

    public List<Integer> getStudentsPerTable() {
        return studentsPerTable;
    }

    public PlayerEnum getPlayerId() {
        return playerId;
    }

    public List<StudentEnum> getProfessors() {
        return professors;
    }

    public List<StudentEnum> getStudentsAtEntrance() {
        return studentsAtEntrance;
    }

    public TeamEnum getTowerColor() {
        return towerColor;
    }

    public void setIdAssistants(List<Integer> idAssistants) {
        this.idAssistants = idAssistants;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public void setPlayerId(PlayerEnum playerId) {
        this.playerId = playerId;
    }

    public void setProfessors(List<StudentEnum> professors) {
        this.professors = professors;
    }

    public void setStudentsAtEntrance(List<StudentEnum> studentsAtEntrance) {
        this.studentsAtEntrance = studentsAtEntrance;
    }

    public void setStudentsPerTable(List<Integer> studentsPerTable) {
        this.studentsPerTable = studentsPerTable;
    }

    public void setTowerColor(TeamEnum towerColor) {
        this.towerColor = towerColor;
    }

    public boolean isLeader() {
        return leader;
    }

    @Override
    public String drawCLI() {
        StringBuilder toReturn = new StringBuilder();

        toReturn.append("\t____________________________________\t\n");
        toReturn.append("\t|Nickname : ").append(nickname).append("\n");
        toReturn.append("\t|\tPlayer id: ").append(playerId).append("\n");
        toReturn.append("\t|\tTower color: ").append(towerColor).append("\n");
        toReturn.append("\t|\tNumber of Towers: ").append(numTowers).append("\n");
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
        toReturn.append("\t____________________________________\t\n");

        return toReturn.toString();
    }

}
