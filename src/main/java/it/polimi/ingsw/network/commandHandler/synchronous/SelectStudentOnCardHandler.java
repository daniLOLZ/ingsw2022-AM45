package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectStudentOnCardHandler extends CommandHandler{

    public SelectStudentOnCardHandler(){
        commandAccepted = CommandEnum.SELECT_STUDENTS_ON_CARD;
    }

    /**
     * Card requirements method.
     * The user sends the selected students on the card they previously selected
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        double[] readStudents = (double[]) messageBroker.readField(NetworkFieldEnum.CHOSEN_CARD_POSITIONS);
        List<Integer> students = Arrays.stream(readStudents)
                .mapToInt(d -> (int) d)
                .boxed()
                .collect(Collectors.toList());
        if(parameters.getUserController().selectStudentsOnCard(students)){
            notifySuccessfulOperation(messageBroker);
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't select the students on the card");
            return false;
        }
    }
}
