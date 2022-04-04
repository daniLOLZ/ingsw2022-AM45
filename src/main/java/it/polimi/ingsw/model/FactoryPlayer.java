package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class FactoryPlayer {
    public static List<Player> getNPlayers(SimpleGame game, int numberOfPlayers){
        List<Player> playerList = new ArrayList<>();
        //TODO handle nicknames
        switch (numberOfPlayers){
            case 2:
                playerList.add(new Player(game, PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true));
                playerList.add(new Player(game, PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.BLACK, true));
                break;
            case 3:
                playerList.add(new Player(game, PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true));
                playerList.add(new Player(game, PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.GREY, true));
                playerList.add(new Player(game, PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true));
                break;
            case 4:
                playerList.add(new Player(game, PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true));
                playerList.add(new Player(game, PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.WHITE, false));
                playerList.add(new Player(game, PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true));
                playerList.add(new Player(game, PlayerEnum.PLAYER4, "mockNickname4", TeamEnum.BLACK, false));
                break;
        }
        return playerList;
    }
}
