package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedParameterHandler;
import it.polimi.ingsw.model.AdvancedPlayer;
import it.polimi.ingsw.model.ParameterHandler;
import it.polimi.ingsw.model.characterCards.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CharacterCardHandler {
    private final List<CharacterCard> cardList;
    private CharacterCard usingCard;
    private final Controller controller;

    public CharacterCardHandler(Controller controller){
        this.controller = controller;
        cardList = new ArrayList<>();
        AdvancedGame game = controller.advancedGame;

        for(int position=0; position < AdvancedParameterHandler.numCharacterCardPerGame; position++)
            cardList.add(game.getCharacterCard(position));
    }

    /**
     * Play the Character Card, that the User selected before.
     * Check if requirements are fulfilled.
     * Spend Player coins.
     * Activate card effect.
     */
    public void playCard(){
        Requirements requirements =   controller.
                                                advancedGame.
                                                getAdvancedParameters().
                                                getRequirementsForThisAction();
        if(!requirements.isSatisfied()){
            controller.simpleGame.getParameters().setErrorState("REQUIREMENTS FOR CARD NOT SATISFIED");
            return;
        }

        spendCoin();
        usingCard.activateEffect();

            //Lucario : ma l'idea dietro lo strategy non era quello di poter chiamare activateEffect(),
            // e poi quest'ultima funzione era ridefinita in ciascun personaggio per chiamare il
            // proprio metodo caratteristico?
        //CAST WITH CORRECT AND SPECIFIC CHARACTER CARD TO USE MORE SPECIFIC ACTION
        //WHERE THESE ARE NECESSARY
        switch (usingCard.id){

            //PRIEST
            case 1 -> {
                Priest priest = (Priest) usingCard;
                priest.placeStudentOnIsland(controller.advancedGame);
            }

            //GLUTTON   useless
            case 2 ->{
                Glutton glutton = (Glutton) usingCard;
            }

            //FLAG-BEARER
            case 3 -> {
                FlagBearer flagBearer = (FlagBearer) usingCard;
                flagBearer.evaluate(controller.advancedGame);
                //TODO NEED ISLAND-HANDLER IN CONTROLLER PACKAGE
                //NEED TO CHECK IF ANY ISLAND NEED TO BE MERGED
            }

            //MAILMAN   useless
            case 4 -> {
                Mailman mailman = (Mailman) usingCard;
            }

            //HERBALIST
            case 5 -> {
                Herbalist herbalist = (Herbalist) usingCard;
                herbalist.blockIsland();
            }

            //CENTAUR   useless
            case 6 -> {
                Centaur centaur = (Centaur) usingCard;
            }

            //JUGGLER   useless
            case 7 -> {
                Juggler juggler = (Juggler) usingCard;
            }

            //KNIGHT    useless
            case 8 -> {
                Knight knight = (Knight) usingCard;
            }

            //FUNGALMANCER  useless
            case 9 -> {
                Fungalmancer fungalmancer = (Fungalmancer) usingCard;
            }

            //MINSTREL  useless
            case 10 -> {
                Minstrel minstrel = (Minstrel) usingCard;
            }

            //DAME
            case 11 -> {
                Dame dame = (Dame) usingCard;
                dame.placeStudentToHall(controller.advancedGame.getSack());
            }

            //LOAN-SHARK
            case 12 -> {
                LoanShark loanShark = (LoanShark) usingCard;
                loanShark.extortStudents(controller.advancedGame);
            }

        }

        usingCard = null;

    }

    /**
     * Check if User can use this card and update Model to make
     * visible the requirements of this card.
     * When the View fulfill the requirements, can be called the method
     * play card
     * @param idCard > 0 && cardList.contains(idCard)
     */
    public void selectCard(int idCard){
        if(!canUseCard(idCard)){
            controller.simpleGame.getParameters().setErrorState("NOT ENOUGH COIN TO PLAY THIS CARD");
        }
        else{
            usingCard.select();
        }
    }

    /**
     * check if Player has enough coin to use this card
     * @param idCard must be contained in cardList
     * @return true if Player has enough coin, false otherwise
     */
    public boolean canUseCard(final int idCard){
        ParameterHandler parameter = controller.simpleGame.getParameters();
        AdvancedPlayer player = (AdvancedPlayer) parameter.getCurrentPlayer();
        int coinPlayer = player.getNumCoins();

        if(correctId(idCard)){
            return(coinPlayer >= usingCard.getCardCost());
        }
        return false;
    }

    /**
     * check if a correct CardId is provided
     * @param idCard > 0
     * @return true if id is present in cardList, false otherwise
     */
    private boolean correctId(final int idCard){
        Optional<CharacterCard> card = cardList.stream().filter(x -> x.id == idCard).findFirst();
        if(card.isEmpty()){
            controller.simpleGame.getParameters().setErrorState("WRONG ID CHARACTER CARD");
            return false;
        }
        else{
            usingCard = card.get();
            return true;
        }

    }

    /**
     * Spend CurrentPlayer coins to use the card.
     * Player ability to pay has been already check
     * when this method is called
     */
    private void spendCoin(){

        //IF THIS CLASS EXIST CURRENT PLAYER IS AN ADVANCED-PLAYER
        AdvancedPlayer player = (AdvancedPlayer) controller.simpleGame.getParameters().getCurrentPlayer();
        AdvancedParameterHandler parameter = controller.advancedGame.getAdvancedParameters();

        parameter.addCoins(usingCard.getCardCost());                    //GAME RECEIVE COINS
        for(int times=0; times < usingCard.getCardCost(); times++)      //PLAYER LOSE COINS
            player.useCoin();
    }
}
