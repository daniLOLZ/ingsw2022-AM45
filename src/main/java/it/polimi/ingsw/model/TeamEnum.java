package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TeamEnum {
    WHITE(0, "White"),
    BLACK(1, "Black"),
    GREY(2,"Grey"),
    NOTEAM(3, "No team");

    public final int index;
    public final String name;

     TeamEnum(int index, String name){
        this.index = index;
        this.name = name;
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

    /**
     * Returns the team with the given id
     * @param id the id of the team
     * @return the team with the given id
     */
    public static TeamEnum getTeamFromId(int id){
        for(TeamEnum team : TeamEnum.values()){
            if(team.index == id) return team;
        }
        return NOTEAM;
    }

    /**
     * Gets the enum value from the read object
     * @param field the team enum in Object form
     * @return the converted enum
     */
    public static TeamEnum fromObjectToEnum(Object field) {
        return TeamEnum.valueOf((String)field);
    }
}
