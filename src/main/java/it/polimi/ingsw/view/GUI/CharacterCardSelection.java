package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.StudentEnum;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Memorizes selection for the activation of a Character Card
 */
public class CharacterCardSelection {

    private List<Integer> selectedEntranceStudents;
    private List<StudentEnum> selectedColors;
    private List<Integer> selectedStudentsOnCard;

    private final EventHandler<GenericEvent> refresh;

    public CharacterCardSelection(EventHandler<GenericEvent> refresh){
        resetSelections();
        this.refresh = refresh;
    }

    public void addEntranceStudent(int index){
        selectedEntranceStudents.add(index);
        refresh();
    }

    public void addColor(StudentEnum color){
        selectedColors.add(color);
        refresh();
    }

    public void addStudentOnCard(int index){
        selectedStudentsOnCard.add(index);
        refresh();
    }

    public void resetSelections(){
        selectedEntranceStudents = new ArrayList<>();
        selectedColors = new ArrayList<>();
        selectedStudentsOnCard = new ArrayList<>();
    }

    public List<Integer> getSelectedEntranceStudents() {
        return selectedEntranceStudents;
    }

    public List<StudentEnum> getSelectedColors() {
        return selectedColors;
    }

    public List<Integer> getSelectedStudentsOnCard() {
        return selectedStudentsOnCard;
    }

    private void refresh(){
        refresh.handle(null);
    }
}
