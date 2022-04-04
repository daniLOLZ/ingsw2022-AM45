package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;

public class Knight extends CharacterCard {

    private final int addableInfluence = 2;

    public Knight(){
        super(2,8);
    }

    /**
     * During this round currentPlayer sum addableInfluence to the
     * Influence count result
     * Set additionalInfluence with addableInfluence
     * @param game
     */
    @Override
    public void activateEffect(AdvancedGame game) {

        super.activateEffect(game);
        game.setAdditionalInfluence(addableInfluence);
    }
}
