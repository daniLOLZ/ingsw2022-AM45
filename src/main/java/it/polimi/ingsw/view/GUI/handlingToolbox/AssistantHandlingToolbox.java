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

    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {
        if (command == CommandEnum.CHOOSE_ASSISTANT){
            int assistantIndex = 0;
            for (EventHandler<MouseEvent> ignored:
                 onAssistantClick) {
                int finalIndex = assistantIndex;
                onAssistantClick.set(assistantIndex, event -> new Thread (() -> resourceProvider.sendAssistantChosen(finalIndex)).start());
                assistantIndex++;
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum command) {

        if (command == CommandEnum.CHOOSE_ASSISTANT){
            int index = 0;
            for (EventHandler<MouseEvent> ignored : onAssistantClick) onAssistantClick.set(index++, DISABLED);
        }
    }

    public EventHandler<MouseEvent> getOnAssistantClick(int pos){
        return onAssistantClick.get(pos);
    }
}
