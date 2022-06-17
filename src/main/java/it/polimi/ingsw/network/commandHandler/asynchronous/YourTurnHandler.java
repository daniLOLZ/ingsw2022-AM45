package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.PlanningPhaseTurn;
import it.polimi.ingsw.network.connectionState.StudentChoosing;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class YourTurnHandler extends AsyncCommandHandler{

    public YourTurnHandler() {
        commandHandled = CommandEnum.SERVER_YOUR_TURN;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {
        if(!triggerCondition(parameters)) return false;

        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);
        PhaseEnum currentPhase = parameters.getUserController().getGamePhase();
        messageBroker.addToMessage(NetworkFieldEnum.ASYNC_GAME_PHASE, currentPhase);

        if(currentPhase.equals(PhaseEnum.PLANNING)){
            parameters.setConnectionState(new PlanningPhaseTurn());
        }
        else if(currentPhase.equals(PhaseEnum.ACTION)){
            parameters.setConnectionState(new StudentChoosing());
        }
        return true;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return false;
        else {
            if(parameters.getUserController().isNewTurn() &&
                    parameters.getUserController().isMyTurn(parameters.getIdUser())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() != null) {
            parameters.getUserController().setNewTurn(false);
        }
    }
}
