package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.BeanEnum;

import java.util.List;
import java.util.Objects;

public final class LobbyBean extends GameElementBean {
    private List<String> nicknames;
    private List<Boolean> readyPlayers; // positional
    private boolean gameStarted;
    private Integer host;

    @Override
    public BeanEnum getBeanEnum() {
        return BeanEnum.LOBBY_BEAN;
    }

    public LobbyBean(List<String> nicknames, List<Boolean> readyPlayers, boolean gameStarted, Integer host) {
        this.nicknames = nicknames;
        this.readyPlayers = readyPlayers;
        this.gameStarted = gameStarted;
        this.host = host;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int index = 0; index < nicknames.size(); index++){
            builder.append(nicknames.get(index) + " : ");
            if(getReadyPlayers().get(index)){
                builder.append("ready");
            }
            else { builder.append("not ready"); }
            if(index == host) builder.append(" [Host]");
            builder.append("\n");
        }
        return builder.toString();
    }
}
