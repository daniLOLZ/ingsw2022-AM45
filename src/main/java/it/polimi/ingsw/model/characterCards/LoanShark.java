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

    @Override
    public void activateEffect(AdvancedGame game) {
        super.activateEffect(game);
    }

    public void extortStudents(StudentEnum colorStudent, AdvancedGame game, AdvancedSack sack){

        for(Player player: game.getPlayers()){
            sack.addStudents(player.getBoard().
                    removeNStudentsFromHall(colorStudent,numStudentToSubtract));
        }
    }
}
