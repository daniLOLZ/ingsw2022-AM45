package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.ClientNetworkManager;
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

        if (command == CommandEnum.SELECT_ISLAND_GROUP){

            int index = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) {
                System.out.println("Selecting island group : " + index);
                //onIslandClick.set(index, resourceProvider.selectIslandGroup());
                index++;
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum commandEnum) {
        //TODO MOVE_MOTHER_NATURE

        if (commandEnum == CommandEnum.SELECT_ISLAND_GROUP){

            int index = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) onIslandClick.set(index++, DISABLED);
        }
    }

    public EventHandler<MouseEvent> getOnIslandClick(int pos){
        return onIslandClick.get(pos);
    }
}
