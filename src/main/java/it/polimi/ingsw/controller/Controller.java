package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.IncorrectPlayersException;
import it.polimi.ingsw.model.SimpleGame;

//TODO
public class Controller {

    protected SimpleGame simpleGame;
    protected AdvancedGame advancedGame;
    protected CharacterCardHandler characterCardHandler;
    protected AssistantHandler assistantHandler;

    public void createSimpleGame(int numPlayers){
        try {
            simpleGame = new SimpleGame(numPlayers);
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
        } catch (IncorrectPlayersException e) {
            e.printStackTrace();
        }

        simpleGame = advancedGame;

        //TODO
    }

    public void createBasicHandlers(){
        characterCardHandler = new CharacterCardHandler(this);
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
