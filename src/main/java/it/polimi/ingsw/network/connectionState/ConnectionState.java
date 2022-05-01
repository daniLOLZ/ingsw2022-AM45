package it.polimi.ingsw.network.connectionState;

import it.polimi.ingsw.network.CommandEnum;

import java.util.ArrayList;
import java.util.List;

public abstract class ConnectionState {

    private final List<CommandEnum> allowedCommands;

    public ConnectionState(){

        allowedCommands = new ArrayList<>();

        allow(CommandEnum.QUIT); //quitting is allowed in every state
    }

    /**
     * Adds a command to the list of allowed commands
     * @param command The command to allow
     */
    //used for concrete class construction
    protected void allow(CommandEnum command){
        if (!allowedCommands.contains(command)) allowedCommands.add(command);
    }

    public boolean isAllowed(CommandEnum command){
        return allowedCommands.contains(command);
    }
}
