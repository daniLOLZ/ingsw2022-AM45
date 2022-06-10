package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class YourTurnHandler extends AsyncCommandHandler{

    public YourTurnHandler() {
        commandHandled = CommandEnum.SERVER_YOUR_TURN;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {
        if(!triggerCondition(parameters)) return false;

        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);
        //todo get the turn of the player with a controller command
//        messageBroker.addToMessage(NetworkFieldEnum.ASYNC_GAME_PHASE, parameters.getUserController().);

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
