package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;

import java.io.OutputStream;

public interface CommandHandler {

    /**
     * Processes the command stored inside the given messageBroker if it's responsibility of the specific class
     * and replyes to the Client
     *
     * @param messageBroker The broker containing the message to execute
     * @param outStream     The output stream the command response(s) will be sent
     * @return true if the operation was successful. false otherwise
     */
    boolean executeCommand(MessageBroker messageBroker, OutputStream outStream) throws UnexecutableCommandException;
}
