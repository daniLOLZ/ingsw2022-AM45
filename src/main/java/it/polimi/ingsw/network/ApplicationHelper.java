package it.polimi.ingsw.network;

public class ApplicationHelper {
    public static int getIntFromBrokerField(Object readField){
        Double dRead = (Double) readField;
        return dRead.intValue();
    }
}
