package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.view.LobbyBean;

public class GetLobbyStatusHandler extends CommandHandler{

    public GetLobbyStatusHandler(){
        this.commandAccepted = CommandEnum.GET_LOBBY_STATUS;
    }
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {
        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        LobbyBean lobbyBean = parameters.getUserLobby().getBean();
        messageBroker.addToMessage(NetworkFieldEnum.LOBBY_BEAN, lobbyBean);
        notifySuccessfulOperation(messageBroker);

        return true;
    }
}
