package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;
import it.polimi.ingsw.network.connectionState.WaitingForControl;

public class SelectWizardHandler extends CommandHandler{

    public SelectWizardHandler(){
        commandAccepted = CommandEnum.SELECT_WIZARD;
    }

    /**
     * The user selects a wizard from the four available, this method calls the game controller to know whether the
     * wizard chosen is available, and notifies it to the user
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        Double dIdWizard = (Double) messageBroker.readField(NetworkFieldEnum.ID_WIZARD);
        int idWizard = dIdWizard.intValue();
        // todo: Change everything into WizardEnum later

        if(parameters.getUserController().setWizard(idWizard*10, parameters.getIdUser())){
            notifySuccessfulOperation(messageBroker);
        }
        else {
            notifyError(messageBroker,"The chosen wizard isn't available, please change your selection");
        }

        //it might be possible that the UI allows selection of unavailable wizard, so no use returning false in that case
        return true;
    }
}
