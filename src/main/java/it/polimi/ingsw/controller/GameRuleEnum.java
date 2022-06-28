package it.polimi.ingsw.controller;

public enum GameRuleEnum {
    NO_RULE(0),
    SIMPLE_2(2),
    SIMPLE_3(3),
    SIMPLE_4(4),
    ADVANCED_2(20),
    ADVANCED_3(30),
    ADVANCED_4(40);

    public final int id;
    GameRuleEnum(int id){
        this.id = id;
    }

    /**
     * Gets a basic rule with the given amount of players
     * @param numPlayers the number of players for this rule
     * @return a simple GameRuleEnum with the specified amount of players
     */
    public static GameRuleEnum getBasicRule(int numPlayers){
        switch ( numPlayers){
            case 2: return SIMPLE_2;
            case 3: return  SIMPLE_3;
            case 4: return  SIMPLE_4;

        }
        return NO_RULE;
    }

    /**
     * Gets an advanced rule with the given amount of players
     * @param numPlayers the number of players for this rule
     * @return an advanced GameRuleEnum with the specified amount of players
     */
    public static GameRuleEnum getAdvancedRule(int numPlayers){
        switch (numPlayers){
            case 2: return ADVANCED_2;
            case 3: return ADVANCED_3;
            case 4: return ADVANCED_4;
        }
        return  NO_RULE;
    }

    /**
     * Gets the number of players from... the <b>id</b> of the rule...
     * @param id the id of the rule
     * @return the number of players that rule describes
     */
    public static int getNumPlayers(int id){
        int numPlayers = id;
        if(numPlayers > 4)
            numPlayers = numPlayers / 10;
        return  numPlayers;
    }

    /**
     *
     * @param id the id of the rule to check
     * @return true if the rule with said id is simple
     */
    public static boolean isSimple(int id){
        return id <= 4;
    }

    /**
     *
     * @param id the id of the rule to check
     * @return true if the rule with said id is advanced
     */
    public static boolean isAdvanced(int id){
        return id > 4;
    }

    /**
     * Gets the enum value from the read object
     * @param rule the rule in Object form
     * @return the converted enum
     */
    public static GameRuleEnum fromObjectToEnum (Object rule){
        return GameRuleEnum.valueOf((String)rule);
    }

}
