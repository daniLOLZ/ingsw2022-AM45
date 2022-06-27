package it.polimi.ingsw.view.GUI.handlingToolbox;

import com.sun.javafx.collections.ImmutableObservableList;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;


/* this is a special HandlingToolbox implementation that
   will provide functionalities for non-physical elements*/
public class HelpingToolBox implements HandlingToolbox{

    private EventHandler<ActionEvent> onEndTurnClick;
    private EventHandler<ActionEvent> onSendEntranceStudentRequirementsClick;
    private EventHandler<ActionEvent> onSendStudentColorRequirementClick;
    private EventHandler<ActionEvent> onSendStudentsOnCardRequirementClick;
    private EventHandler<ActionEvent> onPlayCharacterClick;

    private List<Integer> selectedEntranceStudents;
    private List<StudentEnum> selectedColors;
    private List<Integer> selectedStudentsOnCard;

    public HelpingToolBox(){
        onEndTurnClick                         = NO_ACTION;
        onSendEntranceStudentRequirementsClick = NO_ACTION;
        onSendStudentColorRequirementClick     = NO_ACTION;
        onSendStudentsOnCardRequirementClick   = NO_ACTION;
        onPlayCharacterClick                   = NO_ACTION;

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

        if (command == CommandEnum.END_TURN)                 onEndTurnClick                         = NO_ACTION;

        if (command == CommandEnum.SELECT_ENTRANCE_STUDENTS) onSendEntranceStudentRequirementsClick = NO_ACTION;

        if (command == CommandEnum.SELECT_STUDENT_COLORS)    onSendStudentColorRequirementClick     = NO_ACTION;

        if (command == CommandEnum.SELECT_STUDENTS_ON_CARD)  onSendStudentsOnCardRequirementClick   = NO_ACTION;

        if (command == CommandEnum.PLAY_CHARACTER)           onPlayCharacterClick                   = NO_ACTION;
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

    public EventHandler<ActionEvent> getOnEndTurnClick() {
        return onEndTurnClick;
    }

    public EventHandler<ActionEvent> getOnSendEntranceStudentRequirementsClick() {
        return onSendEntranceStudentRequirementsClick;
    }

    public EventHandler<ActionEvent> getOnSendStudentColorRequirementClick() {
        return onSendStudentColorRequirementClick;
    }

    public EventHandler<ActionEvent> getOnSendStudentsOnCardRequirementClick() {
        return onSendStudentsOnCardRequirementClick;
    }

    public EventHandler<ActionEvent> getOnPlayCharacterClick() {
        return onPlayCharacterClick;
    }
}
