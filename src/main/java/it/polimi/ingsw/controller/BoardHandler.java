package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.player.Player;

public class BoardHandler {

    protected Player currentPlayer;
    protected Controller controller;


    public BoardHandler(Controller controller){
        this.controller = controller;
    }

    //GET THE CURRENT PLAYER
    protected void getCurrentPlayer(){
        currentPlayer =controller.simpleGame.getParameters().getCurrentPlayer();
    }

    /**
     * Empty chosen cloud and add the students at currentPlayer's entrance
     * @param idCloud >=0 && < number of clouds
     */
    public void takeFromCloud(int idCloud){
        getCurrentPlayer();
        controller.simpleGame.getFromCloud(currentPlayer,idCloud);
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
     */
    public void moveFromEntranceToHall(int position){
        getCurrentPlayer();
        controller.simpleGame.selectStudentAtEntrance(currentPlayer,position);
        controller.simpleGame.moveFromEntranceToHall(currentPlayer);
    }

    /**
     * move the student in chosen position from currentPlayer's entrance to
     * the islandGroup with chosen id
     * @param position > 0
     * @param idIslandGroup > 0
     */
    public void moveFromEntranceToIsland(int position, int idIslandGroup){
        getCurrentPlayer();
        if(! controller.simpleGame.checkValidIdIsland(idIslandGroup)){
            controller.simpleGame.getParameters().setErrorState("WRONG ID ISLAND-GROUP");
            return;
        }
        controller.simpleGame.selectStudentAtEntrance(currentPlayer, position);
        controller.simpleGame.moveFromEntranceToIsland(currentPlayer, idIslandGroup);
    }

}
