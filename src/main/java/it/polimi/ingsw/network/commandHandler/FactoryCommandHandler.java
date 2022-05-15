package it.polimi.ingsw.network.commandHandler;

import java.util.ArrayList;
import java.util.List;

public class FactoryCommandHandler {

    public static List<CommandHandler> getAllCommandHandlers(){

        List<CommandHandler> commandHandlers = new ArrayList<>();
        commandHandlers.add(new AskForControlHandler());
        commandHandlers.add(new ChooseAssistantHandler());
        commandHandlers.add(new ChooseCloudHandler());
        commandHandlers.add(new ConnectionRequestHandler());
        commandHandlers.add(new DeselectStudentHandler());
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
}
