package it.polimi.ingsw.model;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FactoryAssistant {

    private final static int numCards = 40;
    /**
     * create and return Assistant corresponding with chosen id
     * @param id > 0 &&
     *           < numCards
     * @return
     */
    public static Assistant getAssistant(int id){
        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();
        long turn, steps;
        turn = 0;
        steps = 0;
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(directoryName +
                    "\\src\\main\\java\\it\\polimi\\ingsw\\model\\AssistantsCards.json"));
            JSONObject JsonObject = (JSONObject)  obj;
            JSONArray turnOrderList = (JSONArray)  JsonObject.get("turnOrder");
            JSONArray stepsList = (JSONArray) JsonObject.get("steps");
            obj =  turnOrderList.get(id % FactoryWizard.numOfCardsPerWizard);
            turn =(long) obj;
            obj =  stepsList.get(id % FactoryWizard.numOfCardsPerWizard);
            steps = (long) obj;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Assistant(id,(int)steps,(int)turn);
    }
}
