package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.MNMoving;
import it.polimi.ingsw.network.connectionState.StudentChoosing;

public class PutInHallHandler extends CommandHandler{

    public PutInHallHandler(){
        commandAccepted = CommandEnum.PUT_IN_HALL;
    }

    /**
     * The user chooses to put their student in their hall
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        if(parameters.getUserController().putInHall()){
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
            notifyError(messageBroker,"Couldn't put the student in the hall");
            return false;
        }
    }
}
