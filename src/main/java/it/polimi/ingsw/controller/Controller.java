package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.SimpleGame;

//TODO
public class Controller {

    protected PlayerCreation playerCreation;
    protected GameRule gameRule;
    protected SimpleGame simpleGame;
    protected AdvancedGame advancedGame;
    protected CharacterCardHandler characterCardHandler;
    protected AssistantHandler assistantHandler;
    protected BoardHandler boardHandler;

    public void createPlayerCreation(){
        playerCreation = new PlayerCreation(this);
    }

    public void createSimpleGame(int numPlayers){
        try {
            simpleGame = new SimpleGame(numPlayers);
            gameRule = GameRule.getRule(numPlayers);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }

        //TODO
    }

    public void createAdvancedGame(int numPlayers){
        final int numCoins = 20;
        final int numCharacterCards = 3;

        try {
            advancedGame = new AdvancedGame(numPlayers, numCoins, numCharacterCards);
            gameRule = GameRule.getRule(numPlayers * 10);
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }

        simpleGame = advancedGame;

        //TODO
    }

    public void createBasicHandlers(){
        characterCardHandler = new CharacterCardHandler(this);
        boardHandler = new BoardHandler(this);
    }

    private void createView(){
        //TODO
    }

    public SimpleGame getSimpleGame() {
        return simpleGame;
    }

    public AdvancedGame getAdvancedGame() {
        return advancedGame;
    }
}
