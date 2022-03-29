package it.polimi.ingsw.model;

public class Glutton extends CharacterCard{

    @Override
    public void activateEffect(AdvancedGame game) {
        game.setDrawIsWin(true);
    }

    @Override
    public void initialize() {

    }
}
