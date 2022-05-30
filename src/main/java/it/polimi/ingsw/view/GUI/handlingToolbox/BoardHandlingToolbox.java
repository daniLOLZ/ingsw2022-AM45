package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.view.GUI.drawers.Drawer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class BoardHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onEntranceStudentClick;
    private List<EventHandler<MouseEvent>> onSelectedEntranceStudentClick;
    private EventHandler<MouseEvent> onHallClick;

    public BoardHandlingToolbox(int entranceStudents){

        onEntranceStudentClick = new ArrayList<>();
        onSelectedEntranceStudentClick = new ArrayList<>();

        for (int student = 0; student < entranceStudents; student++) {
            onEntranceStudentClick.add(Drawer.NO_EFFECT);
            onSelectedEntranceStudentClick.add(Drawer.NO_EFFECT);
        }

        onHallClick = Drawer.NO_EFFECT;
    }

    @Override
    public void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider) {
        //TODO
    }

    @Override
    public void disableCommand(CommandEnum commandEnum) {
        //TODO
    }


    /**
     * Returns the allowed action for the given entrance student
     * @param pos the position of the student at the entrance
     * @param selected indicates if the student is the selected one
     * @return The EventHandler to assign to the Entrance student
     */
    public EventHandler<MouseEvent> getOnEntranceStudentClick(int pos, boolean selected){
        return selected ? onSelectedEntranceStudentClick.get(pos) : onEntranceStudentClick.get(pos);
    }

    public EventHandler<MouseEvent> getOnHallClick() {
        return onHallClick;
    }
}
