package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;

public class SelectWizardHandler extends CommandHandler{

    /**
     * The user selects a wizard from the four available, this method calls the game controller to know whether the
     * wizard chosen is available, and notifies it to the user
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.SELECT_WIZARD)) throw new UnexecutableCommandException();

        Integer idWizard = (Integer)messageBroker.readField(NetworkFieldEnum.ID_WIZARD);
        if(parameters.getUserController().setWizard(idWizard, parameters.getIdUser())){
            notifySuccessfulOperation(messageBroker);
        }
        else {
            notifyError(messageBroker,"The chosen wizard isn't available, please change your selection");
        }
        if (parameters.getUserController().startPlayingGame()){
            parameters.setConnectionState(new WaitingForControl());
        } // todo might need better handling

        //it might be possible that the UI allows selection of unavailable wizard, so no use returning false in that case
        return true;
    }
}
