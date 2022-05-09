package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.assistantCards.Wizard;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.TeamEnum;

import java.util.ArrayList;
import java.util.List;

public class FactoryPlayer {

    /**
     *
     * @param nick != null
     * @param playerId != NOPLAYER
     * @param teamColor != NOTEAM
     * @param leader .
     * @param parameter != null
     * @param advanced .
     * @return a Player if advanced is false and nick is not contained in usedNicknames;
     *         an AdvancedPlayer if advanced is true and nick is not contained in usedNicknames;
     *         null if nick is contained in usedNicknames
     */
    @Deprecated
    public static Player getPlayer(String nick, PlayerEnum playerId, TeamEnum teamColor,
                                   boolean leader, ParameterHandler parameter, boolean advanced){
        Player player;
    /*    if(usedNicknames == null){
            usedNicknames = new ArrayList<>();
        }
        if(usedNicknames.contains(nick))
            return null;
    */
        if(advanced){
            player = new AdvancedPlayer(playerId,nick,teamColor,leader,parameter);
        }
        else{
            player = new Player(playerId,nick,teamColor,leader,parameter);
        }
//      usedNicknames.add(nick);
        return player;

    }


    /**
     * Creates a new player from the given parameters
     * @param nick the nickname of the player
     * @param playerId the id of the player
     * @param teamColor the color of the towers of the player
     * @param wizard the wizard chosen by the player
     * @param leader indicates whether the player is the leader of their team or not
     * @param parameters parameters of the game
     * @param advanced true if the player should be its advanced version
     * @return a new player with the given characteristics
     */
    public static Player getPlayer(String nick, PlayerEnum playerId, TeamEnum teamColor, Wizard wizard,
                                   boolean leader, ParameterHandler parameters, boolean advanced){
        Player player;
        if(advanced){
            player = new AdvancedPlayer(playerId, nick, teamColor, wizard, leader, parameters);
        }
        else player = new Player(playerId, nick, teamColor, wizard, leader, parameters);

        return player;
    }


    /**
     * stub function, useful to create mock players
      */
    public static List<Player> getNPlayers(int numberOfPlayers, ParameterHandler parameters) {
        List<Player> playerList = new ArrayList<>();

        switch (numberOfPlayers) {
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
