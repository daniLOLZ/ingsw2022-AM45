package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.view.LobbyBean;

@Deprecated
public class GetLobbyStatusHandler extends CommandHandler{

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {
        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        LobbyBean lobbyBean = parameters.getUserLobby().toBean();
        messageBroker.addToMessage(NetworkFieldEnum.BEAN_TYPE, lobbyBean.getBeanType());
        messageBroker.addToMessage(NetworkFieldEnum.BEAN, lobbyBean);
        notifySuccessfulOperation(messageBroker);

        return true;
    }
}
