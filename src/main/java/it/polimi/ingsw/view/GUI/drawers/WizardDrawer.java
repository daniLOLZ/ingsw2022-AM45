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

    /**
     * Draws a wizard card at the given position with the given scaling factor.
     * @param wizard The wizard to draw
     * @param pos The position in which the wizard should be drawn
     * @param scale The scaling factor to apply to the view
     * @return The view containing the drawn wizard
     */
    public static ImageView drawWizard(WizardEnum wizard, Coord pos, double scale){


        return drawFromCenterInteractiveImage(wizards.get(wizard.index), pos, scale, HandlingToolbox.NO_EFFECT);
    }

    public static double getWizardWidth() {
        return wizardWidth;
    }

    public static double getWizardHeight() {
        return wizardHeight;
    }
}
