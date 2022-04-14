package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.AdvancedGame;
import it.polimi.ingsw.model.AdvancedIslandGroup;
import it.polimi.ingsw.model.AdvancedParameterHandler;
import it.polimi.ingsw.model.IslandGroup;

public class Priest extends InitialEffect{
    private final int startingStudents = 4;

    public Priest(AdvancedParameterHandler advancedParameters){
        super(1,1,advancedParameters);
    }

    @Override
    public void initialise(AdvancedGame game) {
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
     * @param game
     * @param island
     * @param studentIndex
     */
    public void placeStudentOnIsland(AdvancedGame game, IslandGroup island, int studentIndex){
        island.addStudent(removeStudent(studentIndex));

        if(!game.getSack().isEmpty())
            addAllStudents(game.getSack().drawNStudents(1));
    }

}
