package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum StudentEnum {

    GREEN(0, "Green"),
    RED(1, "Red"),
    YELLOW(2, "Yellow"),
    PINK(3, "Pink"),
    BLUE(4, "Blue"),
    NOSTUDENT(5, "NOSTUDENT");

    public final int index;
    private final String color;

    StudentEnum(int index, String color) {
        this.index = index;
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }

    /**
     * returns the number of different student types.
     * If NOSTUDENT is always the last in the enumeration it is equal to the returned value.
     */
    public static int getNumStudentTypes() {
        return StudentEnum.values().length-1;
    }

    /**
     *
     * @return The values of the enum except the NOSTUDENT instance
     */
    public static List<StudentEnum> getStudents(){
        return Arrays.stream(StudentEnum.values())
                .filter( x -> !x.equals(NOSTUDENT))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id >= 0
     * @return Student enum with chosen id. No STUDENT if id is not correct
     *
     */
    public static StudentEnum getColorById(int id){
        return switch (id) {
            case 0 -> GREEN;
            case 1 -> RED;
            case 2 -> YELLOW;
            case 3 -> PINK;
            case 4 -> BLUE;
            default -> NOSTUDENT;
        };
    }
}
