package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;


public class Dame extends InitialEffect{



    public Dame(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(2,11, parameters, advancedParameters);
        requirements = new Requirements(0,0,0,1);
    }

    @Override
    public void initialise(AdvancedGame game) {
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
    public void placeStudentToHall(Sack sack){
        Player player = parameters.getCurrentPlayer();
        int indexChosenStudent;

        //CHECK IF USER SELECT A STUDENT ON THIS CARD
        if(advancedParameters.getSelectedStudentsOnCard().isPresent())
            indexChosenStudent = advancedParameters.getSelectedStudentsOnCard().get().get(0);
        else{
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentsOnCard");
            return;
        }

        //CHECK IF SELECTED STUDENT IS IN A POSITION AVAILABLE
        if(indexChosenStudent < 0 || indexChosenStudent >= size() ){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentsOnCard");
            return;
        }

        //MOVE STUDENT
        player.getBoard().addToHall(removeStudent(indexChosenStudent));
        //DRAW FROM SACK
        if(! sack.isEmpty())
            addAllStudents(sack.drawNStudents(1));
    }
}
