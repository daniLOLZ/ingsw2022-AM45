package it.polimi.ingsw.network;

import com.google.gson.internal.LinkedTreeMap;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.AdvancedGameBoardBean;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Translates from a LinkedTreeMap to a bean of the correct type
 */
public class BeanTranslator {

    //todo handle various exceptions in case it's not the correct type of bean

    /**
     * Takes a map from the gson serialization and returns a LobbyBean
     * @param map A gson map containing the mapping String -> Object, where the string is the name of
     *            a parameter in the bean
     * @return A new lobbyBean containing the information that the map had
     */
    public static LobbyBean deserializeLobbyBean(LinkedTreeMap<String, Object> map){
        List<String> nicknames = (List<String>) map.get("nicknames");
        List<Boolean> readyPlayers = (List<Boolean>) map.get("readyPlayers");
        boolean gameStarted = (boolean) map.get("gameStarted");
        Double dhost = (Double) map.get("host");
        Integer host = dhost.intValue();
        return new LobbyBean(nicknames, readyPlayers, gameStarted, host);
    }

    public static GameInitBean deserializeGameInitBean(LinkedTreeMap<String, Object> map){
        List<TeamEnum> chosenColors = toListOfEnum(TeamEnum.class, (ArrayList<String>) map.get("chosenColors"));
        List<WizardEnum> chosenWizards = toListOfEnum(WizardEnum.class, (ArrayList<String>) map.get("chosenWizards"));
        Boolean allSetGameStarted = (Boolean) map.get("allSetGameStarted");
        return new GameInitBean(chosenColors,chosenWizards, allSetGameStarted);
    }

    public static AdvancedGameBoardBean deserializeAdvancedGameBoardBean(LinkedTreeMap<String, Object> map){
        List<Integer> idIslandGroups = (List<Integer>) map.get("idIslandGroups");
        List<Integer> idAssistantsPlayed = (List<Integer>) map.get("idAssistantsPlayed");
        List<Integer> idPlayers = (List<Integer>) map.get("idPlayers");
        Double dcurrentPlayerId = (Double) map.get("currentPlayerId");
        Integer currentPlayerId = dcurrentPlayerId.intValue();
        Double dturn = (Double) map.get("turn");
        Integer turn = dturn.intValue();
        String phase = (String) map.get("phase");
        int numGameCoins = (int) map.get("numGameCoins");
        List<Integer> idCharacterCards = (List<Integer>) map.get("idCharacterCards");
        return new AdvancedGameBoardBean(idIslandGroups,idAssistantsPlayed, idPlayers, currentPlayerId, turn, phase, numGameCoins, idCharacterCards);
    }

    public static CloudBean deserializeCloudBean(LinkedTreeMap<String, Object> map){
        int idCloud = (int) map.get("idCloud");
        List<StudentEnum> readyPlayers = toListOfEnum(StudentEnum.class, (ArrayList<String>)map.get("students"));
        return new CloudBean(idCloud, readyPlayers);
    }

    public static AdvancedIslandGroupBean deserializeAdvancedIslandGroupBean(LinkedTreeMap<String, Object> map){
        //todo
        return null;
    }
    //todo gli altri traduttori

    /**
     * Translates the list from a gson map into a list of appropriate enums
     * @param enumClass the type of enum one wishes to translate to
     * @param list the list (an ArrayList<String>) to get the data from
     * @param <T> A generic enum
     * @return a list of enums of type T reflecting the list structure
     */
    private static <T extends Enum<T>> List<T> toListOfEnum (Class<T> enumClass, ArrayList<String> list){
        List<T> retList = new ArrayList<>();
        for(String element : list){
            retList.add(T.valueOf(enumClass, element));
        }
        return retList;
    }
}
