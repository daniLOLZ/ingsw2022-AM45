package it.polimi.ingsw.network;

import java.util.ArrayList;

//TODO change ALL occurrences of command (variables, classes, ...) to messageType
public enum CommandEnum {

    QUIT("Quit",                                        new String[] {"idUser"}),
    CONNECTION_REQUEST("Connection Request",            new String[] {"nickname", "idRequest"}),
    PLAY_GAME("Play Game",                              new String[] {"idUser", "idRequest", "rules"}),
    READY_TO_START("Ready to Start",                    new String[] {"idUser", "idRequest"}),
    NOT_READY("Not Ready",                              new String[] {"idUser", "idRequest"}),
    LEAVE_LOBBY("Leave Lobby",                          new String[] {"idUser", "idRequest"}),
    START_GAME("Start Game",                            new String[] {"idUser", "idRequest"}),
    SELECT_WIZARD("Select Wizard",                      new String[] {"idUser", "idRequest", "idWizard"}),
    SELECT_TOWER_COLOR("Select Tower Color",            new String[] {"idUser", "idRequest", "idTowerColor"}),
    ASK_FOR_CONTROL("Ask for Control",                  new String[] {"idUser", "idRequest"}),
    CHOOSE_ASSISTANT("Choose Assistant",                new String[] {"idUser", "idRequest", "idAssistant"}),
    SELECT_STUDENT("Select Student",                    new String[] {"idUser", "idRequest", "position"}),
    PUT_IN_HALL("Put in Hall",                          new String[] {"idUser", "idRequest"}),
    PUT_IN_ISLAND("Put in Island",                      new String[] {"idUser", "idRequest", "idIslandGroup"}),
    DESELECT_STUDENT("Deselect Student",                new String[] {"idUser", "idRequest"}),
    MOVE_MN_TO_ISLAND("Move MN to Island",              new String[] {"idUser", "idRequest", "steps"}),
    CHOOSE_CLOUD("Choose Cloud",                        new String[] {"idUser", "idRequest", "cloud"}),
    SELECT_CHARACTER("Select Character",                new String[] {"idUser", "idRequest", "characterCardPos"}),
    SELECT_STUDENT_COLOR("Select Student Color",        new String[] {"idUser", "idRequest", "studentColor"}),
    SELECT_ENTRANCE_STUDENT("Select Entrance Student",  new String[] {"idUser", "idRequest", "atEntranceSelected"}), //TODO duplicated of SELECT_STUDENT?
    SELECT_ISLAND_GROUP("Select Island Group",          new String[] {"idUser", "idRequest", "idIslandGroup"}),
    SELECT_STUDENT_ON_CARD("Select Student on Card",    new String[] {"idUser", "idRequest", "onCardSelected"}),
    PLAY_CHARACTER("Play Character",                    new String[] {"idUser", "idRequest", "characterCardPos"});

    public final String command;
    public final String[] allowedFields;

    CommandEnum(String command, String[] allowedFields) {
        this.command = command;
        this.allowedFields = allowedFields;
    }

}

