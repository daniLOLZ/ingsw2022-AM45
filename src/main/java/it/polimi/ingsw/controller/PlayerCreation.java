package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerCreation {
    private final Controller controller;
    private final List<TeamEnum> teamColor ;
    private final List<String> nicknames;
    private final List<Wizard> wizards; // Lucario : This should be a list of wizard ids rather than actual wizards,
                                        // the real ones will be created in the game, not in the controller

    // Lucario : i metodi get/set per i vari parametri vanno bene, ma la creazione vera e propria non deve avvenire qui

    public PlayerCreation(Controller controller){
        this.controller = controller;
        teamColor = new ArrayList<>();
        nicknames = new ArrayList<>();
        wizards = new ArrayList<>();
    }

    //Lucario : player creation shouldn't happen here
    /**
     *
     * @param user > 0
     * @return a Player created with parameters set by user
     */
    public Player createPlayer(int user){
        if(teamColor.get(user) == null || nicknames.get(user) == null || wizards.get(user) == null ){
            controller.simpleGame.getParameters().setErrorState("BAD PARAMETERS CREATING PLAYER");
            return null;
        }
        String nick = nicknames.get(user);
        Wizard wizard = wizards.get(user);
        TeamEnum color = teamColor.get(user);
        PlayerEnum playerId = setPlayerId();
        boolean leader = isLeader(user);
        ParameterHandler parameter = controller.simpleGame.getParameters();
        boolean advanced = controller.gameRule.id >= GameRule.ADVANCED_2.id;


        Player player = FactoryPlayer.getPlayer(nick,playerId,color,leader,parameter,advanced);

        if(player == null){
            controller.simpleGame.getParameters().setErrorState("BAD PARAMETERS CREATING PLAYER");
        }

        if(player != null)
            player.setWizard(wizard);

        return player;

    }

    /**
     *
     * @param nick != null
     * @param user > 0
     * @return true if nick is correctly set
     */
    // Lucario : probabilmente dev'essere synchronized, same per gli altri
    public boolean setNickname(String nick, int user){
        if(FactoryPlayer.validNickname(nick)){
            nicknames.add(user,nick);
            return true;
        }
        return false;
    }

    /**
     * replace nickname chosen by user with null
     * @param user > 0
     */
    // Lucario : in quali occasioni dobbiamo mantenere il giocatore ma con un nick null?
    // Non è meglio cancellarlo direttamente? Non credo di aver capito questi metodi di clear
    public void clearNickname(int user){
        nicknames.add(user, null);
    }

    /**
     * Each game has one color per player except the 2 player teams games that have 2 players per color.
     * Color grey is present only in 3 players game.
     * @param team != NoTeam
     * @param user > 0
     * @return true if team is correctly set.
     */
    public boolean setTeamColor(final TeamEnum team, int user){
        GameRule rule = controller.gameRule;

        //GAME WITHOUT 2 PLAYERS TEAMS, ALL TEAM COLORS ARE DIFFERENT
        if(     (rule.id == GameRule.SIMPLE_2.id)   ||
                (rule.id == GameRule.SIMPLE_3.id)   ||
                (rule.id == GameRule.ADVANCED_2.id) ||
                (rule.id == GameRule.ADVANCED_3.id))
        {
            if(rule != GameRule.ADVANCED_3 &&
                    rule != GameRule.SIMPLE_3 &&
                    team == TeamEnum.GREY)
                return false;

            if(teamColor.stream().noneMatch(otherTeam -> team.index == otherTeam.index)){
                teamColor.add(user, team);
                return true;
            }
        }

        //GAME WITH 2 PLAYER TEAMS, CHOSEN TEAM COLOR IS VALID ONLY IF THERE IS NOT MORE THAN 2 EQUAL COLORS
        if((rule.id == GameRule.ADVANCED_4.id) || (rule.id == GameRule.SIMPLE_4.id)){
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

    // Lucario : non capisco cosa fa questo metodo
    /**
     *
     * @return (in ascending order) PlayerEnum with correct id, based on other players in Game.
     */
    public PlayerEnum setPlayerId(){
        SimpleGame game = controller.simpleGame;
        int numPlayers = game.getPlayers().size();
        return PlayerEnum.getPlayer(numPlayers);

    }

    /**
     * teamColor.get(user) != null
     * @param user > 0
     * @return true if the game has not 2 player team or if user is the first player who choose his color
     * false otherwise
     */
    public boolean isLeader(int user){
        GameRule rule = controller.gameRule;
        final TeamEnum color = teamColor.get(user);
        //IF THE GAME HAS NOT 2 PLAYERS TEAM EACH PLAYER IS LEADER
        if(rule.id != 4 && rule.id != 40)
            return true;

        //IF THERE IS ALREADY A PLAYER WITH MY TEAM COLOR I AM NOT A LEADER
        List<Player> players = controller.simpleGame.getPlayers();
        return players.stream().noneMatch(player -> player.getTeamColor() == color );
    }

    // Lucario : wizards shouldn't be created in the controller
    /**
     * create e set a new wizard only if there not are others equal to it
     * @param idWizard == 0 , 10, 20 ,30
     * @param user > 0
     * @return true if wizard is set correctly
     */
    public boolean setWizard(final int idWizard, int user){
        Wizard wizard;
        if(wizards.stream().noneMatch(wiz -> wiz.getIdWizard() == idWizard)){
            wizard = FactoryWizard.getWizard(idWizard);
            wizards.add(user, wizard);
            return true;
        }
        return false;
    }

    /**
     * replace wizard chosen by user with null
     * @param user > 0
     */
    public void clearWizard(int user){
        wizards.add(user, null);
    }
}
