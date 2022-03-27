package it.polimi.ingsw.model;

public enum StudentEnum {
    GREEN,
    RED,
    YELLOW,
    PINK,
    BLUE,
    NOSTUDENT;

    /**
     * returns the number of different student types.
     * If NOSTUDENT is always the last in the enumeration it is equal to the returned value.
     */
    public static int getNumStudentTypes(){
        int numberOfTypes = 0;
        for(StudentEnum i : StudentEnum.values()){
            numberOfTypes++;
        }
        return numberOfTypes;
    }
}
