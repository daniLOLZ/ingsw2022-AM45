package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.player.Player;

public class BoardHandler {

    protected Player currentPlayer;
    protected Controller controller;
    protected int numberOfStudentsMoved;
    private final int maxNumberOfStudentsToMove;


    public BoardHandler(Controller controller){
        this.controller = controller;
        maxNumberOfStudentsToMove = controller.simpleGame.getParameters().getStudentsPerCloud();
    }

    //GET THE CURRENT PLAYER
    protected void getCurrentPlayer(){
        currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
    }

    /**
     * Empty chosen cloud and add the students at currentPlayer's entrance
     * @param idCloud >=0 && < number of clouds
     */
    public boolean takeFromCloud(int idCloud){
        getCurrentPlayer();
        if(!controller.simpleGame.checkValidIdCloud(idCloud)) {
            controller.simpleGame.getParameters().setErrorState("WRONG CLOUD SELECTED");
            return false;
        }
        controller.simpleGame.getFromCloud(currentPlayer, idCloud);
        return true;
    }

    /**
     * fill clouds with new students, drawing from the sack
     */
    public void refillClouds(){
        controller.simpleGame.fillClouds();
    }


    /**
     * move a student from currentPlayer's entrance to hall.
     *If currentPlayer has more students, with moved student's color,
     *than other players get a professor
     * The student was already selected previously
     * @return true if the student moved successfully
     */
    public boolean moveFromEntranceToHall(){
        getCurrentPlayer();
        if(controller.simpleGame.getParameters().getSelectedEntranceStudents().isEmpty() ||
        controller.simpleGame.getParameters().getSelectedEntranceStudents().get().isEmpty()){
            controller.simpleGame.getParameters().setErrorState("CANNOT ADD TO HALL, STUDENT NOT CHOSEN");
            return false;
        }
        controller.simpleGame.moveFromEntranceToHall(currentPlayer);
        numberOfStudentsMoved++;
        return true;
    }

    /**
     * Checks whether all students for this turn have been moved
     * @return true if all students for this turn for this player have been moved already
     */
    public boolean allStudentsMoved(){
        return maxNumberOfStudentsToMove == numberOfStudentsMoved;
    }

    /**
     * Puts the number of students moved for this turn back at 0
     */
    public void resetStudentsMoved(){
        numberOfStudentsMoved = 0;
    }

    /**
     * move the student in chosen position from currentPlayer's entrance to
     * the islandGroup with chosen id
     * Student was already chosen previously
     * @param idIslandGroup > 0
     * @return true if the student moved successfully
     */
    public boolean moveFromEntranceToIsland(int idIslandGroup){
        getCurrentPlayer();
        if(!controller.simpleGame.checkValidIdIsland(idIslandGroup)){
            controller.simpleGame.getParameters().setErrorState("WRONG ID ISLAND-GROUP");
            return false;
        }
        if(controller.simpleGame.getParameters().getSelectedEntranceStudents().isEmpty()
        || controller.simpleGame.getParameters().getSelectedEntranceStudents().get().isEmpty()){
            controller.simpleGame.getParameters().setErrorState("CANNOT ADD TO ENTRANCE, STUDENT NOT CHOSEN");
            return false;
        }
        controller.simpleGame.moveFromEntranceToIsland(currentPlayer, idIslandGroup);
        numberOfStudentsMoved++;
        return true;
    }

}
