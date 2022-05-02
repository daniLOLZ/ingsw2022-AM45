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
    private void init(){
        currentPlayer =controller.simpleGame.getParameters().getCurrentPlayer();
    }

    /**
     * Empty chosen cloud and add the students at currentPlayer's entrance
     * @param idCloud >=0 && < number of clouds
     */
    public void takeFromCloud(int idCloud){
        init();
        controller.simpleGame.getFromCloud(currentPlayer,idCloud);
    }

    /**
     * fill clouds with new students, drawing from the sack
     */
    public void refillClouds(){
        controller.simpleGame.fillClouds();
    }


    public void addToHall(StudentEnum student){
        //TODO
    }
}
