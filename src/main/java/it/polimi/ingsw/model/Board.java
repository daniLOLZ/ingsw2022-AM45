package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Board {

    private int numberOfTowers;
    private final TeamEnum towerColor;
    private List<StudentEnum> studentsAtEntrance;
    private List<Integer> studentsPerTable;
    private Integer selectedEntranceStudentPos;

    public Board(int numTowers, TeamEnum teamColor){
        numberOfTowers = numTowers;
        towerColor = teamColor;
        studentsAtEntrance = new ArrayList<StudentEnum>();
        studentsPerTable = new ArrayList<Integer>();
        for(int position = 0; position < StudentEnum.getNumStudentTypes(); position++){
            studentsPerTable.add(0);
        }
        selectedEntranceStudentPos = null;
    }

    /**
     * sets numberOfTowers
     * @param numTowers the value to assign to numberOfTowers
     * @return true if out of towers
     */
    public boolean updateTowers(int numTowers){

        numberOfTowers += numTowers;
        return numberOfTowers <= 0;
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

    public void moveFromHallToEntrance(StudentEnum chosenTable) throws FullEntranceException{

        //TODO remove hardcoding
        if (entranceSize() == 7) throw new FullEntranceException();

        int studentsOnTable = studentsPerTable.get(chosenTable.ordinal());
        studentsPerTable.set(chosenTable.ordinal(), studentsOnTable - 1);

        studentsAtEntrance.add(chosenTable);
    }

    /**
     * Remove N strudents from Hall and
     * if Hall.size() < N  remove all students in Hall
     * @param colorStudent != NOSTUDENT
     * @param numToSubtract >= 0
     * @return List of students removed
     */
    public List<StudentEnum> removeNStudentsFromHall(StudentEnum colorStudent, int numToSubtract){
        List<StudentEnum> returnList = new ArrayList<>();
        StudentEnum studentLeaving = StudentEnum.NOSTUDENT;

        for(int draws=0; draws < numToSubtract; draws++){
            studentLeaving = removeFromHall(colorStudent);

            if(studentLeaving.index == StudentEnum.NOSTUDENT.index)
                return returnList;
            else
                returnList.add(studentLeaving);
        }
        return returnList;
    }

    public void addToEntrance(StudentEnum studentToAdd){
        studentsAtEntrance.add(studentToAdd);
    }

    public void addToHall(StudentEnum student){

        Integer previousNumStudents = studentsPerTable.get(student.index);
        previousNumStudents = previousNumStudents + 1;
        studentsPerTable.set(student.index, previousNumStudents);
    }

    /**
     * Remove one student from Hall with chosen color
     * If there is no students at Hall, return NOSTUDENT
     * @param color
     * @return Student with chosen color  if \old(Hall.size()) > 0
     * @return NOSTUDENT if \old(Hall.size()) == 0
     */
    public StudentEnum removeFromHall(StudentEnum color){
        Integer previousNumStudents = studentsPerTable.get(color.index);
        if(previousNumStudents.intValue() == 0){
            return StudentEnum.NOSTUDENT;
        }
        studentsPerTable.set(color.index, previousNumStudents - 1);
        return color;
    }

    public List<StudentEnum> getStudentsAtEntrance(){
        return studentsAtEntrance;
    }

    public StudentEnum getAtEntrance(int index){
        return studentsAtEntrance.get(index);
    }

    public int entranceSize(){
        return studentsAtEntrance.size();
    }

    public Integer getStudentsAtTable(StudentEnum color) {
        return studentsPerTable.get(color.index);
    }

}
