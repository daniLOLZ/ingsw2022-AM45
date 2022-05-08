package it.polimi.ingsw.model.beans;

import java.util.List;

public class AdvancedGameBoardBean extends GameBoardBean{

    private Integer numGameCoins;
    private List<Integer> idCharacterCards;
    public AdvancedGameBoardBean(List<Integer> idIslandGroups, List<Integer> idAssistantsPlayed,
                                 List<Integer> idPlayers, Integer currentPlayerId, Integer turn,
                                 String phase, int numGameCoins,
                                 List<Integer> idCharacterCards) {
        super(idIslandGroups, idAssistantsPlayed, idPlayers, currentPlayerId, turn, phase);
        this.numGameCoins = numGameCoins;
        this.idCharacterCards = idCharacterCards;

    }

    public Integer getNumGameCoins() {
        return numGameCoins;
    }

    public List<Integer> getIdCharacterCards() {
        return idCharacterCards;
    }

    public void setNumGameCoins(Integer numGameCoins) {
        this.numGameCoins = numGameCoins;
    }

    public void setIdCharacterCards(List<Integer> idCharacterCards) {
        this.idCharacterCards = idCharacterCards;
    }
}
