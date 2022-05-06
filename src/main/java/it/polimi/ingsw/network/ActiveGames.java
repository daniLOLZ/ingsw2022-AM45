package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameRule;

import java.util.List;

public class ActiveGames {

    private static List<Controller> gameControllers;

    /**
     * Creates a new game based on information from the lobby
     * @param lobby the lobby to get the information from
     */
    public static void createGame(Lobby lobby){
        Controller controller = new Controller();
        //todo
        GameRule rule = lobby.getGameType();
        int playerNumber = GameRule.getNumPlayers(rule.id);
        if(GameRule.isSimple(rule.id)){
            controller.createSimpleGame(playerNumber);
        }
        else if(GameRule.isAdvanced(rule.id)){
            controller.createAdvancedGame(playerNumber);
        }
        // todo ancora
    }
}
