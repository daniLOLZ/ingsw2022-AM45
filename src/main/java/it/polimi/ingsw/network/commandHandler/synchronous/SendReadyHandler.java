package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class SendReadyHandler extends CommandHandler{

    public SendReadyHandler(){
        commandAccepted = CommandEnum.READY_TO_START;
    }

    /**
     * Sends a message to the server letting it know the user is ready to start the game
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        // This, SendNotReady and StartGame should all be synchronized to some lock
        parameters.getUserLobby().readyLock.lock();

        try{
            parameters.getUserLobby().addReady(parameters.getIdUser());
            //The operation will be successful, but we need to tell the user with an error message
            // that the game isn't ready to start yet, as per the network protocol
            notifySuccessfulOperation(messageBroker);
        }
        finally {
            parameters.getUserLobby().readyLock.unlock();
        }

        return true;
    }
}
