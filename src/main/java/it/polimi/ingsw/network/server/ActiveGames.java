package it.polimi.ingsw.network.server;

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
     * We don't want to instantiate this class
     */
    private ActiveGames(){}

    /**
     * Creates a new game controller based on information from the lobby.
     * The lobby contains information on the number of players, the rules of the game and the ids of the players
     * This method will set the lobby to a 'started' status
     * @param lobby the lobby to get the information from
     */
    public static void createGame(Lobby lobby){
        Controller newController = new Controller(lobby.getPlayers(), lobby.getGameType());
        GameRuleEnum rule = lobby.getGameType();
        int playerNumber = GameRuleEnum.getNumPlayers(rule.id);

        //gets the nicknames of the players in the lobby from the LoginHandler
        for(Integer idUser : lobby.getPlayers()){
            newController.setNickname(LoginHandler.getNicknameFromId(idUser), idUser);
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
        gameControllers.add(newController);

        //Associates each user in the lobby to the newly created game
        for(Integer user : lobby.getPlayers()){
            userToGameAssociation.put(user, newController);
        }
        lobby.setStartGame(true);
    }

    /**
     * Finds the controller of the game that this user is part of
     * @param idUser the user we need to know the game of
     * @return the controller of the game
     */
    public static Controller getGameFromUserId(Integer idUser){
        return userToGameAssociation.get(idUser);
    }


    /**
     * Deletes this player's association with its current game controller
     * @param idUser the user to remove from the association map
     */
    public static void deleteUserAssociation(Integer idUser) {
        userToGameAssociation.remove(idUser);
    }

    /**
     * Looks at all the currently present games and deletes the ones that are no longer active
     * (either won or stopped due to an error)
     */
    public static void removeEndedGames() {
        //todo Call some method in the controller first?
        gameControllers.removeIf(Controller::isGameWon);
        gameControllers.removeIf(Controller::isNetworkError);
    }
}
