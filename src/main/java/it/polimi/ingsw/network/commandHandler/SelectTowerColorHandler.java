package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;

public class SelectTowerColorHandler extends CommandHandler{

    public SelectTowerColorHandler(){
        commandAccepted = CommandEnum.SELECT_TOWER_COLOR;
    }

    /**
     * The user selects a team to be part of, this method calls the game controller to know whether the
     * team chosen is available, and notifies it to the user
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        TeamEnum teamColor = TeamEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.ID_TOWER_COLOR));
        if(parameters.getUserController().setTeamColor(teamColor, parameters.getIdUser())){
            notifySuccessfulOperation(messageBroker);
        }
        else {
            notifyError(messageBroker,"The chosen team isn't available, please change your selection");
        }
        if (parameters.getUserController().startPlayingGame()){
            parameters.setConnectionState(new WaitingForControl());
        } // todo might need better handling

        //it might be possible that the UI allows selection of unavailable tower, so no use returning false in that case
        return true;
    }
}
