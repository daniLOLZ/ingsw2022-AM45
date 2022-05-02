package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.TeamEnum;

public class AdvancedPlayer extends Player {
    private int numCoins;

    public AdvancedPlayer(PlayerEnum playerId, String nickname, TeamEnum teamColor, boolean leader, ParameterHandler parameters){
        super(playerId, nickname, teamColor, leader, parameters);
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
