package it.polimi.ingsw.model;

public abstract class CharacterCard {
    protected int cardCost;
    protected boolean hasBeenUsed;


    public abstract void activateEffect(AdvancedGame game);
}
