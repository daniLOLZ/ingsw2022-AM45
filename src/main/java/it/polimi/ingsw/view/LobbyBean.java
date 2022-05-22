package it.polimi.ingsw.view;

import java.util.List;
import java.util.Objects;

public final class LobbyBean {
    private List<String> nicknames;
    private List<Boolean> readyPlayers; // positional
    private boolean gameStarted;

    public LobbyBean(List<String> nicknames, List<Boolean> readyPlayers, boolean gameStarted) {
        this.nicknames = nicknames;
        this.readyPlayers = readyPlayers;
        this.gameStarted = gameStarted;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public List<Boolean> getReadyPlayers() {
        return readyPlayers;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyBean lobbyBean = (LobbyBean) o;
        return gameStarted == lobbyBean.gameStarted && nicknames.equals(lobbyBean.nicknames) && readyPlayers.equals(lobbyBean.readyPlayers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nicknames, readyPlayers, gameStarted);
    }
}
