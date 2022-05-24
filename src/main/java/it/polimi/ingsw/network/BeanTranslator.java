package it.polimi.ingsw.network;

import com.google.gson.internal.LinkedTreeMap;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.AdvancedGameBoardBean;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.view.LobbyBean;

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
        List<StudentEnum> readyPlayers = (List<StudentEnum>) map.get("students");
        return new CloudBean(idCloud, readyPlayers);
    }

    public static AdvancedIslandGroupBean deserializeAdvancedIslandGroupBean(LinkedTreeMap<String, Object> map){
        //todo
        return null;
    }
    //todo gli altri traduttori
}
