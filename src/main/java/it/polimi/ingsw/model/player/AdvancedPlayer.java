package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.model.assistantCards.Wizard;
import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.board.AdvancedBoard;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.observer.AdvancedPlayerWatcher;
import it.polimi.ingsw.view.observer.PlayerWatcher;

import java.util.ArrayList;
import java.util.List;

public class AdvancedPlayer extends Player {
    private int numCoins;

    /**
     * Basic constructor, doesn't allow for the choice of a wizard
     */
    @Deprecated
    public AdvancedPlayer(PlayerEnum playerId, String nickname, TeamEnum teamColor,
                          boolean leader, ParameterHandler parameters){
        super(playerId, nickname, teamColor, leader, parameters);
        this.numCoins = 1;
    }

    /**
     * View-less constructor, not to be used
     */
    @Deprecated
    public AdvancedPlayer(PlayerEnum playerId, String nickname, TeamEnum teamColor,
                          Wizard wizard, boolean leader, ParameterHandler parameters){
        super(playerId, nickname,teamColor, wizard, leader, parameters);
        this.numCoins = 1;
    }

    public AdvancedPlayer(PlayerEnum playerId, String nickname, TeamEnum teamColor,
                          Wizard wizard, boolean leader,
                          ParameterHandler parameters, VirtualView virtualView){
        super(playerId, nickname,teamColor, wizard, leader, parameters);
        this.numCoins = AdvancedParameterHandler.numInitialCoinsPerPlayer;

        watcherList = new ArrayList<>();
        AdvancedPlayerWatcher watcher = new AdvancedPlayerWatcher(this, virtualView);
        watcherList.add(watcher);
        watchers = watcherList;
        alert();
    }

    @Override
    protected Board createBoard(TeamEnum teamColor, ParameterHandler parameters) {
        return new AdvancedBoard(teamColor, parameters);
    }

    @Override
    public StudentEnum moveFromEntranceToHall() {
        StudentEnum color= board.moveFromEntranceToHall();
        alert();
        return color;
    }

    public void addCoin(){
        numCoins++;
        alert();
    }

    /**
     * numCoins --
     */
    public void useCoin(){
        numCoins--;
        alert();
    }

    public int getNumCoins() {
        return numCoins;
    }

    /**
     *
     * @return a bean with all information about this player,
     * his nickname, color team, student at tables and at entrance,
     * his assistants cards, his professors and his coins
     */
    @Override
    public GameElementBean toBean() {
        int numTowers = getNumTowers();
        List<StudentEnum> studentsAtEntrance = board.getStudentsAtEntrance();
        List<Integer> studPerTable = new ArrayList<>();
        List<StudentEnum> professors;
        List<Assistant> Assistants = wizard.getRemainedAssistants();        //Get assistant cards id
        for(StudentEnum color: StudentEnum.values()){                       //Get students per table
            if(color != StudentEnum.NOSTUDENT)
                studPerTable.add(board.getStudentsAtTable(color));
        }

        professors = parameters.getProfessorsByPlayer(playerId);
        AdvancedPlayerBean bean = new AdvancedPlayerBean(nickname,playerId,leader,teamColor,numTowers,
                studentsAtEntrance, studPerTable, professors, Assistants, numCoins, assistantPlayed, turn);

        if(turn != 0)
            bean.setTurn(turn);

        return bean;
    }
}
