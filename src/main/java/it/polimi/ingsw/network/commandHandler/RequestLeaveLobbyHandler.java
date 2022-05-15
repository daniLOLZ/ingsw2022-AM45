package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.LookingForLobby;

public class RequestLeaveLobbyHandler extends CommandHandler{

    /**
     * The user requests to leave the current lobby
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.LEAVE_LOBBY)) throw new UnexecutableCommandException();

        parameters.getUserLobby().removePlayer(parameters.getIdUser());
        parameters.setUserLobby(null);
        parameters.setConnectionState(new LookingForLobby());
        notifySuccessfulOperation(messageBroker);

        return true;
    }
}
