package it.polimi.ingsw.network;
import it.polimi.ingsw.controller.GameRule;

import java.util.ArrayList;
import java.util.List;

public class ActiveLobbies {

    private static Object genericLock = new Object();

    private static List<Lobby> lobbies = new ArrayList<>();

    public static Lobby assignLobby(GameRule rules){

        Lobby lobby = searchGame(rules);

        if (lobby == null){
            lobby = createLobby(rules);
        }

        return lobby;
    }

    private static Lobby searchGame(GameRule rules){

        for (Lobby lobby:
             lobbies) {
            if (lobby.getGameType().equals(rules)) return lobby;
        }

        return null;
    }

    private static Lobby createLobby(GameRule rules){

        Lobby lobby = new Lobby(rules);

        lobbies.add(lobby);

        return lobby;
    }

    private static void removeLobby(Lobby lobby){
        lobbies.remove(lobby);
    }

    public static boolean startGame(Lobby lobby){


        if (lobbies.contains(lobby)) return true; //TODO create everything
        //todo check if all players ready
        return false;
    }
}
