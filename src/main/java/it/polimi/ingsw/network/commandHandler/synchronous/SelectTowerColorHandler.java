package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.network.ApplicationHelper;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

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

        int teamColorId = ApplicationHelper.getIntFromBrokerField(messageBroker.readField(NetworkFieldEnum.ID_TOWER_COLOR));
        TeamEnum teamColor = TeamEnum.getTeamFromId(teamColorId);

        if(parameters.getUserController().setTeamColor(teamColor, parameters.getIdUser())){
            notifySuccessfulOperation(messageBroker);
            if(parameters.getUserController().startPlayingGame()){
                //todo? maybe something needs to happen here?
            }
        }
        else {
            notifyError(messageBroker,"The chosen team isn't available, please change your selection");
        }

        //it might be possible that the UI allows selection of unavailable tower, so no use returning false in that case
        return true;
    }
}
