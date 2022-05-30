package it.polimi.ingsw.network;

import it.polimi.ingsw.network.commandHandler.CommandHandler;
import it.polimi.ingsw.network.commandHandler.FactoryCommandHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CommandHandlerTest {

    /**
     * Tests whether all commands in the network are mapped to a handler and vice versa
     * (excluding ping/pong)
     */
    @Test
    public void checkAllHandlersUnivocallyAssigned(){

        List<CommandEnum> commandsHandled = FactoryCommandHandler.getAllCommandHandlers()
                                                    .stream()
                                                    .map(CommandHandler::getCommandAccepted)
                                                    .toList();

        List<CommandEnum> excludedCommands = new ArrayList<>();
        excludedCommands.add(CommandEnum.PING);
        excludedCommands.add(CommandEnum.PONG);

        List<CommandEnum> commandsAvailable = Arrays.stream(CommandEnum.values())
                                            .filter(x -> !excludedCommands.contains(x))
                                            .toList();

        assertTrue(commandsAvailable.containsAll(commandsHandled) &&
                    commandsHandled.containsAll(commandsAvailable));
    }
}
