package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.assistantCards.Assistant;
import it.polimi.ingsw.view.GUI.Coord;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AssistantDrawer extends Drawer{

    private final static List<Image> assistants = new ArrayList<>();

    static {
        assistants.add(new Image("assets/assistants/assistant_1.png"));
        assistants.add(new Image("assets/assistants/assistant_2.png"));
        assistants.add(new Image("assets/assistants/assistant_3.png"));
        assistants.add(new Image("assets/assistants/assistant_4.png"));
        assistants.add(new Image("assets/assistants/assistant_5.png"));
        assistants.add(new Image("assets/assistants/assistant_6.png"));
        assistants.add(new Image("assets/assistants/assistant_7.png"));
        assistants.add(new Image("assets/assistants/assistant_8.png"));
        assistants.add(new Image("assets/assistants/assistant_9.png"));
        assistants.add(new Image("assets/assistants/assistant_10.png"));
    }

    private final static double assistantWidth = assistants.get(0).getWidth();
    private final static double assistantHeight = assistants.get(0).getHeight();
    private final static double hoverZoom = 1.7;
    private final static int firstAssistantId = 1;

    public static ImageView drawAssistant(Group root, int id, Coord pos, double scale){

        ImageView assistantView = drawFromCenterInteractiveImage(root, assistants.get(id - firstAssistantId), pos, scale, null);
        addHoveringEffects(assistantView, pos, scale, NO_EFFECT, NO_EFFECT, hoverZoom);
        return assistantView;
    }

    public static double getAssistantWidth(){
        return assistantWidth;
    }

    public static double getAssistantHeight(){
        return assistantHeight;
    }
}
