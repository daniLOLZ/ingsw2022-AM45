package it.polimi.ingsw.controller;

public enum GameRule {
    NO_RULE(0),
    SIMPLE_2(2),
    SIMPLE_3(3),
    SIMPLE_4(4),
    ADVANCED_2(20),
    ADVANCED_3(30),
    ADVANCED_4(40);

    public final int id;
    GameRule(int id){
        this.id = id;
    }

    public static GameRule getRule(int id){
        switch ( id){
            case 2: return SIMPLE_2;
            case 3: return  SIMPLE_3;
            case 4: return  SIMPLE_4;
            case 20: return ADVANCED_2;
            case 30: return ADVANCED_3;
            case 40: return ADVANCED_4;
        }
        return NO_RULE;
    }
}
