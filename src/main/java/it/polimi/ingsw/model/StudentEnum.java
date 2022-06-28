package it.polimi.ingsw.model;

import it.polimi.ingsw.view.StaticColorCLI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum StudentEnum {

    GREEN(0, StaticColorCLI.ANSI_GREEN + "*" + StaticColorCLI.ANSI_RESET),
    RED(1, StaticColorCLI.ANSI_RED + "*" + StaticColorCLI.ANSI_RESET),
    YELLOW(2, StaticColorCLI.ANSI_YELLOW + "*" + StaticColorCLI.ANSI_RESET),
    PINK(3, StaticColorCLI.ANSI_PURPLE + "*" + StaticColorCLI.ANSI_RESET),
    BLUE(4, StaticColorCLI.ANSI_CYAN + "*" + StaticColorCLI.ANSI_RESET),
    NOSTUDENT(5, StaticColorCLI.ANSI_WHITE+"°"+ StaticColorCLI.ANSI_RESET);

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

    /**
     * Gets the enum value from the read object
     * @param field the rule in Object form
     * @return the converted enum
     */
    public static StudentEnum fromObjectToEnum (Object field){
        return StudentEnum.valueOf((String)field);
    }

}
