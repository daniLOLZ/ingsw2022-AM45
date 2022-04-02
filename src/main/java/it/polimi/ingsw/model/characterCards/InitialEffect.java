package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.StudentEnum;

import java.util.ArrayList;
import java.util.List;

public abstract class InitialEffect extends CharacterCard{

    private List<StudentEnum> students;

    public InitialEffect(int cost, int id){

        super(cost, id);
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
}
