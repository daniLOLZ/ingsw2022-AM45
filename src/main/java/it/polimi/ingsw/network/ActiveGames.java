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
     * Creates a new game controller based on information from the lobby
     * The lobby contains information on the number of players, the rules of the game and the ids of the players
     * @param lobby the lobby to get the information from
     */
    public static void createGame(Lobby lobby){
        Controller controller = new Controller(lobby.getPlayers(), lobby.getGameType());
        GameRuleEnum rule = lobby.getGameType();
        int playerNumber = GameRuleEnum.getNumPlayers(rule.id);

        //gets the nicknames of the players in the lobby from the LoginHandler
        for(Integer idUser : lobby.getPlayers()){
            controller.setNickname(LoginHandler.getNicknameFromId(idUser), idUser);
        }
/*
        //Creation of the game can't happen yet, we need the wizards and teams first
        if(GameRuleEnum.isSimple(rule.id)){
            controller.createSimpleGame(playerNumber);
        }
        else if(GameRuleEnum.isAdvanced(rule.id)){
            controller.createAdvancedGame(playerNumber);
        }
*/
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
