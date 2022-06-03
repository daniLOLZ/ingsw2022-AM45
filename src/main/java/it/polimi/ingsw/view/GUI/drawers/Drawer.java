package it.polimi.ingsw.view.GUI.drawers;

import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.GUIApplication;
import javafx.application.Platform;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Drawer{

    protected static final double REAL_SIZE = 1;


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

    public static ImageView drawFromCenterInteractiveImage (Image image, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(image.getWidth() * scale);
        imageView.setFitHeight(image.getHeight() * scale);
        imageView.setX(pos.x - scale * image.getWidth() / 2);
        imageView.setY(pos.y - scale * image.getHeight() / 2);
        imageView.setOnMouseClicked(onClick);

        return imageView;
    }

    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale,EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom){

        List<Node> allNodes = new ArrayList<>();

        List<Node> aboveNodes = new ArrayList<>();

        if (hoverZoom != 1){

            imageView.setOnMouseEntered(event -> {
                //todo create a new class that will look for the node containing the ImageView. This is now blocking the GUI
                allNodes.addAll(imageView.getScene().getRoot().getChildrenUnmodifiable());

                if (!allNodes.isEmpty()) {
                    aboveNodes.addAll(allNodes.subList(allNodes.indexOf(imageView), allNodes.size()));
                }

                getOnMouseEnteredHandler(imageView, pos, scale, entered, hoverZoom).handle(event);
            });

            imageView.setOnMouseExited(event -> {
                for (Node node:
                     aboveNodes) {
                    node.toFront();
                }

                getOnMouseExitedHandler(imageView, pos, scale, exited);
            });
        }
    }

    private static EventHandler<MouseEvent> getOnMouseEnteredHandler(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, double hoverZoom) {
        return event -> {

            enterZoom(imageView, pos, scale, hoverZoom).handle(event);
            imageView.toFront();
            entered.handle(event);

        };
    }

    private static EventHandler<MouseEvent> getOnMouseExitedHandler(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> exited) {
        return event -> {
            exitZoom(imageView, pos, scale).handle(event);
            exited.handle(event);

        };
    }

    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom, List<Node> children){
        addHoveringEffects(imageView, pos, scale, entered, exited, hoverZoom);

        for (Node child:
             children) {

            child.toFront();

            child.setOnMouseEntered(getOnMouseEnteredHandler(imageView, pos, scale, entered, hoverZoom));

            child.setOnMouseExited(getOnMouseExitedHandler(imageView, pos, scale, exited));
        }
    }

    private static EventHandler<MouseEvent> enterZoom(ImageView imageView, Coord pos, double scale, double hoverZoom){

        return event -> {
            Image image = imageView.getImage();

            Coord anchor = getAnchor(image, pos, scale);

            imageView.setFitWidth(image.getWidth() * scale * hoverZoom);
            imageView.setFitHeight(image.getHeight() * scale * hoverZoom);
            imageView.setX(pos.x - scale * image.getWidth() / 2 - anchor.x * (hoverZoom - 1));
            imageView.setY(pos.y - scale * image.getHeight() / 2 - anchor.y * (hoverZoom - 1));
        };
    }

    private static Coord getAnchor(Image image, Coord pos, double scale) {
        return new Coord((pos.x - image.getWidth() * scale / 2) * image.getWidth() * scale / GUIApplication.WINDOW_WIDTH / (1 - image.getWidth() * scale / GUIApplication.WINDOW_WIDTH),
                (pos.y - image.getHeight() * scale / 2) * image.getHeight() * scale / GUIApplication.WINDOW_HEIGHT / (1 - image.getHeight() * scale / GUIApplication.WINDOW_HEIGHT));
    }

    private static EventHandler<MouseEvent> exitZoom(ImageView imageView, Coord pos, double scale){

        return event -> {

            Image image = imageView.getImage();

            imageView.setFitWidth(image.getWidth() * scale);
            imageView.setFitHeight(image.getHeight() * scale);
            imageView.setX(pos.x - scale * image.getWidth() / 2);
            imageView.setY(pos.y - scale * image.getHeight() / 2);

        };
    }
}
