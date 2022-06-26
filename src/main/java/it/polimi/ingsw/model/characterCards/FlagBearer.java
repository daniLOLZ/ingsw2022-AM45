package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.islands.IslandGroup;


public class FlagBearer extends CharacterCard {
    private final static String name = "FLAG-BEARER";
    private final static String description = "Choose an Island and resolve the Island\n" +
                                          "\t|\tas if Mother Nature had ended her movement\n" +
                                          "\t|\tthere. Mother Nature will still move\n" +
                                          "\t|\tand the island where she ends her movement\n" +
                                          "\t|\twill also be resolved";

    public FlagBearer(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(3,3, parameters, advancedParameters, name, description);
        requirements = new Requirements(1,0,0,0);
    }


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
        winnerTeam = island.evaluateMostInfluential();

        //BUILD TOWERS IF NECESSARY
        if(winnerTeam != TeamEnum.NOTEAM){
            island.build(winnerTeam,game.getPlayers());
        }

    }
}
