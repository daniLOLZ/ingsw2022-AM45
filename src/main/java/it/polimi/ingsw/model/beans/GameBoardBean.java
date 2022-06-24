package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.assistantCards.FactoryAssistant;
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

        idAssistantsPlayed = idAssistantsPlayed.stream().mapToInt( x ->{
            if(x == 0)
                return 0;
            int y= x % 10;
            if(y == 0)
                y = 10;
            return y;
        }).collect(ArrayList::new,ArrayList::add,ArrayList::addAll);

        String Eryantis = StaticColorCLI.ANSI_RED + "E" +
                StaticColorCLI.ANSI_YELLOW + "R" +
                StaticColorCLI.ANSI_GREEN + "I" +
                StaticColorCLI.ANSI_BLUE + "A" +
                StaticColorCLI.ANSI_CYAN + "N" +
                StaticColorCLI.ANSI_PURPLE + "T"+
                StaticColorCLI.ANSI_RED + "Y"+
                StaticColorCLI.ANSI_YELLOW + "S" +
                StaticColorCLI.ANSI_RESET  ;

        String s;
        String x;

        s = "______________________________________________________";
        x= String.format("%-105s",s);
        s = "________________________________________";
        x+= String.format("%-40s",s);
        x+="\n";
        s= ("::") + (Eryantis) + ("::") ;
        x+=String.format("%-149s",s);
        s="ASSISTANT VALUES  (MN steps, Turn Order)";
        x+=String.format("%-20s",s);
        x+="\n";
        s= ("TURN: ") + (turn);
        x+=String.format("%-105s",s);
        s="ID";
        x+=String.format("%-10s",s);
        s="VALUES";
        x+=String.format("%-10s|",s);
        s="ID";
        x+=String.format("%-10s",s);
        s="VALUES";
        x+=String.format("%-10s",s);
        x+="\n";


        s = ("PHASE: ")+(phase);
        x+=String.format("%-105s",s);
        s="1";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(1).toString();
        x+=String.format("%-10s|",s);
        s="6";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(6).toString();;
        x+=String.format("%-10s",s);
        x+="\n";

        s= ("ISLANDS: ") + (idIslandGroups);
        x+=String.format("%-105s",s);
        s="2";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(2).toString();
        x+=String.format("%-10s|",s);
        s="7";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(7).toString();;
        x+=String.format("%-10s",s);
        x+="\n";

        s = ("CURRENT PLAYER: ") + (currentPlayer);
        x+=String.format("%-105s",s);
        s="3";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(3).toString();
        x+=String.format("%-10s|",s);
        s="8";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(8).toString();;
        x+=String.format("%-10s",s);
        x+="\n";

        s=("PLAYERS: ") + (players);
        x+=String.format("%-105s",s);
        s="4";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(4).toString();
        x+=String.format("%-10s|",s);
        s="9";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(9).toString();;
        x+=String.format("%-10s",s);
        x+="\n";

        s= ("ASSISTANTS PLAYED: ") + (idAssistantsPlayed);
        x+=String.format("%-105s",s);
        s="5";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(5).toString();
        x+=String.format("%-10s|",s);
        s="10";
        x+=String.format("%-10s",s);
        s= FactoryAssistant.getAssistant(10).toString();;
        x+=String.format("%-10s",s);
        x+="\n";

        s = ("______________________________________________________");
        x+=String.format("%-105s",s);
        s = "________________________________________";
        x+= String.format("%-40s",s);
        x+="\n";


        return x;
    }

}
