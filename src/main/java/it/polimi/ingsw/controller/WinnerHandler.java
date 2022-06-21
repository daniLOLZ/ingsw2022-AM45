package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.TeamEnum;

import java.util.Map;

public class WinnerHandler {
    private Controller controller;
    private TeamEnum winnerTeam;

    public WinnerHandler(Controller controller){
        this.controller = controller;
        winnerTeam = TeamEnum.NOTEAM;
    }

    public TeamEnum getWinnerTeam() {
        return winnerTeam;
    }

    /**
     * Check if a team has won.
     * This method must be called after action phase ends.
     * You win if you put all towers on islands.
     * You win if when the game ends you have placed more towers than others.
     * You win if when the game ends there is no player that have placed more tower than you and
     * you have more professors than others.
     * @return the winner team if exists or NoTeam if nobody won
     */
    public TeamEnum checkWinner(){
        TeamEnum winnerTeam = TeamEnum.NOTEAM;
        winnerTeam = controller.simpleGame.noMoreTowers();

        if(winnerTeam != TeamEnum.NOTEAM){
            this.winnerTeam = winnerTeam;
            return winnerTeam;
        }

        if(controller.simpleGame.islandShortage() || controller.simpleGame.isLastTurn()){
            winnerTeam = morePlacedTowers();
            if(winnerTeam == TeamEnum.NOTEAM){
                winnerTeam = moreProfessors();
            }
        }

        this.winnerTeam = winnerTeam;
        return winnerTeam;
    }

    private TeamEnum morePlacedTowers(){
        Map<TeamEnum, Integer> placedTowers = controller.simpleGame.towersOnIslands();
        TeamEnum winnerTeam = TeamEnum.NOTEAM;
        int max = 0;
        Integer curr;
        for(TeamEnum team: TeamEnum.values()){
            curr = placedTowers.get(team);

            if(curr != null && curr > max){
                max = curr;
                winnerTeam = team;
            }

            if(curr != null && curr.equals(max)){
                winnerTeam = TeamEnum.NOTEAM;
            }
        }

        return winnerTeam;

    }

    private TeamEnum moreProfessors(){
        Map<TeamEnum,Integer> numProfessorsPerTeam = controller.simpleGame.professorsPerTeam();
        TeamEnum winnerTeam = TeamEnum.NOTEAM;
        int max = 0;
        Integer curr = 0;
        for(TeamEnum team: TeamEnum.values()){
            curr = numProfessorsPerTeam.get(team);

            if(curr != null && curr > max){
                max = curr;
                winnerTeam = team;
            }

            if(curr != null && curr.equals(max)){
                winnerTeam = TeamEnum.NOTEAM;
            }
        }

        return winnerTeam;
    }

}
