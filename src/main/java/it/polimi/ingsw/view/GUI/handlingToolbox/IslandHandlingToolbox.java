package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IslandHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onIslandClick;

    private Map<Integer, Integer> indexToId;

    private int motherNaturePos;

    private int maxMNSteps;

    /**
     * Provides the toolbox with the necessary information regarding the islands to keep providing the handling resources
     * @param islands The list containing all the relevant information about each island
     */
    public void updateIslandGroups(List<IslandGroupBean> islands){

        List<EventHandler<MouseEvent>> newOnIslandClick = new ArrayList<>();
        Map<Integer, Integer> newIndexToId = new HashMap<>();

        for (IslandGroupBean island : islands){

            newOnIslandClick.add(NO_EFFECT);

            newIndexToId.put(islands.indexOf(island), island.getIdIslandGroup());

            if (island.isPresentMN()) motherNaturePos = islands.indexOf(island);
        }

        onIslandClick = newOnIslandClick;
        indexToId = newIndexToId;
    }

    public void setMaxMNSteps(int steps){maxMNSteps = steps;}

    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {

        if (command == CommandEnum.PUT_IN_ISLAND) {
            int islandIndex = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) {
                int finalIndex = islandIndex;
                onIslandClick.set(islandIndex, event -> resourceProvider.sendPutInIsland(indexToId.get(finalIndex)));
                islandIndex++;
            }
        }

        if (command == CommandEnum.SELECT_ISLAND_GROUP){

            int islandIndex = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) {
                int finalIndex = islandIndex;
                onIslandClick.set(islandIndex, event ->  resourceProvider.sendSelectIslandGroup(indexToId.get(finalIndex)));
                islandIndex++;
            }
        }

        if (command == CommandEnum.MOVE_MN){

            for (int pos = motherNaturePos + 1; pos < motherNaturePos + maxMNSteps; pos++){

                int finalPos = pos;
                onIslandClick.set(pos % onIslandClick.size(), event -> resourceProvider.sendMoveMN(finalPos - motherNaturePos));
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum command) {
        //TODO MOVE_MOTHER_NATURE

        if (command == CommandEnum.PUT_IN_ISLAND || command == CommandEnum.SELECT_ISLAND_GROUP){

            int index = 0;

            for (EventHandler<MouseEvent> ignored: onIslandClick) onIslandClick.set(index++, DISABLED);
        }

    }

    public EventHandler<MouseEvent> getOnIslandClick(int pos){
        return onIslandClick.get(pos);
    }
}
