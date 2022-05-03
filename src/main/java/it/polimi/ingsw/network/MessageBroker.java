package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import java.nio.charset.StandardCharsets;
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

    public Object readField(String fieldName){
        Object fieldValue = incomingMessage.get(fieldName);
        return fieldValue;
    }

    private void outFlush(){
        outgoingMessage = new HashMap<>();
    }

    private void inFlush(){
        incomingMessage = new HashMap<>();
    }

    /**
     * Send the message stored in the object as a JSON file to the specified output stream
     * @param destinationOutput the OutputStream of the host to send the message to
     */
    public void send(OutputStream destinationOutput){

        String sendable = serialise(outgoingMessage);

        System.out.println("sending" + sendable + " to " + "???");

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
     * Receives and stores the received message as an HashMap in the incoming message attribute
     * @param sourceInput the InputStream of the host to read the message from
     * @return true if the message received is valid
     */
    public boolean receive(InputStream sourceInput){
        String receivedMessage;
        StringBuilder tempString = new StringBuilder();
        int rawReadInt;
        char rawChar;
        boolean endOfMessage = false;
        int numberOfOpenCurlyBrackets = 0;
        try {
            while(!endOfMessage){
                rawReadInt = sourceInput.read();
                rawChar = (char)rawReadInt;

                // Signals the end of the message based on the amount of curly bracket pairs
                if(rawChar == '{') numberOfOpenCurlyBrackets++;
                else if (rawChar == '}') numberOfOpenCurlyBrackets--;
                if (numberOfOpenCurlyBrackets == 0) {
                    endOfMessage = true;
                }
                tempString.append(rawChar);
            }
            receivedMessage = tempString.toString();

            System.out.println("message received");
        } catch (IOException e) {
            System.err.println("Error reading message from the network");
            e.printStackTrace();
            return false;
        }
        incomingMessage = deserialize(receivedMessage);

        return checkValidity();
    }

    /**
     * Check if the incoming message is in a valid format
     * @return true if the message is valid, false otherwise
     */
    public boolean checkValidity(){

        String field;
        Object object;
        // For each field, check whether it can be cast and return false if an exception is raised
        for(int fieldNumber = 0; fieldNumber < incomingMessage.size(); fieldNumber++){
            field = incomingMessage.keySet().iterator().next();
            object = incomingMessage.get(field);

            switch (field){
                case "idUser" : {
                    try{
                        int primitveInt = (int) object;
                    } catch(ClassCastException e) {
                        return false;
                    }
                }
                case "nickname" : {
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


    public static boolean isOfType(Object object, Type type){
        return object.getClass().getTypeName().equals(type.getTypeName());
    }

    public static boolean isOfTheSameType(Object object, Object sample){
        return object.getClass().getTypeName().equals(sample.getClass().getTypeName());
    }

    //TODO IMPLEMENT IDREQUEST CHECKS
}
