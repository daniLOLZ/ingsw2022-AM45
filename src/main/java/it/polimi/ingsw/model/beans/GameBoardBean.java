package it.polimi.ingsw.model.beans;

import java.util.List;

public class GameBoardBean extends GameElementBean{
    private List<Integer> idIslandGroups;
    private List<Integer> idAssistantsPlayed;
    private List<Integer> idPlayers;
    private Integer currentPlayerId;
    private Integer turn;
    private String phase;


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
}
