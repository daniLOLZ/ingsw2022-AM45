package it.polimi.ingsw.network;

import java.util.HashMap;
import java.util.Map;

public class LoginHandler {

    private static Integer progressiveId = 0;

    private static Map<Integer, String> idUserToNickname = new HashMap<>();

    private static boolean checkIfNicknamePresent(String nickname){
        return idUserToNickname.containsValue(nickname);
    }

    /**
     * Attempts to log a user with the proposed nickname and idUser
     * @param nickname The nickname with which you want to log
     * @param idUser The proposed idUser (requires to be unique)
     * @return true if login was successful. false otherwise
     */
    public static boolean login(String nickname, int idUser){

        synchronized (idUserToNickname) {
            if (nickname.length() == 0) return false;
            if (checkIfNicknamePresent(nickname)) return false;
            idUserToNickname.put(idUser, nickname);
        }
        return true;
    }

    /**
     * Creates a new, unique idUser not used by previous connections
     * @return The first available idUser
     */
    public static int getNewUserId(){

        synchronized (progressiveId){
            while (idUserToNickname.containsKey(progressiveId)) progressiveId++;

            return progressiveId++;
        }
    }

    /**
     * Returns the nickname associated with this id at the time of login
     * @param id the idUser of the user
     * @return the nickname of the user with the specified id
     */
    public static String getNicknameFromId(Integer id){
        return idUserToNickname.get(id);
    }
}
