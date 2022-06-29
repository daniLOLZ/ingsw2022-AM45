package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.GUI.CharacterCardSelection;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BoardHandlingToolbox implements HandlingToolbox{

    private final List<EventHandler<MouseEvent>> onEntranceStudentClick;
    private List<EventHandler<MouseEvent>> onTableClick;

    public static final BoardHandlingToolbox NONINTERACTIVE = new BoardHandlingToolbox(9,5);

    private CharacterCardSelection selections;

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
        switch (command) {
            case SELECT_STUDENT -> {
                int studentIndex = 0;

                for (EventHandler<MouseEvent> ignored :
                        onEntranceStudentClick) {

                    int finalStudentIndex = studentIndex;

                    onEntranceStudentClick.set(finalStudentIndex, event -> {
                                onEntranceStudentClick.set(finalStudentIndex, NO_EFFECT);
                                new Thread(() ->
                                        resourceProvider.sendSelectedStudent(finalStudentIndex)).start();
                            }
                    );

                    studentIndex++;
                }
            }

            case PUT_IN_HALL -> {
                int tableIndex = 0;

                for (EventHandler<MouseEvent> ignored : onTableClick) {

                    onTableClick.set(tableIndex, event -> new Thread(resourceProvider::sendPutInHall).start());
                    tableIndex++;
                }
            }

            case SELECT_STUDENT_COLORS -> {
                int tableIndex = 0;

                for (EventHandler<MouseEvent> ignored : onTableClick) {
                    int finalIndex = tableIndex;
                    onTableClick.set(finalIndex, event -> {
                        onTableClick.set(finalIndex, NO_EFFECT);
                        new Thread(() -> selections.addColor(StudentEnum.getColorById(finalIndex + 1))).start();
                    });
                    tableIndex++;
                }
            }

            case SELECT_ENTRANCE_STUDENTS -> {

                int studentIndex = 0;

                for (EventHandler<MouseEvent> ignored : onEntranceStudentClick) {
                    int finalIndex = studentIndex;
                    onEntranceStudentClick.set(finalIndex, event -> {
                        onEntranceStudentClick.set(finalIndex, NO_EFFECT);
                        new Thread(() -> selections.addEntranceStudent(finalIndex + 1)).start();
                    });
                    studentIndex++;
                }
            }

            case DESELECT_STUDENT -> {
                for (EventHandler<MouseEvent> handler :
                        onEntranceStudentClick) {

                    if (handler == NO_EFFECT) {

                        int index = onEntranceStudentClick.indexOf(handler);

                        onEntranceStudentClick.set(
                                index,
                                event -> {
                                    onEntranceStudentClick.set(index, DISABLED);
                                    new Thread(resourceProvider::sendDeselectStudent).start();
                                });
                    }
                }

            }

            default -> {}
        }
    }

    @Override
    public void disableCommand(CommandEnum command) {
        if (command == CommandEnum.SELECT_STUDENT ||
            command == CommandEnum.DESELECT_STUDENT ||
            command == CommandEnum.SELECT_ENTRANCE_STUDENTS) {

            for (EventHandler<MouseEvent> handler:
                 onEntranceStudentClick) {

                if (handler != NO_EFFECT) onEntranceStudentClick.set(onEntranceStudentClick.indexOf(handler), DISABLED);
            }


        }

        if (command == CommandEnum.PUT_IN_HALL ||
            command == CommandEnum.SELECT_STUDENT_COLORS)
            for (EventHandler<MouseEvent> handler:
                 onTableClick) {

                onTableClick.set(onTableClick.indexOf(handler), DISABLED);
            }
    }

    public void setSelectionsContainer(CharacterCardSelection selections){
        this.selections = selections;
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
