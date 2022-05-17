package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
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

        parameters.getUserLobby().removeReady(parameters.getIdUser());
        notifySuccessfulOperation(messageBroker); // other checks to do?

        return true;
    }
}
