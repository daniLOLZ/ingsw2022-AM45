package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.view.GUI.drawers.Drawer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class IslandHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onIslandClick;

    public IslandHandlingToolbox(int numIslands){

        onIslandClick = new ArrayList<>();

        for (int island = 0; island < numIslands; island++) onIslandClick.add(Drawer.NO_EFFECT);
    }

    @Override
    public void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider) {
        //TODO
    }

    @Override
    public void disableCommand(CommandEnum commandEnum) {
        //TODO
    }

    public EventHandler<MouseEvent> getOnIslandClick(int pos){
        return onIslandClick.get(pos);
    }
}
