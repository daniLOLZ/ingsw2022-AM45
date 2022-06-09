package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.client.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class IslandHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onIslandClick;

    public IslandHandlingToolbox(int numIslands){

        onIslandClick = new ArrayList<>();

        for (int island = 0; island < numIslands; island++) onIslandClick.add(DISABLED);
    }

    @Override
    public void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider) {
        //TODO MOVE_MOTHER_NATURE

        if (command == CommandEnum.PUT_IN_ISLAND) {
            int index = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) {
                //onIslandClick.set(index, resourceProvider.putInIsland(index));
                index++;
            }
        }

        if (command == CommandEnum.SELECT_ISLAND_GROUP){

            int index = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) {
                //onIslandClick.set(index, resourceProvider.selectIslandGroup(index));
                index++;
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum command) {
        //TODO MOVE_MOTHER_NATURE

        if (command == CommandEnum.PUT_IN_ISLAND){

            int index = 0;

            for (EventHandler<MouseEvent> ignored:
                 onIslandClick) {
                onIslandClick.set(index, DISABLED);
                index++;
            }
        }

        if (command == CommandEnum.SELECT_ISLAND_GROUP){

            int index = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) onIslandClick.set(index++, DISABLED);
        }
    }

    public EventHandler<MouseEvent> getOnIslandClick(int pos){
        return onIslandClick.get(pos);
    }
}
