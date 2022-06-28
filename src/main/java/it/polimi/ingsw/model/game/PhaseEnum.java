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

    /**
     * Gets the enum value from the read object
     * @param field the phase in Object form
     * @return the converted enum
     */
    public static PhaseEnum fromObjectToEnum(Object field) {
        return PhaseEnum.valueOf((String)field);
    }
}
