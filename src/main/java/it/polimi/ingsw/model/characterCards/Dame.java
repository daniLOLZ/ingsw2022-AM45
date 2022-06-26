package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.AdvancedBoard;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.player.AdvancedPlayer;
import it.polimi.ingsw.model.player.Player;


public class Dame extends InitialEffect{
    private final static String  name = "DAME";
    private final static String description =
            "Take 1 Student from this card and place it in your Dining Room. " +
            "Then draw a new Student from the Bag and place it on this card.";



    public Dame(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,11, parameters, advancedParameters, name, description);
        requirements = new Requirements(0,0,0,1);
    }

    /**
     * Initialise the list of students on this card drawing from the sack
     * @param game != null
     */
    @Override
    public void initialise(AdvancedGame game) {
        super.initialise(game);
        final int startingStudents = 4;
        addAllStudents(game.getSack().drawNStudents(startingStudents));
    }

    @Override
    public void activateEffect() {
        super.activateEffect();
    }

    /**
     * Take  student indexStudentDame from this Card ant put it in player's Hall.
     * Draw a new student from sack and put it on this card
     * @param sack != null
     */
    public StudentEnum placeStudentToHall(Sack sack){
        Player player = parameters.getCurrentPlayer();
        int indexChosenStudent;

        //CHECK IF USER SELECT A STUDENT ON THIS CARD
        if(advancedParameters.getSelectedStudentsOnCard().isPresent())
            indexChosenStudent = advancedParameters.getSelectedStudentsOnCard().get().get(0);
        else{
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentsOnCard");
            return StudentEnum.NOSTUDENT;
        }

        //CHECK IF SELECTED STUDENT IS IN A POSITION AVAILABLE
        if(indexChosenStudent < 0 || indexChosenStudent >= size() ){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentsOnCard");
            return StudentEnum.NOSTUDENT;
        }

        StudentEnum studentToReturn = removeStudent(indexChosenStudent);
        int numOfStudents;

        //MOVE STUDENT
        player.getBoard().addToHall(studentToReturn);
        numOfStudents = player.getBoard().getStudentsAtTable(studentToReturn);

        //Get the coin if necessary
        if(((AdvancedBoard)player.getBoard()).checkCoinSeat(studentToReturn, numOfStudents)){
            ((AdvancedPlayer)player).addCoin();
            advancedParameters.removeCoin();
        }
        //DRAW FROM SACK
        if(! sack.isEmpty())
            addAllStudents(sack.drawNStudents(1));

        return studentToReturn;
    }
}
