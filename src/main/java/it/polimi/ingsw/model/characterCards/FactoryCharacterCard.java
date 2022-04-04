package it.polimi.ingsw.model.characterCards;

import java.time.Instant;
import java.util.ArrayList;
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
        return x;
    }

    /**
     * Get specific CharacterCard with id selected
     * @param id
     * @return
     */
    public static CharacterCard getSpecificCard(int id){
        if(id == 1)
            return  new Priest();
        if(id == 2)
            return  new Glutton();
        if(id == 3)
            return  new FlagBearer();
        if(id == 4)
            return  new Mailman();
        if(id == 5)
            return  new Herbalist();
        if(id == 6)
            return  new Centaur();
        if(id == 7)
            return  new Juggler();
        if(id == 8)
            return  new Knight();
        if(id == 9)
            return  new Fungalmancer();
        if(id == 10)
            return  new Minstrel();
        if(id == 11)
            return  new Dame();
        if(id == 12)
            return  new LoanShark();

        return new Glutton();
    }

    /**
     * Get a random CharacterCard
     * @return
     */
    public static CharacterCard getCharacterCard(){
        List<CharacterCard> list = new ArrayList<>();
        return getCharacterCard(list);
    }


    /**
     *Get all CharacterCards
     * @return
     */
    public static List<CharacterCard> getAllCards(){
        List<CharacterCard> listCards = new ArrayList<>();
        listCards.add(new Priest());
        listCards.add(new Glutton());
        listCards.add(new FlagBearer());
        listCards.add(new Mailman());
        listCards.add(new Herbalist());
        listCards.add(new Centaur());
        listCards.add(new Juggler());
        listCards.add(new Knight());
        listCards.add(new Fungalmancer());
        listCards.add(new Minstrel());
        listCards.add(new Dame());
        listCards.add(new LoanShark());

        return  listCards;
    }

    /**
     * Get CharacterCards with InitialEffect as mother class.
     * Those cards that have students on themselves
     * @return
     */
    public static List<CharacterCard> getInitialEffectCards(){
        List<CharacterCard> listCards = new ArrayList<>();
        listCards.add(new Juggler());
        listCards.add(new Dame());
        listCards.add(new Priest());

        return  listCards;
    }
}
