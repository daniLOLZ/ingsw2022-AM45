package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.AdvancedSack;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.beans.AdvancedGameBoardBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.board.AdvancedBoard;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.FactoryCharacterCard;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.FactoryPlayer;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGame extends SimpleGame {
    private final List<CharacterCard> CharacterCards;
    private  List<AdvancedPlayer> AdvancedPlayers; // Lucario : Non è meglio mettere advancedPlayers in players invece
                                                    // di avere una lista apposita? O lo usi per facilità nel gestirli
                                                    // col controller? In ogni caso si può vedere se servono davvero
    private AdvancedParameterHandler advancedParameters;

    /*
    forse è meglio che il construttore di game abbia come parametro passato una lista di giocatori
    invece che crearseli lui.
     */
    public AdvancedGame(int numPlayers, int numCoins, int numCharacterCards) throws IncorrectPlayersException {
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

    /**
     * AdvanceGame's constructor.
     * Get a players List of AdvancedPlayer (created in network state) and num of coins and num of character
     * cards and create the model of the game
     * @param numCoins > 0
     * @param numCharacterCards > 0
     * @param players != null
     * @throws IncorrectPlayersException if players.size() <= 0 || players.size() > 4
     */
    public AdvancedGame(int numCoins, int numCharacterCards, List<Player> players) throws IncorrectPlayersException {
        super(players.size());
        this.players = players;
        advancedParameters.setNumCoins(numCoins);

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


    /**
     *
     * @param position > 0 && < Charactercards.size()
     * @return Character card in requested position
     */
    public CharacterCard getCharacterCard(int position) {
        return CharacterCards.get(position);
    }

    /**
     *
     * @return advanced parameter of this game
     */
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
    }

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
        advancedParameters = new AdvancedParameterHandler(-1);
    }

    public boolean spendCoin(AdvancedPlayer player, int coin){
        int playerCoin = player.getNumCoins();

        if(playerCoin < coin){
            return false;
        }

        for(int times=0;times<coin;times++){
            player.useCoin();
            advancedParameters.addCoins(1);
        }

        return true;
    }

    public void selectStudentOnCard(int position){
        if(advancedParameters.getSelectedStudentsOnCard().isEmpty()){
            List<Integer> positionList = new ArrayList<>();
            positionList.add(position);
            advancedParameters.setSelectedStudentsOnCard(positionList);
        }
        else
            advancedParameters.selectStudentOnCard(position);
    }

    /**
     * Move a student in selected position at Entrance in Hall table.
     * If the player deserves a coin the game add 1 coin to player and
     * remove 1 coin from advanced parameters
     * @param player != null
     */
    @Override
    public void moveFromEntranceToHall(Player player) {
        AdvancedPlayer advancedPlayer = (AdvancedPlayer) player;         //IF WE HAVE ADVANCED GAME WE HAVE ADVANCED PLAYERS
        StudentEnum studentColor = player.moveFromEntranceToHall();
        AdvancedBoard board = (AdvancedBoard) player.getBoard();        //IF WE HAVE ADVANCED GAME WE HAVE ADVANCED BOARD
        int positionAtTable = board.getStudentsAtTable(studentColor);
        if(board.checkCoinSeat(studentColor, positionAtTable)){
            advancedPlayer.addCoin();
            advancedParameters.removeCoin();
        }
    }

    /**
     * a java Bean with all general information about this game,
     * a list with island groups id, a list of played assistants,
     * a list of players id, the current player id, current turn number,
     * current phase, Game coins and Character Cards id of this game
     * @return
     */
    @Override
    public GameElementBean toBean() {
        List<Integer> idIslands = new ArrayList<>();
        List<Integer> idAssistants = new ArrayList<>();
        List<Integer> idPlayers = new ArrayList<>();
        int currentPlayerId = parameters.getCurrentPlayer().getPlayerId().index;
        int turn = parameters.getTurn();
        String phase = parameters.getCurrentPhase().name;
        int numCoins = advancedParameters.getNumCoins();

        List<Integer> idCharacterCards = CharacterCards.stream().mapToInt(card -> card.id).
                                                collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        for(IslandGroup islandGroup: islandGroups){
            idIslands.add(islandGroup.getIdGroup());
        }
        for(Player player: players){
            Assistant assistant = player.getAssistantPlayed();
            if(assistant != null)
                idAssistants.add(assistant.id);
            else
                idAssistants.add(0);

            idPlayers.add(player.getPlayerId().index);
        }
        AdvancedGameBoardBean bean = new AdvancedGameBoardBean(idIslands,idAssistants,idPlayers,currentPlayerId,
                turn,phase, numCoins, idCharacterCards);
        return bean;
    }
}

