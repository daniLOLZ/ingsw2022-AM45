package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.ArrayList;
import java.util.List;

public class GameBoardBean extends GameElementBean{
    protected List<Integer> idIslandGroups;
    protected List<Integer> idAssistantsPlayed;
    protected List<Integer> idPlayers;
    protected Integer currentPlayerId;
    protected Integer turn;
    protected String phase;


    public GameBoardBean( List<Integer> idIslandGroups, List<Integer> idAssistantsPlayed,
                          List<Integer> idPlayers, Integer currentPlayerId,
                          Integer turn, String phase){
        final int highPriority = 1;
        priority = highPriority;
        this.idIslandGroups = idIslandGroups;
        this.idPlayers= idPlayers;
        this.idAssistantsPlayed = idAssistantsPlayed;
        this.turn = turn;
        this.phase = phase;
        this.currentPlayerId = currentPlayerId;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public void setIdAssistantsPlayed(List<Integer> idAssistantsPlayed) {
        this.idAssistantsPlayed = idAssistantsPlayed;
    }

    public void setCurrentPlayerId(Integer currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public void setIdIslandGroups(List<Integer> idIslandGroups) {
        this.idIslandGroups = idIslandGroups;
    }

    public void setIdPlayers(List<Integer> idPlayers) {
        this.idPlayers = idPlayers;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Integer getCurrentPlayerId() {
        return currentPlayerId;
    }

    public Integer getTurn() {
        return turn;
    }

    public List<Integer> getIdAssistantsPlayed() {
        return idAssistantsPlayed;
    }

    public List<Integer> getIdIslandGroups() {
        return idIslandGroups;
    }

    public List<Integer> getIdPlayers() {
        return idPlayers;
    }

    public String getPhase() {
        return phase;
    }

    @Override
    public String drawCLI() {
        StringBuilder toReturn = new StringBuilder();
        String currentPlayer = PlayerEnum.getPlayer(currentPlayerId).name;
        List<String> players = new ArrayList<>();
        for(Integer x: idPlayers){
            players.add(PlayerEnum.getPlayer(x).name);
        }


        toReturn.append("\t____________________________________________\t\n");
        toReturn.append("\t|\t\t\t::ERYANTIS::").append("\n");
        toReturn.append("\t|\tTURN: ").append(turn).append("\n");
        toReturn.append("\t|\tPHASE: ").append(phase).append("\n");
        toReturn.append("\t|\tCURRENT PLAYER: ").append(currentPlayer).append("\n");
        toReturn.append("\t|\tPLAYERS: ").append(players).append("\n");
        toReturn.append("\t|\tASSISTANTS PLAYED: ").append(idAssistantsPlayed).append("\n");
        toReturn.append("\t____________________________________________\t\n");

        return toReturn.toString();
    }

}
