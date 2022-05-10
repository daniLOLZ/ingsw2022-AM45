package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;

import java.util.ArrayList;
import java.util.List;

public class PlayerCreation {
    private final Controller controller;
    protected final List<TeamEnum> teamColor;
    protected final List<String> nicknames;
    private final List<Integer> wizards; // Wizard ids


    public PlayerCreation(Controller controller){
        this.controller = controller;
        teamColor = new ArrayList<>();
        nicknames = new ArrayList<>();
        wizards = new ArrayList<>();
        int numPlayers = GameRuleEnum.getNumPlayers(controller.gameRule.id);

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
     * @param user the player's enumeration in this game
     * @return true if nick is correctly set
     */
    public synchronized boolean setNickname(String nick, int user){
            nicknames.add(user,nick);
            return true;
    }


    /**
     * Check whether a given color has already been assigned
     * @param color the color to check
     * @return true if the color is already assigned to a player
     */
    public synchronized boolean isColorTaken(TeamEnum color){
        return teamColor.contains(color);
    }

    /**
     * Set player's team color in teamColor list in position user
     * Each game has one color per player except the 2 player teams games that have 2 players per color.
     * Color grey is present only in 3 players game.
     * @param team != NoTeam
     * @param user the player's enumeration in this game
     * @return true if team is correctly set.
     */
    public synchronized boolean setTeamColor(final TeamEnum team, int user){
        GameRuleEnum rule = controller.gameRule;
        final int numPlayersForTeam = 4;
        final int numPlayersForGrey = 3;
        int numPlayers = GameRuleEnum.getNumPlayers(rule.id);
        boolean isAdvancedGame = GameRuleEnum.isAdvanced(rule.id);

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
    public synchronized void clearTeamColor(int user){
        teamColor.add(user, null);
    }

    /**
     * Check whether a given wizard id has already been assigned
     * @param idWizard the wizard to check
     * @return true if the wizard is already assigned to a player
     */
    public synchronized boolean isWizardTaken(int idWizard){
        return wizards.contains(idWizard);
    }
    /**
     * set, in user position, a new wizard id in wizards list only if there not are
     * others equal to it
     * @param idWizard == 0 , 10, 20 ,30
     * @param user the player's enumeration in this game
     * @return true if wizard is set correctly
     */
    public synchronized boolean setWizard(final int idWizard, int user){
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
    public synchronized void clearWizard(int user){
        wizards.add(user, null);
    }

    /**
     *
     * @return true if all users have set their parameters in order to create
     * their players.
     * Nickname, team color and wizard id.
     */
    public synchronized boolean allSet(){
        int numPlayers = GameRuleEnum.getNumPlayers(controller.gameRule.id);

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

    public List<TeamEnum> getTeamColors() {
        return teamColor;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public List<Integer> getWizards() {
        return wizards;
    }
}
