package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.client.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CloudHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onCloudClick;

    public CloudHandlingToolbox(int numClouds){

        onCloudClick = new ArrayList<>();

        for (int cloud = 0; cloud < numClouds; cloud++) {
            onCloudClick.add(DISABLED);
        }
    }

    @Override
    public void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider) {
        if (command == CommandEnum.CHOOSE_CLOUD) {
            AtomicInteger index = new AtomicInteger();
            for (EventHandler<MouseEvent> ignored:
                 onCloudClick) {
                if (onCloudClick.get(index.get()) == DISABLED) {
                    onCloudClick.set(index.get(), event -> {
                       //resourceProvider.chooseCloud();
                       onCloudClick.set(index.get(), NO_EFFECT);

                       if (allIneffective(onCloudClick)) reset();
                    });
                }
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum commandEnum) {
        if (commandEnum == CommandEnum.CHOOSE_CLOUD){
            int index = 0;
            for (EventHandler<MouseEvent> ignored:
                 onCloudClick) {
                if (onCloudClick.get(index) != NO_EFFECT) onCloudClick.set(index, DISABLED);
                index++;
            }
        }
    }

    /**
     * Makes all the clouds available to reawaken
     */
    private void reset(){
        int index = 0;
        for (EventHandler<MouseEvent> ignored: onCloudClick) onCloudClick.set(index++, DISABLED);
    }

    /**
     * Checks if all the effects are NO_EFFECT
     * @param effects the list to evaluate
     * @return true if all effects are NO_EFFECT. false otherwise
     */
    private boolean allIneffective(List<EventHandler<MouseEvent>> effects){

        for (EventHandler<MouseEvent> effect:
             effects) {
            if (effect != NO_EFFECT) return false;
        }
        return true;
    }

    public EventHandler<MouseEvent> getOnCloudClick(int pos) {
        return onCloudClick.get(pos);
    }
}
