package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.game.ParameterHandler;

import java.util.List;

public class SelectionHandler {
    Controller controller;
    ParameterHandler parameters;


    public SelectionHandler(Controller controller){
        this.controller = controller;
        parameters = controller.simpleGame.getParameters();
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

    /**
     * Selects the island in the parameter
     * @param idIslandGroup the island to select
     * @return true if succeeded
     */
    public boolean selectIsland(int idIslandGroup){
        if(!controller.simpleGame.checkValidIdIsland(idIslandGroup)) return false;
        controller.simpleGame.selectIslandGroup(idIslandGroup);
        return true;
    }

    /**
     * Deselects all islands selected previously and selects the ones in the parameter
     * @param idIslandGroups the islands to select, which will replace the previously selected islands
     * @return true if the selection happened successfully.
     * If even just one selection fails, this method returns false
     */
    public boolean selectIsland(List<Integer> idIslandGroups){
        //Deselect all previously selected islands
        deselectIsland();

        for(Integer id: idIslandGroups){
            //If even just one fails, deselect everything
            if(!selectIsland(id)){
                deselectIsland();
                return false;
            }
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

    /**
     * Selects a student at the entrance
     * @param position the position of the student at the entrance to select
     * @return true if succeeded
     */
    public boolean selectStudentAtEntrance(int position){
        if(position >= 0
        && position < parameters.getMaxStudentsAtEntrance()
        && !parameters
            .getCurrentPlayer()
            .getBoard()
            .getAtEntrance(position).equals(StudentEnum.NOSTUDENT))
        {
            controller.simpleGame.selectEntranceStudent(position);
            return true;
        }
        else return false;
    }

    /**
     * Deselects all students selected previously and selects the ones in the parameter
     * @param positions the students to select, which will replace the previously selected students
     * @return true if the selection happened successfully.
     * If even just one selection fails, this method returns false
     */
    public boolean selectStudentAtEntrance(List<Integer> positions){
        //Deselect all previously selected students
        deselectStudentAtEntrance();

        for(Integer position: positions){
            if(!selectStudentAtEntrance(position)){
                //If even one failed, deselect everything
                deselectStudentAtEntrance();
                return false;
            }
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

    /**
     * Selects the student type in the parameter
     * @param type the type of student to select
     * @return true if succeeded
     */
    public boolean selectStudentType(StudentEnum type){
        if(!type.equals(StudentEnum.NOSTUDENT)){
            controller.simpleGame.selectStudentType(type);
            return true;
        }
        return false;
    }

    /**
     * Deselects all types selected previously and selects the ones in the parameter
     * @param types the student types to select, which will replace the previously selected student types
     * @return true if the selection happened successfully.
     * If even just one selection fails, this method returns false
     */
    public boolean selectStudentType(List<StudentEnum> types){
        //Deselect all previously selected student types
        deselectStudentType();

        for(StudentEnum type: types){
            //If even just one fails, deselect everything
            if(!selectStudentType(type)) {
                deselectStudentType();
                return false;
            }
        }
        return true;
    }

    /**
     * Deselects everything selected in the game parameters
     */
    public void deselectAll(){
        deselectStudentAtEntrance();
        deselectStudentType();
        deselectIsland();
    }
}
