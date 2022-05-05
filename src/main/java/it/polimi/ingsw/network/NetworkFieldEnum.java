package it.polimi.ingsw.network;

public enum NetworkFieldEnum {
    COMMAND("command"),
    ID_USER("idUser"),
    ID_REQUEST("idRequest"),
    NICKNAME("nickname"),
    GAME_RULE("gameRule"),
    ID_TOWER_COLOR("idTowerColor"),
    ID_WIZARD("idWizard"),
    ID_ASSISTANT("idAssistant"),
    ID_CHARACTER("idCharacter"),
    ID_CLOUD("idCloud"),
    STEPS_MN("stepsMN"),
    CHOSEN_ENTRANCE_POSITIONS("chosenEntrancePositions"),
    CHOSEN_ISLANDS("chosenIslands"),
    CHOSEN_CARD_POSITIONS("chosenCardPositions"),
    CHOSEN_STUDENT_COLORS("chosenStudentColors"),

    SERVER_REPLY_MESSAGE("serverReplyMessage"),
    SERVER_REPLY_STATUS("serverReplyStatus"),
    ERROR_STATE("errorState"),
    TURN("turn"),
    POSITION_MN("positionMN"),
    NUM_TOWERS("numTowers"),
    IDS_ASSISTANTS_PLAYED("idsAssistantsPlayed"),
    //TODO other game state related fields
    ENTRANCE_REQUIRED("entranceRequired"),
    COLOR_REQUIRED("colorRequired"),
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
