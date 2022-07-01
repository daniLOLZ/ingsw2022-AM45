package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.characterCards.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CharacterCardHandler {
    private final List<CharacterCard> cardList;
    private CharacterCard usingCard;
    private final Controller controller;
    private boolean alreadyPlayed;

    public CharacterCardHandler(Controller controller){
        this.controller = controller;
        alreadyPlayed = false;
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
     * @return true if the action succeeded
     */
    public boolean playCard(){

        if(!checkRequirements() || !checkCardSpecificRequisites()){
            controller.simpleGame.getParameters().setErrorState("Requirements for this card not satisfied");
            controller.selectionHandler.deselectAll();
            return false;
        }

        usingCard.getRequirements().setSatisfied();
        //Keep these two in this order
        spendCoin();
        usingCard.activateEffect();

        controller.advancedGame.alert(); //ugly but it's necessary to update the board's coins
        alreadyPlayed = true;

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
                if(controller.simpleGame.getParameters().getSelectedIslands().isEmpty()){
                    controller.simpleGame.getParameters().setErrorState("MISSING ISLAND");
                    return false;
                }

                int idIsland = controller.simpleGame.getParameters().getSelectedIslands().get().get(0).getIdGroup();
                FlagBearer flagBearer = (FlagBearer) usingCard;
                flagBearer.evaluate(controller.advancedGame); //todo possible duplicate evaluation
                controller.islandHandler.evaluateIsland(idIsland);
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

            //MINSTREL
            case 10 -> {
                Minstrel minstrel = (Minstrel) usingCard;
                // coins gotten inside the activateEffect method
            }

            //DAME
            case 11 -> {
                Dame dame = (Dame) usingCard;
                StudentEnum toUpdate = dame.placeStudentToHall(controller.advancedGame.getSack()); // Given the coin already
                controller.advancedGame.updateProfessor(toUpdate);
            }

            //LOAN-SHARK
            case 12 -> {
                LoanShark loanShark = (LoanShark) usingCard;
                loanShark.extortStudents(controller.advancedGame);
            }

        }

        controller.selectionHandler.deselectAll();
        usingCard = null;
        controller.checkInstantWinner();

        return true;
    }

    /**
     * Checks whether the generic requirements specified in the requirements attribute of
     * each character card are met. Unneeded requirements are ignored even if previously selected
     * @return true if the requirements are met.
     */
    private boolean checkRequirements() {
        Requirements requirements =   controller.
                advancedGame.
                getAdvancedParameters().
                getRequirementsForThisAction();

        boolean satisfied = true;

        //Checks that the requirements have been satisfied
        // Here the check is done both to see if the optional is not present
        // AND to see if it's present but the list is empty

        if(requirements.islands != 0){
            if(controller.simpleGame.getParameters().getSelectedIslands().isEmpty()
                    || controller.simpleGame.getParameters().getSelectedIslands().get().isEmpty()) satisfied = false;
            else if(requirements.islands == 1 &&
                    controller.simpleGame.getParameters().getSelectedIslands().get().size() != 1)
                satisfied = false;
            else if(requirements.islands > 1 &&
                    controller.simpleGame.getParameters().getSelectedIslands().get().size() > requirements.islands)
                satisfied = false;
        }
        if(requirements.studentType != 0){
            if(controller.simpleGame.getParameters().getSelectedStudentTypes().isEmpty()
                    || controller.simpleGame.getParameters().getSelectedStudentTypes().get().isEmpty()) satisfied = false;
            else if(requirements.studentType == 1 &&
                    controller.simpleGame.getParameters().getSelectedStudentTypes().get().size() != 1)
                satisfied = false;
            else if(requirements.studentType > 1 &&
                    controller.simpleGame.getParameters().getSelectedStudentTypes().get().size() > requirements.studentType)
                satisfied = false;
        }
        if(requirements.studentOnCard != 0){
            if(controller.advancedGame.getAdvancedParameters().getSelectedStudentsOnCard().isEmpty()
                    || controller.advancedGame.getAdvancedParameters().getSelectedStudentsOnCard().get().isEmpty()) satisfied = false;
            else if(requirements.studentOnCard == 1 &&
                    controller.advancedGame.getAdvancedParameters().getSelectedStudentsOnCard().get().size() != 1)
                satisfied = false;
            else if(requirements.studentOnCard > 1 &&
                    controller.advancedGame.getAdvancedParameters().getSelectedStudentsOnCard().get().size() > requirements.studentOnCard)
                satisfied = false;
        }
        if(requirements.studentAtEntrance != 0){
            if(controller.simpleGame.getParameters().getSelectedEntranceStudents().isEmpty()
                    || controller.simpleGame.getParameters().getSelectedEntranceStudents().get().isEmpty()) satisfied = false;
            else if(requirements.studentAtEntrance == 1 &&
                    controller.simpleGame.getParameters().getSelectedEntranceStudents().get().size() != 1)
                satisfied = false;
            else if(requirements.studentAtEntrance > 1 &&
                    controller.simpleGame.getParameters().getSelectedEntranceStudents().get().size() > requirements.studentAtEntrance)
                satisfied = false;
        }

        return satisfied;
    }

    /**
     * Checks character specific requirements
     * @return true if the additional requirements have been fulfilled
     */
    private boolean checkCardSpecificRequisites() {
        ParameterHandler parameters = controller.simpleGame.getParameters();
        AdvancedParameterHandler advancedParameters = controller.advancedGame.getAdvancedParameters();
        boolean entranceEmpty = (parameters.getSelectedEntranceStudents().isEmpty() || parameters.getSelectedEntranceStudents().get().isEmpty());
        boolean cardEmpty = (advancedParameters.getSelectedStudentsOnCard().isEmpty() || advancedParameters.getSelectedStudentsOnCard().get().isEmpty());
        boolean islandEmpty = (parameters.getSelectedIslands().isEmpty() || parameters.getSelectedIslands().get().isEmpty());
        boolean colorEmpty = (parameters.getSelectedStudentTypes().isEmpty() || parameters.getSelectedStudentTypes().get().isEmpty());


        switch (usingCard.id){
            //PRIEST, GLUTTON, FLAG-BEARER, MAILMAN, HERBALIST, CENTAUR, KNIGHT, FUNGALMANCER, LOANSHARK
            case 1, 2, 3, 4, 5, 6, 8, 9, 12 -> {
                //Not necessary
            }
            //JUGGLER
            case 7 -> {
                //Check that the amount of selected students on the card and at the entrance is the same
                if ( entranceEmpty || cardEmpty ) return false;
                if (parameters.getSelectedEntranceStudents().get().size()
                        != advancedParameters.getSelectedStudentsOnCard().get().size()) return false;
            }
            //MINSTREL
            case 10 -> {
                // todo what if there's already too many students of a type, it'll cause an overflow
                if(entranceEmpty || colorEmpty) return false;
                if(parameters.getSelectedEntranceStudents().get().size()
                        < parameters.getSelectedStudentTypes().get().size()) return false;

                // horrible but it must be done
                // this only works when the amount of students to exchange is 2

                Board playerBoard = parameters.getCurrentPlayer().getBoard();
                // Just one student, so also one color
                if(parameters.getSelectedEntranceStudents().get().size() == 1){
                    if(playerBoard.getStudentsAtTable(parameters.getSelectedStudentTypes().get().get(0)) < 1) return false;
                }
                // Two students
                else {
                    // Just one type OR two of the same type
                    if (parameters.getSelectedStudentTypes().get().size() == 1
                            || parameters.getSelectedStudentTypes().get().get(0).equals(parameters.getSelectedStudentTypes().get().get(1))
                    ) {
                        if (playerBoard.getStudentsAtTable(parameters.getSelectedStudentTypes().get().get(0)) < 2)
                            return false;
                    }
                    // Two different types
                    else {
                        for (StudentEnum type : parameters.getSelectedStudentTypes().get()) {
                            if (playerBoard.getStudentsAtTable(type) < 1) return false;
                        }
                    }
                }
            }
            //DAME
            case 11 -> {
                //todo check that it doesn't overflow the table
            }
        }
        return true;
    }


    /**
     * Check if User can use this card and update Model to make
     * visible the requirements of this card.
     * When the UserInterface fulfill the requirements, can be called the method
     * play card
     * @param idCard > 0 && cardList.contains(idCard)
     * @return true if the card could be selected
     */
    public boolean selectCard(int idCard){
        if(!playableThisTurn()){
            controller.simpleGame.getParameters().setErrorState("A character card has already been played this turn");
            return false;
        }
        if(!correctId(idCard)){
            controller.simpleGame.getParameters().setErrorState("Incorrect card id");
            return false;
        }
        if(!canUseCard(idCard)){
            controller.simpleGame.getParameters().setErrorState("Not enough coins to play this card");
            return false;
        }
        else{
            usingCard.select();
            return true;
        }
    }

    /**
     *  Checks whether a character card was already played this turn
     * @return True if a character card was already played this turn
     */
    private boolean playableThisTurn() {
        return !alreadyPlayed;
    }

    /**
     * check if Player has enough coin to use this card
     * @param idCard must be contained in cardList
     * @return true if Player has enough coin, false otherwise
     */
    public boolean canUseCard(int idCard){
        ParameterHandler parameter = controller.simpleGame.getParameters();
        AdvancedPlayer player = (AdvancedPlayer) parameter.getCurrentPlayer();
        return controller.advancedGame.canUseCharacterCard(player, idCard);
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
        AdvancedGame game = controller.advancedGame;

        game.spendCoin(player, usingCard.getCardCost());

    }

    /**
     * Gets the id of the character card at the position given
     * @param cardPosition the position on the board
     * @return the id of the card in that position or -1 if the position was invalid
     * and the card not found
     */
    public int getIdFromPosition(Integer cardPosition) {
        if(cardPosition < 0 || cardPosition > 2) return -1; //todo hardcoding
        return controller.advancedGame.getCharacterCard(cardPosition).id;
    }

    /**
     * Resets the played status for character cards for this turn
     */
    public void resetTurnRestriction() {
        alreadyPlayed = false;
    }

    /**
     * Resets the effects of character cards played this turn
     */
    public void resetCardEffects() {
        controller.advancedGame.getAdvancedParameters().resetCardEffects();
    }

    //friendly
    CharacterCard getUsingCard() {
        return usingCard;
    }

}
