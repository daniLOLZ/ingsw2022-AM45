package it.polimi.ingsw.model;

public enum PlayerEnum {
    PLAYER1(0),
    PLAYER2(1),
    PLAYER3(2),
    PLAYER4(3),
    NOPLAYER(4);

    public int index;

    PlayerEnum(int index){
        this.index = index;
    }
}
