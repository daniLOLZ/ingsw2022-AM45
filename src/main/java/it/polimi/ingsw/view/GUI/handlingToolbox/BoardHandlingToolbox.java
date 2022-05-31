package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onEntranceStudentClick;
    private EventHandler<MouseEvent> onHallClick;

    public BoardHandlingToolbox(int entranceStudents){

        onEntranceStudentClick = new ArrayList<>();

        for (int student = 0; student < entranceStudents; student++) {
            onEntranceStudentClick.add(DISABLED);
        }

        onHallClick = DISABLED;
    }

    @Override
    public void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider) {
        if (command == CommandEnum.SELECT_STUDENT){
            AtomicInteger index = new AtomicInteger();
            for (EventHandler<MouseEvent> ignored :
                 onEntranceStudentClick) {
                if (onEntranceStudentClick.get(index.get()) == DISABLED) {
                    onEntranceStudentClick.set(index.get(), event -> {
                        System.out.println("Selecting student : " + index);
                        //resourceProvider.selectStudent(index);
                        onEntranceStudentClick.set(index.get(), NO_EFFECT);
                        index.getAndIncrement();
                    });
                }
            }
        }
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
        return onEntranceStudentClick.get(pos);
    }

    public EventHandler<MouseEvent> getOnHallClick() {
        return onHallClick;
    }
}
