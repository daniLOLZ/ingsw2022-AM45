package it.polimi.ingsw.model;

public class Centaur extends CharacterCard{

    @Override
    public void activateEffect(AdvancedGame game) {
        game.setCountTowers(false);
    }

}
