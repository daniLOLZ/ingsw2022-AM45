package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;

import java.util.ArrayList;
import java.util.List;

public class PlayerCreation {
    private final Controller controller;
    protected final List<TeamEnum> teamColor ;
    protected final List<String> nicknames;
    protected final List<Integer> wizards;

    public PlayerCreation(Controller controller){
        this.controller = controller;
        teamColor = new ArrayList<>();
        nicknames = new ArrayList<>();
        wizards = new ArrayList<>();
        int numPlayers = GameRule.getNumPlayers(controller.gameRule.id);

        //IN THIS WAY THE LISTS ARE INITIALIZED
        for(int user=0; user < numPlayers; user++ ){
            teamColor.add(user,null);
            nicknames.add(user,null);
            wizards.add(user,null);
        }
    }


    /**
     * nick was checked in network level
     * @param nick != null
     * @param user > 0
     * @return true if nick is correctly set
     */
    public boolean setNickname(String nick, int user){
            nicknames.add(user,nick);
            return true;

    }



    /**
     * Set player's team color in teamColor list in position user
     * Each game has one color per player except the 2 player teams games that have 2 players per color.
     * Color grey is present only in 3 players game.
     * @param team != NoTeam
     * @param user > 0
     * @return true if team is correctly set.
     */
    public boolean setTeamColor(final TeamEnum team, int user){
        GameRule rule = controller.gameRule;
        final int numPlayersForTeam = 4;
        final int numPlayersForGrey = 3;
        int numPlayers = GameRule.getNumPlayers(rule.id);
        boolean isAdvancedGame = GameRule.isAdvanced(rule.id);

        //GAME WITHOUT 2 PLAYERS TEAMS, ALL TEAM COLORS ARE DIFFERENT
        if( numPlayers != numPlayersForTeam)
        {
            if(numPlayers != numPlayersForGrey &&
                    team == TeamEnum.GREY)
                return false;

            if(teamColor.stream().noneMatch(otherTeam -> team.index == otherTeam.index)){
                teamColor.add(user, team);
                return true;
            }
        }

        //GAME WITH 2 PLAYER TEAMS, CHOSEN TEAM COLOR IS VALID ONLY IF THERE IS NOT MORE THAN 2 EQUAL COLORS
        if(numPlayers == numPlayersForTeam){
            if(team != TeamEnum.GREY){
                long sameColor = teamColor.stream().filter(otherTeam -> otherTeam.index == team.index).count();
                if(sameColor <= 1){
                    teamColor.add(user, team);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * replace team chosen by user with null
     * @param user > 0
     */
    public void clearTeamColor(int user){
        teamColor.add(user, null);
    }


    /**
     * set, in user position, a new wizard id in wizards list only if there not are
     * others equal to it
     * @param idWizard == 0 , 10, 20 ,30
     * @param user > 0
     * @return true if wizard is set correctly
     */
    public boolean setWizard(final int idWizard, int user){
        if(!FactoryWizard.checkCorrectId(idWizard))
            return false;

        if(wizards.contains(idWizard))
            return false;

        wizards.add(user, idWizard);
        return true;
    }

    /**
     * replace wizard chosen by user with null
     * @param user > 0
     */
    public void clearWizard(int user){
        wizards.add(user, null);
    }

    /**
     *
     * @return true if all users have set their parameters in order to create
     * their players.
     * Nickname, team color and wizard id.
     */
    public boolean allSet(){
        int numPlayers = GameRule.getNumPlayers(controller.gameRule.id);

        for(int user=0; user < numPlayers; user++){

            if(teamColor.get(user) == null)
                return false;

            if(nicknames.get(user) == null)
                return false;

            if(wizards.get(user) == null)
                return false;
        }

        return true;
    }
}
