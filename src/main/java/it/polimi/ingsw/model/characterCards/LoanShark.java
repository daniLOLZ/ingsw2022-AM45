package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedSack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudentEnum;

import java.util.List;

public class LoanShark extends CharacterCard {

    private final int numStudentToSubtract = 3;

    public LoanShark(){
        super(3,12);
    }

    /**
     * Select student color and each player put into the sack
     * numStudentToSubtract students from Hall with this color
     * @param game
     */
    @Override
    public void activateEffect(AdvancedGame game) {
        super.activateEffect(game);
    }

    /**
     * Subtract students with colorStudent from Halls of each Player
     * @param colorStudent
     * @param game
     * @param sack
     */
    public void extortStudents(StudentEnum colorStudent, AdvancedGame game, AdvancedSack sack){

        for(Player player: game.getPlayers()){
            sack.addStudents(player.getBoard().
                    removeNStudentsFromHall(colorStudent,numStudentToSubtract));
        }
    }
}
