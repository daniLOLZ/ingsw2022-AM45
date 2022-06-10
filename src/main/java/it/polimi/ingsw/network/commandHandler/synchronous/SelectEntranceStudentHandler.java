package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.StudentMoving;

/**
 * The user asks to select a student from their entrance
 */
public class SelectEntranceStudentHandler extends CommandHandler{

    public SelectEntranceStudentHandler(){
        commandAccepted = CommandEnum.SELECT_STUDENT;
    }

    /**
     * The user asks to select a student from their entrance
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Integer selectedStudent = (Integer)messageBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_STUDENT);
        if(parameters.getUserController().selectStudent(selectedStudent)){
            notifySuccessfulOperation(messageBroker);
            parameters.setConnectionState(new StudentMoving());
            return true;
        }
        else {
            notifyError(messageBroker,"Error selecting the student, you've chosen all students already");
            return false;
        }
    }
}
