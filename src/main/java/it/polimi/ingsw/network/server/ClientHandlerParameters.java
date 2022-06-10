package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.connectionState.Authentication;
import it.polimi.ingsw.network.connectionState.ConnectionState;

/**
 * Holds the parameters for this user's connection: <br>
 * - user id  <br>
 * - connection state (and its callback for character card playing) <br>
 * - lobby <br>
 * - controller <br>
 */
public class ClientHandlerParameters {

    private int idUser;
    private ConnectionState connectionState;
    private ConnectionState callbackConnectionState;
    private Lobby userLobby;
    private Controller userController;

    public ClientHandlerParameters(){
        this.connectionState = new Authentication();
        this.userLobby = null;
        this.userController = null;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public ConnectionState getCallbackConnectionState() {
        return callbackConnectionState;
    }

    public void setCallbackConnectionState(ConnectionState callbackConnectionState) {
        this.callbackConnectionState = callbackConnectionState;
    }

    public Lobby getUserLobby() {
        return userLobby;
    }

    public void setUserLobby(Lobby userLobby) {
        this.userLobby = userLobby;
    }

    public Controller getUserController() {
        return userController;
    }

    public void setUserController(Controller userController) {
        this.userController = userController;
    }

}
