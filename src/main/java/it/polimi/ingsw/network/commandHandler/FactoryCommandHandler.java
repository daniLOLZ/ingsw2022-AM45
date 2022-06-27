package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.commandHandler.asynchronous.*;
import it.polimi.ingsw.network.commandHandler.synchronous.*;

import java.util.ArrayList;
import java.util.List;

public class FactoryCommandHandler {

    /**
     *
     * @return a list of all Synchronous command handlers
     */
    public static List<CommandHandler> getAllCommandHandlers(){

        List<CommandHandler> commandHandlers = new ArrayList<>();
        commandHandlers.add(new ChooseAssistantHandler());
        commandHandlers.add(new ChooseCloudHandler());
        commandHandlers.add(new ConnectionRequestHandler());
        commandHandlers.add(new DeselectStudentHandler());
        commandHandlers.add(new EndTurnHandler());
        commandHandlers.add(new MoveMNToIslandHandler());
        commandHandlers.add(new PlayCharacterHandler());
        commandHandlers.add(new PlayGameHandler());
        commandHandlers.add(new PutInHallHandler());
        commandHandlers.add(new PutInIslandHandler());
        commandHandlers.add(new QuitGameHandler());
        commandHandlers.add(new RequestLeaveLobbyHandler());
        commandHandlers.add(new SelectCharacterHandler());
        commandHandlers.add(new SelectEntranceStudentHandler());
        commandHandlers.add(new SelectEntranceStudentsHandler());
        commandHandlers.add(new SelectIslandGroupHandler());
        commandHandlers.add(new SelectStudentColorHandler());
        commandHandlers.add(new SelectStudentOnCardHandler());
        commandHandlers.add(new SelectTowerColorHandler());
        commandHandlers.add(new SelectWizardHandler());
        commandHandlers.add(new SendNotReadyHandler());
        commandHandlers.add(new SendReadyHandler());
        commandHandlers.add(new StartGameHandler());

        return commandHandlers;
    }

    /**
     * Gets all the asynchronous command handlers.
     * The order in which these are returned, and thus the order in which
     * they are queried by the client handler, matters as some updates have priority over others
     * @return a list of all the Async command handlers
     */
    public static List<AsyncCommandHandler> getAsyncCommandHandlers() {
        List<AsyncCommandHandler> asyncHandlers = new ArrayList<>();
        //LobbyStart must come before GameInitUpdate!
        // Do not change!
        asyncHandlers.add(new UserDisconnectedHandler());
        asyncHandlers.add(new LobbyStartHandler());
        asyncHandlers.add(new GameInitUpdateHandler());
        asyncHandlers.add(new GameStartHandler());
        asyncHandlers.add(new GameUpdateHandler());
        asyncHandlers.add(new GameWonHandler());
        asyncHandlers.add(new LobbyUpdateHandler());
        asyncHandlers.add(new YourTurnHandler());

        return asyncHandlers;
    }
}
