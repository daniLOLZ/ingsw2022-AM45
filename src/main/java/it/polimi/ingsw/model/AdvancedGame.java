package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.FactoryCharacterCard;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGame extends SimpleGame{
    private int numCoins;
    private boolean drawIsWin;                             //Glutton effect active
    private int idCharacterCardActive;                     //CharacterCard active this round
    private int TradeableStudent;                          //Minstrel(or Juggler) effect active when != 0
    private boolean IslandToEvaluateDue;                   //FlagBearer effect active
    private int MNAdditionalSteps;                         //Mailman effect active
    private boolean CountTowers;                           //Centaur effect active
    private int AdditionalInfluence;                       //Knight effect active when != 0
    private boolean isIgnoredStudent;                      //Fungalmacer effect active
    private StudentEnum ChoosenStudentType;                //useful for LoanShark,Fungalmancer
    private final List<CharacterCard> CharacterCards;
    private AdvancedIslandGroup choosenIsland;             //useful for FlagBearer, Herbalist

    public AdvancedGame(int numPlayers, int numCoins, int numCharacterCards) throws IncorrectPlayersException{
        super(numPlayers);
        sack = new AdvancedSack(26);
        this.numCoins = numCoins;
        drawIsWin = false;
        MNAdditionalSteps = 0;
        CountTowers = false;
        isIgnoredStudent = false;
        idCharacterCardActive = 0;
        IslandToEvaluateDue = false;
        AdditionalInfluence = 0;
        TradeableStudent = 0;
        choosenIsland = null;
        ChoosenStudentType = StudentEnum.NOSTUDENT;
        CharacterCards = new ArrayList<>();


        for(int card= 0; card < numCharacterCards; card++){
            CharacterCards.add(FactoryCharacterCard.getCharacterCard(CharacterCards));
        }

    }

    public void resetCardEffect(){
        drawIsWin = false;
        MNAdditionalSteps = 0;
        CountTowers = false;
        isIgnoredStudent = false;
        idCharacterCardActive = 0;
        IslandToEvaluateDue = false;
        AdditionalInfluence = 0;
        TradeableStudent = 0;
        ChoosenStudentType = StudentEnum.NOSTUDENT;
        choosenIsland = null;
    }

    public void setAdditionalInfluence(int additionalInfluence) {
        AdditionalInfluence = additionalInfluence;
    }

    public void setCountTowers(boolean countTowers) {
        CountTowers = countTowers;
    }

    public void setDrawIsWin(boolean drawIsWin) {
        this.drawIsWin = drawIsWin;
    }

    public void setChoosenStudentType(StudentEnum type) {
        ChoosenStudentType = type;
    }

    public void setMNAdditionalSteps(int MNAdditionalSteps) {
        this.MNAdditionalSteps = MNAdditionalSteps;
    }

    public void setIslandToEvaluate(AdvancedIslandGroup choosenIsland) {
        this.choosenIsland = choosenIsland;
    }

    public AdvancedIslandGroup getChoosenIsland() {
        return choosenIsland;
    }

    public void setIgnoredStudent(boolean ignoredStudent) {
        isIgnoredStudent = ignoredStudent;
    }

    public int getIdCharacterCardActive() {
        return idCharacterCardActive;
    }

    public void UsedCharacterCard(int id) {
        idCharacterCardActive= id;
    }

    public int getTradeableStudent() {
        return TradeableStudent;
    }

    public void setTradeableStudent(int tradeableStudent) {
        TradeableStudent = tradeableStudent;
    }

    public void setChoosenIsland(AdvancedIslandGroup choosenIsland) {
        this.choosenIsland = choosenIsland;
    }

    public StudentEnum getChoosenStudentType() {
        return ChoosenStudentType;
    }

    public void setIslandToEvaluateDue(boolean islandToEvaluateDue) {
        IslandToEvaluateDue = islandToEvaluateDue;
    }

    public boolean IslandToEvaluateDue() {
        return IslandToEvaluateDue;
    }
}

