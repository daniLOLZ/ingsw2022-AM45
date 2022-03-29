package it.polimi.ingsw.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AdvancedGame extends SimpleGame{
    private int numCoins;
    private boolean drawIsWin;
    private int MNAdditionalSteps;
    private boolean CountTowers;
    private int AdditionalInfluence;
    private StudentEnum IgnoredStudentType;
    private List<CharacterCard> CharacterCards;

    public AdvancedGame(int numPlayers, int numCoins, int numCharacterCards){
        super(numPlayers);
        this.numCoins = numCoins;
        drawIsWin = false;
        MNAdditionalSteps = 0;
        CountTowers = false;
        AdditionalInfluence = 0;
        IgnoredStudentType = StudentEnum.NOSTUDENT;
        CharacterCards = new ArrayList<>();


        for(int card= 0; card < numCharacterCards; card++){
            CharacterCards.add(CharacterCardCreator.getCharacterCard(CharacterCards));
        }

    }

    public void resetCardEffect(){
        drawIsWin = false;
        MNAdditionalSteps = 0;
        CountTowers = false;
        AdditionalInfluence = 0;
        IgnoredStudentType = StudentEnum.NOSTUDENT;
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

    public void setIgnoredStudentType(StudentEnum ignoredStudentType) {
        IgnoredStudentType = ignoredStudentType;
    }

    public void setMNAdditionalSteps(int MNAdditionalSteps) {
        this.MNAdditionalSteps = MNAdditionalSteps;
    }
}

