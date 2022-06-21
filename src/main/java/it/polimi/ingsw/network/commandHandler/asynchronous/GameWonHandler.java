package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.LookingForLobby;
import it.polimi.ingsw.network.server.ActiveGames;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class GameWonHandler extends AsyncCommandHandler{

    public GameWonHandler() {
        commandHandled = CommandEnum.SERVER_GAME_WON;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {
        if(!triggerCondition(parameters)) return false;
        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);

        TeamEnum winner = parameters.getUserController().getWinnerTeam();
        messageBroker.addToMessage(NetworkFieldEnum.ASYNC_WINNER, winner);

        //This command being executed means the game is over, and thus the controller should reflect this  new situation
        parameters.setConnectionState(new LookingForLobby());
        parameters.setUserLobby(null);
        parameters.setUserController(null);
        ActiveGames.deleteUserAssociation(parameters.getIdUser());

        return true;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return false;
        else return parameters.getUserController().isGameWon();
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        /* This event doesn't need be cleared, since the controller will
            be killed regardless after the game is over.
            Instead, take care of the controller and delete everything from the now finished game
         */
        ActiveGames.removeEndedGames();
        //todo how do we delete the lobby though
    }
}
