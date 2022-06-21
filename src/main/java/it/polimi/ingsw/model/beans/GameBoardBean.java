package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.BeanEnum;
import it.polimi.ingsw.view.StaticColorCLI;

import java.util.ArrayList;
import java.util.List;

public class GameBoardBean extends GameElementBean{
    protected List<Integer> idIslandGroups;
    protected List<Integer> idAssistantsPlayed;
    protected List<Integer> idPlayers;
    protected Integer currentPlayerId;
    protected Integer turn;
    protected PhaseEnum phase;

    public GameBoardBean( List<Integer> idIslandGroups, List<Integer> idAssistantsPlayed,
                          List<Integer> idPlayers, Integer currentPlayerId,
                          Integer turn, PhaseEnum phase){
        final int highPriority = 1;
        priority = highPriority;
        this.idIslandGroups = idIslandGroups;
        this.idPlayers= idPlayers;
        this.idAssistantsPlayed = idAssistantsPlayed;
        this.turn = turn;
        this.phase = phase;
        this.currentPlayerId = currentPlayerId;
    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.GAMEBOARD_BEAN;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public void setIdAssistantsPlayed(List<Integer> idAssistantsPlayed) {
        this.idAssistantsPlayed = idAssistantsPlayed;
    }

    public void setCurrentPlayerId(Integer currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public void setIdIslandGroups(List<Integer> idIslandGroups) {
        this.idIslandGroups = idIslandGroups;
    }

    public void setIdPlayers(List<Integer> idPlayers) {
        this.idPlayers = idPlayers;
    }

    public void setPhase(PhaseEnum phase) {
        this.phase = phase;
    }

    public Integer getCurrentPlayerId() {
        return currentPlayerId;
    }

    public Integer getTurn() {
        return turn;
    }

    public List<Integer> getIdAssistantsPlayed() {
        return idAssistantsPlayed;
    }

    public List<Integer> getIdIslandGroups() {
        return idIslandGroups;
    }

    public List<Integer> getIdPlayers() {
        return idPlayers;
    }

    public PhaseEnum getPhase() {
        return phase;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        String currentPlayer = PlayerEnum.getPlayer(currentPlayerId).name;
        List<String> players = new ArrayList<>();
        for(Integer x: idPlayers){
            players.add(PlayerEnum.getPlayer(x).name);
        }

        String Eryantis = StaticColorCLI.ANSI_RED + "E" +
                StaticColorCLI.ANSI_YELLOW + "R" +
                StaticColorCLI.ANSI_GREEN + "I" +
                StaticColorCLI.ANSI_BLUE + "A" +
                StaticColorCLI.ANSI_CYAN + "N" +
                StaticColorCLI.ANSI_PURPLE + "T"+
                StaticColorCLI.ANSI_RED + "Y"+
                StaticColorCLI.ANSI_YELLOW + "S" +
                StaticColorCLI.ANSI_RESET  ;


        toReturn.append("\t______________________________________________________\t\n");
        toReturn.append("\t\t\t\t::").append(Eryantis).append("::").append("\n");
        toReturn.append("\t\tTURN: ").append(turn).append("\n");
        toReturn.append("\t\tPHASE: ").append(phase).append("\n");
        toReturn.append("\t\tISLANDS: ").append(idIslandGroups).append("\n");
        toReturn.append("\t\tCURRENT PLAYER: ").append(currentPlayer).append("\n");
        toReturn.append("\t\tPLAYERS: ").append(players).append("\n");
        toReturn.append("\t\tASSISTANTS PLAYED: ").append(idAssistantsPlayed).append("\n");
        toReturn.append("\t______________________________________________________\t\n");

        return toReturn.toString();
    }

}
