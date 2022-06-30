package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.GUI.CharacterCardSelection;
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

    private CharacterCardSelection selections;

    public HelpingToolBox(){
        onEndTurnClick                         = NO_ACTION;
        onSendEntranceStudentRequirementsClick = NO_ACTION;
        onSendStudentColorRequirementClick     = NO_ACTION;
        onSendStudentsOnCardRequirementClick   = NO_ACTION;
        onPlayCharacterClick                   = NO_ACTION;
    }

    /**
     * Implements the above method for helping functionalities handling.
     * @param command          the command for which the toolbox will provide the handling resource if asked
     * @param resourceProvider the provider on which the toolbox relies
     */
    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {

        if (command == CommandEnum.END_TURN){
            onEndTurnClick = event -> new Thread(resourceProvider::sendEndTurn).start();
        }


        if (command == CommandEnum.SELECT_ENTRANCE_STUDENTS){
            onSendEntranceStudentRequirementsClick = event -> new Thread(() -> resourceProvider.sendSelectEntranceStudents(selections.getSelectedEntranceStudents())).start();
        }

        if (command == CommandEnum.SELECT_STUDENT_COLORS){
            onSendStudentColorRequirementClick = event -> new Thread(() -> resourceProvider.sendSelectStudentColors(selections.getSelectedColors())).start();
        }

        if (command == CommandEnum.SELECT_STUDENTS_ON_CARD){
            onSendStudentsOnCardRequirementClick = event -> new Thread(() -> resourceProvider.sendSelectStudentsOnCard(selections.getSelectedStudentsOnCard())).start();
        }

        if (command == CommandEnum.PLAY_CHARACTER){
            onPlayCharacterClick = event -> new Thread(resourceProvider::sendPlayCharacter).start();
        }
    }

    /**
     * Implements the above method for helping functionalities handling.
     * @param command the command to disable
     */
    @Override
    public void disableCommand(CommandEnum command) {

        if (command == CommandEnum.END_TURN)                 onEndTurnClick                         = NO_ACTION;

        if (command == CommandEnum.SELECT_ENTRANCE_STUDENTS) onSendEntranceStudentRequirementsClick = NO_ACTION;

        if (command == CommandEnum.SELECT_STUDENT_COLORS)    onSendStudentColorRequirementClick     = NO_ACTION;

        if (command == CommandEnum.SELECT_STUDENTS_ON_CARD)  onSendStudentsOnCardRequirementClick   = NO_ACTION;

        if (command == CommandEnum.PLAY_CHARACTER)           onPlayCharacterClick                   = NO_ACTION;
    }

    /**
     * Sets the container who will store the selections made by the user.
     * @param selections The object able to store user selection
     */
    public void setSelectionsContainer(CharacterCardSelection selections){
        this.selections = selections;
    }

    /**
     * Returns the allowed action for the "End turn" Button.
     * @return The EventHandler to assign to the Button
     */
    public EventHandler<ActionEvent> getOnEndTurnClick() {
        return onEndTurnClick;
    }

    /**
     * Returns the allowed action for the "Confirm Students (at Entrance)" Button.
     * @return The EventHandler to assign to the Button
     */
    public EventHandler<ActionEvent> getOnSendEntranceStudentRequirementsClick() {
        return onSendEntranceStudentRequirementsClick;
    }

    /**
     * Returns the allowed action for the "Confirm Colors" Button.
     * @return The EventHandler to assign to the Button
     */
    public EventHandler<ActionEvent> getOnSendStudentColorRequirementClick() {
        return onSendStudentColorRequirementClick;
    }

    /**
     * Returns the allowed action for the "Confirm Students (on Card)" Button.
     * @return The EventHandler to assign to the Button
     */
    public EventHandler<ActionEvent> getOnSendStudentsOnCardRequirementClick() {
        return onSendStudentsOnCardRequirementClick;
    }

    /**
     * Returns the allowed action for the "Play Character" Button.
     * @return The EventHandler to assign to the Button
     */
    public EventHandler<ActionEvent> getOnPlayCharacterClick() {
        return onPlayCharacterClick;
    }
}
