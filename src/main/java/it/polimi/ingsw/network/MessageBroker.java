package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageBroker {

    private final static int FIRST_RECEIVED = 0;

    private final Gson gson;
    private static final Type mapType = new TypeToken<Map<NetworkFieldEnum, Object>>() {}.getType();    //Map<NetworkFieldEnum, Object> is a generic Type. It needs to be specified
                                                                                                        //when using gson functions toJson and fromJson
    private final String connectionResetString = "Connection Reset";

    private List<Map<NetworkFieldEnum, Object>> incomingMessages;
    private Map<NetworkFieldEnum, Object> outgoingMessage;
    private boolean readyForNext;

    public MessageBroker(){
        gson = new Gson();
        readyForNext = true;
        outFlush();
        inFlush();
    }

    private String serialise(Map<NetworkFieldEnum, Object> map){
        return gson.toJson(map, mapType);
    }

    private Map<NetworkFieldEnum, Object> deserialize(String JSONString){
        return gson.fromJson(JSONString, mapType);
    }

    /**
     * Adds an object to the outgoing message. Requires a field name
     * @param fieldName The name used to identify the object
     * @param messageObject The object to be sent
     */
    public void addToMessage(NetworkFieldEnum fieldName, Object messageObject){

        outgoingMessage.put(fieldName, messageObject);
    }

    public Object readField(NetworkFieldEnum fieldName){
        return incomingMessages.get(FIRST_RECEIVED).get(fieldName);
    }

    /**
     * Removes the oldest received message from the incoming messages buffer.
     * Allows the broker to give access to next received message
     */
    private void flushFirstMessage(){
        incomingMessages.remove(FIRST_RECEIVED);
        readyForNext = true;
    }

    private void outFlush(){
        outgoingMessage = new HashMap<>();
    }

    private void inFlush(){
        incomingMessages = new ArrayList<>();
    }

    /**
     * Send the message stored in the object as a JSON file to the specified output stream
     * @param destinationOutput the OutputStream of the host to send the message to
     */
    public void send(OutputStream destinationOutput){

        String sendable = serialise(outgoingMessage);

        // System.out.println("sending" + sendable + " to " + "???"); // Either we get the ip via the socket or we don't log this line

        try {
            destinationOutput.write(sendable.getBytes(StandardCharsets.UTF_8));
            destinationOutput.flush();
        } catch (IOException e) {
            System.err.println("Couldn't send the message via the network");
            e.printStackTrace();
        }
        outFlush();
    }

    /**
     * Receives and stores the received message as an HashMap in the incoming message buffer
     * @param sourceInput the InputStream of the host to read the message from
     * @return true if the message received is valid
     */
    public boolean receive(InputStream sourceInput){

        readyForNext = false;
        String receivedMessage;
        StringBuilder tempString = new StringBuilder();
        int rawReadInt;
        char rawChar;
        boolean endOfMessage = false;
        boolean inString = false; //signals whether we are reading the content of a String
        boolean escaped = false; //signals whether the next character is meant to be read as raw text
        int numberOfOpenCurlyBrackets = 0;
        try {
            while(!endOfMessage) {
                rawReadInt = sourceInput.read();
                rawChar = (char) rawReadInt;

                if (!escaped) {
                    if (rawChar == '\\') escaped = true;
                    if (rawChar == '"') inString = !inString;
                }
                else escaped = false;

                //Signals the end of the message based on the amount of curly bracket pairs
                if (!inString && rawChar == '{') numberOfOpenCurlyBrackets++;
                else if (!inString && rawChar == '}') numberOfOpenCurlyBrackets--;
                if (numberOfOpenCurlyBrackets == 0) {
                    endOfMessage = true;
                }
                tempString.append(rawChar);
            }
        } catch (SocketException e) {
            System.err.println("Socket error, couldn't read the message");
            System.err.println(e.getMessage());
            //TODO handle user disconnection by passing it to ClientHandler somehow
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Error reading message from the network");
            e.printStackTrace();
            return false;
        }
        receivedMessage = tempString.toString();

        System.out.println("message received");
        incomingMessages.add(deserialize(receivedMessage));

        return checkFirstValidity();
    }

    /**
     * Check if the first received message is in a valid format
     * @return true if the message is valid, false otherwise
     */
    public boolean checkFirstValidity(){

        Object object;
        // For each field, check whether it can be cast and return false if an exception is raised
        List<NetworkFieldEnum> keyArray = new ArrayList<>(incomingMessages.get(FIRST_RECEIVED).keySet());
        for(NetworkFieldEnum field : keyArray){
            object = incomingMessages.get(FIRST_RECEIVED).get(field);

            switch (field){
                case ID_USER : {
                    try{
                        int primitiveInt = (int) object;
                    } catch(ClassCastException e) {
                        return false;
                    }
                }
                case NICKNAME : {
                    try{
                        String string = (String) object;
                    } catch(ClassCastException e) {
                        return false;
                    }
                }
                // TODO add other fields
                // TODO check for cleaner implementation
            }
        }
        return true;
    }

    public boolean isReadyForNext(){
        return readyForNext;
    }

    public static boolean isOfType(Object object, Type type){
        return object.getClass().getTypeName().equals(type.getTypeName());
    }

    public static boolean isOfTheSameType(Object object, Object sample){
        return object.getClass().getTypeName().equals(sample.getClass().getTypeName());
    }

    //TODO IMPLEMENT IDREQUEST CHECKS
}
