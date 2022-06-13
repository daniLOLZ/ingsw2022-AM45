package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.VirtualViewBean;
import it.polimi.ingsw.model.game.PhaseEnum;

public enum NetworkFieldEnum {
    COMMAND("command", CommandEnum.class),
    ID_USER("idUser", int.class),
    ID_REQUEST("idRequest", int.class),
    ID_PING_REQUEST("idPingRequest", int.class),
    NICKNAME("nickname", String.class),
    GAME_RULE("gameRule", GameRuleEnum.class),
    ID_TOWER_COLOR("idTowerColor", int.class),
    ID_WIZARD("idWizard", int.class),
    ID_ASSISTANT("idAssistant", int.class),
    ID_CHARACTER("idCharacter", int.class),
    ID_CLOUD("idCloud", int.class),
    CHOSEN_ENTRANCE_STUDENT("chosenEntranceStudent", int.class), // For now this is separated from CHOSEN_ENTRANCE_POSITIONS
                                                               // to have a stronger separations between normal play and
                                                               // character card invocations
    CHOSEN_ISLAND("chosenIsland", int.class), // Same happens here as with CHOSEN_ENTRANCE_STUDENT
    STEPS_MN("stepsMN", int.class),
    CHARACTER_CARD_POSITION("characterCardPosition", int.class),
    CHOSEN_ENTRANCE_POSITIONS("chosenEntrancePositions", int[].class),
    CHOSEN_ISLANDS("chosenIslands", int[].class),
    CHOSEN_CARD_POSITIONS("chosenCardPositions", int[].class),
    CHOSEN_STUDENT_COLORS("chosenStudentColors", StudentEnum[].class),

    // SERVER-ONLY FIELDS
    SERVER_REPLY_MESSAGE("serverReplyMessage", String.class),
    SERVER_REPLY_STATUS("serverReplyStatus", String.class),
    ERROR_STATE("errorState", String.class),
    BEAN_TYPE("beanType", BeanEnum.class),
    BEAN("bean", Bean.class),
    TURN("turn", int.class),
    GAME_PHASE("gamePhase", PhaseEnum.class),
    MORE_STUDENTS_TO_MOVE("moreStudentsToMove", boolean.class),
    POSITION_MN("positionMN", int.class),
    NUM_TOWERS("numTowers", int.class),
    IDS_ASSISTANTS_PLAYED("idsAssistantsPlayed", int[].class),
    //TODO other game state related fields
    ENTRANCE_REQUIRED("entranceRequired", int[].class),
    COLORS_REQUIRED("colorRequired", int[].class),
    ISLANDS_REQUIRED("islandsRequired", int[].class),
    ON_CARD_REQUIRED("onCardRequired", int[].class),

    //Asynchronous fields
    ASYNC_ID_REQUEST("asyncIdRequest", int.class),
    ASYNC_ID_USER("asyncIdUser", int.class),
    ASYNC_GAME_PHASE("asyncGamePhase", PhaseEnum.class),
    ASYNC_VIEW("asyncView", VirtualViewBean.class);

    private final String fieldName;
    private final Class fieldClass;

    NetworkFieldEnum(String fieldName, Class fieldClass){
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
    }

    public static Class getClass(NetworkFieldEnum field){
        return field.fieldClass;
    }

    public static NetworkFieldEnum fromObjectToEnum (Object field){
        return NetworkFieldEnum.valueOf((String)field);
    }
}
