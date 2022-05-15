package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.ClientHandlerParameters;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

public class PlayCharacterHandler extends CommandHandler{

    /**
     * The player plays the card for which they (optionally) selected the requirements
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.PLAY_CHARACTER)) throw new UnexecutableCommandException();

        //        Integer cardPosition = (Integer)broker.readField(NetworkFieldEnum.CHARACTER_CARD_POSITION);
        if(parameters.getUserController().playCard()){
            super.notifySuccessfulOperation(messageBroker);
            parameters.setConnectionState(parameters.getCallbackConnectionState());
            return true;
        }
        else {
            notifyError(messageBroker,"Couldn't play the character card");
            return false;
        }
    }
}
