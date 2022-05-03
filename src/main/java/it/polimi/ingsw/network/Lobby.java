package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRule;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private final GameRule gameType;
    private List<Integer> playersReady; //identified by idUser
    private List<Integer> players;
    private int emptySeats;
    private Integer host;

    public Lobby(GameRule gameType){

        this.gameType = gameType;
        players = new ArrayList<>();
        host = null;
        playersReady = new ArrayList<>();
        switch (this.gameType){
            case SIMPLE_2, ADVANCED_2 ->  emptySeats = 2;
            case SIMPLE_3, ADVANCED_3 -> emptySeats = 3;
            case SIMPLE_4, ADVANCED_4 -> emptySeats = 4;
        }
    }

    public boolean isFull(){
        return emptySeats == 0;
    }

    public void addReady(int idUser){
        if (players.contains(idUser)) playersReady.add(idUser);
        else ;//maybe handle case of idUser not present
    }

    public void removeReady(int idUser){

        Integer integer = idUser;
        playersReady.remove(integer);
    }

    public GameRule getGameType(){
        return gameType;
    }

    public synchronized void removePlayer(int idUser){

        if (players.contains(idUser)) {
            Integer integer = idUser;
            players.remove(integer);
            playersReady.remove(integer);
            assignHost();
        }
    }

    public synchronized void addPlayer(int idUser){

        players.add(idUser);

        assignHost();
    }

    private void assignHost(){

        if (host != null) return;

        if (players.size() == 0){
            host = null;
        }

        else host = players.get(0);
    }
}
