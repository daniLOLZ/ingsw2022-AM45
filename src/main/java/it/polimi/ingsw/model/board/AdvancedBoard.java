package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class AdvancedBoard extends Board {

    private static final Integer[] positionOfCoins = {9, 6, 3};

    private List<Stack<Integer>> coinsOnBoard;

    public AdvancedBoard(TeamEnum teamColor, ParameterHandler parameters){
        super(teamColor, parameters);

        coinsOnBoard = new ArrayList<>();
        for (StudentEnum studentEnum : StudentEnum.getStudents()){
            coinsOnBoard.add(new Stack<>());
        }

        //for each pos in positionOfCoins, pushes pos in each stack of coinsOnBoard
        Arrays.stream(positionOfCoins).forEach(pos -> coinsOnBoard.forEach(stack -> stack.push(pos)));
    }

    /**
     * checks if the player who just put a student in his board should receive a coin
     * If that's the case, remove it from the pile of available coins on this table
     * @param table the table whose seat you want to check
     * @param seat  the seat of the table you want to check
     * @return true if the given seat in the given table has a coin on it; false otherwise
     */
    public boolean checkCoinSeat(StudentEnum table, int seat){

        //no coins in non-existent tables
        if (table.ordinal() >= coinsOnBoard.size()) return false;

        //no coins on the table ==> No point in checking
        if (coinsOnBoard.get(table.ordinal()).empty()) return false;

        //look if the top of the corresponding pile contains the given seat
        if (coinsOnBoard.get(table.ordinal()).peek() == seat){

            //remove the coin from the board
            coinsOnBoard.get(table.ordinal()).pop();
            return true;
        }
        else return false;
    }

    public List<Stack<Integer>> getCoinsOnBoard() {
        return coinsOnBoard;
    }
}
