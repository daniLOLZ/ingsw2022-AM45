package it.polimi.ingsw.network;

import static it.polimi.ingsw.network.NetworkFieldEnum.*;

//TODO change ALL occurrences of command (variables, classes, ...) to messageType
public enum CommandEnum {

    QUIT("Quit",                                        new NetworkFieldEnum[] {ID_USER}),
    CONNECTION_REQUEST("Connection Request",            new NetworkFieldEnum[] {ID_REQUEST}),
    PLAY_GAME("Play Game",                              new NetworkFieldEnum[] {ID_USER, ID_REQUEST, GAME_RULE}),
    READY_TO_START("Ready to Start",                    new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    NOT_READY("Not Ready",                              new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    LEAVE_LOBBY("Leave Lobby",                          new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    START_GAME("Start Game",                            new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    SELECT_WIZARD("Select Wizard",                      new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_WIZARD}),
    SELECT_TOWER_COLOR("Select Tower Color",            new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_TOWER_COLOR}),
    ASK_FOR_CONTROL("Ask for Control",                  new NetworkFieldEnum[] {ID_USER, ID_REQUEST, GAME_PHASE}),
    CHOOSE_ASSISTANT("Choose Assistant",                new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_ASSISTANT}),
    SELECT_STUDENT("Select Student",                    new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ENTRANCE_STUDENT}),
    PUT_IN_HALL("Put in Hall",                          new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    PUT_IN_ISLAND("Put in Island",                      new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ISLAND}),
    DESELECT_STUDENT("Deselect Student",                new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    MOVE_MN_TO_ISLAND("Move MN to Island",              new NetworkFieldEnum[] {ID_USER, ID_REQUEST, STEPS_MN}),
    CHOOSE_CLOUD("Choose Cloud",                        new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_CLOUD}),
    SELECT_CHARACTER("Select Character",                new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHARACTER_CARD_POSITION}),
    SELECT_STUDENT_COLOR("Select Student Color",        new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_STUDENT_COLORS}),
    SELECT_ENTRANCE_STUDENTS("Select Entrance Student",  new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ENTRANCE_POSITIONS}), //TODO duplicated of SELECT_STUDENT?
    // For now this and SELECT_STUDENT are kept separate to better separate these two different scenarios
    SELECT_ISLAND_GROUP("Select Island Group",          new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ISLANDS}),
    SELECT_STUDENT_ON_CARD("Select Student on Card",    new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_CARD_POSITIONS}),
    PLAY_CHARACTER("Play Character",                    new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHARACTER_CARD_POSITION});
    //TODO add commands to exchange game information (beans)

    public final String command;
    public final NetworkFieldEnum[] allowedFields;

    CommandEnum(String command, NetworkFieldEnum[] allowedFields) {
        this.command = command;
        this.allowedFields = allowedFields;
    }

    public static CommandEnum fromObjectToEnum (Object command){
        return CommandEnum.valueOf((String)command);
    }
}

