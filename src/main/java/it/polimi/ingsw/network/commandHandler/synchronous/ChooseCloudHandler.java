package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.connectionState.EndTurn;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;

public class ChooseCloudHandler extends CommandHandler{

    public ChooseCloudHandler(){
        commandAccepted = CommandEnum.CHOOSE_CLOUD;
    }

    /**
     * The user chooses a cloud to refill their entrance with
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Integer idCloud = (Integer) messageBroker.readField(NetworkFieldEnum.ID_CLOUD);
        if(parameters.getUserController().chooseCloud(idCloud)){
            parameters.setConnectionState(new EndTurn());
            notifySuccessfulOperation(messageBroker);
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't select the cloud");
            return false;
        }
    }
}
