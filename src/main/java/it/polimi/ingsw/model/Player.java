package it.polimi.ingsw.model;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
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
}
