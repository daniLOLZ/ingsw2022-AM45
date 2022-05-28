package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.GUIApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public abstract class Drawer{


    private static final double REAL_SIZE = 1;

    /**
     * Draws an image scaled by the given scaling factor.
     * If alignment coordinates are inside the scene, the drawn image is always totally inside the scene (unless it's too big)
     * @param graphicsContext The graphics context required to draw the image
     * @param image The image to draw
     * @param alignment The point the image should be placed
     * @param scale The scaling factor applied to the image
     */
    public static void drawImage (GraphicsContext graphicsContext, Image image, Coord alignment, double scale){

        double imageWidth = image.getWidth()*scale, imageHeight = image.getHeight()*scale;

        double windowWidth = graphicsContext.getCanvas().getWidth();
        double windowHeight = graphicsContext.getCanvas().getHeight();

        Coord pos = new Coord(alignment.x/windowWidth, alignment.y/windowHeight),
                anchor = new Coord(pos.x * imageWidth, pos.y * imageHeight);

        Platform.runLater(() -> graphicsContext.drawImage(image, alignment.x - anchor.x, alignment.y - anchor.y, imageWidth, imageHeight));
    }

    public static void drawImage (GraphicsContext graphicsContext, Image image, Coord alignment){
        Drawer.drawImage(graphicsContext, image, alignment, REAL_SIZE);
    }

    /**
     * Draws an image scaled by the given scaling factor, positioning its center at the give coordinates.
     * @param graphicsContext The graphics context required to draw the image
     * @param image The image to draw
     * @param pos The point the image's center should be placed
     * @param scale The scaling factor applied to the image
     */
    public static void drawFromCenterImage (GraphicsContext graphicsContext, Image image, Coord pos, double scale){

        double imageWidth = image.getWidth()*scale, imageHeight = image.getHeight()*scale;
        Coord imageCenter = new Coord(imageWidth/2, imageHeight/2);

        Platform.runLater(() -> graphicsContext.drawImage(image,pos.x - imageCenter.x,pos.y - imageCenter.y, imageWidth, imageHeight));
    }

    public static void drawFromCenterImage (GraphicsContext graphicsContext, Image image, Coord pos){
        Drawer.drawFromCenterImage(graphicsContext, image, pos, REAL_SIZE);
    }

    public static ImageView drawFromCenterInteractiveImage (Group root, Image image, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(image.getWidth() * scale);
        imageView.setFitHeight(image.getHeight() * scale);
        imageView.setX(pos.x - scale * image.getWidth() / 2);
        imageView.setY(pos.y - scale * image.getHeight() / 2);
        imageView.setOnMouseClicked(onClick);

        root.getChildren().add(imageView);
        return imageView;
    }

    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale,EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom){

        Image image = imageView.getImage();
        if (hoverZoom != 1){

            Coord anchor = new Coord((pos.x - image.getWidth() * scale / 2) * image.getWidth() * scale / GUIApplication.WINDOW_WIDTH / (1 - image.getWidth() * scale / GUIApplication.WINDOW_WIDTH),
                    (pos.y - image.getHeight() * scale / 2) * image.getHeight() * scale / GUIApplication.WINDOW_HEIGHT / (1 - image.getHeight() * scale / GUIApplication.WINDOW_HEIGHT));

            imageView.setOnMouseEntered(event -> {
                imageView.setFitWidth(image.getWidth() * scale * hoverZoom);
                imageView.setFitHeight(image.getHeight() * scale * hoverZoom);
                imageView.setX(pos.x - scale * image.getWidth() / 2 - anchor.x * (hoverZoom - 1));
                imageView.setY(pos.y - scale * image.getHeight() / 2 - anchor.y * (hoverZoom - 1));
                entered.handle(event);

            });

            imageView.setOnMouseExited(event -> {
                imageView.setFitWidth(image.getWidth() * scale);
                imageView.setFitHeight(image.getHeight() * scale);
                imageView.setX(pos.x - scale * image.getWidth() / 2);
                imageView.setY(pos.y - scale * image.getHeight() / 2);
                exited.handle(event);

            });
        }
    }
}
