package it.polimi.ingsw.model;

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
        int numberOfTypes = 0;
        for (StudentEnum i : StudentEnum.values()) {
            numberOfTypes++;
        }
        return numberOfTypes;
    }
}
