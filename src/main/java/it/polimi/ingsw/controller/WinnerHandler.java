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

    /**
     *
     * @return the winning team
     */
    public TeamEnum getWinnerTeam() {
        return winnerTeam;
    }

    /**
     * Check if a team has won.
     * This method must be called after any tower or island update is detected <br>
     * You win if you put all towers on islands or if there are too few island groups and
     * you meet the remaining winning criteria
     * @return the winner team if it exists or NOTEAM if nobody won
     */
    public TeamEnum checkInstantWinner() {
        TeamEnum winner = TeamEnum.NOTEAM;
        winner = controller.simpleGame.noMoreTowers();

        if (winner == TeamEnum.NOTEAM) {
            if(controller.simpleGame.islandShortage()){
                winner = morePlacedTowers();
                if(winnerTeam == TeamEnum.NOTEAM){
                    winner = moreProfessors();
                    if(winnerTeam == TeamEnum.NOTEAM){
                        //Default to the white team, as the rules don't specify this possibility
                        winner = TeamEnum.WHITE;
                    }
                }
            }
        }
        this.winnerTeam = winner;
        return winnerTeam;
    }

    /**
     * The game is won if when the game ends you have placed more towers than the other teams. <br>
     * The game is won if when the game ends there is no player who built more towers than the other players
     * AND you have more professors than others. <br>
     * If both conditions are equal, the white team wins
     * @return true if there is a winner
     */
    public TeamEnum checkDeferredWinner(){

        TeamEnum winner = TeamEnum.NOTEAM;

        if(controller.simpleGame.isLastTurn()){
            winner = morePlacedTowers();
            if(winner == TeamEnum.NOTEAM){
                winner = moreProfessors();
                if(winner == TeamEnum.NOTEAM){
                    //Default to the white team, as the rules don't specify this possibility
                    winner = TeamEnum.WHITE;
                }
            }
        }
        this.winnerTeam = winner;
        return winnerTeam;
    }

    /**
     * Checks which team has the most towers placed
     * @return the team with the most towers put on islands
     */
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

            else if(curr != null && curr.equals(max)){
                winnerTeam = TeamEnum.NOTEAM;
            }
        }

        return winnerTeam;

    }

    /**
     * Checks which team controls the most professors
     * @return The team with the most professors controlled
     */
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
            else if(curr != null && curr.equals(max)){
                winnerTeam = TeamEnum.NOTEAM;
            }
        }

        return winnerTeam;
    }

}
