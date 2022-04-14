package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedParameterHandler;

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
     * @param advancedParameters
     * @return CharacterCard
     */
    public static CharacterCard getCharacterCard(List<CharacterCard> listCardsGot, AdvancedParameterHandler advancedParameters){

        CharacterCard x = new Glutton(advancedParameters); //placeholder
        int times=0;
        int random = (int) Instant.now().getEpochSecond() % numCharacterCards;

        while(times < numCharacterCards){
            if(random == 0)
                x = new Priest(advancedParameters);
            if(random == 1)
                x = new Glutton(advancedParameters);
            if(random == 2)
                x = new FlagBearer(advancedParameters);
            if(random == 3)
                x = new Mailman(advancedParameters);
            if(random == 4)
                x = new Herbalist(advancedParameters);
            if(random == 5)
                x = new Centaur(advancedParameters);
            if(random == 6)
                x = new Juggler(advancedParameters);
            if(random == 7)
                x = new Knight(advancedParameters);
            if(random == 8)
                x = new Fungalmancer(advancedParameters);
            if(random == 9)
                x = new Minstrel(advancedParameters);
            if(random == 10)
                x = new Dame(advancedParameters);
            if(random == 11)
                x = new LoanShark(advancedParameters);


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
     * @param advancedParameters
     * @return
     */
    public static CharacterCard getSpecificCard(int id, AdvancedParameterHandler advancedParameters){
        if(id == 1)
            return  new Priest(advancedParameters);
        if(id == 2)
            return  new Glutton(advancedParameters);
        if(id == 3)
            return  new FlagBearer(advancedParameters);
        if(id == 4)
            return  new Mailman(advancedParameters);
        if(id == 5)
            return  new Herbalist(advancedParameters);
        if(id == 6)
            return  new Centaur(advancedParameters);
        if(id == 7)
            return  new Juggler(advancedParameters);
        if(id == 8)
            return  new Knight(advancedParameters);
        if(id == 9)
            return  new Fungalmancer(advancedParameters);
        if(id == 10)
            return  new Minstrel(advancedParameters);
        if(id == 11)
            return  new Dame(advancedParameters);
        if(id == 12)
            return  new LoanShark(advancedParameters);

        return new Glutton(advancedParameters);
    }

    /**
     * Get a random CharacterCard
     * @return
     */
    public static CharacterCard getCharacterCard(AdvancedParameterHandler advancedParameters){
        List<CharacterCard> list = new ArrayList<>();
        return getCharacterCard(list, advancedParameters);
    }


    /**
     *Get all CharacterCards
     * @return
     */
    public static List<CharacterCard> getAllCards(AdvancedParameterHandler advancedParameters){
        List<CharacterCard> listCards = new ArrayList<>();
        listCards.add(new Priest(advancedParameters));
        listCards.add(new Glutton(advancedParameters));
        listCards.add(new FlagBearer(advancedParameters));
        listCards.add(new Mailman(advancedParameters));
        listCards.add(new Herbalist(advancedParameters));
        listCards.add(new Centaur(advancedParameters));
        listCards.add(new Juggler(advancedParameters));
        listCards.add(new Knight(advancedParameters));
        listCards.add(new Fungalmancer(advancedParameters));
        listCards.add(new Minstrel(advancedParameters));
        listCards.add(new Dame(advancedParameters));
        listCards.add(new LoanShark(advancedParameters));

        return  listCards;
    }

    /**
     * Get CharacterCards with InitialEffect as mother class.
     * Those cards that have students on themselves
     * @return
     */
    public static List<CharacterCard> getInitialEffectCards(AdvancedParameterHandler advancedParameterHandler){
        List<CharacterCard> listCards = new ArrayList<>();
        listCards.add(new Juggler(advancedParameterHandler));
        listCards.add(new Dame(advancedParameterHandler));
        listCards.add(new Priest(advancedParameterHandler));

        return  listCards;
    }
}
