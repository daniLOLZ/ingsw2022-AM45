package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ActiveClients;
import it.polimi.ingsw.network.server.ActiveGames;
import it.polimi.ingsw.network.server.ClientHandlerParameters;

public class QuitGameHandler extends CommandHandler{

    public QuitGameHandler(){
        commandAccepted = CommandEnum.QUIT;
    }

    /**
     * Closes the connection for this user
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        if(parameters.getUserController() == null){
            // The user decides to quit the game while still in the lobby, so the other users
            // shouldn't be kicked out automatically
            parameters.getUserLobby().removePlayer(parameters.getIdUser());
        }
        else {
            // The game is already starting or already started, so the game shall end for
            // every player
            parameters.getUserController().lostConnectionHandle(parameters.getIdUser());
            //Awful way to do it
            ActiveClients.getHandlerFromId(parameters.getIdUser()).connectionLostAlert("The user quit the application");//TODO how to communicate that the connection should be shut down?
        }

        return true;
    }
}
