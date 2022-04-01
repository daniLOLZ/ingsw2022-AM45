package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedIslandGroup;

public class Priest extends InitialEffect{
    private final int startingStudents = 4;

    public Priest(){
        super(1,1);
    }

    @Override
    public void initialise(AdvancedGame game) {
        addAllStudents(game.getSack().drawNStudents(startingStudents));
    }

    @Override
    public void activateEffect(AdvancedGame game) {
        super.activateEffect(game);

    }

    public void placeStudentOnIsland(AdvancedGame game, AdvancedIslandGroup island, int studentIndex){
        island.addStudent(removeStudent(studentIndex));

        if(!game.getSack().isEmpty())
            addAllStudents(game.getSack().drawNStudents(1));
    }

}
