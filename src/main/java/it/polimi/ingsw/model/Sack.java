package it.polimi.ingsw.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Sack {
    protected Stack<StudentEnum> students;

    public Sack(int numStudentsByType){
        List<StudentEnum> studentsList = new ArrayList<>();

        for(StudentEnum type: StudentEnum.values())
            for(int istances= 0; istances < numStudentsByType; istances++ ){
                if(type != StudentEnum.NOSTUDENT)
                studentsList.add(type);
            }

        students = shuffle(studentsList);
    }
    private StudentEnum drawOneStudent(){
        return students.pop();
    }
    public List<StudentEnum> drawNStudents(int numStudents){
        List<StudentEnum> drawnStudents = new ArrayList<>();

        for(int draws = 0; draws < numStudents; ++draws)
            drawnStudents.add(drawOneStudent());

        return drawnStudents;
    }
    public boolean isEmpty(){
        return students.isEmpty();
    }
    public int size(){return students.size();}
    private Stack<StudentEnum> shuffle(List<StudentEnum> studentList){

        Instant seed = Instant.now();
        Random generator = new Random(seed.getEpochSecond());
        Stack<StudentEnum> stackStudents = new Stack<>();
        int position = 0;

        while(studentList.size() > 0){
            position = generator.nextInt(studentList.size());
            stackStudents.add(studentList.remove(position));
        }

        return stackStudents;

    }
}
