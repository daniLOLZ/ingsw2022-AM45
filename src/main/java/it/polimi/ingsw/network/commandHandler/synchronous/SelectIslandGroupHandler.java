package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectIslandGroupHandler extends CommandHandler{

    public SelectIslandGroupHandler(){
        commandAccepted = CommandEnum.SELECT_ISLAND_GROUP;
    }

    /**
     * Card requirements method.
     * The user sends the selected islands
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        double[] readIslandIds = (double[]) messageBroker.readField(NetworkFieldEnum.CHOSEN_ISLAND_CHAR);
        List<Integer> islandIds = Arrays.stream(readIslandIds)
                .mapToInt(d -> (int) d)
                .boxed()
                .collect(Collectors.toList());
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
