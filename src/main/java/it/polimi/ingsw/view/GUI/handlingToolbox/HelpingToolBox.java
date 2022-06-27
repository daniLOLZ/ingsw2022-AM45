package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/* this is a special HandlingToolbox implementation that
   will provide functionalities for non-physical elements*/
public class HelpingToolBox implements HandlingToolbox{

    private EventHandler<ActionEvent> onEndTurnClick;
    private EventHandler<ActionEvent> onSendEntranceStudentRequirementsClick;
    private EventHandler<ActionEvent> onSendStudentColorRequirementClick;
    private EventHandler<ActionEvent> onSendStudentsOnCardRequirementClick;
    private EventHandler<ActionEvent> onPlayCharacterClick;

    private AtomicReference<List<Integer>> selectedEntranceStudents;
    private AtomicReference<List<StudentEnum>> selectedColors;
    private AtomicReference<List<Integer>> selectedStudentsOnCard;

    public HelpingToolBox(){
        onEndTurnClick                         = NO_ACTION;
        onSendEntranceStudentRequirementsClick = NO_ACTION;
        onSendStudentColorRequirementClick     = NO_ACTION;
        onSendStudentsOnCardRequirementClick   = NO_ACTION;
        onPlayCharacterClick                   = NO_ACTION;
    }

    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {

        if (command == CommandEnum.END_TURN){
            onEndTurnClick = event -> new Thread(resourceProvider::sendEndTurn).start();
        }


        if (command == CommandEnum.SELECT_ENTRANCE_STUDENTS){
            onSendEntranceStudentRequirementsClick = event -> new Thread(() -> resourceProvider.sendSelectEntranceStudents(selectedEntranceStudents.get())).start();
        }

        if (command == CommandEnum.SELECT_STUDENT_COLORS){
            onSendStudentColorRequirementClick = event -> new Thread(() -> resourceProvider.sendSelectStudentColors(selectedColors.get())).start();
        }

        if (command == CommandEnum.SELECT_STUDENTS_ON_CARD){
            onSendStudentsOnCardRequirementClick = event -> new Thread(() -> resourceProvider.sendSelectStudentsOnCard(selectedStudentsOnCard.get())).start();
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

    public void setSelectedEntranceStudents(AtomicReference<List<Integer>> selectedEntranceStudents) {
        this.selectedEntranceStudents = selectedEntranceStudents;
    }

    public void setSelectedColors(AtomicReference<List<StudentEnum>> selectedColors) {
        this.selectedColors = selectedColors;
    }

    public void setSelectedStudentsOnCard(AtomicReference<List<Integer>> selectedStudentsOnCard) {
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
