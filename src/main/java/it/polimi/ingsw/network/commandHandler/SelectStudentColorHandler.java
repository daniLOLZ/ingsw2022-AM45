package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.util.ArrayList;
import java.util.List;

public class SelectStudentColorHandler extends CommandHandler{

    /**
     * Card requirements method.
     * The user sends the selected student color
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.SELECT_STUDENT_COLOR)) throw new UnexecutableCommandException();

        Object[] objectArray = (Object[]) messageBroker.readField(NetworkFieldEnum.COLORS_REQUIRED);
        List<StudentEnum> colors = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            colors.add(StudentEnum.fromObjectToEnum(o));
        }

        if(parameters.getUserController().selectStudentColor(colors)){
            notifySuccessfulOperation(messageBroker);
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't select student color");
            return false;
        }
    }
}
