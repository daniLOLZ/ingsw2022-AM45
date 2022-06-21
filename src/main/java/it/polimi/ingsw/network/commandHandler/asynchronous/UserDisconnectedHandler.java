package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class UserDisconnectedHandler extends AsyncCommandHandler {

    public UserDisconnectedHandler() {
        commandHandled = CommandEnum.SERVER_USER_DISCONNECTED;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {
        if(!triggerCondition(parameters)) return false;

        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);
        messageBroker.addToMessage(NetworkFieldEnum.ASYNC_ID_USER, parameters.getUserController().getDisconnectedUser());

        //todo
        return true;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return false;
        else {
            return parameters.getUserController().isNetworkError();
        }
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        //If this catastrophic event happens, we don't want to clear the condition.
        // Theoretically, the client handlers will stop and not query this again
    }
}
