package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class AssistantHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onAssistantClick;

    public AssistantHandlingToolbox(int numAssistants){

        onAssistantClick = new ArrayList<>();

        for (int assistant = 0; assistant < numAssistants; assistant++) onAssistantClick.add(DISABLED);
    }

    /**
     * Implements the above method for assistant handling.
     * @param command          the command for which the toolbox will provide the handling resource if asked
     * @param resourceProvider the provider on which the toolbox relies
     */
    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {
        if (command == CommandEnum.CHOOSE_ASSISTANT){
            int assistantIndex = 1;
            for (EventHandler<MouseEvent> ignored:
                 onAssistantClick) {
                int finalIndex = assistantIndex;
                onAssistantClick.set(assistantIndex - 1, event -> new Thread (() -> resourceProvider.sendAssistantChosen(finalIndex)).start());
                assistantIndex++;
            }
        }
    }

    /**
     * Implements the above method for assistant handling.
     * @param command the command to disable
     */
    @Override
    public void disableCommand(CommandEnum command) {

        if (command == CommandEnum.CHOOSE_ASSISTANT){
            int index = 0;
            for (EventHandler<MouseEvent> ignored : onAssistantClick) onAssistantClick.set(index++, DISABLED);
        }
    }

    /**
     * Returns the allowed action for the given Assistant.
     * @param id The id of the assistant card
     * @return The EventHandler to assign to the Assistant
     */
    public EventHandler<MouseEvent> getOnAssistantClick(int id){
        return onAssistantClick.get((id - 1) % onAssistantClick.size());
    }
}
