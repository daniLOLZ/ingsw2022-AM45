package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.StudentEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has the list of students presents on specific CharacterCards,
 * give all method useful for manipulate and observe the list
 */
public abstract class InitialEffect extends CharacterCard{

    private final List<StudentEnum> students;

    public InitialEffect(int cost, int id, ParameterHandler parameters, AdvancedParameterHandler advancedParameters){

        super(cost, id, parameters, advancedParameters);
        students = new ArrayList<>();
    }

    public void addStudent(StudentEnum studToAdd){
        students.add(studToAdd);
    }

    public StudentEnum removeStudent(int index){
        return students.remove(index);
    }

    public StudentEnum exchangeStudent(StudentEnum studToExchange, int index){
        return students.set(index, studToExchange);
    }

    public boolean isEmpty(){
        return students.isEmpty();
    }

    public void addAllStudents(List<StudentEnum> studentsToAdd){
        students.addAll(studentsToAdd);
    }

    public StudentEnum getStudents(int id) {
        return students.get(id);
    }

    public List<StudentEnum> removeAll(){
        List<StudentEnum> listToReturn = new ArrayList<>();

        while(!students.isEmpty()){
            listToReturn.add(students.remove(0));
        }

        return listToReturn;
    }

    public int size(){
        return students.size();
    }
}
