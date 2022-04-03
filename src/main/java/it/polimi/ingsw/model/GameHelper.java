package it.polimi.ingsw.model;

import java.util.List;

public class GameHelper {

    //TODO maybe handle the case in which the player is not present better
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