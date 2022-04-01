package it.polimi.ingsw.model.characterCards;

import java.time.Instant;
import java.util.List;

/**
 * use this class to create one CharacterCard with static method getCharacterCard,
 * with listCardsGot param you can create a random card without create two identical cards.
 * this class has a numCharacterCards field with the amount of cards that can be created
 */
public class FactoryCharacterCard {

    public final static int numCharacterCards = 12;

    /**
     * choose a random card to create and check if this one already exists in listCardsGot,
     * if yes create another one
     * if no return the card
     * @param listCardsGot != null
     * @return CharacterCard
     */
    public static CharacterCard getCharacterCard(List<CharacterCard> listCardsGot){

        CharacterCard x = new Glutton(); //placeholder
        int times=0;
        int random = (int) Instant.now().getEpochSecond() % numCharacterCards;

        while(times < numCharacterCards){
            if(random == 0)
                x = new Priest();
            if(random == 1)
                x = new Glutton();
            if(random == 2)
                x = new FlagBearer();
            if(random == 3)
                x = new Mailman();
            if(random == 4)
                x = new Herbalist();
            if(random == 5)
                x = new Centaur();
            if(random == 6)
                x = new Juggler();
            if(random == 7)
                x = new Knight();
            if(random == 8)
                x = new Fungalmancer();
            if(random == 9)
                x = new Minstrel();
            if(random == 10)
                x = new Dame();
            if(random == 11)
                x = new LoanShark();


            if(listCardsGot.contains(x)){
                times++;
                random = (random + 1) % numCharacterCards;
            }
            else
                return x;
        }
        //TODO
        return x;
    }
}
