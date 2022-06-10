package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientNetworkManager;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onEntranceStudentClick;
    private List<EventHandler<MouseEvent>> onTableClick;

    public BoardHandlingToolbox(int entranceStudents, int numTables){

        onEntranceStudentClick = new ArrayList<>();

        for (int student = 0; student < entranceStudents; student++) {
            onEntranceStudentClick.add(DISABLED);
        }

        onTableClick = new ArrayList<>();

        for (int table = 0; table < numTables; table++) {
            onTableClick.add(DISABLED);
        }
    }

    @Override
    public void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider) {
        if (command == CommandEnum.SELECT_STUDENT){
            AtomicInteger index = new AtomicInteger();
            for (EventHandler<MouseEvent> ignored :
                 onEntranceStudentClick) {
                if (onEntranceStudentClick.get(index.get()) == DISABLED) {
                    onEntranceStudentClick.set(index.get(), event -> {
                        //resourceProvider.selectStudent(index);
                        onEntranceStudentClick.set(index.get(), NO_EFFECT);
                        index.getAndIncrement();
                    });
                }
            }
        }

        if (command == CommandEnum.PUT_IN_HALL){
            int index = 0;

            for (EventHandler<MouseEvent> ignored : onTableClick) {
                //onTableClick.set(index++, resourceProvider.putInHall());
                index++;
            }
        }

        if (command == CommandEnum.SELECT_STUDENT_COLOR) {
            int index = 0;

            for (EventHandler<MouseEvent> ignored : onTableClick){
                //onTableClick.set(index, resourceProvider.selectStudentColor(StudentEnum.getColorById(index)));
                index++;
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

    public EventHandler<MouseEvent> getOnHallClick(int pos) {
        return onTableClick.get(pos);
    }
}
