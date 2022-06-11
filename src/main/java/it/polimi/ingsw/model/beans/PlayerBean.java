package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.FactoryAssistant;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.BeanEnum;

import java.util.ArrayList;
import java.util.List;

public class PlayerBean extends GameElementBean{
    protected String nickname;
    protected PlayerEnum playerId;
    protected boolean leader;
    protected TeamEnum towerColor;
    protected int numTowers;
    protected List<StudentEnum> studentsAtEntrance;
    protected List<Integer> studentsPerTable;
    protected List<Assistant> assistants;
    protected Assistant assistantPlayed;
    protected List<StudentEnum> professors;


    public PlayerBean(String nickname, PlayerEnum playerId, boolean leader, TeamEnum towerColor,
                      int numTowers, List<StudentEnum> studentsAtEntrance,
                      List<Integer> studentsPerTable, List<StudentEnum> professors,
                      List<Assistant> Assistants, Assistant assistantPlayed){
        final int lowPriority = 5;
        priority = lowPriority;
        this.nickname = nickname;
        this.playerId = playerId;
        this.leader = leader;
        this.towerColor = towerColor;
        this.numTowers = numTowers;
        this.studentsAtEntrance = studentsAtEntrance;
        this.studentsPerTable = studentsPerTable;
        this.professors = professors;
        this.assistants = Assistants;
        this.assistantPlayed = assistantPlayed;

    }

    @Deprecated
    public PlayerBean(String nickname, PlayerEnum playerId, boolean leader, TeamEnum towerColor,
                      int numTowers, List<StudentEnum> studentsAtEntrance,
                      List<Integer> studentsPerTable, List<StudentEnum> professors,
                      List<Integer> idAssistants){
        final int lowPriority = 5;
        priority = lowPriority;
        this.nickname = nickname;
        this.playerId = playerId;
        this.leader = leader;
        this.towerColor = towerColor;
        this.numTowers = numTowers;
        this.studentsAtEntrance = studentsAtEntrance;
        this.studentsPerTable = studentsPerTable;
        this.professors = professors;
        assistants = new ArrayList<>();
        for(Integer x: idAssistants){
            assistants.add(FactoryAssistant.getAssistant(x));
        }


    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.PLAYER_BEAN;
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumTowers() {
        return numTowers;
    }

    public List<Assistant> getAssistants() {
        return assistants;
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

    public Assistant getAssistantPlayed() {
        return assistantPlayed;
    }

    public List<Integer> getIdAssistants(){
        List<Integer> list = new ArrayList<>();
        for(Assistant a: assistants)
            list.add(a.id);
        return list;
    }

    public void setIdAssistants(List<Integer> idAssistants){
        List<Assistant> list= new ArrayList<>();
        for(Integer x: idAssistants)
            list.add(FactoryAssistant.getAssistant(x));
        this.assistants = list;
    }

    public void setAssistantPlayed(Assistant assistantPlayed) {
        this.assistantPlayed = assistantPlayed;
    }

    public void setAssistants(List<Assistant> Assistants) {
        this.assistants = Assistants;
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
    public String toString() {
        StringBuilder toReturn = new StringBuilder();

        toReturn.append("\t________________________________________________________\t\n");
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

        toReturn.append("\t|\tAssistant Played: ").append(assistantPlayed).append("\n");


        if(assistants.size() > 4){
            List<Assistant> list1 = new ArrayList<>();
            for(int i=0; i<4; i++)
                list1.add(assistants.get(i));

            List<Assistant> list2 = new ArrayList<>();
            for(int i = 4; i< assistants.size(); i++)
                list2.add(assistants.get(i));

            toReturn.append("\t|\tAssistants: ").append(list1).append("\n");
            toReturn.append("\t|\t").append(list2).append("\n");

        }
        else
            toReturn.append("\t|\tAssistants: ").append(assistants).append("\n");

        toReturn.append("\t________________________________________________________\t\n");
        String border = 	"AAAAAAAA___________________________________________________\t\n";

        return setTab(toReturn.toString(), border.length() );
    }

}
