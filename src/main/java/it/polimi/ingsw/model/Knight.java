package it.polimi.ingsw.model;

public class Knight extends CharacterCard{

    private final int addabelInfluent = 2;

    @Override
    public void activateEffect(AdvancedGame game) {
        game.setAdditionalInfluence(addabelInfluent);
    }
}
