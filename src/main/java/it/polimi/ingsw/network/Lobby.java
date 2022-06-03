package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.view.LobbyBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lobby {

    private final GameRuleEnum gameType;
    private List<Integer> playersReady; //identified by idUser
    private List<Integer> players;
    private int emptySeats;
    private Integer host;
    private boolean gameStarted;
    public ReentrantLock readyLock;

    public Lobby(GameRuleEnum gameType){

        this.gameType = gameType;
        players = new ArrayList<>();
        host = null;
        gameStarted = false;
        playersReady = new ArrayList<>();
        readyLock = new ReentrantLock();
        switch (this.gameType){
            case SIMPLE_2, ADVANCED_2 ->  emptySeats = 2;
            case SIMPLE_3, ADVANCED_3 -> emptySeats = 3;
            case SIMPLE_4, ADVANCED_4 -> emptySeats = 4;
        }
    }

    public boolean isFull(){
        return emptySeats == 0;
    }

    /**
     * Adds a player to the ready players' list, if not already present
     * @param idUser The idUser of the player to add (must be in the lobby)
     */
    public void addReady(int idUser){
        //We don't add them twice if already present
        if (players.contains(idUser) &&
            !playersReady.contains(idUser)) playersReady.add(idUser);
        else ;//maybe handle case of idUser not present

    }

    /**
     * Removes a player from the ready players' list
     * @param idUser The idUser of the player to remove
     */
    public void removeReady(int idUser){

        Integer integer = idUser;
        playersReady.remove(integer);

    }

    public GameRuleEnum getGameType(){
        return gameType;
    }


    /**
     * Removes a player from the Lobby.
     * If he was the host, the role is reassigned.
     * @param idUser The idUser of the player to remove (must be in the lobby)
     */
    public synchronized void removePlayer(int idUser){

        if (players.contains(idUser)) {
            Integer integer = idUser;
            players.remove(integer);
            playersReady.remove(integer);
            emptySeats++;
            assignHost();
        }

        if (players.size() == 0) destroyLobby();

    }

    /**
     * Adds a player to the Lobby.
     * If he's the first player added, it becomes the host.
     * @param idUser The idUser of the player to add
     */
    public synchronized void addPlayer(int idUser){

        if (emptySeats == 0) return;

        players.add(idUser);

        emptySeats--;

        assignHost();

    }

    /**
     * Promotes a player as the host if the actual one left or has not been assigned yet
     */
    private void assignHost(){

        if (players.size() == 0){
            host = null;
            return;
        }

        if (!players.contains(host)) host = null;

        if (host == null) host = players.get(0);

    }


    /**
     * Removes the lobby from the global active lobbies' list
     */
    public void destroyLobby(){

        ActiveLobbies.removeLobby(this);
    }

    /**
     *
     * @return the host of this lobby
     */
    public Integer getHost() {
        return host;
    }

    /**
     * Returns the list of the current players' user IDs
     * @return the idUser of this lobby's players
     */
    public List<Integer> getPlayers(){
        return this.players;
    }

    /**
     * Checks whether the user is the host of this lobby
     * @param idUser the user we're checking for
     * @return true if idUser is equal to this.host
     */
    public boolean isHost(int idUser){
        return idUser == this.host;
    }

    /**
     * Tests whether every player in the lobby is ready by comparing the players and playersReady lists
     * @return true if every player in the lobby is ready
     */
    public boolean everyoneReady(){
        if( GameRuleEnum.getNumPlayers(this.gameType.id) == this.players.size() &&
            this.players.containsAll(this.playersReady) &&
            this.playersReady.containsAll(this.players)){
            return true;
        }
        else return false;
    }

    /**
     * Sets the flag "gameStarted" to true
     */
    public void setStartGame(){
        gameStarted = true;

    }

    public boolean isGameStarted(){
        return gameStarted;
    }

    public synchronized LobbyBean toBean() {
        List<String> beanNickList = new ArrayList<>();
        List<Boolean> beanReadyList = new ArrayList<>();
        Integer returnHostPosition = null;
        for(int index = 0; index < players.size(); index++){
            beanNickList.add(
                    LoginHandler.getNicknameFromId(players.get(index))
            );
            beanReadyList.add(
                playersReady.contains(players.get(index))
            );
            if(players.get(index).equals(this.host)) returnHostPosition = index;
        }
        return new LobbyBean(beanNickList, beanReadyList, this.gameStarted, returnHostPosition);
    }
}
