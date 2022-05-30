package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum WizardEnum {

    KING(0, "King"),
    PIXIE(1, "Pixie"),
    SORCERER(2, "Sorcerer"),
    WIZARD(3, "Wizard"),
    NO_WIZARD(4, "No wizard");

    public final int index;
    public final String name;

    WizardEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }

    /**
     *
     * @return The values of the enum except the NOWIZARD instance
     */
    public static List<WizardEnum> getWizards(){
        return Arrays.stream(WizardEnum.values())
                .filter( x -> !x.equals(NO_WIZARD))
                .collect(Collectors.toList());
    }

    public static WizardEnum fromObjectToEnum(Object field) {
        return WizardEnum.valueOf((String)field);
    }

}
