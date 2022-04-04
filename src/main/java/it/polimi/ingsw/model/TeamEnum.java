package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TeamEnum {
    WHITE(0),
    BLACK(1),
    GREY(2),
    NOTEAM(3);

    public final int index;

     TeamEnum(int index){
        this.index = index;
    }

    /**
     * returns the number of teams.
     * If NOTEAM is always the last in the enumeration it is equal to the returned value.
     */
    public static int getNumTeams(){
        return TeamEnum.values().length-1;
    }

    /**
     *
     * @return The values of the enum except the NOTEAM instance
     */
    public static List<TeamEnum> getTeams(){
        return Arrays.stream(TeamEnum.values())
                .filter( x -> !x.equals(NOTEAM))
                .collect(Collectors.toList());
    }
}
