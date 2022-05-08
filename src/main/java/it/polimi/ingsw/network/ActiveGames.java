package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameRuleEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveGames {

    private static List<Controller> gameControllers = new ArrayList<>();
    private static Map<Integer, Controller> userToGameAssociation = new HashMap<>();

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
        // todo still
        // Adds the controller to the list
        gameControllers.add(controller);

        //Associates each user in the lobby to the newly created game
        for(Integer user : lobby.getPlayers()){
            userToGameAssociation.put(user, controller);
        }
    }

    /**
     * Finds the controller of the game that this user is part of
     * @param idUser the user we need to know the game of
     * @return the controller of the game
     */
    public static Controller getGameFromUserId(Integer idUser){
        return userToGameAssociation.get(idUser);
    }

}
