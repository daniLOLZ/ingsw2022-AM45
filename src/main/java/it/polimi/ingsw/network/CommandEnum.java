package it.polimi.ingsw.network;

import java.util.ArrayList;

//TODO change ALL occurrences of command (variables, classes, ...) to messageType
public enum CommandEnum {

    QUIT("Quit",                                        new String[] {"idUser"}),
    CONNECTION_REQUEST("Connection Request",            new String[] {"nickname", ""}),
    PLAY_GAME("Play Game",                              new String[] {"idUser"}),
    READY_TO_START("Ready to Start",                    new String[] {"idUser"}),
    NOT_READY("Not Ready",                              new String[] {"idUser"}),
    LEAVE_LOBBY("Leave Lobby",                          new String[] {"idUser"}),
    START_GAME("Start Game",                            new String[] {"idUser"}),
    SELECT_WIZARD("Select Wizard",                      new String[] {"idUser"}),
    SELECT_TOWER_COLOR("Select Tower Color",            new String[] {"idUser"}),
    ASK_FOR_CONTROL("Ask for Control",                  new String[] {"idUser"}),
    CHOOSE_ASSISTANT("Choose Assistant",                new String[] {"idUser"}),
    SELECT_STUDENT("Select Student",                    new String[] {"idUser"}),
    PUT_IN_HALL("Put in Hall",                          new String[] {"idUser"}),
    PUT_IN_ISLAND("Put in Island",                      new String[] {"idUser"}),
    DESELECT_STUDENT("Deselect Student",                new String[] {"idUser"}),
    MOVE_MN_TO_ISLAND("Move MN to Island",              new String[] {"idUser"}),
    CHOOSE_CLOUD("Choose Cloud",                        new String[] {"idUser"}),
    SELECT_CHARACTER("Select Character",                new String[] {"idUser"}),
    SELECT_STUDENT_COLOR("Select Student Color",        new String[] {"idUser"}),
    SELECT_ENTRANCE_STUDENT("Select Entrance Student",  new String[] {"idUser"}), //TODO duplicated of SELECT_STUDENT?
    SELECT_ISLAND_GROUP("Select Island Group",          new String[] {"idUser"}),
    SELECT_STUDENT_ON_CARD("Select Student on Card",    new String[] {"idUser"}),
    PLAY_CHARACTER("Play Character",                    new String[] {"idUser"});

    public final String command;
    public final String[] allowedFields;

    CommandEnum(String command, String[] allowedFields) {
        this.command = command;
        this.allowedFields = allowedFields;
    }
}
