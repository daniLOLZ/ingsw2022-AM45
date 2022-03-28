package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;

public class Board {

    private int numberOfTowers;
    private TeamEnum towerColor;
    private ArrayList<StudentEnum> studentsAtEntrance;
    private ArrayList<Integer> studentsPerTable;
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
    }

    void setSelectedEntranceStudentPos(int pos){
        selectedEntranceStudentPos = pos;
    }

    /**
     * remove Student from studentsAtEntrance.
     * add 1 Student to right position of StudentsPerTable
     * calls updateProfessors;
     * resets selectedEntranceStudentPos
     * @return moved student's type
     */
    public StudentEnum moveFromEntranceToHall(){

        StudentEnum student = studentsAtEntrance.get(selectedEntranceStudentPos);

        int studentsOnTable = studentsPerTable.get(student.ordinal());

        if (studentsOnTable >= 10) return StudentEnum.NOSTUDENT; //TODO remove hardcoding

        studentsAtEntrance.remove(selectedEntranceStudentPos.intValue());

        studentsPerTable.set(student.ordinal(), studentsOnTable + 1);

        selectedEntranceStudentPos = null;

        return student;
    }
}
