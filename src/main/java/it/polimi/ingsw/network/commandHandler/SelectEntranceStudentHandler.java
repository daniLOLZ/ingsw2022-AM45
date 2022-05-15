package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.StudentMoving;

public class SelectEntranceStudentHandler extends CommandHandler{

    /**
     * The user asks to select a student from their entrance
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.SELECT_STUDENT)) throw new UnexecutableCommandException();

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
