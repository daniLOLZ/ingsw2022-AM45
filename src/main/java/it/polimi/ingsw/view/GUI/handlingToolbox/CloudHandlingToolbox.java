package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
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

    /**
     * Implements the above method for cloud handling.
     * @param command          the command for which the toolbox will provide the handling resource if asked
     * @param resourceProvider the provider on which the toolbox relies
     */
    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {
        if (command == CommandEnum.CHOOSE_CLOUD) {
            AtomicInteger cloudIndex = new AtomicInteger();
            for (EventHandler<MouseEvent> ignored:
                 onCloudClick) {

                int index = cloudIndex.getAndIncrement();

                if (onCloudClick.get(index) == DISABLED) {

                    onCloudClick.set(index, event -> new Thread(() -> {
                        onCloudClick.set(index, DISABLED);
                        resourceProvider.sendChooseCloud(index);
                    }).start());
                }
            }
        }
    }

    /**
     * Implements the above method for cloud handling.
     * @param command the command to disable
     */
    @Override
    public void disableCommand(CommandEnum command) {
        if (command == CommandEnum.CHOOSE_CLOUD){
            int index = 0;
            for (EventHandler<MouseEvent> ignored:
                 onCloudClick) {
                if (onCloudClick.get(index) != NO_EFFECT) onCloudClick.set(index, DISABLED);
                index++;
            }
        }
    }

    /**
     * Returns the allowed action for the given Cloud.
     * @param pos The position of the cloud
     * @return The EventHandler to assign to the Cloud
     */
    public EventHandler<MouseEvent> getOnCloudClick(int pos) {
        return onCloudClick.get(pos);
    }
}
