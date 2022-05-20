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
    private Optional<Integer> selectedEntranceStudentPos;

    public Board(TeamEnum teamColor, ParameterHandler parameters){
        numberOfTowers = parameters.getNumTowers();
        towerColor = teamColor;
        studentsAtEntrance = new ArrayList<>();
        studentsPerTable = new ArrayList<>();
        this.parameters = parameters;
        for(StudentEnum table : StudentEnum.getStudents()){
            studentsPerTable.add(0);
        }
        selectedEntranceStudentPos = Optional.empty();
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

    //todo remove legacy selection, now in parameters
    public void setSelectedEntranceStudentPos(int pos){
        selectedEntranceStudentPos = Optional.of(pos);
    }

    public int getNumberOfTowers() {return numberOfTowers;}

    public TeamEnum getTowerColor() {return towerColor;}

    /**
     * Remove Student from studentsAtEntrance.
     * Add 1 Student to right position of StudentsPerTable.
     * Resets selectedEntranceStudentPos.
     * @return moved student's type
     */
    //todo remove legacy selection, now in parameters
    public StudentEnum moveFromEntranceToHall(){

        //no selected student
        if (studentsAtEntrance.isEmpty()) return StudentEnum.NOSTUDENT;


        StudentEnum student = studentsAtEntrance.get(selectedEntranceStudentPos.get());

        //table is full
        if (studentsPerTable.get(student.ordinal()) >= tableSize) return StudentEnum.NOSTUDENT;

        removeFromEntrance(selectedEntranceStudentPos.get());
        addToHall(student);

        selectedEntranceStudentPos = Optional.empty();

        return student;
    }


    /**
     * Removes the right student from studentsAtEntrance.
     * Adds 1 student, of the right type, in chosenIsland.
     * Resets selectedEntranceStudentPos.
     * @param chosenIsland the IslandGroup selected by the current player
     */
    //todo remove legacy selection, now in parameters
    public void moveFromEntranceToIsland(IslandGroup chosenIsland){



        //no selected student
        if(selectedEntranceStudentPos.isEmpty()) return;

        StudentEnum student = studentsAtEntrance.get(selectedEntranceStudentPos.get());

        removeFromEntrance(selectedEntranceStudentPos.get());

        chosenIsland.addStudent(student);

        selectedEntranceStudentPos = Optional.empty();
    }

    /**
     * call empty from chosenCloud and add the elements of the obtained collection to studentsAtEntrance collection
     * @param chosenCloud the Cloud chosen by the current player
     */
    //todo remove legacy selection, now in parameters
    public void moveFromCloud(Cloud chosenCloud){

        //get students from chosenCloud
        Collection<StudentEnum> students = chosenCloud.empty();

        //adds the returned students to the Entrance
        studentsAtEntrance.addAll(students);
    }

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

    public void addToEntrance(StudentEnum studentToAdd){
        studentsAtEntrance.add(studentToAdd);
    }

    /**
     * Removes the selected student from the Entrance.
     * @param position The position of the student to remove
     * @return The removed student
     */
    public StudentEnum removeFromEntrance(int position){

        //position out of bounds
        if(position >= studentsAtEntrance.size()) return StudentEnum.NOSTUDENT;

        return studentsAtEntrance.remove(position);
    }

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
        return studentsAtEntrance.size();
    }

    public Integer getStudentsAtTable(StudentEnum color) {
        return studentsPerTable.get(color.index);
    }

}
