package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.FactoryCharacterCard;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGame extends SimpleGame{
    private final List<CharacterCard> CharacterCards;
    private  List<AdvancedPlayer> AdvancedPlayers; // Lucario : Non è meglio mettere advancedPlayers in players invece
                                                    // di avere una lista apposita? O lo usi per facilità nel gestirli
                                                    // col controller? In ogni caso si può vedere se servono davvero
    private  AdvancedParameterHandler advancedParameters;

    public AdvancedGame(int numPlayers, int numCoins, int numCharacterCards) throws IncorrectPlayersException{
        super(numPlayers);
        advancedParameters.setNumCoins(numCoins); // number of coins in the parameters is added at a later
                                                    // time because we need to create parameters before
                                                    // creating the islands, this happens in the createParameters()
                                                    // method, which don't have the number of coins as input
        CharacterCards = new ArrayList<>();
        // TODO what about these advancedPlayers?
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
     * @param numPlayers > 1 useful for version with override
     */
    @Override
    protected void createPlayers(int numPlayers){
        // Lucario: la creazione dei giocatori l'ho fatta con FactoryPlayer, se togliamo advancedPlayer non servirà questo metodo visto che saranno inizializzati nel super()
        //RISPOSTA: ok se proprio vogliamo togliere AdvancedPlayer. Prima si sistema Player, poi si sistema
        //il resto usando il nuovo Player e alla fine si toglie Advanced Player.
        players = FactoryPlayer.getNPlayers(numPlayers, getParameters());
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
        sack = new AdvancedSack(super.getMaxStudentsByType()-2);
    } // Lucario : ho cambiato qui, invece che usare 24 fisso, per usare il parametro in simpleGame
        //RISPOSTA: ok. A sto punto si può sostituire anche il 2 (visto che anche lui è un mezzo magic number)


    // Lucario : per ragioni simili a quelle di Sack ho fatto creare a parte le isole
    // non dovrebbero esserci altre modifiche da fare in questa classe
    //RISPOSTA: ok
    /**
     * Creates the island groups of this game in their advanced form.
     */
    @Override
    protected void createIslandGroups(){
        this.islandGroups = AdvancedIslandGroup.
                getCollectionAdvancedIslandGroup(
                        getAdvancedParameters(),
                        getParameters(),
                        getCurrentIslandGroupId(),
                        getAmountOfIslands());
        setCurrentIslandGroupId(getCurrentIslandGroupId() + getAmountOfIslands());
    }

    @Override
    protected void createParameters() {
        super.createParameters();
        this.advancedParameters = new AdvancedParameterHandler(-1);
    }
}

