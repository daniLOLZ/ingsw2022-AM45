package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.io.OutputStream;

public class PingHandler implements CommandHandler{



    /**
     * Answers the ping message with a pong message
     * @param messageBroker The broker containing the message to execute
     * @param outStream The output stream the command response(s) will be sent
     * @return true if the operation was successful. false otherwise
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, OutputStream outStream) throws UnexecutableCommandException{

        if( !(messageBroker.readField(NetworkFieldEnum.COMMAND) == CommandEnum.PING)) throw new UnexecutableCommandException();

        messageBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.PONG);
        messageBroker.addToMessage(NetworkFieldEnum.ID_PING_REQUEST, messageBroker.readField(NetworkFieldEnum.ID_PING_REQUEST));
        messageBroker.send(outStream);

        return true;
    }
}
