package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class FactoryPlayer {
    public static List<Player> getNPlayers(int numberOfPlayers, ParameterHandler parameters){
        List<Player> playerList = new ArrayList<>();
        //TODO handle nicknames and assignment of player to team
        switch (numberOfPlayers){
            case 2:
                playerList.add(new Player(PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.BLACK, true, parameters));
                break;
            case 3:
                playerList.add(new Player(PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.GREY, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true, parameters));
                break;
            case 4:
                playerList.add(new Player(PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.WHITE, false, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER4, "mockNickname4", TeamEnum.BLACK, false, parameters));
                break;
        }
        return playerList;
    }
}
