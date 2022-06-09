package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.assistantCards.FactoryWizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerCreation {
    private final Controller controller;
    protected final List<TeamEnum> teamColor;
    protected final List<String> nicknames;
    private final List<Integer> wizards; // Wizard ids
    private boolean modified;

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

        modified = false;
    }

    /**
     * nick was checked in network level
     * @param nick != null
     * @param user the player's enumeration in this game
     * @return true if nick is correctly set
     */
    public synchronized boolean setNickname(String nick, int user){
            nicknames.set(user,nick);
            return true;
    }


    /**
     * Check whether a given color can be assigned to another player
     * will answer with true in case the color wasn't chosen (2 or 3 player game)
     * if it's not grey (2 or 4 player game)
     * or if only one player chose a particular color (4 player game)
     * @param color the color to check
     * @return true if the color is available for selection
     */
    public synchronized boolean isColorAvailable(TeamEnum color){
        int numberOfPlayers = GameRuleEnum.getNumPlayers(controller.gameRule.id);

        if(numberOfPlayers != 3 && color.equals(TeamEnum.GREY)) return false;

        if(numberOfPlayers == 2 || numberOfPlayers == 3){
            return !teamColor.contains(color);
        }
        else { // 4 players
            return teamColor.stream()
                    .filter(Objects::nonNull)
                    .filter(x -> x.equals(color))
                    .count() < 2;
        }
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

            if(teamColor.stream()
                    .filter(Objects::nonNull)
                    .noneMatch(otherTeam -> team.index == otherTeam.index)){
                teamColor.set(user, team);
                modified = true;
                return true;
            }
        }

        //GAME WITH 2 PLAYER TEAMS, CHOSEN TEAM COLOR IS VALID ONLY IF THERE IS NOT MORE THAN 2 EQUAL COLORS
        if(numPlayers == numPlayersForTeam){
            if(team != TeamEnum.GREY){
                long sameColor = teamColor.stream()
                        .filter(Objects::nonNull)
                        .filter(otherTeam -> otherTeam.index == team.index).count();
                if(sameColor <= 1){
                    teamColor.set(user, team);
                    modified = true;
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
        teamColor.set(user, null);
        modified = true;
    }

    /**
     * Check whether a given color can be assigned to another player
     * will answer with true in case the color wasn't chosen
     * @param idWizard the color to check
     * @return true if the wizard is available for selection
     */
    public synchronized boolean isWizardAvailable(int idWizard){
        return !wizards.contains(idWizard);
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

        wizards.set(user, idWizard);
        modified = true;
        return true;
    }

    /**
     * replace wizard chosen by user with null
     * @param user > 0
     */
    public synchronized void clearWizard(int user){
        wizards.set(user, null);
        modified = true;
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

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
