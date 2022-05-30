package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.connectionState.StartingGame;

public class SendReadyHandler extends CommandHandler{

    public SendReadyHandler(){
        commandAccepted = CommandEnum.READY_TO_START;
    }

    /**
     * Sends a message to the server letting it know the user is ready to start the game
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        // This, SendNotReady and StartGame should all be synchronized to some lock
        parameters.getUserLobby().readyLock.lock();

        try{
            parameters.getUserLobby().addReady(parameters.getIdUser());
            //The operation will be successful, but we need to tell the user with an error message
            // that the game isn't ready to start yet, as per the network protocol
            if(parameters.getUserLobby().isGameStarted()){
                //Sets the user's handler to the appropriate configuration
                parameters.setConnectionState(new StartingGame());
                parameters.setUserController(ActiveGames.getGameFromUserId(parameters.getIdUser()));
                notifySuccessfulOperation(messageBroker);
            }
            else{
                notifyError(messageBroker, "GameNotStarting");
            }
        }
        finally {
            parameters.getUserLobby().readyLock.unlock();
        }

        return true;
    }
}
