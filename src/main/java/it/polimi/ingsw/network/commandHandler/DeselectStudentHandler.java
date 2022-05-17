package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.StudentChoosing;

public class DeselectStudentHandler extends CommandHandler{

    public DeselectStudentHandler(){
        commandAccepted = CommandEnum.DESELECT_STUDENT;
    }

    /**
     * The user deselects the student
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Integer studentPosition = (Integer)messageBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_STUDENT);
        if(parameters.getUserController().deselectStudent(studentPosition)){
            notifySuccessfulOperation(messageBroker);
            parameters.setConnectionState(new StudentChoosing());
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't deselect the student");
            return false;
        }
    }
}
