package it.polimi.ingsw.network;

import java.util.ArrayList;
import java.util.List;

public class ApplicationHelper {

    public static int getIntFromBrokerField(Object readField){
        Double dRead = (Double) readField;
        return dRead.intValue();
    }

    public static List<Integer> getIntListFromBrokerField(Object readField) {
        List<Object> readList = (List<Object>) readField;
        List<Integer> retList = new ArrayList<>();
        Double dRead;
        for(Object o : readList){
            dRead = (Double) o;
            retList.add(dRead.intValue());
        }
        return retList;
    }
}
