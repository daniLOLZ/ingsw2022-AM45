package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Board {

    private int numberOfTowers;
    private TeamEnum towerColor;
    private List<StudentEnum> studentsAtEntrance;
    private List<Integer> studentsPerTable;
    private Integer selectedEntranceStudentPos;

    public Board(int numTowers, TeamEnum colorTeam){
        numberOfTowers = numTowers;
        towerColor = colorTeam;
        studentsAtEntrance = new ArrayList<StudentEnum>();
        studentsPerTable = new ArrayList<Integer>();
        selectedEntranceStudentPos = null;
    }

    /**
     * sets numberOfTowers
     * @param numTowers the value to assign to numberOfTowers
     */
    public void updateTowers(int numTowers){
        numberOfTowers = numTowers;
        //TODO should notify the controller that the game is over
    }

    public void setSelectedEntranceStudentPos(int pos){
        selectedEntranceStudentPos = pos;
    }

    public int getNumberOfTowers() {return numberOfTowers;}

    public TeamEnum getTowerColor() {return towerColor;}

    /**
     * remove Student from studentsAtEntrance
     * add 1 Student to right position of StudentsPerTable
     * calls updateProfessors
     * resets selectedEntranceStudentPos
     * @return moved student's type
     */
    public StudentEnum moveFromEntranceToHall(){

        StudentEnum student = studentsAtEntrance.get(selectedEntranceStudentPos);

        int studentsOnTable = studentsPerTable.get(student.ordinal());

        //table is full
        //TODO remove hardcoding
        if (studentsOnTable >= 10) return StudentEnum.NOSTUDENT;

        //remove student from entrance
        studentsAtEntrance.remove(selectedEntranceStudentPos.intValue());

        //add student to table
        studentsPerTable.set(student.ordinal(), studentsOnTable + 1);

        selectedEntranceStudentPos = null;

        return student;
    }


    /**
     * removes the right student from studentsAtEntrance
     * adds 1 student, of the right type, in chosenIsland
     * @param chosenIsland the IslandGroup selected by the current player
     */
    public void moveFromEntranceToIsland(IslandGroup chosenIsland){

        StudentEnum student = studentsAtEntrance.get(selectedEntranceStudentPos);

        //remove student from entrance
        studentsAtEntrance.remove(selectedEntranceStudentPos.intValue());

        //adds the student to the IslandGroup
        chosenIsland.addStudent(student);
    }

    //should this method be here?
    void moveFromHallToEntrance(StudentEnum chosenTable) throws FullEntranceException{

        //TODO remove hardcoding
        if (studentsAtEntrance.size() == 7) throw new FullEntranceException();

        int studentsOnTable = studentsPerTable.get(chosenTable.ordinal());
        studentsPerTable.set(chosenTable.ordinal(), studentsOnTable - 1);

        studentsAtEntrance.add(chosenTable);
    }

    /**
     * call empty from chosenCloud and add the elements of the obtained collection to studentsAtEntrance collection
     * @param chosenCloud the Cloud chosen by the current player
     */
    public void moveFromCloud(Cloud chosenCloud){

        //get students from chosenCloud
        Collection<StudentEnum> students = chosenCloud.empty();

        //adds the returned students to the Entrance
        studentsAtEntrance.addAll(students);
    }

    public List<StudentEnum> removeNStudentsFromHall(StudentEnum colorStudent, int numToSubtract){
        List<StudentEnum> returnList = new ArrayList<>();

        //TODO

        return returnList;
    }

    public void addToEntrance(StudentEnum studentToAdd){
        studentsAtEntrance.add(studentToAdd);
    }

    public void addToHall(StudentEnum student){

        Integer previousNumStudents = studentsPerTable.get(student.index);
        studentsPerTable.set(student.index, previousNumStudents++);
    }

    public StudentEnum removeFromHall(StudentEnum color){
        Integer previousNumStudents = studentsPerTable.get(color.index);
        studentsPerTable.set(color.index, previousNumStudents--);
        return color;
    }
}
