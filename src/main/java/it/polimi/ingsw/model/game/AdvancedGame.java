package it.polimi.ingsw.model.game;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.AdvancedSack;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.beans.AdvancedGameBoardBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.board.AdvancedBoard;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.FactoryCharacterCard;
import it.polimi.ingsw.model.islands.AdvancedIslandGroup;
import it.polimi.ingsw.model.islands.IslandGroup;
import it.polimi.ingsw.model.islands.UnmergeableException;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.FactoryPlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.observer.AdvancedGameWatcher;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGame extends SimpleGame {
    private List<CharacterCard> CharacterCards;
    private AdvancedParameterHandler advancedParameters;

    @Deprecated
    public AdvancedGame(int numPlayers, List<Integer> selectedWizards, List<TeamEnum> selectedColors, List<String> nicknames, int numCoins, int numCharacterCards) throws  IncorrectPlayersException{
        super(numPlayers, selectedWizards, selectedColors, nicknames);
        advancedParameters.setNumCoins(numCoins); // number of coins in the parameters is added at a later
                                                    // time because we need to create parameters before
                                                    // creating the islands, this happens in the createParameters()
                                                    // method, which don't have the number of coins as input
        CharacterCards = new ArrayList<>();
        for(int card = 0; card < numCharacterCards; card++){
            CharacterCards.add(FactoryCharacterCard.
                    getCharacterCard(CharacterCards, super.getParameters(), advancedParameters));
        }

    }


    /**
     * Version with observer pattern
     * @param numPlayers
     * @param selectedWizards
     * @param selectedColors
     * @param nicknames
     * @param numCoins
     * @param numCharacterCards
     * @param virtualView
     * @throws IncorrectPlayersException
     */
    public AdvancedGame(int numPlayers, List<Integer> selectedWizards,
                        List<TeamEnum> selectedColors, List<String> nicknames,
                        int numCoins, int numCharacterCards, VirtualView virtualView) throws  IncorrectPlayersException{
        super(numPlayers, selectedWizards, selectedColors, nicknames, virtualView);
        advancedParameters.setNumCoins(numCoins); // number of coins in the parameters is added at a later
        // time because we need to create parameters before
        // creating the islands, this happens in the createParameters()
        // method, which don't have the number of coins as input
        CharacterCards = new ArrayList<>();
        for(int card = 0; card < numCharacterCards; card++){
            CharacterCards.add(FactoryCharacterCard.
                    getCharacterCard(CharacterCards, super.getParameters(), advancedParameters));
        }

        for(int card = 0; card < numCharacterCards; card++){
            CharacterCards.get(card).addWatcher(virtualView);
        }

        watcherList = new ArrayList<>();
        AdvancedGameWatcher watcher = new AdvancedGameWatcher(this, virtualView);
        watcherList.add(watcher);
        watchers = watcherList;



    }

    @Override
    public void initializeGame() {
        super.initializeGame();
        int numCharacterCards = CharacterCards.size();
        for(int card = 0; card < numCharacterCards; card++){
            CharacterCards.get(card).initialise(this);
        }
        alert();
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




    @Override
    protected void createPlayers(int numPlayers, List<Integer> selectedWizards,
                                 List<TeamEnum> selectedColors, List<String> nicknames) {
        super.createPlayers(numPlayers, selectedWizards, selectedColors, nicknames);
        List<Player> advancedPlayers = new ArrayList<>();
        for(Player player: players){ // Unhappy cast that could be resolved by separating
            // into two methods : getPlayer and getAdvancedPlayer
            advancedPlayers.add(
                    (AdvancedPlayer)FactoryPlayer.getPlayer(
                            player.getNickname(),
                            player.getPlayerId(),
                            player.getTeamColor(),
                            player.getWizard(),
                            player.isLeader(),
                            getParameters(),
                            true));
        }

        players = advancedPlayers;
    }


    @Override
    protected void createPlayers(int numPlayers, List<Integer> selectedWizards,
                                 List<TeamEnum> selectedColors, List<String> nicknames,
                                 VirtualView virtualView) {
        super.createPlayers(numPlayers, selectedWizards, selectedColors, nicknames, virtualView );
        List<Player> advancedPlayers = new ArrayList<>();
        for(Player player: players){ // Unhappy cast that could be resolved by separating
            // into two methods : getPlayer and getAdvancedPlayer
            advancedPlayers.add(
                    (AdvancedPlayer)FactoryPlayer.getPlayer(
                            player.getNickname(),
                            player.getPlayerId(),
                            player.getTeamColor(),
                            player.getWizard(),
                            player.isLeader(),
                            getParameters(),
                            true, virtualView));
        }

        players = advancedPlayers;
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

    /**
     * Creates the island groups of this game in their advanced form.
     */
    @Override
    protected void createIslandGroups(VirtualView virtualView){
        this.islandGroups = AdvancedIslandGroup.
                getCollectionAdvancedIslandGroup(
                        getAdvancedParameters(),
                        getParameters(),
                        getCurrentIslandGroupId(),
                        getAmountOfIslands(), virtualView);
        setCurrentIslandGroupId(getCurrentIslandGroupId() + getAmountOfIslands());
    }

    @Override
    protected void createParameters() {
        super.createParameters();
        advancedParameters = new AdvancedParameterHandler(-1);
    }

    /**
     * Player spends a number of coins equals to coin argument.
     * If player does not have enough coins return false and do not
     * subtract player coins
     * @param player != null
     * @param coin > 0
     * @return false if player can not spend that number of coins
     */
    public boolean spendCoin(AdvancedPlayer player, int coin){
        int playerCoin = player.getNumCoins();

        if(playerCoin < coin){
            return false;
        }

        for(int times=0;times<coin;times++){
            player.useCoin();
            advancedParameters.addCoins(1);
        }

        alert();

        return true;
    }

    /**
     * Deselects the students on this card by assigning a new empty list
     */
    public void deselectAllCardStudents() {
        advancedParameters.setSelectedStudentsOnCard(new ArrayList<>());
    }

    /**
     * Deselects the student on the card selected at the position given
     * @param position the position of the student to deselect
     */
    public void deselectStudentOnCard(int position){
        //todo move deselection logic to parameters
        if(advancedParameters.getSelectedStudentsOnCard().isEmpty()) return;
        else {
            advancedParameters.getSelectedStudentsOnCard().get().remove((Integer) position);
        }
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
     * remove 1 coin from advanced parameters.
     * Update professors.
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
        updateProfessor(studentColor);
        deselectAllEntranceStudents();
        player.alert();
    }

    /**
     * Update professor considering the presence of glutton's effect
     * @param professor the professor who needs to checked for updates
     */
    @Override
    public void updateProfessor(StudentEnum professor) {
        if(!advancedParameters.isDrawIsWin())
            super.updateProfessor(professor);
        else{
            Player currentPlayer = parameters.getCurrentPlayer();
            int max = currentPlayer.getNumStudentAtTable(professor);
            Player challenger = currentPlayer;
            int curr;

            for(Player player: players){
                curr = player.getNumStudentAtTable(professor);
                if( curr > max ){
                    challenger = player;
                }
            }

            parameters.addProfessor(challenger.getPlayerId(), professor);
        }
        for (Player player : this.players){
            player.alert();
        }

        alert();
    }

    /**
     *
     * @param player who want to use the card
     * @param idCard of the character card
     * @return true if id is valid and player has enough coins
     */
    public boolean canUseCharacterCard(AdvancedPlayer player, final int idCard){
        CharacterCard card = null;
        for(CharacterCard x : CharacterCards){
            if(x.id == idCard)
                card = x;
        }

        return !(card == null || card.getCardCost() > player.getNumCoins());

    }


    /**
     *
     * @param idIsland > 0
     * @return true if chosen island have one or more block tiles
     */
    public boolean isBlocked(int idIsland){
        IslandGroup island = null;
        for(IslandGroup islandGroup: islandGroups){
            if(islandGroup.getIdGroup() == idIsland)
                island = islandGroup;
        }

        if(island == null){
            parameters.setErrorState("WRONG ID ISLAND GROUP");
            return false;
        }

        //In advanced game we have advanced islandGroup
        AdvancedIslandGroup advancedIsland = (AdvancedIslandGroup) island;
        return advancedIsland.getNumBlockTiles() > 0;
    }

    /**
     * Remove a block tile from chosen island
     * @param idIsland > 0
     */
    public void unblockIsland(int idIsland){
        IslandGroup island = null;
        for(IslandGroup islandGroup: islandGroups){
            if(islandGroup.getIdGroup() == idIsland)
                island = islandGroup;
        }

        if(island == null){
            parameters.setErrorState("WRONG ID ISLAND GROUP");
            return;
        }

        AdvancedIslandGroup advancedIsland = (AdvancedIslandGroup) island;
        advancedIsland.unblock();
    }

    /**
     *
     * @return a java Bean with all general information about this game,
     *       a list with island groups id, a list of played assistants,
     *       a list of players id, the current player id, current turn number,
     *       current phase, Game coins and Character Cards id of this game
     */
    @Override
    public GameElementBean toBean() {
        List<Integer> idIslands = new ArrayList<>();
        List<Integer> idAssistants = new ArrayList<>();
        List<Integer> idPlayers = new ArrayList<>();
        int currentPlayerId = parameters.getCurrentPlayer().getPlayerId().index;
        int turn = parameters.getTurn();
        PhaseEnum phase = parameters.getCurrentPhase();
        int numCoins = advancedParameters.getNumCoins();

        List<Integer> idCharacterCards = CharacterCards.stream().mapToInt(card -> card.id).
                                                collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        sortIslandGroups();
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



    @Override
    public void initialiseSelection() {
        super.initialiseSelection();
        List<Integer> emptyStudentsOnCard = new ArrayList<>();
        advancedParameters.setSelectedStudentsOnCard(emptyStudentsOnCard);
    }

    public void setCharacterCards(List<CharacterCard> characterCards) {
        CharacterCards = characterCards;
    }
}

