
package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;

public class GetGameInitializationStatus extends CommandHandler{

    public GetGameInitializationStatus(){
        this.commandAccepted = CommandEnum.GET_GAME_INITIALIZATION_STATUS;
    }
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
                parameters.getUserController().getTowerColorsChosen(),
                parameters.getUserController().getWizardsChosen(),
                parameters.getUserController().allSelectionsMadeGameStart() //...Then the bean will have immediately after the value true,
                                                                            // and the client will be notified of the change
        );
        messageBroker.addToMessage(NetworkFieldEnum.BEAN_TYPE, gameInitBean.getBeanEnum());
        messageBroker.addToMessage(NetworkFieldEnum.BEAN, gameInitBean);
        notifySuccessfulOperation(messageBroker);
        return true;
    }
}
