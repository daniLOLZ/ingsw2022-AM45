package it.polimi.ingsw.model;

public class AdvancedParameterHandler {

    private int numCoins;
    private boolean drawIsWin;
    private int MNAdditionalSteps;
    private boolean countTowers;
    private int additionalInfluence;
    private StudentEnum ignoredStudentType;

    public AdvancedParameterHandler(int numCoins){
        this.numCoins = numCoins;
        resetCardEffects();
    }

    public int getNumCoins() {
        return numCoins;
    }

    public boolean isDrawIsWin() {
        return drawIsWin;
    }

    public int getMNAdditionalSteps() {
        return MNAdditionalSteps;
    }

    public boolean isCountTowers() {
        return countTowers;
    }

    public int getAdditionalInfluence() {
        return additionalInfluence;
    }

    public StudentEnum getIgnoredStudentType() {
        return ignoredStudentType;
    }

    /**
     * @param coinsToAdd The number of coins to add (must be > 0)
     */
    public void addCoins(int coinsToAdd){
        numCoins += coinsToAdd;
    }


    public void removeCoin(){
        numCoins--;
    }

    /**
     * @param coinsToRemove The number of coins to remove (must be > 0)
     */
    public int removeCoins(int coinsToRemove){

        int removedCoins = 0;
        for (int coin = 0; coin < coinsToRemove; coin++) {
            if (numCoins > 0){
                removeCoin();
                removedCoins++;
            }
            else return removedCoins;
        }
        return removedCoins;
    }


    public void setDrawIsWin(boolean drawIsWin) {
        this.drawIsWin = drawIsWin;
    }

    public void setCountTowers(boolean countTowers) {
        this.countTowers = countTowers;
    }

    public void addMNSteps(int stepsToAdd){
        MNAdditionalSteps += stepsToAdd;
    }

    public void addInfluence(int infToAdd){
        additionalInfluence += infToAdd;
    }

    public void ignoreStudent(StudentEnum studentType){
        ignoredStudentType = studentType;
    }

    /**
     * Sets every card effect parameter to its default value
     */
    public void resetCardEffects(){
        drawIsWin = false;
        MNAdditionalSteps = 0;
        countTowers = true;
        additionalInfluence = 0;
        ignoredStudentType = StudentEnum.NOSTUDENT;
    }
}
