package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Sack;

public class Dame extends InitialEffect{

    private final int startingStudents = 4;

    public Dame(){
        super(2,11);
    }

    @Override
    public void initialise(AdvancedGame game) {
        addAllStudents(game.getSack().drawNStudents(startingStudents));
    }

    @Override
    public void activateEffect(AdvancedGame game) {
        super.activateEffect(game);
    }

    public void placeStudentToHall(Player player, int indexStudenDame, Sack sack){
        player.getBoard().addToHall(removeStudent(indexStudenDame));
        addAllStudents(sack.drawNStudents(1));
    }
}
