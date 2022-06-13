package it.polimi.ingsw.view;

public class StaticColorCLI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public static String getColor(int id){
        switch (id){
            case 0: return ANSI_GREEN;
            case 1: return ANSI_RED;
            case 2: return ANSI_YELLOW;
            case 3: return ANSI_PURPLE;
            case 4: return  ANSI_CYAN;
            default: return ANSI_RESET;
        }
    }
}
