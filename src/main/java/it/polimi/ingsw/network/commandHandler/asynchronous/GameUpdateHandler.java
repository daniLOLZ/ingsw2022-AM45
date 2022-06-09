package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class GameUpdateHandler extends AsyncCommandHandler {

    public GameUpdateHandler() {
        commandHandled = CommandEnum.SERVER_GAME_UPDATE;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {

        if(!triggerCondition(parameters)) return false;

        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);
        //todo do we send a single bean containing all other beans? might be best
//        messageBroker.addToMessage(NetworkFieldEnum.ASYNC_VIEW, );
        return true;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return false;
        return parameters.getUserController().isGameUpdated();
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return;
        parameters.getUserController().setGameUpdated(false);
    }
}
