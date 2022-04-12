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
    private StudentEnum ChosenStudentType;                 //useful for LoanShark,Fungalmancer
    private final List<CharacterCard> CharacterCards;
    private AdvancedIslandGroup chosenIsland;              //useful for FlagBearer, Herbalist

    public AdvancedGame(int numPlayers, int numCoins, int numCharacterCards) throws IncorrectPlayersException{
        super(numPlayers);
        this.numCoins = numCoins;
        drawIsWin = false;
        MNAdditionalSteps = 0;
        CountTowers = false;
        isIgnoredStudent = false;
        idCharacterCardActive = 0;
        IslandToEvaluateDue = false;
        AdditionalInfluence = 0;
        TradeableStudent = 0;
        chosenIsland = null;
        ChosenStudentType = StudentEnum.NOSTUDENT;
        CharacterCards = new ArrayList<>();


        for(int card= 0; card < numCharacterCards; card++){
            CharacterCards.add(FactoryCharacterCard.getCharacterCard(CharacterCards));
        }
        for(int card= 0; card < numCharacterCards; card++){
            CharacterCards.get(card).initialise(this);
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
        ChosenStudentType = StudentEnum.NOSTUDENT;
        chosenIsland = null;
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

    public void setChosenStudentType(StudentEnum type) {
        ChosenStudentType = type;
    }

    public void setMNAdditionalSteps(int MNAdditionalSteps) {
        this.MNAdditionalSteps = MNAdditionalSteps;
    }

    public void setIslandToEvaluate(AdvancedIslandGroup choosenIsland) {
        this.chosenIsland = choosenIsland;
    }

    public AdvancedIslandGroup getChosenIsland() {
        return chosenIsland;
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

    public void setChosenIsland(AdvancedIslandGroup choosenIsland) {
        this.chosenIsland = choosenIsland;
    }

    public StudentEnum getChosenStudentType() {
        return ChosenStudentType;
    }

    public void setIslandToEvaluateDue(boolean islandToEvaluateDue) {
        IslandToEvaluateDue = islandToEvaluateDue;
    }


    /**
     *
     * @param position >= 0 && position < CharacterCards.size()
     * @return Character card in position 'position'
     */
    public CharacterCard getCharacterCard(int position) {
        return CharacterCards.get(position);
    }

    public int getNumCoins() {
        return numCoins;
    }

    public void addCoin(int coinsToAdd){
        numCoins+= coinsToAdd;
    }

    public void subtractCoin(int coinsToSubtract ){
        numCoins -= coinsToSubtract;
    }

    public boolean isCountTowers() {
        return CountTowers;
    }

    public boolean isDrawIsWin() {
        return drawIsWin;
    }

    public boolean isIgnoredStudent() {
        return isIgnoredStudent;
    }

    public boolean isIslandToEvaluateDue() {
        return IslandToEvaluateDue;
    }

    public int getMNAdditionalSteps() {
        return MNAdditionalSteps;
    }

    public int getAdditionalInfluence() {
        return AdditionalInfluence;
    }

    /**
     *
     * @param positionCard >= 0 && positionCard < CharacterCard.size()
     */
    public void playCharacterCard(int positionCard){

        CharacterCards.get(positionCard).activateEffect(this);
    }

    @Override
    protected void createPlayingSack() {
        sack = new AdvancedSack(24);
    }

}

