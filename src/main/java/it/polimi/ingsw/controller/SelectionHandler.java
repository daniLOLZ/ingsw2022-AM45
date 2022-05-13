package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

import java.util.List;

public class SelectionHandler {
    Controller controller;
    ParameterHandler parameter;
    AdvancedParameterHandler advancedParameter;

    public SelectionHandler(Controller controller){
        this.controller = controller;
        parameter = controller.simpleGame.getParameters();
    }

    /**
     * Deselects the selected island group
     * @param idIslandGroup the id of the island to deselect
     * @return true if succeeded
     */
    public boolean deselectIsland(int idIslandGroup){
        controller.simpleGame.deselectIslandGroup(idIslandGroup);
        return true;
    }

    /**
     * Deselects all island groups
     * @return true if succeeded
     */
    public boolean deselectIsland(){
        controller.simpleGame.deselectAllIslandGroup();
        return true;
    }

    public void selectIsland(int idIslandGroup){
        controller.simpleGame.selectIslandGroup(idIslandGroup);
    }

    public boolean selectIsland(List<Integer> idIslandGroups){
        for(Integer id: idIslandGroups){
            selectIsland(id);
        }
        return true;
    }

    /**
     * Deselects the student at the player's entrance at the position given
     * @param position the position of the student to deselect
     * @return true if the deselection didn't cause errors (so if it deselected student or did nothing)
     */
    public boolean deselectStudentAtEntrance(Integer position){
        controller.simpleGame.deselectEntranceStudent(position);
        return true;
    }

    /**
     * Deselects all students at the entrance of the player's board
     * @return true if the action succeeded
     */
    public boolean deselectStudentAtEntrance(){
        controller.simpleGame.deselectAllEntranceStudents();
        return true;
    }

    public void selectStudentAtEntrance(int position){
        controller.simpleGame.selectEntranceStudent(position);
    }

    public boolean selectStudentAtEntrance(List<Integer> positions){
        for(Integer position: positions){
            selectStudentAtEntrance(position);
        }
        return true;
    }

    /**
     * Deselects the type of student chosen
     * @param type the type of the student
     * @return true if successful
     */
    public boolean deselectStudentType(StudentEnum type){
        controller.simpleGame.deselectStudentType(type);
        return true;
    }

    /**
     * Deselects all student types
     * @return true if successful
     */
    public boolean deselectStudentType(){
        controller.simpleGame.deselectAllStudentTypes();
        return true;
    }

    public void selectStudentType(StudentEnum type){
        controller.simpleGame.selectStudentType(type);
    }

    public boolean selectStudentType(List<StudentEnum> types){
        for(StudentEnum type: types){
            selectStudentType(type);
        }
        return true;
    }

    /**
     * Deselects everything selected in the game parameters that doesn't relate to character cards
     */
    public void deselectAll(){
        deselectStudentAtEntrance();
        deselectStudentType();
        deselectIsland();
    }
}
