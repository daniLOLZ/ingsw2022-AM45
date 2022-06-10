package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.StartingGame;
import it.polimi.ingsw.network.server.ActiveGames;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class LobbyStartHandler extends AsyncCommandHandler {

    public LobbyStartHandler() {
        commandHandled = CommandEnum.SERVER_LOBBY_START;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {
        if(!triggerCondition(parameters)) return false;

        // Put the user in the appropriate state
        parameters.setConnectionState(new StartingGame());

        if(parameters.getUserController() == null){
            parameters.setUserController(ActiveGames.getGameFromUserId(parameters.getIdUser()));
        }
        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);

        return true;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserLobby() == null) return false;
        else return parameters.getUserLobby().isGameStarted();
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserLobby() != null){
            parameters.getUserLobby().setStartGame(false);
        }
    }
}
