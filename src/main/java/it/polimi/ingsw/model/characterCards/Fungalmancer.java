package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.StudentEnum;

public class Fungalmancer extends CharacterCard {
    private static final String name = "FUNGALMANCER";
    private static final String description =
            "Choose a color of Student: " +
            "during the influence calculation this turn, " +
            "that color adds no influence.";

    public Fungalmancer(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(3,9, parameters,advancedParameters, name, description);
        requirements = new Requirements(0,0,1,0);
    }

    /**
     * During this turn one color of students don't give influence points
     * Set IgnoredStudent with SelectedStudentType
     */
    @Override
    public void activateEffect() {
        super.activateEffect();

        //CHECK IF USER SELECT A STUDENT TYPE
        if(parameters.getSelectedStudentTypes().isPresent()){
            StudentEnum color = parameters.getSelectedStudentTypes().get().get(0);

            //CHECK IF STUDENT TYPE IS DIFFERENT FROM NO-STUDENT
            if(color != StudentEnum.NOSTUDENT){
                advancedParameters.ignoreStudent(color);
                return;
            }
        }

        //ERROR MESSAGE
        parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentTypes");

    }
}
