package it.polimi.ingsw.model;

import java.util.*;

public class ParameterHandler {

    private static final int studentsPerCloud2or4Players = 3;
    private static final int studentsPerCloud3Players = 4;
    private static final int maxStudentsAtEntrance2or4Players = 7;
    private static final int maxStudentsAtEntrance3Players = 9;
    private static final int numTowers2or4Players = 8;
    private static final int numTowers3Players = 6;

    private final int studentsPerCloud;
    private final int maxStudentsAtEntrance;
    private final int numTowers;
    private Map<PlayerEnum, TeamEnum> playersAllegiance;

    private String errorMessage = "";

    public ParameterHandler(int numPlayers){

        if (numPlayers == 3) {
            studentsPerCloud      = studentsPerCloud3Players;
            maxStudentsAtEntrance = maxStudentsAtEntrance3Players;
            numTowers             = numTowers3Players;
        } else {
            studentsPerCloud      = studentsPerCloud2or4Players;
            maxStudentsAtEntrance = maxStudentsAtEntrance2or4Players;
            numTowers             = numTowers2or4Players;
        }

        playersAllegiance = new HashMap<>();
    }

    public int getStudentsPerCloud() {
        return studentsPerCloud;
    }

    public int getMaxStudentsAtEntrance() {
        return maxStudentsAtEntrance;
    }

    public int getNumTowers() {
        return numTowers;
    }

    public void setPlayersAllegiance(List<Player> players){
        for (Player player : players){
            playersAllegiance.put(player.getPlayerId(), player.getTeamColor());
        }
    }

    /**
     * Returns the Team of a Player by its PlayerID without accessing the Player class
     * @param player The ID of the Player
     * @return The Team of the Player
     */
    public TeamEnum getPlayerTeamById(PlayerEnum player){
        if (playersAllegiance.containsKey(player)) return playersAllegiance.get(player);
        return TeamEnum.NOTEAM;
    }

    /**
     * @param team The Team whom you want the members
     * @return The IDs of the Team members
     */
    public List<PlayerEnum> getTeamComponentsID(TeamEnum team){
        List<PlayerEnum> teamPlayers = new ArrayList<>();

        for (PlayerEnum player: playersAllegiance.keySet()) {
            if (playersAllegiance.get(player) == team) teamPlayers.add(player);
        }

        return teamPlayers;
    }

    public String getErrorState(){
        return errorMessage;
    }

    public void setErrorState(String error){
        errorMessage = error;
    }
}
