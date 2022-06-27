package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.islands.IslandGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class Board {

    private static final int tableSize = 10;

    private int numberOfTowers;
    private final TeamEnum towerColor;
    private List<StudentEnum> studentsAtEntrance;
    private List<Integer> studentsPerTable;
    private ParameterHandler parameters;

    public Board(TeamEnum teamColor, ParameterHandler parameters){
        numberOfTowers = parameters.getNumTowers();
        towerColor = teamColor;
        studentsAtEntrance = new ArrayList<>();

        //initialise each entrance student with an empty student
        for (int studentAtEntrance = 0; studentAtEntrance < parameters.getMaxStudentsAtEntrance(); studentAtEntrance++){
            studentsAtEntrance.add(StudentEnum.NOSTUDENT);
        }

        studentsPerTable = new ArrayList<>();
        this.parameters = parameters;
        for(StudentEnum table : StudentEnum.getStudents()){
            studentsPerTable.add(0);
        }
    }


    /**
     * Increments this board's towers by the number specified
     * @param numTowers the value to increment the towers by
     * @return true if this board has no more towers
     */
    public boolean updateTowers(int numTowers){

        numberOfTowers += numTowers;
        return numberOfTowers <= 0;
    }

    public int getNumberOfTowers() {return numberOfTowers;}

    public TeamEnum getTowerColor() {return towerColor;}

    /**
     * Remove Student from studentsAtEntrance.
     * Add 1 Student to right position of StudentsPerTable.
     * Resets selectedEntranceStudentPos.
     * @return moved student's type
     */
    public StudentEnum moveFromEntranceToHall(){

        //entrance is empty
        if (entranceSize() == 0) return StudentEnum.NOSTUDENT;

        //no selected student
        if (parameters.getSelectedEntranceStudents().isEmpty()) return StudentEnum.NOSTUDENT;

        int studentPos = parameters.getSelectedEntranceStudents().get().stream().findFirst().get();
        StudentEnum student = studentsAtEntrance.get(studentPos);

        //table is full
        if (studentsPerTable.get(student.ordinal()) >= tableSize) return StudentEnum.NOSTUDENT;

        removeFromEntrance(studentPos);
        addToHall(student);

        parameters.setSelectedEntranceStudents(new ArrayList<>());
        return student;
    }


    /**
     * Removes the right student from studentsAtEntrance.
     * Adds 1 student, of the right type, in chosenIsland.
     * Resets selectedEntranceStudentPos.
     * @param chosenIsland the IslandGroup selected by the current player
     */
    public void moveFromEntranceToIsland(IslandGroup chosenIsland){

        //no selected student
        if(parameters.getSelectedEntranceStudents().isEmpty()
        || parameters.getSelectedEntranceStudents().get().isEmpty()) return;

        int studentPos = parameters.getSelectedEntranceStudents().get().stream().findFirst().get();
        StudentEnum student = studentsAtEntrance.get(studentPos);

        removeFromEntrance(studentPos);

        parameters.setSelectedEntranceStudents(new ArrayList<>());
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
        for (StudentEnum student : students) addToEntrance(student);
    }

    /**
     * Moves a student from the hall to the entrance of the board
     * @param chosenTable the table from which to extract a student
     * @throws FullEntranceException if the entrance is full and no more students can be added
     */
    public void moveFromHallToEntrance(StudentEnum chosenTable) throws FullEntranceException {

        if (entranceSize() == parameters.getMaxStudentsAtEntrance()) throw new FullEntranceException();

        removeFromHall(chosenTable);

        addToEntrance(chosenTable);
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
        StudentEnum studentLeaving;

        for(int draws=0; draws < numToSubtract; draws++){
            studentLeaving = removeFromHall(colorStudent);

            if(studentLeaving.index == StudentEnum.NOSTUDENT.index)
                return returnList;
            else
                returnList.add(studentLeaving);
        }
        return returnList;
    }

    /**
     * Adds a student to the entrance in the first available space
     * @param studentToAdd the color of the student to add
     */
    public void addToEntrance(StudentEnum studentToAdd){

        int freeSlot = studentsAtEntrance.indexOf(StudentEnum.NOSTUDENT);

        if (freeSlot >= 0) studentsAtEntrance.set(freeSlot, studentToAdd);
    }

    /**
     * Removes the selected student from the Entrance.
     * @param position The position of the student to remove
     * @return The removed student
     */
    public StudentEnum removeFromEntrance(int position){

        //position out of bounds
        if(position >= parameters.getMaxStudentsAtEntrance()) return StudentEnum.NOSTUDENT;

        StudentEnum removedStudent = studentsAtEntrance.get(position);

        studentsAtEntrance.set(position, StudentEnum.NOSTUDENT);

        return removedStudent;
    }

    /**
     * Adds a student to the hall in the right table
     * @param student the color of the student to add
     */
    public void addToHall(StudentEnum student){

        Integer previousNumStudents = studentsPerTable.get(student.index);
        previousNumStudents = previousNumStudents + 1;
        studentsPerTable.set(student.index, previousNumStudents);
    }

    /**
     * Remove one student from Hall with chosen color
     * If there is no students at Hall, return NOSTUDENT
     * @param color the table which to remove the student
     * @return Student with chosen color or NOSTUDENT if the table is already empty
     */
    public StudentEnum removeFromHall(StudentEnum color){
        Integer previousNumStudents = studentsPerTable.get(color.index);
        if(previousNumStudents == 0){
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

        int size = 0;

        //count every studentEnum who is a representation of an actual student
        for (StudentEnum student : studentsAtEntrance) size += student != StudentEnum.NOSTUDENT? 1 : 0;

        return size;
    }

    public Integer getStudentsAtTable(StudentEnum color) {
        return studentsPerTable.get(color.index);
    }

}
