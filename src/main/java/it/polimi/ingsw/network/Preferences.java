package it.polimi.ingsw.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import java.util.Map;


public class Preferences {

    private static final String filename = "/jsonFiles/NetworkParameters.json";
    private static final int standardPort = 54321;
    private static final String standardHostname = "127.0.0.1";
    private static final Gson gson = new Gson();

    /**
     * Reads the default network port from a json file
     * @return the port read from the file
     */
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

        return ApplicationHelper.getIntFromBrokerField(map.get("defaultPort"));
    }

    /**
     * Reads the default network host from a json file
     * @return the host name read from the file
     */
    public static String readHostnameFromJson(){

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();    //Map<NetworkFieldEnum, Object> is a generic Type. It needs to be specified
        Map<String, Object> map;
        Reader reader;
        String returnString;
        InputStream stream;

        try {
            stream = Preferences.class.getResourceAsStream(filename);
            reader = new InputStreamReader(stream);
        } catch (NullPointerException e) {
            System.err.println("Preferences file hasn't been found, using standard hostname");
            e.printStackTrace();
            return standardHostname;
        }

        map = gson.fromJson(reader, mapType);
        returnString = (String)map.get("defaultHostname");
        return returnString;
    }
}
