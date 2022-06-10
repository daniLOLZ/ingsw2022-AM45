package it.polimi.ingsw.network.server;

import java.util.HashMap;
import java.util.Map;

public class ActiveClients {

    private static Map<Integer, ClientHandler> userIdToClientHandlerAssociation = new HashMap<>();
    private static int globalAsyncIdRequest = 1;
    /**
     * We don't want to instantiate this class
     */
    private ActiveClients(){}

    /**
     * Gets the client handler associated with the user id specified
     * @param userId the user id of the client
     * @return the client handler corresponding to the given user id
     */
    public static ClientHandler getHandlerFromId(Integer userId){
        return userIdToClientHandlerAssociation.get(userId);
    }

    /**
     * Sets the user with its respective client handle
     * @param userId the user id of the client to which the handler is assigned
     * @param handler the ClientHandler for this user
     */
    public static void setHandler(Integer userId, ClientHandler handler){
        userIdToClientHandlerAssociation.put(userId, handler);
    }

    /**
     * Removes this user's client handler from the active clients list
     * @param userId the user to remove
     */
    public static void removeUserIdToClientHandlerAssociation(Integer userId){
        userIdToClientHandlerAssociation.remove(userId);
    }

    /**
     * Increases the global request id used for asynchronous calls (server -> client)
     * @return the newly generated request id
     */
    //todo needs a lock?
    public static int increaseAndGetAsyncIdRequest(){
        globalAsyncIdRequest++;
        return globalAsyncIdRequest;
    }

}
