package it.polimi.ingsw.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSack extends Sack{

    public AdvancedSack(int numStudentsByType){
        super(numStudentsByType);
    }

    /**
     * add  in a pseudorandom position param student to Sack, drawing a number of students from the Sack
     * then reinsert them with pseudorandom choice
     * @param student != NOSTUDENT
     */
    private void addStudent(StudentEnum student){
        List<StudentEnum> shuffleNeeds = new ArrayList<>();
        int random = (int) Instant.now().getEpochSecond() % students.size();
        StudentEnum replaced;

        shuffleNeeds = drawNStudents(size()-1);
        replaced = shuffleNeeds.set(random, student);
        shuffleNeeds.add(replaced);
        while (!shuffleNeeds.isEmpty())
            students.add(shuffleNeeds.remove(0));

    }

    public void addStudents(List<StudentEnum> studentsToAdd){
        for(StudentEnum istance: studentsToAdd)
            addStudent(istance);
    }
}
