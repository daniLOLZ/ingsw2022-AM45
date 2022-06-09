package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.connectionState.LookingForLobby;
import it.polimi.ingsw.network.server.ActiveClients;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.server.LoginHandler;

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
            ActiveClients.removeUserIdToClientHandlerAssociation(parameters.getIdUser());
            //quitGame(); //TODO tell the server to shutdown the connection
            return false;
        }
        else {
            messageBroker.addToMessage(NetworkFieldEnum.ID_USER, parameters.getIdUser());
            notifySuccessfulOperation(messageBroker);
        }
        parameters.setConnectionState(new LookingForLobby());
        return true;
    }
}
