package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.LookingForLobby;

public class RequestLeaveLobbyHandler extends CommandHandler{

    public RequestLeaveLobbyHandler(){
        commandAccepted = CommandEnum.LEAVE_LOBBY;
    }

    /**
     * The user requests to leave the current lobby
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        parameters.getUserLobby().removePlayer(parameters.getIdUser());
        parameters.setJustLeftLobby(parameters.getUserLobby());
        parameters.setUserLobby(null);
        parameters.setConnectionState(new LookingForLobby());
        notifySuccessfulOperation(messageBroker);

        return true;
    }
}
