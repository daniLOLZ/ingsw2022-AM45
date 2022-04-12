package it.polimi.ingsw.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FactoryAssistant {

    public final static int numCards = 40;
    private final static String filePath = "/src/resources/AssistantsCards.json";
    public final static Assistant standardAssistant = new Assistant(0,0,0);
    /**
     * create and return Assistant corresponding with chosen id
     * @param id > 0 &&
     *           <= numCards
     * @return Assistant card with chosen id with right steps and turnOrder values
     * return a standard card with values set to 0 if id is wrong
     * @throws IOException if file is not found
     * @throws ParseException if something goes wrong reading file json
     */
    public static Assistant getAssistant(int id) throws IOException, ParseException {
        if(id<=0 || id > numCards)
            return standardAssistant;

        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();
        long turn, steps;

        //READING JSON
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(directoryName + filePath));
        JSONObject JsonObject = (JSONObject)  obj;
        JSONArray turnOrderList = (JSONArray)  JsonObject.get("turnOrder");
        JSONArray stepsList = (JSONArray) JsonObject.get("steps");

        //IMPORTANT POSITIONS GO FROM 1 TO NUMCARDSPERWIZARD(10)
        int position;
        position= id % FactoryWizard.numOfCardsPerWizard;
        if(position == 0)
            position = FactoryWizard.numOfCardsPerWizard;

        turn = (long)  turnOrderList.get(position); //JSONArray return object, I cast to right type
        steps = (long)  stepsList.get(position);    //JSONArray return object, I cast to right type

        return new Assistant(id,(int)steps,(int)turn);
    }
}
