package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;



public class FlagBearer extends CharacterCard {

    public FlagBearer(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(3,3, parameters, advancedParameters);
        requirements = new Requirements(1,0,0,0);
    }

    /**
     *
     *
     */
    @Override
    public void activateEffect() {
        super.activateEffect();

    }

    /**
     * Controller call this method to use effect of this card and
     * then Controller must check if Islands need to be merged
     * @param game != null
     */
    public void evaluate(AdvancedGame game){
        IslandGroup island;

        //CHECK IF USER SELECT ISLAND
        if(parameters.getSelectedIslands().isPresent())
            island = parameters.getSelectedIslands().get().get(0);
        else{
            parameters.setErrorState("BAD PARAMETERS WITH SelectedIslands");
            return;
        }

        //EVALUATE INFLUENCE OF ISLAND
        TeamEnum winnerTeam;
        winnerTeam = island.evaluateMostInfluential(); //Lucario : tolto parametro ora inutile
                                                        //RISPOSTA: non ho idea a cosa ti riferisci.

        //BUILD TOWERS IF NECESSARY
        if(winnerTeam != TeamEnum.NOTEAM){
            island.build(winnerTeam,game.getPlayers());
        }

    }
}
