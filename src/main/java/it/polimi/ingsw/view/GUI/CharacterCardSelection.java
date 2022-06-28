package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.StudentEnum;

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

    public CharacterCardSelection(){
        resetSelections();
    }

    public void addEntranceStudent(int index){
        selectedEntranceStudents.add(index);
    }

    public void addColor(StudentEnum color){
        selectedColors.add(color);
    }

    public void addStudentOnCard(int index){
        selectedStudentsOnCard.add(index);
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
}
