package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardHandlingToolbox implements HandlingToolbox{

    private List<EventHandler<MouseEvent>> onEntranceStudentClick;
    private List<EventHandler<MouseEvent>> onTableClick;

    public static final BoardHandlingToolbox NONINTERACTIVE = new BoardHandlingToolbox(9,5);

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
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {
        if (command == CommandEnum.SELECT_STUDENT){
            AtomicInteger studentIndex = new AtomicInteger();
            for (EventHandler<MouseEvent> ignored :
                 onEntranceStudentClick) {
                if (onEntranceStudentClick.get(studentIndex.get()) == DISABLED) {
                    onEntranceStudentClick.set(studentIndex.get(), event -> new Thread(() -> {
                        resourceProvider.sendSelectedStudent(studentIndex.get());
                        onEntranceStudentClick.set(studentIndex.get(), NO_EFFECT);
                        studentIndex.getAndIncrement();
                    }).start());
                }
            }
        }

        if (command == CommandEnum.PUT_IN_HALL){
            int tableIndex = 0;

            for (EventHandler<MouseEvent> ignored : onTableClick) {
                onTableClick.set(tableIndex, event -> new Thread (resourceProvider::sendPutInHall).start());
                tableIndex++;
            }
        }

        if (command == CommandEnum.SELECT_STUDENT_COLOR) {
            int tableIndex = 0;

            for (EventHandler<MouseEvent> ignored : onTableClick){
                int finalIndex = tableIndex;
                onTableClick.set(tableIndex, event -> new Thread (() -> resourceProvider.sendSelectStudentColor(StudentEnum.getColorById(finalIndex))).start());
                tableIndex++;
            }
        }

        if (command == CommandEnum.SELECT_ENTRANCE_STUDENTS){

            //TODO might work in an odd way
        }

        if (command == CommandEnum.DESELECT_STUDENT){

            for (EventHandler<MouseEvent> handler:
                 onEntranceStudentClick) {

                if (handler == NO_EFFECT)
                    onEntranceStudentClick.set(
                            onEntranceStudentClick.indexOf(handler),
                            event -> new Thread(() -> {
                                resourceProvider.sendDeselectStudent();
                                onEntranceStudentClick.set(onEntranceStudentClick.indexOf(handler), DISABLED);
                            }).start());
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum commandEnum) {
        //TODO
    }


    /**
     * Returns the allowed action for the given entrance student
     * @param pos the position of the student at the entrance
     * @return The EventHandler to assign to the Entrance student
     */
    public EventHandler<MouseEvent> getOnEntranceStudentClick(int pos){
        return onEntranceStudentClick.get(pos);
    }

    public EventHandler<MouseEvent> getOnHallClick(int pos) {
        return onTableClick.get(pos);
    }
}
