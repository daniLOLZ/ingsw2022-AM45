package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.GUIApplication;
import javafx.geometry.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class DecorationsDrawer extends Drawer{

    public static void showMenuBackground(Region region){
        Image background = new Image("assets/background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT,0.5,true,Side.TOP,0.5,true),
                new BackgroundSize(GUIApplication.WINDOW_WIDTH, GUIApplication.WINDOW_WIDTH, false, false, false, false));

        region.setBackground(new Background(backgroundImage));
    }

    public static void drawLogo(GraphicsContext graphicsContext){
        Image profsLogo = new Image("assets/decorations/login_screen/professors.png");
        Image textLogo = new Image("assets/decorations/login_screen/eriantys_text_logo.png");
        drawImage(graphicsContext,
                profsLogo,
                new Coord(GUIApplication.centerX, GUIApplication.centerY * 0.1),
                0.6);
        drawImage(graphicsContext,
                textLogo,
                new Coord(GUIApplication.centerX, GUIApplication.centerY * 0.35),
                0.15);
    }

    public static void showDecorativeIslands(GraphicsContext graphicsContext){

        Image island1 = new Image("assets/tiles/island1.png");
        Image island2 = new Image("assets/tiles/island2.png");
        Image island3 = new Image("assets/tiles/island3.png");

        Image cloud = new Image("assets/tiles/cloud_card.png");

        double scale = 0.2;


        drawImage(graphicsContext, cloud, GUIApplication.upLeftCorner, scale);
        drawImage(graphicsContext, island1, GUIApplication.upLeftCorner, scale);
        drawImage(graphicsContext, cloud, GUIApplication.upRightCorner, scale);
        drawImage(graphicsContext, island1, GUIApplication.upRightCorner, scale);

        drawImage(graphicsContext, cloud, GUIApplication.centerLeft, scale);
        drawImage(graphicsContext, island2, GUIApplication.centerLeft, scale);
        drawImage(graphicsContext, cloud, GUIApplication.centerRight, scale);
        drawImage(graphicsContext, island2, GUIApplication.centerRight, scale);

        drawImage(graphicsContext, cloud, GUIApplication.downLeftCorner, scale);
        drawImage(graphicsContext, island3, GUIApplication.downLeftCorner, scale);
        drawImage(graphicsContext, cloud, GUIApplication.downRightCorner, scale);
        drawImage(graphicsContext, island3, GUIApplication.downRightCorner, scale);
    }
}
