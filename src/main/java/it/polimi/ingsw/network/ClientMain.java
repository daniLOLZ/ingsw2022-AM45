package it.polimi.ingsw.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {

    private Socket socket;
    private String hostname;
    private int portNumber;
    private String nickname;
    // private MessageBroker broker; // we will need a way to couple this broker to the one in ClientHandler
    private int idUser;  // May be removed

    public ClientMain(String hostname, int portNumber, String nickname) {
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.nickname = nickname;
    }

    public static void main(String[] args){
        ClientMain client = new ClientMain("127.0.0.1", 54321, "mock1"); //TODO remove hardcoded network parameters

        if(!client.login(client.getHostname(), client.getPortNumber(), client.getNickname())){
            System.err.println("Error logging in");
            return;
        }

    }

    /**
     * Connects to a Server and sends the user nickname via private methods
     * @param hostname the host to connect to
     * @param port the host's port to connect to
     * @param username a nickname chosen by the user to be used during the game
     */
    public boolean login(String hostname, int port, String username){

        if(!connect(hostname, port)){ // Might be substituted with an exception
            System.err.println("Couldn't connect to host " + hostname + "on port " + port);
            return false;
        }
        if(!sendNickname(username)){
            System.err.println("Username rejected");
            try{
                socket.close();
            } catch (IOException e){
                System.err.println(e.getMessage());
            } finally {
                System.err.println("Connection closed");
            }
            return false;
        }
        return true;
    }

    /**
     * Establishes a connection to a Server
     * @param hostname the host ip address
     * @param port the host's port to connect to
     */
    private boolean connect(String hostname, int port){

        try {
            socket = new Socket(hostname, port);
        }
        catch (UnknownHostException e){
            System.err.println("Can't find host " + hostname);
            System.exit(1);
            return false;
        }
        catch (IOException e){
            System.err.println("Couldn't get I/O for the connection to " +
                    hostname);
            System.exit(1);
            return false;
        }
        return true;
    }

    /**
     * Sends the chosen nickname to the server to evaluate whether it's acceptable or not
     * @param nickname the nickname chosen by the user
     * @return a boolean : true if the username was accepted
     */
    private boolean sendNickname(String nickname){
        ObjectOutputStream serverOut;
        ObjectInputStream serverIn;
        boolean returnValue = false;

        try {
            serverOut = new ObjectOutputStream(socket.getOutputStream());
            serverIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        try {
            serverOut.writeChars(nickname);
            returnValue = serverIn.readBoolean();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return returnValue; //TODO very quick and dirty implementation, to fix
    }

    public String getNickname() {
        return nickname;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
