package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.game.PhaseEnum;

import java.io.*;
import java.lang.reflect.Type;

import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageBroker {

    private final static int FIRST_RECEIVED = 0;

    private final Gson gson;
    private static final Type mapType = new TypeToken<Map<NetworkFieldEnum, Object>>() {}.getType();    //Map<NetworkFieldEnum, Object> is a generic Type. It needs to be specified
                                                                                                        //when using gson functions toJson and fromJson
    private final String connectionResetString = "Connection Reset";

    private BlockingQueue<Map<NetworkFieldEnum, Object>> incomingSyncMessages;
    private BlockingQueue<Map<NetworkFieldEnum, Object>> incomingAsyncMessages;
    private Map<NetworkFieldEnum, Object> currentIncomingSyncMessage;
    private Map<NetworkFieldEnum, Object> currentIncomingAsyncMessage;
    private Map<NetworkFieldEnum, Object> outgoingMessage;

    private final int BLOCKING_QUEUE_CAPACITY = 100;
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
     * If a field was set already, it gets overwritten
     * @param fieldName The name used to identify the object
     * @param messageObject The object to be sent
     */
    public void addToMessage(NetworkFieldEnum fieldName, Object messageObject){
        outgoingMessage.put(fieldName, messageObject);
    }

    /**
     * Returns the value of the given fieldName for the first SYNCHRONOUS inbound message, taken from the queue
     * or null in case the message received doesn't have the specified field
     * @param fieldName the field name of which one wants to know the value
     * @return the value associated with that field, or null in case the field isn't present
     * @throws NullPointerException if there is no message read with the
     *                              waitSyncMessage() method
     */
    public Object readField(NetworkFieldEnum fieldName) throws NullPointerException {
        return currentIncomingSyncMessage.get(fieldName);
    }

    /**
     * Returns the value of the given fieldName for the first ASYNCHRONOUS inbound message, taken from the queue
     * or null in case the message received doesn't have the specified field
     * @param fieldName the field name of which one wants to know the value
     * @return the value associated with that field, or null in case the field isn't present
     * @throws NullPointerException if there is no message read with the
     *                              waitAsyncMessage() method
     */
    public Object readAsyncField(NetworkFieldEnum fieldName) throws NullPointerException {
        return currentIncomingAsyncMessage.get(fieldName);
    }

    /**
     * Waits for a new message on the synchronous message buffer
     * Acts as a buffer between the queue and the list of messages received.
     * Puts the first message from the Queue into the current incoming synchronous message
     * To be called before any readField() is called
     * @throws InterruptedException if the thread is interrupted before a message was available
     */
    public void waitSyncMessage() throws InterruptedException{
        currentIncomingSyncMessage = incomingSyncMessages.take();
    }

    /**
     * Waits for a new message on the synchronous message buffer
     * Acts as a buffer between the queue and the list of messages received.
     * Puts the first message from the Queue into the current incoming synchronous message,
     * waiting for at most waitTimeMillis milliseconds before returning
     * To be called before any readField() is called
     * @param waitTimeMillis the maximum amount of time in milliseconds to wait before this method returns
     * @return true if the polling was successful, false if the timer expired before a message was available
     * @throws InterruptedException if the thread is interrupted before a message was available
     */
    public boolean waitSyncMessage(Integer waitTimeMillis) throws InterruptedException{
        Map<NetworkFieldEnum, Object> message = incomingSyncMessages.poll(waitTimeMillis, TimeUnit.MILLISECONDS);
        if(message != null){
            currentIncomingSyncMessage = message;
            return true;
        }
        return false;
    }

    /**
     * Waits for a new message on the asynchronous message buffer
     * Acts as a buffer between the queue and the list of messages received.
     * Puts the first message from the Queue into the current incoming asynchronous message
     * To be called before any readAsyncField() is called
     * @throws InterruptedException if the thread is interrupted before a message was available
     */
    public void waitAsyncMessage() throws InterruptedException{
        currentIncomingAsyncMessage = incomingAsyncMessages.take();
    }

    /**
     * Removes the oldest received synchronous message from the incoming messages buffer.
     */
    public void flushFirstSyncMessage(){
        currentIncomingSyncMessage = null;
    }

    /**
     * Removes the oldest received asynchronous message from the incoming messages buffer.
     */
    public void flushFirstAsyncMessage(){
        currentIncomingAsyncMessage = null;
    }

    private void outFlush(){
        outgoingMessage = new HashMap<>();
    }

    private void inFlush(){
        incomingSyncMessages = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        incomingAsyncMessages = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
    }

    /**
     * Send the message stored in the object as a JSON file to the specified output stream
     * and then empties the message body for the next message to send
     * @param destinationOutput the OutputStream of the host to send the message to
     */
    public void send(OutputStream destinationOutput) throws IOException {

        String sendable = serialise(outgoingMessage);

        // System.out.println("sending" + sendable + " to " + "???"); // Either we get the ip via the socket or we don't log this line

        try {
            destinationOutput.write(sendable.getBytes(StandardCharsets.UTF_8));
            destinationOutput.flush();
        } catch (IOException e) {
            System.err.println("Couldn't send the message via the network");
            e.printStackTrace();
            throw e;
        }
        outFlush();
    }

    /**
     * Receives and stores the received message as an HashMap in the incoming message buffer
     * @param sourceInput the InputStream of the host to read the message from
     */
    public void receive(InputStream sourceInput) throws IOException  //IOException includes SocketException
    {
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
            //e.printStackTrace();
            throw e;

        } catch (IOException e) {
            System.err.println("Error reading message from the network");
            //e.printStackTrace();
            throw e;

        }
        receivedMessage = tempString.toString();
        if(receivedMessage.charAt(0) != '{') {
            System.err.println("Bad message incoming, dropping");
            return;
        }
        Map<NetworkFieldEnum, Object> deserializedMessage = deserialize(receivedMessage);

        convertListsToArrays(deserializedMessage);

        if (checkValidity(deserializedMessage)) {
            if(isAsynchronous(deserializedMessage)){
                if (!incomingAsyncMessages.offer(deserializedMessage)) {
                    System.err.println("ERROR: Message buffer full, the message was dropped");
                }
            }
            else {
                if (!incomingSyncMessages.offer(deserializedMessage)) {
                    System.err.println("ERROR: Message buffer full, the message was dropped");
                }
            }
        }
    }

    private void convertListsToArrays(Map<NetworkFieldEnum, Object> deserializedMessage) {
        //extremely crude, change if possible

        for (NetworkFieldEnum field : deserializedMessage.keySet()){
            if(field.equals(NetworkFieldEnum.CHOSEN_ENTRANCE_POSITIONS) ||
                field.equals(NetworkFieldEnum.CHOSEN_CARD_POSITIONS) ||
                field.equals(NetworkFieldEnum.CHOSEN_ISLANDS)){
                ArrayList<Double> values = (ArrayList<Double>) deserializedMessage.get(field);
                deserializedMessage.put(field,
                        (double[])(values.stream()
                                .mapToDouble(Double::doubleValue)
                                .toArray()));
            }
            else if(field.equals(NetworkFieldEnum.CHOSEN_STUDENT_COLORS)){
                ArrayList<String> colors = (ArrayList<String>) deserializedMessage.get(field);
                deserializedMessage.put(field,
                                 colors.stream()
                                .map(StudentEnum::fromObjectToEnum)
                                .toArray(StudentEnum[]::new)
                );
            }
        }
    }

    /**
     * An asynchronous message is a message sent by the server to the client when a client didn't
     * specifically send a message first. An asynchronous message is characterized either by its
     * request id, which will have different names depending on the type of message
     * (specified in the relative Network field enumerator)
     * @param deserializedMessage the message for which twe want to check its asynchronousness
     * @return true if the message is asynchronous
     */
    private boolean isAsynchronous(Map<NetworkFieldEnum, Object> deserializedMessage) {
        //Here we will check whether a message is one or the other looking at the id_request field
        // for sync messages
        if(deserializedMessage.get(NetworkFieldEnum.ID_REQUEST) == null &&
            deserializedMessage.get(NetworkFieldEnum.ASYNC_ID_REQUEST) != null){
            return true;
        }
        else return false;
    }

    /**
     * Checks if the first message in the buffer is in a valid format
     * @return true if the message is valid, false otherwise
     */
    @Deprecated
    public boolean checkFirstValidity() throws InterruptedException{
        return checkValidity(incomingSyncMessages.take());
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
                case ASYNC_ID_REQUEST:
                case ASYNC_ID_USER:
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
                    // The json reads these parameters as doubles, where they should be ints,
                    // in case they actually are doubles, "cast" to integer
                    if(objClass.equals(double[].class)) objClass = int[].class;
                    neededClass = int[].class;
                    break;
                case CHOSEN_STUDENT_COLORS:
                    // This would be read as a String, but we need to make sure it's
                    // actually a StudentEnum
                    neededClass = StudentEnum[].class;
                    break;
                case GAME_PHASE:
                case ASYNC_GAME_PHASE:
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
                case ASYNC_WINNER:
                    // This would be read as a String, but we need to make sure it's
                    // actually a TeamEnum
                    try{
                        TeamEnum.fromObjectToEnum(object);
                    } catch (IllegalArgumentException e){
                        objClass = Void.class;
                    }
                    objClass = TeamEnum.class;
                    neededClass = TeamEnum.class;
                    break;
                    //todo case for async_view

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

    public static boolean isOfType(Object object, Type type){
        return object.getClass().getTypeName().equals(type.getTypeName());
    }

    public static boolean isOfTheSameType(Object object, Object sample){
        return object.getClass().getTypeName().equals(sample.getClass().getTypeName());
    }

    //for testing purposes
    public BlockingQueue<Map<NetworkFieldEnum, Object>> getIncomingSyncMessages() {
        return incomingSyncMessages;
    }

    //for testing purposes
    public Map<NetworkFieldEnum, Object> getOutgoingMessage() {
        return outgoingMessage;
    }

    /**
     * True if there is at least a message in the incoming buffer
     */
    @Deprecated
    public synchronized boolean messagePresent() {
        return (incomingSyncMessages.size() > 0);
    }


}
