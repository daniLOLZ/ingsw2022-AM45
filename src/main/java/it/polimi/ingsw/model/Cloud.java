package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.model.beans.GameElementBean;

import java.util.ArrayList;
import java.util.List;

public class Cloud implements DrawableObject{
    private List<StudentEnum> students;
    private final int id;
    private final int studentsPerCloud;

    public Cloud(int id, int studentsPerCloud){
        this.id = id;
        this.studentsPerCloud = studentsPerCloud;
        students = new ArrayList<>();
    }

    public void fill(List<StudentEnum> numStudents){
        for(StudentEnum student: numStudents)
            students.add(student);
    }

    public StudentEnum remove(int index){
        if(students.isEmpty())
            return null;

        return students.remove(index);
    }

    public List<StudentEnum> empty(){
        ArrayList<StudentEnum> studentsToReturn = new ArrayList<>();
        while(!students.isEmpty())
            studentsToReturn.add(students.remove(0));

        return studentsToReturn;
    }

    public boolean isEmpty(){
        return students.isEmpty();
    }

    @Override
    public GameElementBean toBean() {
        CloudBean bean = new CloudBean(id, students);
        return bean;
    }
}
