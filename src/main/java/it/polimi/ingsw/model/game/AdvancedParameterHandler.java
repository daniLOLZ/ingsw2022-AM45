package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.characterCards.Requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdvancedParameterHandler {

    //STATIC PARAMETERS
    public static final int numCharacterCardPerGame = 3;

    //DYNAMIC PARAMETERS
    private int numCoins;
    private boolean drawIsWin;
    private int MNAdditionalSteps;
    private boolean countTowers;
    private int additionalInfluence;
    private StudentEnum ignoredStudentType;
    private int characterCardId;
    private Requirements requirementsForThisAction;

    //CHOSEN PARAMETERS
    private Optional<List<Integer>> selectedStudentsOnCard;

    public AdvancedParameterHandler(int numCoins){
        this.numCoins = numCoins;
        resetCardEffects();
    }

    public int getNumCoins() {
        return numCoins;
    }

    public void setNumCoins(int numCoins) { this.numCoins = numCoins; }

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

    public Optional<List<Integer>> getSelectedStudentsOnCard(){
        return selectedStudentsOnCard;
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
     * @return the amount of coins actually removed
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

    public void selectStudentOnCard(int studentPos){
        selectedStudentsOnCard.ifPresent(studentsPos -> studentsPos.add(studentPos));
    }

    public void setSelectedStudentsOnCard(List<Integer> studentsPos){
        selectedStudentsOnCard = Optional.of(new ArrayList<>());
        for (Integer studentPos: studentsPos) selectStudentOnCard(studentPos);
    }

    public void setRequirementsForThisAction(Requirements requirementsForThisAction) {
        this.requirementsForThisAction = requirementsForThisAction;
    }

    public Requirements getRequirementsForThisAction() {
        return requirementsForThisAction;
    }

    public void setCharacterCardId(int characterCardId) {
        this.characterCardId = characterCardId;
    }

    public int getCharacterCardId() {
        return characterCardId;
    }

    /**
     * Sets every card effect parameter to its default value
     */
    public void resetCardEffects(){
        characterCardId = 0;
        drawIsWin = false;
        MNAdditionalSteps = 0;
        countTowers = true;
        additionalInfluence = 0;
        ignoredStudentType = StudentEnum.NOSTUDENT;
        undoSelection();
    }

    public void undoSelection(){
        selectedStudentsOnCard = Optional.empty();
    }
}
