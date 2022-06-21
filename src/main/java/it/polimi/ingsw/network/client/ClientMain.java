

//TODO move remaining classes to the view

package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIApplication;
import it.polimi.ingsw.view.UserInterface;

/**
 * Entry point for the client application, instances
 * an actual client
 */
public class ClientMain {

    private ClientReceiver receiver;
    private UserInterface userInterface;
    private ClientSender sender;
    private InitialConnector initialConnector;

    /**
     * @param hostname the host to connect to
     * @param portNumber the port of the server to connect to
     * @param guiMode false for CLI, true for GUI
     */
    public ClientMain(String hostname, int portNumber, boolean guiMode){

        initialConnector = new InitialConnector(hostname, portNumber);
        receiver = new ClientReceiver(initialConnector);
        sender = new ClientSender(initialConnector);
        if(guiMode) {
            userInterface = new GUI(initialConnector);
        }
        else {
            userInterface = new CLI(initialConnector);
        }

    }

    public void startApplication(){
        initialConnector.setSender(sender);
        initialConnector.setReceiver(receiver);

        receiver.setUI(userInterface);
        userInterface.setSender(sender);

        userInterface.startInterface();
    }

}

