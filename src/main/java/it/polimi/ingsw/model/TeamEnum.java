package it.polimi.ingsw.model;

public enum TeamEnum {
    WHITE,
    BLACK,
    GREY,
    NOTEAM;

    /**
     * returns the number of teams.
     * If NOTEAM is always the last in the enumeration it is equal to the returned value.
     */
    public static int getNumTeams(){
        return TeamEnum.values().length;
    }
}
