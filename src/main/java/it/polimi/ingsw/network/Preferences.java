package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import java.util.Map;


public class Preferences {

    private static final String filename = "/NetworkParameters.json";
    private static final int standardPort = 54321;
    private static final Gson gson = new Gson();

    public static int readPortFromJson(){

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();    //Map<NetworkFieldEnum, Object> is a generic Type. It needs to be specified
        Map<String, Object> map;
        Reader reader;
        Integer returnPort;
        InputStream stream;

        try {
            stream = Preferences.class.getResourceAsStream(filename);
            reader = new InputStreamReader(stream);
        } catch (NullPointerException e) {
            System.err.println("Preferences file hasn't been found, using standard port");
            e.printStackTrace();
            return standardPort;
        }

        map = gson.fromJson(reader, mapType);
        Double parsedValue = (Double)map.get("defaultPort");
        returnPort = parsedValue.intValue();
        return returnPort;
    }
}
