package it.polimi.ingsw.view;

import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.network.Bean;
import it.polimi.ingsw.network.BeanEnum;

import java.util.List;
import java.util.Objects;

public class GameInitBean implements Bean {
    private final List<TeamEnum> availableColors;
    private final List<WizardEnum> availableWizards;

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.GAME_INIT_BEAN;
    }

    /**
     * Returns a bean containing the already chosen team colors and wizards
     * @param availableColors the colors still available to be selected by the players
     * @param availableWizards the wizards still available to be selected by the players
     */
    public GameInitBean(List<TeamEnum> availableColors, List<WizardEnum> availableWizards) {
        this.availableColors = availableColors;
        this.availableWizards = availableWizards;
    }

    public List<TeamEnum> getChosenColors() {
        return availableColors;
    }

    public List<WizardEnum> getChosenWizards() {
        return availableWizards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameInitBean that = (GameInitBean) o;
        return availableColors.equals(that.availableColors) && availableWizards.equals(that.availableWizards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableColors, availableWizards);
    }

    @Override
    public String toString(){
        StringBuilder returnString = new StringBuilder();
        returnString.append("Available team colors:\n");
        for(TeamEnum team : TeamEnum.getTeams()){
            if(availableColors.contains(team)){
                returnString.append("- ");
                returnString.append(team.name);
                returnString.append("\n");
            }
        }
        returnString.append("Available wizards:\n");
        for(WizardEnum wizard : WizardEnum.getWizards()){
            if(availableWizards.contains(wizard)){
                returnString.append("- ");
                returnString.append(wizard.name);
                returnString.append("\n");
            }
        }
        return returnString.toString();
    }
}
