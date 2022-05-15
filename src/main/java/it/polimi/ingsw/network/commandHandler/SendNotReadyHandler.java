package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

public class SendNotReadyHandler extends CommandHandler{

    /**
     * Sends a message to the server letting it know the user is not ready to start the game
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.NOT_READY)) throw new UnexecutableCommandException();

        parameters.getUserLobby().removeReady(parameters.getIdUser());
        notifySuccessfulOperation(messageBroker); // other checks to do?

        return true;
    }
}
