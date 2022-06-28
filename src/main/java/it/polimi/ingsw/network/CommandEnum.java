package it.polimi.ingsw.network;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.NetworkFieldEnum.*;

public enum CommandEnum {

    QUIT("Quit",                                                new NetworkFieldEnum[] {ID_USER}),
    PING("Ping",                                                new NetworkFieldEnum[] {ID_USER, ID_PING_REQUEST}),
    PONG("Pong",                                                new NetworkFieldEnum[] {ID_USER, ID_PING_REQUEST}),

    // Out of game commands
    CONNECTION_REQUEST("Connection Request",                    new NetworkFieldEnum[] {NICKNAME, ID_REQUEST}),
    PLAY_GAME("Play Game",                                      new NetworkFieldEnum[] {ID_USER, ID_REQUEST, GAME_RULE}),
    READY_TO_START("Ready to Start",                            new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    NOT_READY("Not Ready",                                      new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    LEAVE_LOBBY("Leave Lobby",                                  new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    START_GAME("Start Game",                                    new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    SELECT_WIZARD("Select Wizard",                              new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_WIZARD}),
    SELECT_TOWER_COLOR("Select Tower Color",                    new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_TOWER_COLOR}),

    // In game commands
    CHOOSE_ASSISTANT("Choose Assistant",                        new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_ASSISTANT}),
    SELECT_STUDENT("Select Student",                            new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ENTRANCE_STUDENT}),
    PUT_IN_HALL("Put in Hall",                                  new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    PUT_IN_ISLAND("Put in Island",                              new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ISLAND}),
    DESELECT_STUDENT("Deselect Student",                        new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    MOVE_MN("Move MN",                                          new NetworkFieldEnum[] {ID_USER, ID_REQUEST, STEPS_MN}),
    CHOOSE_CLOUD("Choose Cloud",                                new NetworkFieldEnum[] {ID_USER, ID_REQUEST, ID_CLOUD}),
    END_TURN("End of turn",                                     new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),
    SELECT_CHARACTER("Select Character",                        new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHARACTER_CARD_POSITION}),
    SELECT_STUDENT_COLORS("Select Student Colors",                new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_STUDENT_COLORS}),
    SELECT_ENTRANCE_STUDENTS("Select Entrance Student",         new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ENTRANCE_POSITIONS}),
    // For now this and SELECT_STUDENT are kept separate to better separate these two different scenarios
    SELECT_ISLAND_GROUP("Select Island Group",                  new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_ISLAND_CHAR}),
    SELECT_STUDENTS_ON_CARD("Select Student on Card",            new NetworkFieldEnum[] {ID_USER, ID_REQUEST, CHOSEN_CARD_POSITIONS}),
    PLAY_CHARACTER("Play Character",                            new NetworkFieldEnum[] {ID_USER, ID_REQUEST}),

    //From here we're talking about server commands
    SERVER_LOBBY_STATUS("Lobby status",                         new NetworkFieldEnum[] {ASYNC_ID_REQUEST, BEAN_TYPE, BEAN}),
    SERVER_LOBBY_START("Lobby starting",                     new NetworkFieldEnum[] {ASYNC_ID_REQUEST}),
    SERVER_GAME_INITIALIZATION_STATUS("Game init status",       new NetworkFieldEnum[] {ASYNC_ID_REQUEST, BEAN_TYPE, BEAN}),
    SERVER_GAME_START("Game starting",                       new NetworkFieldEnum[] {ASYNC_ID_REQUEST}),
    SERVER_YOUR_TURN("Your turn to move",                       new NetworkFieldEnum[] {ASYNC_ID_REQUEST, ASYNC_GAME_PHASE}),
    SERVER_GAME_UPDATE("Game model update",                     new NetworkFieldEnum[] {ASYNC_ID_REQUEST, ASYNC_VIEW}),
    SERVER_USER_DISCONNECTED("User disconnected",               new NetworkFieldEnum[] {ASYNC_ID_REQUEST, ASYNC_ID_USER, ASYNC_USER_NICKNAME}),
    SERVER_GAME_WON("Game won",                                 new NetworkFieldEnum[]{ASYNC_ID_REQUEST, ASYNC_WINNER});


    public final String command;
    public final NetworkFieldEnum[] allowedFields;

    CommandEnum(String command, NetworkFieldEnum[] allowedFields) {
        this.command = command;
        this.allowedFields = allowedFields;
    }

    /**
     * Gets all the commands that are sent from the client first (synchronous)
     * Bases itself on the implementation of the async version of this command
     * @return a list of only the synchronous commands
     */
    public static List<CommandEnum> getSyncCommands(){
        return Arrays.stream(values())
                .filter(x -> !getAsyncCommands().contains(x))
                .collect(Collectors.toList());
    }

    /**
     * Gets all the commands that are sent from the server first (asynchronous)
     * @return a list of only the asynchronous commands
     */
    public static List<CommandEnum> getAsyncCommands(){
        return Arrays.stream(values())
                .filter(x -> x.name().matches("^SERVER(.*)"))
                .collect(Collectors.toList());
    }

    /**
     * Gets and returns the fields required for this command (excluding network ones) <br>
     * Not included: ID_USER, ID_REQUEST, ID_PING_REQUEST
     * @param command the command chosen
     * @return a list of fields required to correctly form the message
     */
    public static List<NetworkFieldEnum> getFieldsNeeded(CommandEnum command){
        List<NetworkFieldEnum> retList = Arrays.stream(command.allowedFields).collect(Collectors.toList());
        retList.remove(ID_USER);
        retList.remove(ID_REQUEST);
        retList.remove(ID_PING_REQUEST);
        return retList;
    }

    /**
     * Gets the enum value from the read object
     * @param command the command in Object form
     * @return the converted enum
     */
    public static CommandEnum fromObjectToEnum (Object command){
        return CommandEnum.valueOf((String)command);
    }
}

