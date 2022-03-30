package it.polimi.ingsw.model;

public class FlagBearer extends CharacterCard{

    @Override
    public void activateEffect(AdvancedGame game) {

        game.getIslandToEvaluate().
                evaluateMostInfluential(game.getProfessors(), game.getPlayers());

        game.getIslandToEvaluate().build(game.getCurrentTeam(), game.getPlayers());


    }
}
