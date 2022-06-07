package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Card requirements method.
 * The user sends the selected students at their entrance
 */
public class SelectEntranceStudentsHandler extends CommandHandler{

    public SelectEntranceStudentsHandler(){
        commandAccepted = CommandEnum.SELECT_ENTRANCE_STUDENTS;
    }

    /**
     * Card requirements method.
     * The user sends the selected students at their entrance
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Object[] objectArray = (Object[]) messageBroker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_POSITIONS);
        List<Integer> students = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            students.add((Integer)o);
        }
        if(parameters.getUserController().selectEntranceStudents(students)){
            notifySuccessfulOperation(messageBroker);
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't select the students at the entrance");
            return false;
        }
    }
}
