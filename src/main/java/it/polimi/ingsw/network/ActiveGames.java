package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameRuleEnum;

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
        GameRuleEnum rule = lobby.getGameType();
        int playerNumber = GameRuleEnum.getNumPlayers(rule.id);
        if(GameRuleEnum.isSimple(rule.id)){
            controller.createSimpleGame(playerNumber);
        }
        else if(GameRuleEnum.isAdvanced(rule.id)){
            controller.createAdvancedGame(playerNumber);
        }
        // todo ancora
    }
}
