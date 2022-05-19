

//TODO move remaining classes to the view

package it.polimi.ingsw.network;

import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.GUI.GUIMain;
import it.polimi.ingsw.view.UserInterface;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Entry point for the client application
 * It evaluates the parameters passed on launch and instances
 * an actual player
 */

public class ClientMain {



    /**
     * Handles quitting the game on the client side
     */
    private void quitGame() {
        //todo
    }

    /**
     * This logic will be moved in the UI and is just a mock class
     * The part of the game where the user can send commands after the login phase.
     * Here it's just a loop of sending and receiving messages
     */
    /*
    private void mockViewGameLoop() throws IOException {

        while(isConnected()) {

            sendUserInput();
            receiveServerReply();

            //After askForControl(planning) is over, starts to continuously send askForControl(action)
        }
    }

     */

    /**
     * Receives the reply from the server and outputs the response message
     * @throws IOException
     */
    /*
    private void receiveServerReply() throws IOException {
        mainBroker.receive(mainSocket.getInputStream());
        //View visualization of the received data somehow
        for(NetworkFieldEnum field : NetworkFieldEnum.values()){
            if(mainBroker.readField(field) != null){
                System.out.println(mainBroker.readField(field));
            }
        }
    }
     */

    /**
     * mock class
     * Asks the user to input a command and the relative fields
     * @throws IOException
     */
    /*
    private void sendUserInput() throws IOException {
        List<NetworkFieldEnum> fieldsNeeded;
        List<String> fieldsUserInput;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the command");
        String inputCommand = scanner.nextLine();
        CommandEnum commandRead = CommandEnum.valueOf(inputCommand);

        fieldsNeeded = CommandEnum.getFieldsNeeded(commandRead);
        fieldsUserInput = new ArrayList<>();
        for(NetworkFieldEnum field : fieldsNeeded){
            System.out.println("Insert the value for the field: "+ field.toString());
            fieldsUserInput.add(scanner.nextLine());
        }

        mainBroker.addToMessage(NetworkFieldEnum.COMMAND, commandRead);
        for(int field = 0; field < fieldsNeeded.size(); field++){
            mainBroker.addToMessage(fieldsNeeded.get(field), fieldsUserInput.get(field));
        }
        mainBroker.send(mainSocket.getOutputStream());

        //After askForControl(planning) is over, starts to continuously send askForControl(action)
    }
    */
}

