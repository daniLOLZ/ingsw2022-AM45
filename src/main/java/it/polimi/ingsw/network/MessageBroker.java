package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.game.PhaseEnum;

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
    //private boolean readyForNext;

    public MessageBroker(){
        gson = new Gson();
        //readyForNext = true;
        outFlush();
        inFlush();
    }

    private String serialise(Map<NetworkFieldEnum, Object> map){
        return gson.toJson(map, mapType);
    }

    private Map<NetworkFieldEnum, Object> deserialize(String JSONString){

        Map<NetworkFieldEnum, Object> returnableMap = gson.fromJson(JSONString, mapType);
        returnableMap.replaceAll((k, v) -> (Object) returnableMap.get(k));

        return returnableMap;
    }

    /**
     * Adds an object to the outgoing message. Requires a field name
     * @param fieldName The name used to identify the object
     * @param messageObject The object to be sent
     */
    public void addToMessage(NetworkFieldEnum fieldName, Object messageObject){

        outgoingMessage.put(fieldName, messageObject);
    }

    /**
     * Returns the value of the given fieldName for the first inbound message,
     * or null in case the message received doesn't have the specified field
     * @param fieldName the field name of which one wants to know the value
     * @return the value associated with that field, or null in case the field isn't present
     */
    public Object readField(NetworkFieldEnum fieldName){
        return incomingMessages.get(FIRST_RECEIVED).get(fieldName);
    }

    /**
     * Removes the oldest received message from the incoming messages buffer.
     */
    public void flushFirstMessage(){
        if (incomingMessages.size() > 0) incomingMessages.remove(FIRST_RECEIVED);
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
     */
    public void receive(InputStream sourceInput){

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
            return;
        } catch (IOException e) {
            System.err.println("Error reading message from the network");
            e.printStackTrace();
            return;
        }
        receivedMessage = tempString.toString();

        Map<NetworkFieldEnum, Object> deserializedMessage = deserialize(receivedMessage);

        if (checkValidity(deserializedMessage)) {
            incomingMessages.add(deserializedMessage);
        }
    }



    /**
     * Asks to lock the incoming buffer to process the message
     * @return false if broker is already locked or if there's no incoming message
     */
    /*
    public boolean lock(){//maybe this should just be synchronized
        if (isReadyForNext()) {
            if (incomingMessages.size() == 0) return false;
            readyForNext = false;
            return true;
        }
        return false;
    }
    */

    /**
     * unlocks the incoming buffer and flushes the last executed message
     */
    /*
    public void unlock(){
        if (!isReadyForNext()) {
            readyForNext = true;
            flushFirstMessage();
        }
    }
    */
    /**
     * Checks if the first message in the buffer is in a valid format
     * @return true if the message is valid, false otherwise
     */
    public boolean checkFirstValidity(){
        return checkValidity(incomingMessages.get(FIRST_RECEIVED));
    }

    /**
     * Checks if the message is in a valid format
     * @param message The message to check
     * @return true if the message is valid, false otherwise
     */
    private boolean checkValidity(Map<NetworkFieldEnum, Object> message){

        Object object;
        Class objClass;
        Class neededClass;
        // For each field, check whether it can be cast and return false if an exception is raised
        List<NetworkFieldEnum> keyArray = new ArrayList<>(message.keySet());
        for(NetworkFieldEnum field : keyArray){
            object = message.get(field);

            objClass = object.getClass();

            switch (field){
                case ID_USER:
                case ID_REQUEST:
                case ID_PING_REQUEST:
                case ID_TOWER_COLOR:
                case ID_WIZARD:
                case ID_ASSISTANT:
                case ID_CHARACTER:
                case ID_CLOUD:
                case CHOSEN_ENTRANCE_STUDENT:
                case CHOSEN_ISLAND:
                case STEPS_MN:
                case CHARACTER_CARD_POSITION:
                    // The json reads these parameters as doubles, where they should be ints,
                    // in case they actually are doubles, "cast" to integer
                    if(objClass.equals(Double.class)) objClass = Integer.class;
                    neededClass = Integer.class;
                    break;
                case COMMAND:
                case NICKNAME:
                    neededClass = String.class;
                    break;
                case GAME_RULE:
                    // This would be read as a String, but we need to make sure it's
                    // actually a GameRuleEnum
                    try{
                        GameRuleEnum.fromObjectToEnum(object);
                    } catch (IllegalArgumentException e){
                        objClass = Void.class;
                    }
                    objClass = GameRuleEnum.class;
                    neededClass = GameRuleEnum.class;
                    break;
                case CHOSEN_ENTRANCE_POSITIONS:
                case CHOSEN_ISLANDS:
                case CHOSEN_CARD_POSITIONS:
                    neededClass = Double[].class;
                    break;
                case CHOSEN_STUDENT_COLORS:
                    // This would be read as a String, but we need to make sure it's
                    // actually a StudentEnum
                    try{
                        StudentEnum.fromObjectToEnum(object);
                    } catch (IllegalArgumentException e){
                        objClass = Void.class;
                    }
                    objClass = StudentEnum.class;
                    neededClass = StudentEnum.class;
                    break;
                case GAME_PHASE:
                    // This would be read as a String, but we need to make sure it's
                    // actually a PhaseEnum
                    try{
                        PhaseEnum.fromObjectToEnum(object);
                    } catch (IllegalArgumentException e){
                        objClass = Void.class;
                    }
                    objClass = PhaseEnum.class;
                    neededClass = PhaseEnum.class;
                    break;
                default:
                    // If an unrecognized field is encountered, it's implicitly accepted
                    neededClass = objClass;
                    break;
            }
            if (!objClass.equals(neededClass)){
                return false;
            }
        }
        return true;
    }

    /*
    public boolean isReadyForNext(){
        return readyForNext;
    }
     */

    public static boolean isOfType(Object object, Type type){
        return object.getClass().getTypeName().equals(type.getTypeName());
    }

    public static boolean isOfTheSameType(Object object, Object sample){
        return object.getClass().getTypeName().equals(sample.getClass().getTypeName());
    }

    //for testing purposes
    public List<Map<NetworkFieldEnum, Object>> getIncomingMessages() {
        return incomingMessages;
    }

    //for testing purposes
    public Map<NetworkFieldEnum, Object> getOutgoingMessage() {
        return outgoingMessage;
    }

    /**
     * True if there is at least a message in the incoming buffer
     */
    public synchronized boolean messagePresent() {
        return (incomingMessages.size() > 0);
    }
}
