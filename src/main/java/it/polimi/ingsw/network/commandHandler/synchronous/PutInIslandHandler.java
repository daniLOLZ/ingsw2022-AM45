package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.MNMoving;
import it.polimi.ingsw.network.connectionState.StudentChoosing;

public class PutInIslandHandler extends CommandHandler{

    public PutInIslandHandler(){
        commandAccepted = CommandEnum.PUT_IN_ISLAND;
    }

    /**
     * The user chooses to put the student on the selected island
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Integer idIsland = (Integer)messageBroker.readField(NetworkFieldEnum.CHOSEN_ISLAND);
        if(parameters.getUserController().putInIsland(idIsland)){
            notifySuccessfulOperation(messageBroker);
            if(parameters.getUserController().allStudentsMoved()){
                messageBroker.addToMessage(NetworkFieldEnum.MORE_STUDENTS_TO_MOVE, false);
                parameters.setConnectionState(new MNMoving());
            }
            else {
                messageBroker.addToMessage(NetworkFieldEnum.MORE_STUDENTS_TO_MOVE, true);
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
