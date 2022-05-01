package it.polimi.ingsw.network;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner; // For Testing

public class ClientHandler implements Runnable{
    private Socket socket;
    private boolean isConnected;
    private int idUser;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
//          idUser = LoginHandler.getNewUserId();

            // Testing if objects can be read to and from, to be deleted
            /* This block will later be handled by the MessageBroker */
            ObjectInputStream clientInput = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream clientOutput = new ObjectOutputStream(socket.getOutputStream());

            String testString = (String) clientInput.readObject();
            System.out.println(testString);
            boolean toReturn;
            if(!testString.equals("mock1")){
                toReturn = false;
            }
            else toReturn = true;

            clientOutput.writeBoolean(toReturn);
            /* end of block */

            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
