package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

public class SendNotReadyHandler extends CommandHandler{

    public SendNotReadyHandler(){
        commandAccepted = CommandEnum.NOT_READY;
    }

    /**
     * Sends a message to the server letting it know the user is not ready to start the game
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        // This, SendReady and StartGame should all be synchronized to some lock
        parameters.getUserLobby().readyLock.lock();
        try {
            parameters.getUserLobby().removeReady(parameters.getIdUser());
            //Like for the SendReadyHandler, we send an error
            notifySuccessfulOperation(messageBroker);
        }
        finally {
            parameters.getUserLobby().readyLock.unlock();
        }
        return true;
    }
}
