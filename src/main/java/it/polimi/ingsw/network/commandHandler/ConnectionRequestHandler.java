package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.connectionState.LookingForLobby;

public class ConnectionRequestHandler extends CommandHandler{

    public ConnectionRequestHandler(){
        commandAccepted = CommandEnum.CONNECTION_REQUEST;
    }

    /**
     * Handles the connections of a new user, checking whether their nickname
     * satisfies the requirement of uniqueness
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        boolean loginSuccessful;
        loginSuccessful= LoginHandler.login((String)messageBroker.readField(NetworkFieldEnum.NICKNAME),
                                            parameters.getIdUser());
        if(!loginSuccessful){
            notifyError(messageBroker,"Nickname already taken");
            //quitGame(); //TODO tell the server to shutdown the connection
            return false;
        }
        else notifySuccessfulOperation(messageBroker);
        parameters.setConnectionState(new LookingForLobby());
        return true;
    }
}
