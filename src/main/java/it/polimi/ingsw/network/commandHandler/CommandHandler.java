package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

public abstract class CommandHandler {

    /**
     * Processes the command stored inside the given messageBroker if it's responsibility of the specific class
     * and replies to the Client
     *
     * @param messageBroker The broker containing the message to execute
     * @param parameters    The clientHandler parameters containing all the fields the command is allowed to see and edit
     * @return true if the operation was successful. false if an occurred error should be brought to the attention of the server
     * @throws UnexecutableCommandException If the command is not this class' responsibility
     */
    public abstract boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException;

    /**
     * Adds the reply fields in the server message in case of a successful operation (Message and status)
     * @param messageBroker The messageBroker which will contain the successful operation response
     */
    protected void notifySuccessfulOperation(MessageBroker messageBroker){
        messageBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "OK");
        messageBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 0);
        messageBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, messageBroker.readField(NetworkFieldEnum.ID_REQUEST));
    }

    /**
     * Adds the reply fields in the server message in case of a failed operation (Message and status)
     * @param messageBroker The messageBroker which will contain the failed operation response
     * @param errorMessage A verbose message describing the error
     */
    protected void notifyError(MessageBroker messageBroker, String errorMessage){ // parametrize reply status as well
        messageBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_MESSAGE, "ERR");
        messageBroker.addToMessage(NetworkFieldEnum.SERVER_REPLY_STATUS, 1);
        messageBroker.addToMessage(NetworkFieldEnum.ID_REQUEST, messageBroker.readField(NetworkFieldEnum.ID_REQUEST));
        messageBroker.addToMessage(NetworkFieldEnum.ERROR_STATE, errorMessage);
    }
}
