package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AdvancedPlayer;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudentEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvancedBoardHandler extends BoardHandler{

    private final List<Player> playerList;
    private final List<List<Integer>> playerHalls;   //LIST OF MAX COIN POSITIONS IN EACH TABLES PER EACH PLAYER


    public AdvancedBoardHandler(Controller controller){
        super(controller);
        playerHalls = new ArrayList<>();
        playerList = controller.getSimpleGame().getPlayers();

        //EACH PLAYER HAS AN INTEGER PER COLOR TO REMEMBER THE MAX COIN POSITION REACHED FOR EACH TABLE
        for(int player = 0; player < playerList.size(); player++){
            playerHalls.add(new ArrayList<>());
            for(int tables = 0; tables < StudentEnum.getNumStudentTypes(); tables++)
                playerHalls.get(player).add(0);
        }
    }

    /**
     * This method is called only if Player put a student on a coin position
     * @param colorTable != No Student && != null
     * @return true if and only if the current Player has reached for the first time a coin positions
     */
    public boolean checkIfCoinAvailable(StudentEnum colorTable){
        Player currentPlayer = controller.simpleGame.getParameters().getCurrentPlayer();
        int indexCurrentPlayer = currentPlayer.getPlayerId().index;
        Board currentBoard = currentPlayer.getBoard();
        int lastMaxPosition = playerHalls.get(indexCurrentPlayer).get(colorTable.index);
        int positionNow =  currentBoard.getStudentsAtTable(colorTable);

        return( lastMaxPosition < positionNow);

    }

    /**
     * add student to hall and check if current player deserves a coin,
     * in this case add a coin to player and remove one from parameters
     * @param student != NO Student && != null
     */
    @Override
    public void addToHall(StudentEnum student) {

        final int[] coinPositions = {3,6,9};

        //IF THIS CLASS EXISTS PLAYERS ARE ADVANCED PLAYERS
        AdvancedPlayer currentPlayer = (AdvancedPlayer) controller.simpleGame.getParameters().getCurrentPlayer();
        Board board = currentPlayer.getBoard();

        //ADD STUDENT TO HALL
        board.addToHall(student);
        int currentPositionAtTable = board.getStudentsAtTable(student);


        //PLAYER HAS NOT PUT THE STUDENT ON A COIN POSITION
        if(Arrays.stream(coinPositions).noneMatch(position -> position == currentPositionAtTable)){
            return;
        }

        //PLAYER PUT THE STUDENT ON A COIN POSITION AND IT IS THE FIRST TIME
        // HE PUTS A STUDENT ON THAT POSITION
        if(checkIfCoinAvailable(student)){
            int playerIndex = currentPlayer.getPlayerId().index;

            //GIVE COIN TO PLAYER
            currentPlayer.addCoin();
            controller.advancedGame.getAdvancedParameters().removeCoin();

            //UPDATE THE MAX COIN POSITION OF THIS PLAYER
            playerHalls.get(playerIndex).add(student.index, currentPositionAtTable);
        }


    }
}
