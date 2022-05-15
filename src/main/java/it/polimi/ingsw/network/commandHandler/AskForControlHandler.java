package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.PlanningPhaseTurn;
import it.polimi.ingsw.network.connectionState.StudentChoosing;

public class AskForControlHandler extends CommandHandler{

    /**
     * While waiting for their turn, the user periodically sends this message to ask for their turn to start
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.ASK_FOR_CONTROL)) throw new UnexecutableCommandException();

        PhaseEnum gamePhase = PhaseEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.GAME_PHASE));
        if(parameters.getUserController().askForControl(parameters.getIdUser(), gamePhase)){
            if(gamePhase.equals(PhaseEnum.PLANNING)){
                parameters.setConnectionState(new PlanningPhaseTurn());
            }
            else if(gamePhase.equals(PhaseEnum.ACTION)){
                parameters.setConnectionState(new StudentChoosing());
            }
            notifySuccessfulOperation(messageBroker);
        }
        else{
            notifyError(messageBroker,"Not your turn to play");
        }

        //it's no use returning false when it's not the player's turn
        return true;
    }
}
