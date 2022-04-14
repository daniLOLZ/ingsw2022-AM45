package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;

import java.lang.reflect.Parameter;
import java.util.Objects;

public abstract class CharacterCard {
    protected int cardCost;
    protected boolean hasBeenUsed;
    public final int id;
    private AdvancedParameterHandler advancedParameters;

    public CharacterCard(int cardCost, int id, AdvancedParameterHandler advancedParameters){
        this.cardCost = cardCost;
        hasBeenUsed = false;
        this.id = id;
        this.advancedParameters = advancedParameters;

    }

    /**
     * Initialise CharacterChard fields where this operation is useful
     * @param game
     */
    public void initialise(AdvancedGame game){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterCard that = (CharacterCard) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * CharacterCard set field idCharacterCardActive of AdvancedGame with Card's id,
     * in this way Controller, when check Model's state, understand which Card has been
     * used and implements the Card's logic, calling human's participation and modifying
     * Model state.
     * Increment cardCost if it is the first time in the game that the effect is activated
     * Set HasBeenUsed true
     */
    //TODO fix javadoc
    public  void activateEffect(){
        if(!HasBeenUsed()){
            hasBeenUsed = true;
            cardCost += 1;
        }
        //game.UsedCharacterCard(id); //probably won't be necessary anymore

    }

    public int getCardCost() {
        return cardCost;
    }

    public boolean HasBeenUsed() {
        return hasBeenUsed;
    }

    public AdvancedParameterHandler getAdvancedParameters(){
        return advancedParameters;
    }
}
