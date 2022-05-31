package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class WizardDrawer extends Drawer{

    private static final List<Image> wizards = new ArrayList<>();

    static {
        wizards.add(new Image("assets/wizards/king.png"));
        wizards.add(new Image("assets/wizards/pixie.png"));
        wizards.add(new Image("assets/wizards/sorcerer.png"));
        wizards.add(new Image("assets/wizards/wizard.png"));
    }

    private static final double wizardWidth = wizards.get(0).getWidth(),
                                wizardHeight = wizards.get(0).getHeight();

    public static ImageView drawWizard(WizardEnum wizard, Coord pos, double scale){

        //will be removed after updating Drawer class
        Group dummy = new Group();

        return drawFromCenterInteractiveImage(dummy, wizards.get(wizard.index), pos, scale, HandlingToolbox.NO_EFFECT);
    }

    public static double getWizardWidth() {
        return wizardWidth;
    }

    public static double getWizardHeight() {
        return wizardHeight;
    }
}
