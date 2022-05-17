package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.util.ArrayList;
import java.util.List;

public class SelectStudentOnCardHandler extends CommandHandler{

    public SelectStudentOnCardHandler(){
        commandAccepted = CommandEnum.SELECT_STUDENT_ON_CARD;
    }

    /**
     * Card requirements method.
     * The user sends the selected students on the card they previously selected
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Object[] objectArray = (Object[]) messageBroker.readField(NetworkFieldEnum.CHOSEN_CARD_POSITIONS);
        List<Integer> students = new ArrayList<>();
        for(Object o : objectArray){ //TODO could be wrong
            students.add((Integer)o);
        }
        if(parameters.getUserController().selectStudentOnCard(students)){
            notifySuccessfulOperation(messageBroker);
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't select the students on the card");
            return false;
        }
    }
}
