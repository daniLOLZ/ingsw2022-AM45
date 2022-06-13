package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.game.PhaseEnum;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.BeanEnum;
import it.polimi.ingsw.view.StaticColorCLI;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGameBoardBean extends GameBoardBean{

    private Integer numGameCoins;
    private List<Integer> idCharacterCards;
    public AdvancedGameBoardBean(List<Integer> idIslandGroups, List<Integer> idAssistantsPlayed,
                                 List<Integer> idPlayers, Integer currentPlayerId, Integer turn,
                                 PhaseEnum phase, int numGameCoins,
                                 List<Integer> idCharacterCards) {
        super(idIslandGroups, idAssistantsPlayed, idPlayers, currentPlayerId, turn, phase);
        this.numGameCoins = numGameCoins;
        this.idCharacterCards = idCharacterCards;

    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.ADVANCED_GAMEBOARD_BEAN;
    }

    public Integer getNumGameCoins() {
        return numGameCoins;
    }

    public List<Integer> getIdCharacterCards() {
        return idCharacterCards;
    }

    public void setNumGameCoins(Integer numGameCoins) {
        this.numGameCoins = numGameCoins;
    }

    public void setIdCharacterCards(List<Integer> idCharacterCards) {
        this.idCharacterCards = idCharacterCards;
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
                          StaticColorCLI.ANSI_GREEN + "Y" +
                          StaticColorCLI.ANSI_BLUE + "A" +
                          StaticColorCLI.ANSI_CYAN + "N" +
                          StaticColorCLI.ANSI_PURPLE + "T"+
                          StaticColorCLI.ANSI_RED + "I"+
                          StaticColorCLI.ANSI_YELLOW + "S" +
                          StaticColorCLI.ANSI_RESET  ;


        toReturn.append("\t______________________________________________________\t\n");
        toReturn.append("\t\t\t\t::").append(Eryantis).append("::").append("\n");
        toReturn.append("\t\t\t\t::ADVANCED::").append("\n");
        toReturn.append("\t\tTURN: ").append(turn).append("\n");
        toReturn.append("\t\tPHASE: ").append(phase).append("\n");
        toReturn.append("\t\tISLANDS: ").append(idIslandGroups).append("\n");
        toReturn.append("\t\tGAME COINS: ").append(numGameCoins).append("\n");
        toReturn.append("\t\tCHARACTER CARDS: ").append(idCharacterCards).append("\n");
        toReturn.append("\t\tCURRENT PLAYER: ").append(currentPlayer).append("\n");
        toReturn.append("\t\tPLAYERS: ").append(players).append("\n");
        toReturn.append("\t\tASSISTANTS PLAYED: ").append(idAssistantsPlayed).append("\n");
        toReturn.append("\t______________________________________________________\t\n");

        return toReturn.toString();
    }

}
