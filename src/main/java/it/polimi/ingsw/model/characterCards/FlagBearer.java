package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.CharacterCard;

public class FlagBearer extends CharacterCard {

    @Override
    public void activateEffect(AdvancedGame game) {

        game.getIslandToEvaluate().
                evaluateMostInfluential(game.getProfessors(), game.getPlayers());

        game.getIslandToEvaluate().build(game.getCurrentTeam(), game.getPlayers());


    }
}
