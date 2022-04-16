package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

public class Priest extends InitialEffect{


    public Priest(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(1,1, parameters,advancedParameters);
        requirements = new Requirements(1,0,0,1);
    }

    @Override
    public void initialise(AdvancedGame game) {
        final int startingStudents = 4;
        addAllStudents(game.getSack().drawNStudents(startingStudents));
    }

    /**
     * You can put one student from this card on an Island
     */
    @Override
    public void activateEffect() {
        super.activateEffect();

    }

    /**
     * put studentIndex student from this card and put it on island, then take one student from the
     * sack
     * @param game != null
     */
    public void placeStudentOnIsland(AdvancedGame game){

        //CHECK IF USER SELECT AN ISLAND
        if(parameters.getSelectedIslands().isEmpty()){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedIslands");
            return;
        }

        //CHECK IF USER SELECT STUDENT FROM THE CARD
        if(advancedParameters.getSelectedStudentsOnCard().isEmpty()){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentsOnCard");
            return;
        }

        int studentIndex = advancedParameters.getSelectedStudentsOnCard().get().get(0);
        IslandGroup island = parameters.getSelectedIslands().get().get(0);
        island.addStudent(removeStudent(studentIndex));

        if(!game.getSack().isEmpty())
            addAllStudents(game.getSack().drawNStudents(1));
    }

}
