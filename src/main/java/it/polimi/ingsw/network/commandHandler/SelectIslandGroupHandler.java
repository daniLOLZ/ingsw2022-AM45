package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.util.ArrayList;
import java.util.List;

public class SelectIslandGroupHandler extends CommandHandler{

    /**
     * Card requirements method.
     * The user sends the selected islands
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.SELECT_ISLAND_GROUP)) throw new UnexecutableCommandException();

        Object[] objectArray = (Object[]) messageBroker.readField(NetworkFieldEnum.CHOSEN_ISLANDS);
        List<Integer> islandIds = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            islandIds.add((Integer)o);
        }
        if(parameters.getUserController().selectIslandGroups(islandIds)){
            notifySuccessfulOperation(messageBroker);
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't select the islands");
            return false;
        }
    }
}
