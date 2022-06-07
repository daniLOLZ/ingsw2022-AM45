package it.polimi.ingsw.network;

import it.polimi.ingsw.network.commandHandler.asynchronous.AsyncCommandHandler;
import it.polimi.ingsw.network.commandHandler.synchronous.CommandHandler;
import it.polimi.ingsw.network.commandHandler.FactoryCommandHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CommandHandlerTest {

    /**
     * Tests whether all synchronous commands in the network are mapped to a handler and vice versa
     * (excluding ping/pong)
     */
    @Test
    public void checkAllSyncHandlersUnivocallyAssigned(){

        List<CommandEnum> commandsHandled = FactoryCommandHandler.getAllCommandHandlers()
                                                    .stream()
                                                    .map(CommandHandler::getCommandAccepted)
                                                    .toList();

        List<CommandEnum> excludedCommands = new ArrayList<>();
        excludedCommands.add(CommandEnum.PING);
        excludedCommands.add(CommandEnum.PONG);

        List<CommandEnum> commandsAvailable = CommandEnum.getSyncCommands().stream()
                                            .filter(x -> !excludedCommands.contains(x))
                                            .toList();

        assertTrue(commandsAvailable.containsAll(commandsHandled) &&
                    commandsHandled.containsAll(commandsAvailable));
    }

    /**
     * Tests whether all asynchronous commands in the network are mapped to a handler and vice versa
     */
    @Test
    public void checkAllAsyncHandlersUnivocallyAssigned(){

        List<CommandEnum> commandsHandled = FactoryCommandHandler.getAsyncCommandHandlers()
                .stream()
                .map(AsyncCommandHandler::getCommandHandled)
                .toList();

        List<CommandEnum> commandsAvailable = CommandEnum.getAsyncCommands();

        assertTrue(commandsAvailable.containsAll(commandsHandled) &&
                commandsHandled.containsAll(commandsAvailable));
    }
}
