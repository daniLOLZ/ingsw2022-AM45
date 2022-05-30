package it.polimi.ingsw.view;

import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.BeanEnum;

import java.util.List;
import java.util.Objects;

public class GameInitBean implements Bean {
    private final List<TeamEnum> chosenColors;
    private final List<WizardEnum> chosenWizards;
    private final boolean allSetGameStarted;

    @Override
    public BeanEnum getBeanEnum() {
        return BeanEnum.GAME_INIT_BEAN;
    }

    /**
     * Returns a bean containing the already chosen team colors and wizards
     * IMPORTANT: Even if the color wasn't actually chosen by someone, but
     * is simply not selectable, the array will contain it anyways
     * Example: Grey team in a 2 or 4 player game
     * @param chosenColors the colors chosen by the players
     * @param chosenWizards the wizards chosen by the players
     */
    public GameInitBean(List<TeamEnum> chosenColors, List<WizardEnum> chosenWizards, boolean allSetGameStarted) {
        this.chosenColors = chosenColors;
        this.chosenWizards = chosenWizards;
        this.allSetGameStarted = allSetGameStarted;
    }

    public List<TeamEnum> getChosenColors() {
        return chosenColors;
    }

    public List<WizardEnum> getChosenWizards() {
        return chosenWizards;
    }

    public boolean isAllSetGameStarted() {
        return allSetGameStarted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameInitBean that = (GameInitBean) o;
        return allSetGameStarted == that.allSetGameStarted && chosenColors.equals(that.chosenColors) && chosenWizards.equals(that.chosenWizards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chosenColors, chosenWizards, allSetGameStarted);
    }

    @Override
    public String toString(){
        StringBuilder returnString = new StringBuilder();
        returnString.append("Available team colors:\n");
        for(TeamEnum team : TeamEnum.getTeams()){
            if(!chosenColors.contains(team)){
                returnString.append("- ");
                returnString.append(team.name);
                returnString.append("\n");
            }
        }
        returnString.append("Available wizards:\n");
        for(WizardEnum wizard : WizardEnum.getWizards()){
            if(!chosenWizards.contains(wizard)){
                returnString.append("- ");
                returnString.append(wizard.name);
                returnString.append("\n");
            }
        }
        return returnString.toString();
    }
}
