package it.polimi.ingsw.network;

public enum CommandEnum {

    QUIT("Quit"),
    CONNECTION_REQUEST("Connection Request"),
    PLAY_GAME("Play Game"),
    READY_TO_START("Ready to Start"),
    NOT_READY("Not Ready"),
    LEAVE_LOBBY("Leave Lobby"),
    START_GAME("Start Game"),
    SELECT_WIZARD("Select Wizard"),
    SELECT_TOWER_COLOR("Select Tower Color"),
    ASK_FOR_CONTROL("Ask for Control"),
    CHOOSE_ASSISTANT("Choose Assistant"),
    SELECT_STUDENT("Select Student"),
    PUT_IN_HALL("Put in Hall"),
    PUT_IN_ISLAND("Put in Island"),
    DESELECT_STUDENT("Deselect Student"),
    MOVE_MN_TO_ISLAND("Move MN to Island"),
    CHOOSE_CLOUD("Choose Cloud"),
    SELECT_CHARACTER("Select Character"),
    SELECT_STUDENT_COLOR("Select Student Color"),
    SELECT_ENTRANCE_STUDENT("Select Entrance Student"), //TODO duplicated of SELECT_STUDENT?
    SELECT_ISLAND_GROUP("Select Island Group"),
    SELECT_STUDENT_ON_CARD("Select Student on Card"),
    PLAY_CHARACTER("Play Character");

    public final String command;

    CommandEnum(String command) {
        this.command = command;
    }
}
