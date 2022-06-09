package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class GameStartHandler extends AsyncCommandHandler {

    public GameStartHandler() {
        commandHandled = CommandEnum.SERVER_GAME_START;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {
        if(!triggerCondition(parameters)) return false;

        parameters.setConnectionState(new WaitingForControl());
        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);

        return true;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return false;
        return parameters.getUserController().isGameStarted();
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return;
        parameters.getUserController().unsetGameStarted();
    }
}
