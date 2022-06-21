package it.polimi.ingsw.model.characterCards;

/**
 * Java Bean used for Character card requirements.
 * Each field stands for the amount of object needed by the card.
 * If attribute value is greater than 1 means that the card need
 * from zero to that value object of that type.
 */
public class Requirements {
    public final int islands;
    public final int studentAtEntrance;
    public final int studentType;
    public final int studentOnCard;
    protected boolean satisfied;

    public Requirements(int islands, int studentAtEntrance, int studentType, int studentOnCard){
        this.islands = islands;
        this.studentAtEntrance = studentAtEntrance;
        this.studentType = studentType;
        this.studentOnCard = studentOnCard;
        satisfied = false;
    }

    public void setSatisfied() {
        this.satisfied = true;
    }

    public boolean isSatisfied(){
        return  satisfied;
    }
}
