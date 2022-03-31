package it.polimi.ingsw.model;

public enum TeamEnum {
    WHITE(0),
    BLACK(1),
    GREY(2),
    NOTEAM(3);

    public int index;

     TeamEnum(int index){
        this.index = index;
    }

    /**
     * returns the number of teams.
     * If NOTEAM is always the last in the enumeration it is equal to the returned value.
     */
    public static int getNumTeams(){
        return TeamEnum.values().length;
    }
}
