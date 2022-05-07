package it.polimi.ingsw.model.game;

public enum PhaseEnum {
    PLANNING(1),
    ACTION(2);

    public final int id;

     PhaseEnum(int id){
        this.id = id;
    }

    public static PhaseEnum fromObjectToEnum(Object field) {
        return PhaseEnum.valueOf((String)field);
    }
}
