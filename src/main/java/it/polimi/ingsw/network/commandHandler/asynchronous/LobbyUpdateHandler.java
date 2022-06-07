package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.view.LobbyBean;

public class LobbyUpdateHandler extends AsyncCommandHandler {

    public LobbyUpdateHandler() {
        commandHandled = CommandEnum.SERVER_LOBBY_STATUS;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserLobby() == null) return false;
        return parameters.getUserLobby().isModified();
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {

        if(!triggerCondition(parameters)) return false;
        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);

        LobbyBean lobbyBean = parameters.getUserLobby().toBean();
        messageBroker.addToMessage(NetworkFieldEnum.BEAN_TYPE, lobbyBean.getBeanType());
        messageBroker.addToMessage(NetworkFieldEnum.BEAN, lobbyBean);
        return true;
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserLobby() != null){
            parameters.getUserLobby().setModified(false);
        }
    }
}
