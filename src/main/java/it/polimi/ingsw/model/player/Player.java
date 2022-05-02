package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;
import it.polimi.ingsw.model.assistantCards.NoSuchAssistantException;
import it.polimi.ingsw.model.assistantCards.Wizard;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.game.ParameterHandler;

import java.util.List;

public class Player {
    private PlayerEnum playerId;
    private String nickname;
    private Assistant assistantPlayed;
    private TeamEnum teamColor;
    private boolean leader;
    private Board board;
    private Wizard wizard;

    /**
     * Basic Player constructor
     * @param playerId id given by SimpleGame at initialization
     * @param nickname string chosen by the user, should not be equal to other players
     * @param teamColor the team's color
     * @param leader true if the player is the leader of their team (has the towers on their board)
     */
    public Player(PlayerEnum playerId, String nickname, TeamEnum teamColor, boolean leader, ParameterHandler parameters) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.teamColor = teamColor;
        this.leader = leader;
        this.board = new Board(teamColor, parameters);
        //TODO make the player choose which wizard they want to pick


        this.wizard = FactoryWizard.getWizard(playerId.index*10);
    }

    /**
     * Player constructor which features all choices made by the user: nickname, tower color and wizard
     * @param playerId
     * @param nickname
     * @param teamColor
     * @param leader
     * @param parameters
     * @param wizard
     */
    public Player(PlayerEnum playerId, String nickname, TeamEnum teamColor, boolean leader, ParameterHandler parameters, Wizard wizard){
        this.playerId = playerId;
        this.nickname = nickname;
        this.teamColor = teamColor;
        this.leader = leader;
        this.board = new Board(teamColor, parameters);
        this.wizard = wizard;
    }


    public Board getBoard() {
        return board;
    }

    public PlayerEnum getPlayerId() {
        return playerId;
    }

    public TeamEnum getTeamColor() {
        return teamColor;
    }

    public boolean isLeader() {
        return leader;
    }

    public Assistant getAssistantPlayed() {
        return assistantPlayed;
    }

    public Wizard getWizard(){
        return wizard;
    }

    private void setAssistantPlayed(Assistant assistant){
        this.assistantPlayed = assistant;
    }

    /**
     * Plays the assistant by setting it as assistantPlayed
     * @param assistantId id of the assistant to play
     */
    public void playAssistant(int assistantId){
        Assistant tempAssistant = null;
        try {
            tempAssistant = wizard.playCard(assistantId);
        }
        catch (NoSuchAssistantException e){
            e.printStackTrace();
            //TODO handle exception better or propagate upwards
        }
        setAssistantPlayed(tempAssistant);
    }

    public String getNickname() {
        return nickname;
    }

    //LUXRAY: con questo metodo possiamo creare il player e indipendentemente il Wizard.
    //In questo modo possiamo dare la possibilità di scegliere il wizard che si vuole.
    public void setWizard(Wizard wizard){
        this.wizard = wizard;
    }

    /**
     * Get from cloud the students to add at Entrance
     * @param cloud != null
     */
    public void getFromCloud(Cloud cloud){
        List<StudentEnum> students = cloud.empty();
        for(StudentEnum stud: students){
            board.addToEntrance(stud);
        }
    }

    /**
     *
     * @return true if player's wizard is empty
     */
    public boolean noMoreAssistant(){
        return wizard.isEmpty();
    }

    /**
     *
     * @return false if player is not leader or if he has at least 1 tower
     *  true if player is a leader, and he has not towers
     */
    public boolean noMoreTowers(){
        if(!leader)
            return false;

        return board.getNumberOfTowers() == 0;
    }

    public int getNumTowers(){
        return board.getNumberOfTowers();
    }

    public void moveFromEntranceToHall(){
        board.moveFromEntranceToHall();
    }
}
