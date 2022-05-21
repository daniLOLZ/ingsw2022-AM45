package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.CharacterCardBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.observer.CharacterWatcher;
import it.polimi.ingsw.view.observer.Watcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CharacterCard extends DrawableObject {
    private List<Watcher> watcherList;
    protected final String name;
    protected final String description;
    protected int cardCost;
    protected boolean hasBeenUsed;
    public final int id;
    protected ParameterHandler parameters;
    protected AdvancedParameterHandler advancedParameters;
    protected Requirements requirements;

    public CharacterCard(int cardCost, int id, ParameterHandler parameters,
                         AdvancedParameterHandler advancedParameters, String name, String description){
        this.cardCost = cardCost;
        hasBeenUsed = false;
        this.id = id;
        this.parameters = parameters;
        this.advancedParameters = advancedParameters;
        this.name = name;
        this.description = description;

    }




    /**
     * Initialise CharacterChard fields where this operation is useful
     * @param game != null
     */
    public void initialise(AdvancedGame game){

        alert();
    }

    /**
     * Create a Watcher to observe this card and add the Watcher to VirtualView list
     * @param virtualView != null
     */
    public void addWatcher(VirtualView virtualView){
        watcherList = new ArrayList<>();
        CharacterWatcher watcher = new CharacterWatcher(this, virtualView);
        watcherList.add(watcher);
        watchers = watcherList;

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
     * Increment cardCost if it is the first time in the game that the effect is activated
     * Set HasBeenUsed true
     * Activate the effect of the card using parameters in parameterHandlers
     */
    public void activateEffect(){
        if(!HasBeenUsed()){
            hasBeenUsed = true;
            cardCost += 1;
            alert();
        }

        //PARAMETER NOT SET
        if(requirements != null){
            if(!requirements.satisfied){
                parameters.setErrorState("PARAMETERS NOT SATISFIED");
                return;
            }

            requirements.satisfied = false;
        }

    }

    /**
     * Update AdvancedParameterHandler saving this.id in characterCardId, in
     * this way the view can see which card has been chosen and can see the
     * requirements of the right card
     */
    public void select(){
        advancedParameters.setCharacterCardId(id);
        advancedParameters.setRequirementsForThisAction(requirements);
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

    public Requirements getRequirements() {
        return requirements;
    }

    @Override
    public GameElementBean toBean() {
        return new CharacterCardBean(id, name,description,null,cardCost);
    }
}
