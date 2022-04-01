package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.Glutton;

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
        //TODO
        return x;
    }
}
