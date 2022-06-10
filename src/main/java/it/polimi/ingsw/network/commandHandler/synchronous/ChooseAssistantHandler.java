package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;

public class ChooseAssistantHandler extends CommandHandler{

    public ChooseAssistantHandler(){
        commandAccepted = CommandEnum.CHOOSE_ASSISTANT;
    }

    /**
     * The user selects which assistant they want to play, the server replies with an error
     * if the assistant can't be played
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Integer idAssistant = (Integer)messageBroker.readField(NetworkFieldEnum.ID_ASSISTANT);
        if(parameters.getUserController().playAssistant(idAssistant)){
            notifySuccessfulOperation(messageBroker);
            parameters.setConnectionState(new WaitingForControl());
            return true;
        }
        else {
            notifyError(messageBroker,"The assistant couldn't be played");
            return false;
        }
    }
}
