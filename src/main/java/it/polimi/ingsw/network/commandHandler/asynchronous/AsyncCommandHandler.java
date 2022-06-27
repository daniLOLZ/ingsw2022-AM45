package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

/**
 * This class is to be extended by handlers of a single asynchronous network command started by the server
 * Each handler deals with one command, which must be assigned in the constructor
 */
public abstract class AsyncCommandHandler {

    CommandEnum commandHandled;

    public CommandEnum getCommandHandled() {
        return commandHandled;
    }

    /**
     * Each AsyncCommandHandler will execute a single command
     * @param messageBroker the broker on which to write the result of the command
     * @param parameters the parameters of this connection
     * @return true if the command was executed successfully
     */
    public abstract boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters);

    /**
     * Each handler specifies on what condition they will send a command
     * @param parameters the client handler parameters used to determine whether to take the action
     * @return true if the condition that triggers the sending of the command is verified
     */
    public abstract boolean triggerCondition(ClientHandlerParameters parameters);

    /**
     * Clears the condition that allowed this handler to trigger
     * @param parameters the client handler parameters used to determine whether to take the action
     */
    public abstract void clearCondition(ClientHandlerParameters parameters);

}
