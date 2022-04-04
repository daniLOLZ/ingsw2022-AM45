package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PlayerEnum {
    PLAYER1(0),
    PLAYER2(1),
    PLAYER3(2),
    PLAYER4(3),
    NOPLAYER(4);

    public final int index;

    PlayerEnum(int index){
        this.index = index;
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
}
