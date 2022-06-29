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
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

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

    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale,EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom, boolean bringToFront, int rotation){

        List<Node> allNodes = new ArrayList<>();

        List<Node> aboveNodes = new ArrayList<>();

        if (hoverZoom != 1){

            imageView.setOnMouseEntered(event -> {
                if (bringToFront) {
                    allNodes.addAll(imageView.getParent().getChildrenUnmodifiable());

                    if (!allNodes.isEmpty()) {
                        aboveNodes.addAll(allNodes.subList(allNodes.indexOf(imageView), allNodes.size()));
                    }
                }

                getOnMouseEnteredHandler(imageView, pos, scale, entered, hoverZoom, bringToFront).handle(event);
            });

            imageView.setOnMouseExited(event -> {
                if (bringToFront) {
                    for (Node node:
                         aboveNodes) {
                        node.toFront();
                    }
                }

                getOnMouseExitedHandler(imageView, pos, scale, exited, rotation).handle(event);
            });
        }
    }

    private static EventHandler<MouseEvent> getOnMouseEnteredHandler(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, double hoverZoom, boolean bringToFront) {
        return event -> {

            enterZoom(imageView, pos, scale, hoverZoom).handle(event);
            if (bringToFront)imageView.toFront();
            entered.handle(event);

        };
    }

    private static EventHandler<MouseEvent> getOnMouseExitedHandler(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> exited, int rotation) {
        return event -> {
            exitZoom(imageView, pos, scale).handle(event);
            exited.handle(event);

        };
    }

    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom, List<Node> children, int rotation){
        addHoveringEffects(imageView, pos, scale, event -> {
            entered.handle(event);
            for (Node child:
                 children) {
                child.toFront();
            }
        }, exited, hoverZoom, true, rotation);

        for (Node child:
             children) {

            child.setOnMouseEntered(getOnMouseEnteredHandler(imageView, pos, scale, event -> {
                entered.handle(event);
                for (Node brother:
                     children) {
                    brother.toFront();
                }

            }, hoverZoom, true));

            child.setOnMouseExited(getOnMouseExitedHandler(imageView, pos, scale, exited, rotation));
        }
    }

    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom, boolean bringToFront){
        addHoveringEffects(imageView, pos, scale, entered, exited, hoverZoom, bringToFront, Coord.NO_ROTATION);
    }

    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom, List<Node> children){
        addHoveringEffects(imageView, pos, scale, entered, exited, hoverZoom, children, Coord.NO_ROTATION);
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

    public static EventHandler<MouseEvent> getChildrenEnteredZoom(ImageView child, Coord slot, double scale, double hoverZoom, ImageView parentView, int rotation){

        Coord rotatedSlot = slot.pureRotate(new Coord(0,0), rotation);

        return event -> {
            child.setFitWidth(child.getFitWidth() * hoverZoom);
            child.setFitHeight(child.getFitHeight() * hoverZoom);
            child.setX(rotatedSlot.x * scale * hoverZoom + parentView.getX() + parentView.getFitWidth() / 2 - child.getFitWidth() / 2);
            child.setY(rotatedSlot.y * scale * hoverZoom + parentView.getY() + parentView.getFitHeight() / 2 - child.getFitHeight() / 2);
            child.setY(rotatedSlot.y * scale * hoverZoom + parentView.getY() + parentView.getFitHeight() / 2 - child.getFitHeight() / 2);
        };
    }

    public static EventHandler<MouseEvent> getChildrenEnteredZoom(ImageView child, Coord slot, double scale, double hoverZoom, ImageView parentView){
        return getChildrenEnteredZoom(child, slot, scale, hoverZoom, parentView, Coord.NO_ROTATION);
    }

    public static EventHandler<MouseEvent> getChildrenExitedZoom(ImageView child, Coord slot, double scale, double hoverZoom, ImageView parentView, int rotation){

        Coord rotatedSlot = slot.pureRotate(new Coord(0,0), rotation);

        return event -> {
            child.setFitWidth(child.getFitWidth() / hoverZoom);
            child.setFitHeight(child.getFitHeight() / hoverZoom);
            child.setX(rotatedSlot.x * scale + parentView.getX() + parentView.getFitWidth() / 2 - child.getFitWidth() / 2);
            child.setY(rotatedSlot.y * scale + parentView.getY() + parentView.getFitHeight() / 2 - child.getFitHeight() / 2);
        };
    }

    public static EventHandler<MouseEvent> getChildrenExitedZoom(ImageView child, Coord slot, double scale, double hoverZoom, ImageView parentView){
        return getChildrenExitedZoom(child, slot, scale, hoverZoom, parentView, Coord.NO_ROTATION);
    }

    public static EventHandler<MouseEvent> getChildrenEnteredZoom(Rectangle rectangle, Coord slot, double scale, double hoverZoom, ImageView parentView, int rotation){

        double childWidth, childHeight, parentWidth, parentHeight;

        if (rotation == Coord.CLOCKWISE || rotation == Coord.COUNTERCLOCKWISE){

            childWidth = rectangle.getHeight();
            childHeight = rectangle.getWidth();
            parentWidth = parentView.getFitHeight();
            parentHeight = parentView.getFitWidth();
        }

        else {
            childWidth = rectangle.getWidth();
            childHeight = rectangle.getHeight();
            parentWidth = parentView.getFitWidth();
            parentHeight = parentView.getFitHeight();
        }

        return event -> {
            rectangle.setWidth(rectangle.getWidth() * hoverZoom);
            rectangle.setHeight(rectangle.getHeight() * hoverZoom);
            rectangle.setX(parentView.getX() + parentWidth * hoverZoom / 2 + slot.x * scale * hoverZoom - childWidth * hoverZoom / 2);
            rectangle.setY(parentView.getY() + parentHeight * hoverZoom / 2 + slot.y * scale * hoverZoom - childHeight * hoverZoom / 2);
        };
    }

    public static EventHandler<MouseEvent> getChildrenExitedZoom(Rectangle rectangle, Coord slot, double scale, double hoverZoom, ImageView parentView, int rotation){

        double childWidth, childHeight, parentWidth, parentHeight;

        if (rotation == Coord.CLOCKWISE || rotation == Coord.COUNTERCLOCKWISE){

            childWidth = rectangle.getHeight();
            childHeight = rectangle.getWidth();
            parentWidth = parentView.getFitHeight();
            parentHeight = parentView.getFitWidth();
        }

        else {
            childWidth = rectangle.getWidth();
            childHeight = rectangle.getHeight();
            parentWidth = parentView.getFitWidth();
            parentHeight = parentView.getFitHeight();
        }

        return event -> {
            rectangle.setWidth(rectangle.getWidth() / hoverZoom);
            rectangle.setHeight(rectangle.getHeight() / hoverZoom);
            rectangle.setX(parentView.getX() + parentWidth / 2 + slot.x * scale - childWidth / 2);
            rectangle.setY(parentView.getY() + parentHeight / 2 + slot.y * scale - childHeight / 2);
        };
    }

    public static EventHandler<MouseEvent> getChildrenEnteredZoom(Text text, Coord slot, double scale, double hoverZoom, ImageView parentView){

        return event -> {

            text.setFont(Font.font(text.getFont().getName(), text.getFont().getSize() * hoverZoom));
            text.setWrappingWidth(text.getWrappingWidth() * hoverZoom);
            text.setX(parentView.getX() + parentView.getFitWidth() / 2 + slot.x * scale * hoverZoom - text.getWrappingWidth() / 2);
            text.setY(parentView.getY() + parentView.getFitHeight() / 2 + slot.y * scale * hoverZoom - text.maxHeight(-1) / 2);
        };
    }

    public static EventHandler<MouseEvent> getChildrenExitedZoom(Text text, Coord slot, double scale, double hoverZoom, ImageView parentView){


        return event -> {
            text.setFont(Font.font(text.getFont().getName(), text.getFont().getSize() / hoverZoom));
            text.setWrappingWidth(text.getWrappingWidth() / hoverZoom);
            text.setX(parentView.getX() + parentView.getFitWidth() / 2 + slot.x * scale - text.getWrappingWidth() / 2);
            text.setY(parentView.getY() + parentView.getFitHeight() / 2 + slot.y * scale - text.maxHeight(-1) / 2);
        };
    }

    public static void addLightning(ImageView imageView, Color color){
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(45, 45, color));

        imageView.setEffect(lighting);
    }
}
