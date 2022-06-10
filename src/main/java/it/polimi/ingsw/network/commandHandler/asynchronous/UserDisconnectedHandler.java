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

        //todo
        return false; //change
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        return false;
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {

    }
}
