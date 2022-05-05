package it.polimi.ingsw.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {

    private int progressiveIdRequest;
    private Socket socket;
    private String hostname;
    private int portNumber;
    private String nickname;
    private MessageBroker broker;
    // private MessageBroker broker; // we will need a way to couple this broker to the one in ClientHandler
    private int idUser;  // May be removed

    public ClientMain(String hostname, int portNumber, String nickname) {
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.nickname = nickname;
        this.broker = new MessageBroker();
        this.progressiveIdRequest = 0;
    }

    /**
     * Increases idRequest and returns it
     * @return The increased idRequest
     */
    private int increaseAndGetRequestId(){

        progressiveIdRequest++;
        return progressiveIdRequest;
    }

    public static void main(String[] args){
        ClientMain client = new ClientMain("127.0.0.1", 54321, "username"); //TODO remove hardcoded network parameters

        Scanner scanner = new Scanner(System.in);

        System.out.println("Insert your username");
        String inputNickname = scanner.nextLine();
        client.setNickname(inputNickname);

        if(!client.login(client.getHostname(), client.getPortNumber(), client.getNickname())){
            System.err.println("Error logging in");
            return;
        }
        System.out.println("Username " + client.nickname + " was accepted");

        String userInput;
        while(true){ // generic game loop
            userInput = scanner.nextLine();
        }
    }

    /**
     * Connects to a Server and sends the user nickname via private methods
     * @param hostname the host to connect to
     * @param port the host's port to connect to
     * @param nickname a nickname chosen by the user to be used during the game
     */
    public boolean login(String hostname, int port, String nickname){

        if(!connect(hostname, port)){ // Might be substituted with an exception
            System.err.println("Couldn't connect to host " + hostname + "on port " + port);
            return false;
        }
        if(!sendNickname(nickname)) {
            System.err.println("Nickname rejected");
            try {
                socket.close();
            } catch (IOException e) {
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
     * @return a boolean : true if the nickname was accepted
     */
    private boolean sendNickname(String nickname){

        broker.addToMessage("command", CommandEnum.CONNECTION_REQUEST);
        broker.addToMessage("nickname", nickname);
        addIdRequest();
        OutputStream outStream;
        InputStream inStream;
        try {
            outStream = socket.getOutputStream();
            inStream = socket.getInputStream();
        } catch (IOException e) {
            System.err.println("Couldn't get input/output streams");
            e.printStackTrace();
            return false;
        }
        broker.send(outStream);
        System.out.println("Sent message to the server");
        broker.receive(inStream);
        System.out.println("Received reply from the server");

        return "OK".equals(
                (String) broker.readField("serverReplyMessage")); // TODO maybe we should have this be less hardcoded?

    }

    /**
     * Adds the idRequest field to the current outgoing message
     */
    private void addIdRequest(){
        broker.addToMessage("idRequest", increaseAndGetRequestId());
    }

    /**
     * Compares the idRequest of the Server reply and returns true if it matches with the current progressiveIdRequest
     * @return true if the Server reply has the same idRequest as the last request that has been sent
     */
    private boolean checkIdRequest(){
        return progressiveIdRequest == (int) broker.readField("idRequest");
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
