package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.connectionState.WaitingForControl;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class EndTurnHandler extends CommandHandler{

    public EndTurnHandler(){
        commandAccepted = CommandEnum.END_TURN;
    }

    /**
     * The user presses asks to end the current turn
     * @param messageBroker The broker containing the message to execute
     * @param parameters    The clientHandler parameters containing all the fields the command is allowed to see and edit
     * @return true if the command could be executed
     * @throws UnexecutableCommandException if the command could not be executed by this handler
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        parameters.setConnectionState(new WaitingForControl());

        //todo
        return false;
    }
}
