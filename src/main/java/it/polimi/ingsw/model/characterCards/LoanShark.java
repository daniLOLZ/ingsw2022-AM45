package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.List;

public class LoanShark extends CharacterCard {

    private final int numStudentToSubtract = 3;

    public LoanShark(ParameterHandler parameters, AdvancedParameterHandler advancedParameters){
        super(3,12, parameters, advancedParameters);
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
     * @param colorStudent
     * @param game
     * @param sack
     */
    //TODO modify this method to not have access to AdvancedGame
    public void extortStudents(StudentEnum colorStudent, AdvancedGame game, AdvancedSack sack){

        for(Player player: game.getPlayers()){
            sack.addStudents(player.getBoard().
                    removeNStudentsFromHall(colorStudent,numStudentToSubtract));
        }
    }
}
