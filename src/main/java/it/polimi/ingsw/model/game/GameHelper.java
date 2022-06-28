package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.List;

public class GameHelper {

    /**
     * Finds and returns the player in a list associated with a specific id
     * returns null if there is no such player
     * @param players A list of players to search in
     * @param id the key to the search
     * @return the player in the list whose id matches the parameter
     */
    public static Player getPlayerById(List<Player> players, PlayerEnum id){
        for(Player player : players){
            if (player.getPlayerId().equals(id)){
                return player;
            }
        }
        return null;
    }
}
