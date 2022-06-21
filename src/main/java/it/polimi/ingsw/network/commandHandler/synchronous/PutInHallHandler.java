package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
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
                messageBroker.addToMessage(NetworkFieldEnum.MORE_STUDENTS_TO_MOVE, false);
                parameters.setConnectionState(new MNMoving(parameters.getUserController().getGameRule()));
            }
            else {
                messageBroker.addToMessage(NetworkFieldEnum.MORE_STUDENTS_TO_MOVE, true);
                parameters.setConnectionState(new StudentChoosing(parameters.getUserController().getGameRule()));
            }
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't put the student in the hall");
            return false;
        }
    }
}
