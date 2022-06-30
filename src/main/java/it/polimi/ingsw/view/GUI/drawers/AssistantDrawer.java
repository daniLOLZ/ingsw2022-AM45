package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
    private final static int firstAssistantId = 1;

    /**
     * Draws the Assistant with the given parameters
     * @param id The id of the assistant to draw
     * @param pos The position in which the assistant must be drawn
     * @param scale The scale to apply to the view
     * @param onClick The action to perform when the assistant is clicked on
     * @return The view containing the drawn assistant
     */
    public static ImageView drawAssistant(int id, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        id = (id - firstAssistantId) % assistants.size();

        return drawFromCenterInteractiveImage(assistants.get(id), pos, scale, onClick);
    }

    /**
     * Draws the Assistant with the given parameters
     * @param id The id of the assistant to draw
     * @param pos The position in which the assistant must be drawn
     * @param scale The scale to apply to the view
     * @return The view containing the drawn assistant
     */
    public static ImageView drawAssistant(int id, Coord pos, double scale){
        return drawAssistant(id, pos, scale, HandlingToolbox.NO_EFFECT);
    }

    /**
     * Return the actual width of the Assistant image
     * @return The width of the Assistant
     */
    public static double getAssistantWidth(){
        return assistantWidth;
    }

    /**
     * Return the actual height of the Assistant image
     * @return The height of the Assistant
     */
    public static double getAssistantHeight(){
        return assistantHeight;
    }
}
