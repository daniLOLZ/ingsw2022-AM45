package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;

public class ChooseCloudHandler extends CommandHandler{

    /**
     * The user chooses a cloud to refill their entrance with
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.CHOOSE_CLOUD)) throw new UnexecutableCommandException();

        Integer idCloud = (Integer) messageBroker.readField(NetworkFieldEnum.ID_CLOUD);
        if(parameters.getUserController().chooseCloud(idCloud)){
            parameters.setConnectionState(new WaitingForControl());
            notifySuccessfulOperation(messageBroker);
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't select the cloud");
            return false;
        }
    }
}
