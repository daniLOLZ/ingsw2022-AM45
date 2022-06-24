package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.assistantCards.FactoryAssistant;
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
        s= ("::ADVANCED:: ");
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


        s= ("TURN: ") + (turn);
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

        s = ("PHASE: ")+(phase);
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

        s= ("ISLANDS: ") + (idIslandGroups);
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

        s = ("CURRENT PLAYER: ") + (currentPlayer);
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

        s=("PLAYERS: ") + (players);
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



        s= ("ASSISTANTS PLAYED: ") + (idAssistantsPlayed);
        x+=String.format("%-105s",s);
        s = "________________________________________";
        x+= String.format("%-40s",s);
        x+="\n";

        x+=("COIN: ")+getNumGameCoins();
        x+="\n";
        x+=("CHARACTER CARD: ")+idCharacterCards;
        x+="\n";

        s = ("______________________________________________________");
        x+=String.format("%-105s",s);

        x+="\n";


        return x;
    }

}
