package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

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
     * @param parameters != null
     * @param advancedParameters != null
     * @return one random CharacterCard that is not contained in listCardsGot
     */
    public static CharacterCard getCharacterCard(List<CharacterCard> listCardsGot, ParameterHandler parameters, AdvancedParameterHandler advancedParameters){

        CharacterCard x = new Glutton(parameters, advancedParameters); //placeholder
        int times=0;
        int random = (int) Instant.now().getEpochSecond() % numCharacterCards;

        while(times < numCharacterCards){
            if(random == 0)
                x = new Priest(parameters ,advancedParameters);
            if(random == 1)
                x = new Glutton(parameters ,advancedParameters);
            if(random == 2)
                x = new FlagBearer(parameters ,advancedParameters);
            if(random == 3)
                x = new Mailman(parameters ,advancedParameters);
            if(random == 4)
                x = new Herbalist(parameters ,advancedParameters);
            if(random == 5)
                x = new Centaur(parameters ,advancedParameters);
            if(random == 6)
                x = new Juggler(parameters ,advancedParameters);
            if(random == 7)
                x = new Knight(parameters ,advancedParameters);
            if(random == 8)
                x = new Fungalmancer(parameters ,advancedParameters);
            if(random == 9)
                x = new Minstrel(parameters ,advancedParameters);
            if(random == 10)
                x = new Dame(parameters ,advancedParameters);
            if(random == 11)
                x = new LoanShark(parameters ,advancedParameters);


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
     * @param id > 0
     * @param parameters != null
     * @param advancedParameters != null
     * @return CharacterCard with chosen id
     */
    public static CharacterCard getSpecificCard(int id, ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        if(id == 1)
            return  new Priest(parameters, advancedParameters);
        if(id == 2)
            return  new Glutton(parameters ,advancedParameters);
        if(id == 3)
            return  new FlagBearer(parameters, advancedParameters);
        if(id == 4)
            return  new Mailman(parameters, advancedParameters);
        if(id == 5)
            return  new Herbalist(parameters, advancedParameters);
        if(id == 6)
            return  new Centaur(parameters, advancedParameters);
        if(id == 7)
            return  new Juggler(parameters, advancedParameters);
        if(id == 8)
            return  new Knight(parameters, advancedParameters);
        if(id == 9)
            return  new Fungalmancer(parameters, advancedParameters);
        if(id == 10)
            return  new Minstrel(parameters, advancedParameters);
        if(id == 11)
            return  new Dame(parameters, advancedParameters);
        if(id == 12)
            return  new LoanShark(parameters, advancedParameters);

        return new Glutton(parameters, advancedParameters);
    }

    /**
     * Get a random CharacterCard
     * @return a random CharacterCard
     */
    public static CharacterCard getCharacterCard(ParameterHandler parameters ,AdvancedParameterHandler advancedParameters){
        List<CharacterCard> list = new ArrayList<>();
        return getCharacterCard(list, parameters, advancedParameters);
    }


    /**
     *Get all CharacterCards
     * @return list of all CharacterCard
     */
    public static List<CharacterCard> getAllCards(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        List<CharacterCard> listCards = new ArrayList<>();
        listCards.add(new Priest(parameters, advancedParameters));
        listCards.add(new Glutton(parameters, advancedParameters));
        listCards.add(new FlagBearer(parameters, advancedParameters));
        listCards.add(new Mailman(parameters, advancedParameters));
        listCards.add(new Herbalist(parameters, advancedParameters));
        listCards.add(new Centaur(parameters, advancedParameters));
        listCards.add(new Juggler(parameters, advancedParameters));
        listCards.add(new Knight(parameters, advancedParameters));
        listCards.add(new Fungalmancer(parameters, advancedParameters));
        listCards.add(new Minstrel(parameters, advancedParameters));
        listCards.add(new Dame(parameters, advancedParameters));
        listCards.add(new LoanShark(parameters, advancedParameters));

        return  listCards;
    }

    /**
     * Get CharacterCards with InitialEffect as mother class.
     * Those cards that have students on themselves
     * @return list of CharacterCard with Initial effect
     */
    public static List<CharacterCard> getInitialEffectCards(ParameterHandler parameters ,AdvancedParameterHandler advancedParameterHandler){
        List<CharacterCard> listCards = new ArrayList<>();
        listCards.add(new Juggler(parameters ,advancedParameterHandler));
        listCards.add(new Dame(parameters ,advancedParameterHandler));
        listCards.add(new Priest(parameters ,advancedParameterHandler));

        return  listCards;
    }
}
