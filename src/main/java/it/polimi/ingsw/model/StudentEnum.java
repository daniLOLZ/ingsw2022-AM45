package it.polimi.ingsw.model;

public enum StudentEnum{

    GREEN(0, "Green"),
    RED(1, "Red"),
    YELLOW(2, "Yellow"),
    PINK(3, "Pink"),
    BLUE(4, "Blue"),
    NOSTUDENT(5, "NOSTUDENT");

    public final int index;
    private final String color;

    StudentEnum(int index, String color){
        this.index = index;
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
