package it.polimi.ingsw.network;

public enum NetworkFieldEnum {
    COMMAND("command"),
    ID_USER("idUser"),
    ID_REQUEST("idRequest"),
    ID_PING_REQUEST("idPingRequest"),
    NICKNAME("nickname"),
    GAME_RULE("gameRule"),
    LOBBY_BEAN("lobbyBean"),
    GAME_BEAN("gameBean"), // inferred, may be modified
    ID_TOWER_COLOR("idTowerColor"),
    ID_WIZARD("idWizard"),
    ID_ASSISTANT("idAssistant"),
    ID_CHARACTER("idCharacter"),
    ID_CLOUD("idCloud"),
    CHOSEN_ENTRANCE_STUDENT("chosenEntranceStudent"), // For now this is separated from CHOSEN_ENTRANCE_POSITIONS
                                                               // to have a stronger separations between normal play and
                                                               // character card invocations
    CHOSEN_ISLAND("chosenIsland"), // Same happens here as with CHOSEN_ENTRANCE_STUDENT
    STEPS_MN("stepsMN"),
    CHARACTER_CARD_POSITION("characterCardPosition"),
    CHOSEN_ENTRANCE_POSITIONS("chosenEntrancePositions"),
    CHOSEN_ISLANDS("chosenIslands"),
    CHOSEN_CARD_POSITIONS("chosenCardPositions"),
    CHOSEN_STUDENT_COLORS("chosenStudentColors"),

    SERVER_REPLY_MESSAGE("serverReplyMessage"),
    SERVER_REPLY_STATUS("serverReplyStatus"),
    ERROR_STATE("errorState"),
    TURN("turn"),
    GAME_PHASE("gamePhase"),
    POSITION_MN("positionMN"),
    NUM_TOWERS("numTowers"),
    IDS_ASSISTANTS_PLAYED("idsAssistantsPlayed"),
    //TODO other game state related fields
    ENTRANCE_REQUIRED("entranceRequired"),
    COLORS_REQUIRED("colorRequired"),
    ISLANDS_REQUIRED("islandsRequired"),
    ON_CARD_REQUIRED("onCardRequired");

    private String fieldName;

    NetworkFieldEnum(String fieldName){
        this.fieldName = fieldName;
    }

    public static NetworkFieldEnum fromObjectToEnum (Object field){
        return NetworkFieldEnum.valueOf((String)field);
    }
}
