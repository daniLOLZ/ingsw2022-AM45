package it.polimi.ingsw.model;

public class AdvancedPlayer extends Player{
    private int numCoins;

    public AdvancedPlayer(SimpleGame game, PlayerEnum playerId, String nickname, TeamEnum teamColor, boolean leader){
        super(game, playerId, nickname, teamColor, leader);
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
