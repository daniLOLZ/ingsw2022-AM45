package it.polimi.ingsw.model;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final SimpleGame game;
    private PlayerEnum playerId;
    private String nickname;
    private Assistant assistantPlayed;
    private TeamEnum teamColor;
    private boolean leader;
    private Board board;
    private Wizard wizard;

    /**
     * Basic Player constructor
     * @param game game hosting the players
     * @param playerId id given by SimpleGame at initialization
     * @param nickname string chosen by the user, should not be equal to other players
     * @param teamColor the team's color
     * @param leader true if the player is the leader of their team (has the towers on their board)
     */
    public Player(SimpleGame game, PlayerEnum playerId, String nickname, TeamEnum teamColor, boolean leader) {
        this.game = game;
        this.playerId = playerId;
        this.nickname = nickname;
        this.teamColor = teamColor;
        this.leader = leader;
        this.board = new Board(game.getNumTowers(), teamColor);
        //TODO make the player choose which wizard they want to pick

        //I catch these exceptions here to not throw them too many times in code
        //Player modify SimpleGame to notify the error presence and in this way
        //View can see the error and show to User what goes wrong, then View
        //notify to Controller that the game should be closed
        try {
            this.wizard = FactoryWizard.getWizard(playerId.index*10);
        } catch (IOException e) {
            ErrorState error = new ErrorState(e.getMessage());
            game.setErrorState(error);
            e.printStackTrace();        //TO DELETE, for now it can be useful
        } catch (ParseException e) {
            ErrorState error = new ErrorState(e.getMessage());
            game.setErrorState(error);
            e.printStackTrace();         //TO DELETE, for now it can be useful
        }
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


}
