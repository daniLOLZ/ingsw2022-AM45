package it.polimi.ingsw.model.player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PlayerEnum {
    PLAYER1(0, "Player 1"),
    PLAYER2(1, "Player 2"),
    PLAYER3(2, "Player 3"),
    PLAYER4(3, "Player 4"),
    NOPLAYER(4, "No Player");

    public final int index;
    public final String name;

    PlayerEnum(int index, String name){
        this.index = index;
        this.name = name;
    }

    /**
     * returns the number of different player types.
     * If NOPLAYER is always the last in the enumeration it is equal to the returned value.
     */
    public static int getNumPlayerTypes() {
        return PlayerEnum.values().length-1;
    }

    /**
     *
     * @return The values of the enum except the NOPLAYER instance
     */
    public static List<PlayerEnum> getPlayers(){
        return Arrays.stream(PlayerEnum.values())
                .filter( x -> !x.equals(NOPLAYER))
                .collect(Collectors.toList());
    }

    /**
     * Returns the player with number id + 1
     * @param id the player to return will have its number be the id + 1
     * @return the chosen PlayerEnum
     */
    public static PlayerEnum getPlayer(int id){
        switch(id){
            case 0: return PLAYER1;
            case 1: return PLAYER2;
            case 2: return PLAYER3;
            case 3: return PLAYER4;
        }

        return NOPLAYER;
    }

    /**
     * Gets the enum value from the read object
     * @param field the player enum in Object form
     * @return the converted enum
     */
    public static PlayerEnum fromObjectToEnum(Object field) {
        return PlayerEnum.valueOf((String)field);
    }
}
