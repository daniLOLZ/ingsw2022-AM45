package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.game.AdvancedGame;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.player.Player;


public class LoanShark extends CharacterCard {



    public LoanShark(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(3,12, parameters, advancedParameters);
        requirements = new Requirements(0,0,1,0);
    }

    /**
     * Select student color and each player put into the sack
     * numStudentToSubtract students from Hall with this color
     */
    @Override
    public void activateEffect() {
        super.activateEffect();
    }

    /**
     * Subtract students with colorStudent from Halls of each Player
     * @param game != null
     */
    public void extortStudents( AdvancedGame game){
        final int numStudentToSubtract = 3;
        AdvancedSack sack = (AdvancedSack) game.getSack();  //IF THIS CARD EXISTS THE SACK IS AN ADVANCED SACK

        //CHECK IF USER SELECT A STUDENT TYPE
        if(parameters.getSelectedStudentTypes().isEmpty()){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentType");
            return;
        }

        StudentEnum colorStudent = parameters.getSelectedStudentTypes().get().get(0);

        //CHECK IF COLOR IS NOT NO-STUDENT
        if(colorStudent == StudentEnum.NOSTUDENT){
            parameters.setErrorState("BAD PARAMETERS WITH SelectedStudentType");
            return;
        }

        for(Player player: game.getPlayers()){
            sack.addStudents(player.getBoard().
                    removeNStudentsFromHall(colorStudent,numStudentToSubtract));
        }
    }
}
