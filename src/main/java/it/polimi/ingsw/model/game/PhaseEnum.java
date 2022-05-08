package it.polimi.ingsw.model.game;

public enum PhaseEnum {
    PLANNING(1, "PLANNING"),
    ACTION(2,"ACTION");

    public final int id;
    public final String name;

     PhaseEnum(int id, String name){
        this.id = id;
        this.name = name;
    }
}
