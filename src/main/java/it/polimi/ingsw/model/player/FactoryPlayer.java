package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.TeamEnum;

import java.util.ArrayList;
import java.util.List;

public class FactoryPlayer {

    private static List<String> usedNicknames; // Lucario: Il controllo nickname va fatto a livello più alto

    /**
     *
     * @param nick != null
     * @return true if nick is not present in usedNicknames
     */
    public static boolean validNickname(String nick){
        return ! usedNicknames.contains(nick);
    }

    //LUXRAY: creare un player alla volta permette di gestire meglio l'arrivo degli Utenti.
    //Grazie alla classe PlayerCreation si possono gestire tutte le scelte dell'utente, se sono
    //ammissibili o meno, e infine usare queste scelte per creare il giocatore con la classe FactoryPlayer.
    //La classe PlayerCreation è propria di ogni partita e gestirà le scelte del giocatore per quella
    //relativa partita, mentre la classe factoryPlayer vedrà tutti i game e potrà gestire l'unicità dei
    //nickname.
    //La creazione del Wizard è lasciata a PlayerCreation che da la possibilità di scegliere quale mazzo
    //avere e controlla eventuali ripetizioni, successivamente verrà assegnato al player il Wizard scelto.
    //La classe PlayerCreation gestisce sia le scelte già consolidate (quindi di player già creati e che sono
    //nella partita), sia scelte che arrivano da utenti che sono ancora in fase di creazione.
    /**
     *
     * @param nick != null
     * @param playerId != NOPLAYER
     * @param teamColor != NOTEAM
     * @param leader .
     * @param parameter != null
     * @param advanced .
     * @return a Player if advanced is false and nick is not contained in usedNicknames;
     *         an AdvancedPlayer if advanced is true and nick is not contained in usedNicknames;
     *         null if nick is contained in usedNicknames
     */
    public static Player getPlayer(String nick, PlayerEnum playerId, TeamEnum teamColor,
                                   boolean leader, ParameterHandler parameter, boolean advanced){
        Player player;
        if(usedNicknames == null){
            usedNicknames = new ArrayList<>();
        }
        if(usedNicknames.contains(nick))
            return null;

        if(advanced){
            player = new AdvancedPlayer(playerId,nick,teamColor,leader,parameter);
        }
        else{
            player = new Player(playerId,nick,teamColor,leader,parameter);
        }
        usedNicknames.add(nick);
        return player;

    }
    // stub function
    //TODO handle nicknames and assignment of player to team to make a proper player creation
    public static List<Player> getNPlayers(int numberOfPlayers, ParameterHandler parameters) {
        List<Player> playerList = new ArrayList<>();

        switch (numberOfPlayers) {
            case 2:
                playerList.add(new Player(PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.BLACK, true, parameters));
                break;
            case 3:
                playerList.add(new Player(PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.GREY, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true, parameters));
                break;
            case 4:
                playerList.add(new Player(PlayerEnum.PLAYER1, "mockNickname1", TeamEnum.WHITE, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER2, "mockNickname2", TeamEnum.WHITE, false, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER3, "mockNickname3", TeamEnum.BLACK, true, parameters));
                playerList.add(new Player(PlayerEnum.PLAYER4, "mockNickname4", TeamEnum.BLACK, false, parameters));
                break;
        }
        return playerList;
    }

    //MIGHT BE CHANGED TO PRIVATE
    public static void removeNickname(String nick){
        usedNicknames.remove(nick);
    }
}
