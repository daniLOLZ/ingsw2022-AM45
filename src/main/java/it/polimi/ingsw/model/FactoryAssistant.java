package it.polimi.ingsw.model;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FactoryAssistant {

    public static Assistant getAssistant(int id){
        long turn, steps;
        turn = 0;
        steps = 0;
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\User\\IdeaProjects\\ingsw2022-AM45" +
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
