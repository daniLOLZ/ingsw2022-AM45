package it.polimi.ingsw.view.GUI.handlingToolbox;

import com.sun.javafx.collections.ImmutableObservableList;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;


/* this is a special HandlingToolbox implementation that
   will provide functionalities for non-physical elements*/
public class HelpingToolBox implements HandlingToolbox{

    private EventHandler<MouseEvent> onEndTurnClick;
    private EventHandler<MouseEvent> onSendEntranceStudentRequirementsClick;
    private EventHandler<MouseEvent> onSendStudentColorRequirementClick;
    private EventHandler<MouseEvent> onSendStudentsOnCardRequirementClick;
    private EventHandler<MouseEvent> onPlayCharacterClick;

    private List<Integer> selectedEntranceStudents;
    private List<StudentEnum> selectedColors;
    private List<Integer> selectedStudentsOnCard;

    public HelpingToolBox(){
        onEndTurnClick                         = DISABLED;
        onSendEntranceStudentRequirementsClick = DISABLED;
        onSendStudentColorRequirementClick     = DISABLED;
        onSendStudentsOnCardRequirementClick   = DISABLED;
        onPlayCharacterClick                   = DISABLED;

        selectedEntranceStudents = new ArrayList<>();
        selectedColors = new ArrayList<>();
        selectedStudentsOnCard = new ArrayList<>();
    }

    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {

        if (command == CommandEnum.END_TURN){
            onEndTurnClick = event -> new Thread(resourceProvider::sendEndTurn).start();
        }


        if (command == CommandEnum.SELECT_ENTRANCE_STUDENTS){
            List<Integer> finalSelectedEntranceStudents = selectedEntranceStudents;
            onSendEntranceStudentRequirementsClick = event -> new Thread(() -> resourceProvider.sendSelectEntranceStudents(finalSelectedEntranceStudents)).start();
        }

        if (command == CommandEnum.SELECT_STUDENT_COLORS){
            List<StudentEnum> finalSelectedColor = selectedColors;
            onSendStudentColorRequirementClick = event -> new Thread(() -> resourceProvider.sendSelectStudentColors(finalSelectedColor)).start();
        }

        if (command == CommandEnum.SELECT_STUDENTS_ON_CARD){
            List<Integer> finalSelectedStudentsOnCard = selectedStudentsOnCard;
            onSendStudentsOnCardRequirementClick = event -> new Thread(() -> resourceProvider.sendSelectStudentsOnCard(finalSelectedStudentsOnCard)).start();
        }

        if (command == CommandEnum.PLAY_CHARACTER){
            onPlayCharacterClick = event -> new Thread(resourceProvider::sendPlayCharacter).start();
        }
    }

    @Override
    public void disableCommand(CommandEnum command) {

        if (command == CommandEnum.END_TURN)                 onEndTurnClick                         = DISABLED;

        if (command == CommandEnum.SELECT_ENTRANCE_STUDENTS) onSendEntranceStudentRequirementsClick = DISABLED;

        if (command == CommandEnum.SELECT_STUDENT_COLORS)    onSendStudentColorRequirementClick     = DISABLED;

        if (command == CommandEnum.SELECT_STUDENTS_ON_CARD)  onSendStudentsOnCardRequirementClick   = DISABLED;

        if (command == CommandEnum.PLAY_CHARACTER)           onPlayCharacterClick                   = DISABLED;
    }

    public void setSelectedEntranceStudents(List<Integer> selectedEntranceStudents) {
        this.selectedEntranceStudents = selectedEntranceStudents;
    }

    public void setSelectedColors(List<StudentEnum> selectedColors) {
        this.selectedColors = selectedColors;
    }

    public void setSelectedStudentsOnCard(List<Integer> selectedStudentsOnCard) {
        this.selectedStudentsOnCard = selectedStudentsOnCard;
    }

    public EventHandler<MouseEvent> getOnEndTurnClick() {
        return onEndTurnClick;
    }

    public EventHandler<MouseEvent> getOnSendEntranceStudentRequirementsClick() {
        return onSendEntranceStudentRequirementsClick;
    }

    public EventHandler<MouseEvent> getOnSendStudentColorRequirementClick() {
        return onSendStudentColorRequirementClick;
    }

    public EventHandler<MouseEvent> getOnSendStudentsOnCardRequirementClick() {
        return onSendStudentsOnCardRequirementClick;
    }

    public EventHandler<MouseEvent> getOnPlayCharacterClick() {
        return onPlayCharacterClick;
    }
}
