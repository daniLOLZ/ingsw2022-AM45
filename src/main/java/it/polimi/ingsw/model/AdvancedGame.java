package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.FactoryCharacterCard;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGame extends SimpleGame{
    private final List<CharacterCard> CharacterCards;
    private  List<AdvancedPlayer> AdvancedPlayers;
    private final AdvancedParameterHandler advancedParameters;

    public AdvancedGame(int numPlayers, int numCoins, int numCharacterCards) throws IncorrectPlayersException{
        super(numPlayers);
        advancedParameters = new AdvancedParameterHandler(numCoins);
        CharacterCards = new ArrayList<>();

        for(int card= 0; card < numCharacterCards; card++){
            CharacterCards.add(FactoryCharacterCard.
                    getCharacterCard(CharacterCards, super.getParameters(), advancedParameters));
        }
        for(int card= 0; card < numCharacterCards; card++){
            CharacterCards.get(card).initialise(this);
        }

        createPlayingSack();

    }

    public CharacterCard getCharacterCard(int position) {
        return CharacterCards.get(position);
    }

    public AdvancedParameterHandler getAdvancedParameters(){
        return advancedParameters;
    }


    /**
     * It was useful if also SimpleGame had this method, in this way I can
     * override it and not create 2 times all players
     * @param numPlayer > 1 useful for version with override
     */
    private void  createPlayers(int numPlayer){
        AdvancedPlayers = new ArrayList<>();
        for(Player player: players){
            AdvancedPlayers.add(
                    new AdvancedPlayer(
                    player.getPlayerId(),
                    player.getNickname(),
                    player.getTeamColor(),
                    player.isLeader(),
                    getParameters()));
        }

        //TODO VERSION WITH OVERRIDE
    }

    public List<AdvancedPlayer> getAdvancedPlayers() {
        return AdvancedPlayers;
    }

    @Override
    protected void createPlayingSack() {
        sack = new AdvancedSack(24);
    }

}

