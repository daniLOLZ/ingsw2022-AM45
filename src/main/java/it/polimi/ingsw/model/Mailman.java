package it.polimi.ingsw.model;

public class Mailman extends CharacterCard{

    private final int additionalSteps = 2;
    @Override
    public void activateEffect(AdvancedGame game) {
        game.setMNAdditionalSteps(additionalSteps);
    }
}
