package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.StudentEnum;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * The CharacterCardSelection class memorizes selection for the activation of a Character Card.
 */
public class CharacterCardSelection {

    private List<Integer> selectedEntranceStudents;
    private List<StudentEnum> selectedColors;
    private List<Integer> selectedStudentsOnCard;

    /**
     * The EventHandler containing the action which will allow the GUI to be updated after a selection is made
     */
    private final EventHandler<GenericEvent> refresh;

    public CharacterCardSelection(EventHandler<GenericEvent> refresh){
        resetSelections();
        this.refresh = refresh;
    }

    /**
     * Adds a student to the list of selected entrance students.
     * @param index The student index to store
     */
    public void addEntranceStudent(int index){
        selectedEntranceStudents.add(index);
        refresh();
    }

    /**
     * Adds a color to the list of selected colors.
     * @param color The color to store
     */
    public void addColor(StudentEnum color){
        selectedColors.add(color);
        refresh();
    }

    /**
     * Adds a student to the list of selected student on card.
     * @param index The student index to store
     */
    public void addStudentOnCard(int index){
        selectedStudentsOnCard.add(index);
        refresh();
    }

    /**
     * Empties all selections.
     */
    public void resetSelections(){
        selectedEntranceStudents = new ArrayList<>();
        selectedColors = new ArrayList<>();
        selectedStudentsOnCard = new ArrayList<>();
    }

    /**
     * Returns the current value of the selectedEntranceStudents property.
     * @return The selected students at entrance
     */
    public List<Integer> getSelectedEntranceStudents() {
        return selectedEntranceStudents;
    }

    /**
     * Returns the current value of the selectedColors property.
     * @return The selected colors
     */
    public List<StudentEnum> getSelectedColors() {
        return selectedColors;
    }

    /**
     * Returns the current value of the selectedEntranceStudents property.
     * @return The selected students on card
     */
    public List<Integer> getSelectedStudentsOnCard() {
        return selectedStudentsOnCard;
    }

    /**
     * Refreshes the interface.
     */
    private void refresh(){
        refresh.handle(null);
    }
}
