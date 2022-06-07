
package it.polimi.ingsw.network.commandHandler.synchronous;

import it.polimi.ingsw.network.commandHandler.UnexecutableCommandException;
import it.polimi.ingsw.network.server.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;
import it.polimi.ingsw.view.GameInitBean;

@Deprecated
public class GetGameInitializationStatus extends CommandHandler{

    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {
        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        parameters.getUserController().startLock.lock();
        try {
            if (parameters.getUserController().isGameStarted()) { // If the game has started here for this client...
                parameters.setConnectionState(new WaitingForControl());
            } else if (parameters.getUserController().startPlayingGame()) { // ...or starts here for everyone...
                parameters.setConnectionState(new WaitingForControl());
            }
        } catch(RuntimeException e){
            System.err.println("Error creating game");
            //quit game
        }
        finally {
          parameters.getUserController().startLock.unlock();
        }
        GameInitBean gameInitBean = new GameInitBean(
                parameters.getUserController().getTowerColorsAvailable(),
                parameters.getUserController().getWizardsAvailable()
        );
        messageBroker.addToMessage(NetworkFieldEnum.BEAN_TYPE, gameInitBean.getBeanType());
        messageBroker.addToMessage(NetworkFieldEnum.BEAN, gameInitBean);
        notifySuccessfulOperation(messageBroker);
        return true;
    }
}
