package it.polimi.ingsw.network;

import java.util.HashMap;
import java.util.Map;

public class LoginHandler {

    private static Integer progressiveId = 0;

    private static Map<Integer, String> idUserToNickname = new HashMap<>();

    private boolean checkIfNicknamePresent(String nickname){

        return idUserToNickname.containsValue(nickname);
    }

    /**
     * Attempts to log a user with the proposed nickname and idUser
     * @param nickname The nickname with which you want to log
     * @param idUser The proposed idUser (requires to be unique)
     * @return true if login was successful. false otherwise
     */
    public boolean login(String nickname, int idUser){

        if (!checkIfNicknamePresent(nickname)) return false;
        idUserToNickname.put(idUser, nickname);
        return true;
    }

    /**
     * @return The first available idUser
     */
    public int getUserId(){

        synchronized (progressiveId){
            while (idUserToNickname.containsKey(progressiveId)) progressiveId++;

            return progressiveId++;
        }
    }
}
