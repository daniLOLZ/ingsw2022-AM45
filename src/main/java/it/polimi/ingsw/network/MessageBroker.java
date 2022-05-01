package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Map;

public class MessageBroker {

    private final Gson gson;
    private static final Type mapType = new TypeToken<Map<String, Object>>() {}.getType(); //Map<String, Object> is a generic Type. It needs to be specified
                                                                                           //when using gson functions toJson and fromJson

    private Map<String, Object> outgoingMessage, incomingMessage;

    public MessageBroker(){
        gson = new Gson();
        outFlush();
        inFlush();
    }

    private String serialise(Map<String, Object> map){
        return gson.toJson(map, mapType);
    }

    private Map<String, Object> deserialize(String JSONString){
        return gson.fromJson(JSONString, mapType);
    }

    /**
     * Adds an object to the outgoing message. Requires a field name
     * @param fieldName The name used to identify the object
     * @param messageObject The object to be sent
     */
    public void addToMessage(String fieldName, Object messageObject){

        outgoingMessage.put(fieldName, messageObject);
    }

    private void outFlush(){
        outgoingMessage = new HashMap<>();
    }

    private void inFlush(){
        incomingMessage = new HashMap<>();
    }

    /**
     * Send the message stored in the object as a JSON file to the specified IP
     * @param ReceiverIP The IP to send the message to
     */
    public void send(String ReceiverIP){

        String sendable = serialise(outgoingMessage);

        //TODO actually send the message
        System.out.println("sending" + sendable + " to " + ReceiverIP);

        outFlush();
    }

    /**
     * Stores the received message as an HashMap in the incoming message attribute
     * @param JSONString The received JSONString
     */
    public boolean receive(String JSONString){

        incomingMessage = deserialize(JSONString);

        return checkValidity();
    }

    /**
     * Check if the incoming message is in a valid format
     * @return true if the message is valid, false otherwise
     */
    public boolean checkValidity(){

        //TODO
        return true;
    }
}
