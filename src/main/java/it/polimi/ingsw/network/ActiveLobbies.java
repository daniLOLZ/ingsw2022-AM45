package it.polimi.ingsw.network;
import it.polimi.ingsw.controller.GameRule;

import java.util.ArrayList;
import java.util.List;

public class ActiveLobbies {

    private static Object genericLock = new Object();

    private static List<Lobby> lobbies = new ArrayList<>();

    /**
     * If there's already an available Lobby with the same game rules and at least an empty seat it returns the said lobby.
     * If there's no joinable lobby, it creates one fitting the specified game rules.
     * @param rules The rules of the wanted lobby
     * @return A lobby with the specified game rules
     */
    public static Lobby assignLobby(GameRule rules){

        Lobby lobby = searchGame(rules);

        if (lobby == null){
            lobby = createLobby(rules);
        }

        return lobby;
    }

    /**
     * Looks for a Lobby with the specified game rules and at least an empty seat.
     * Returns null if no active lobby is suitable.
     * @param rules The rules of the wanted lobby
     * @return The joinable lobby or null if there's none
     */
    private static Lobby searchGame(GameRule rules){

        for (Lobby lobby:
             lobbies) {
            if (lobby.getGameType().equals(rules) && !lobby.isFull()) return lobby;
        }

        return null;
    }

    /**
     * Creates a Lobby with the specified game rules
     * @param rules The game rules of the lobby
     * @return An empty lobby with the desired gamerules
     */
    private static Lobby createLobby(GameRule rules){

        Lobby lobby = new Lobby(rules);

        lobbies.add(lobby);

        return lobby;
    }

    /**
     * Removes a lobby from the list of active lobbies
     * @param lobby The lobby to be removed
     */
    public static void removeLobby(Lobby lobby){
        lobbies.remove(lobby);
    }

    /**
     * Creates and starts a new game with the users in the lobby and the rules specified at the creation of the lobby
     * @param lobby The lobby in which all players all ready and want to start playing
     * @return true if the game started correctly. false instead
     */
    public static boolean startGame(Lobby lobby){

        if (lobbies.contains(lobby)) return true; //TODO create everything
        //todo check if all players ready
        return false;
    }
}
