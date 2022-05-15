package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.MNMoving;
import it.polimi.ingsw.network.connectionState.StudentChoosing;

public class PutInIslandHandler extends CommandHandler{

    /**
     * The user chooses to put the student on the selected island
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.PUT_IN_ISLAND)) throw new UnexecutableCommandException();

        Integer idIsland = (Integer)messageBroker.readField(NetworkFieldEnum.CHOSEN_ISLAND);
        if(parameters.getUserController().putInIsland(idIsland)){
            notifySuccessfulOperation(messageBroker);
            if(parameters.getUserController().allStudentsMoved()){
                parameters.setConnectionState(new MNMoving());
            }
            else {
                parameters.setConnectionState(new StudentChoosing());
            }
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't put the student on the island");
            return false;
        }
    }
}
