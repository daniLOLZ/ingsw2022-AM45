package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.IncorrectPlayersException;
import it.polimi.ingsw.model.game.SimpleGame;

//TODO
public class Controller {

    protected PlayerCreation playerCreation;
    protected GameRuleEnum gameRule;
    protected SimpleGame simpleGame;
    protected AdvancedGame advancedGame;
    protected CharacterCardHandler characterCardHandler;
    protected AssistantHandler assistantHandler;
    protected BoardHandler boardHandler;
    protected TurnHandler turnHandler;
    protected WinnerHandler winnerHandler;

    public void createPlayerCreation(){
        playerCreation = new PlayerCreation(this);
    }

    public Controller(){
        //TODO
    }

    public void createSimpleGame(int numPlayers){
        try {
            simpleGame = new SimpleGame(numPlayers);
            gameRule = GameRuleEnum.getBasicRule(numPlayers);
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
            gameRule = GameRuleEnum.getAdvancedRule(numPlayers);
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
