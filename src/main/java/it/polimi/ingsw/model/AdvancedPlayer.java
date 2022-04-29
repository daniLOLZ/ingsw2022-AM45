package it.polimi.ingsw.model;

public class AdvancedPlayer extends Player{
    private int numCoins;

    @Deprecated
     /**
     * Basic constructor
     * doesn't allow for choice of wizard
     */
    public AdvancedPlayer(PlayerEnum playerId, String nickname, TeamEnum teamColor, boolean leader, ParameterHandler parameters){
        super(playerId, nickname, teamColor, leader, parameters);
        this.numCoins = 1;
    }

    public AdvancedPlayer(PlayerEnum playerId, String nickname, TeamEnum teamColor, Wizard wizard, boolean leader, ParameterHandler parameters){
        super(playerId, nickname,teamColor, wizard, leader, parameters);
        this.numCoins = 1;
    }



    public void addCoin(){
        numCoins++;
    }
    public void useCoin(){
        numCoins--;
    }

    public int getNumCoins() {
        return numCoins;
    }
}
