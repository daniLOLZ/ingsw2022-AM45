package it.polimi.ingsw.network.commandHandler.asynchronous;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.view.GameInitBean;

public class GameInitUpdateHandler extends AsyncCommandHandler {

    public GameInitUpdateHandler() {
        commandHandled = CommandEnum.SERVER_GAME_INITIALIZATION_STATUS;
    }

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) {

        if(!triggerCondition(parameters)) return false;
        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, commandHandled);

        GameInitBean gameInitBean = new GameInitBean(
                parameters.getUserController().getTowerColorsAvailable(),
                parameters.getUserController().getWizardsAvailable()
        );
        messageBroker.addToMessage(NetworkFieldEnum.BEAN_TYPE, gameInitBean.getBeanType());
        messageBroker.addToMessage(NetworkFieldEnum.BEAN, gameInitBean);

        return true;
    }

    @Override
    public boolean triggerCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return false;
        return parameters.getUserController().isPlayerCreationModified();
    }

    @Override
    public void clearCondition(ClientHandlerParameters parameters) {
        if(parameters.getUserController() == null) return;
        parameters.getUserController().setPlayerCreationModified(false);
    }
}
