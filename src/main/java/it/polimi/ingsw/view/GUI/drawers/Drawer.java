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
    /**
     * Draws an image (in its real size).
     * If alignment coordinates are inside the scene, the drawn image is always totally inside the scene (unless it's too big)
     * @param graphicsContext The graphics context required to draw the image
     * @param image The image to draw
     * @param alignment The point the image should be placed
     */
    public static void drawImage (GraphicsContext graphicsContext, Image image, Coord alignment){
        Drawer.drawImage(graphicsContext, image, alignment, REAL_SIZE);
    }

    /**
     * Draws an image scaled by the given scaling factor, positioning its center at the given coordinates.
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

    /**
     * Creates a view of an image scaled by the given scaling factor, positioning its center at the given coordinates.
     * Also allows to assign an action when the image is clicked on.
     * @param image The image to draw
     * @param pos The point the image's center should be placed
     * @param scale The scaling factor applied to the image
     * @param onClick The Handler containing the action to perform on click
     * @return The view containing the desired image
     */
    public static ImageView drawFromCenterInteractiveImage (Image image, Coord pos, double scale, EventHandler<MouseEvent> onClick){

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(image.getWidth() * scale);
        imageView.setFitHeight(image.getHeight() * scale);
        imageView.setX(pos.x - scale * image.getWidth() / 2);
        imageView.setY(pos.y - scale * image.getHeight() / 2);
        imageView.setOnMouseClicked(onClick);

        return imageView;
    }

    /**
     * Makes the provided view enlarge when hovered on. Also allows additional actions when the mouse enters and exits the view.
     * @param imageView The view that should be zoomed
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @param entered The additional action to perform on mouse entered
     * @param exited The additional action to perfrom on mouse exited
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param bringToFront true if the view must be brought in front of the other objects currently drawn on screen
     * @param rotation The rotation that was applied to the view
     */
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

                getOnMouseExitedHandler(imageView, pos, scale, exited).handle(event);
            });
        }
    }

    /**
     * Constructs the final Handler to assign when the user hover an image.
     * @param imageView The view that should be zoomed
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @param entered The additional action to perform on mouse entered
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param bringToFront true if the view must be brought in front of the other objects currently drawn on screen
     * @return The Handler that will be assigned to the view
     */
    private static EventHandler<MouseEvent> getOnMouseEnteredHandler(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, double hoverZoom, boolean bringToFront) {
        return event -> {

            enterZoom(imageView, pos, scale, hoverZoom).handle(event);
            if (bringToFront)imageView.toFront();
            entered.handle(event);

        };
    }

    /**
     * Constructs the final Handler to assign when the user hover an image.
     * @param imageView The view that should be zoomed
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @param exited The additional action to perform on mouse exited
     * @return The Handler that will be assigned to the view
     */
    private static EventHandler<MouseEvent> getOnMouseExitedHandler(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> exited) {
        return event -> {
            exitZoom(imageView, pos, scale).handle(event);
            exited.handle(event);

        };
    }

    /**
     * Makes the provided view enlarge when hovered on. Also allows additional actions when the mouse enters and exits the view.
     * Also applies the same zoom effect to the provided children.
     * @param imageView The view that should be zoomed
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @param entered The additional action to perform on mouse entered
     * @param exited The additional action to perfrom on mouse exited
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param children The child nodes of the object to draw
     * @param rotation The rotation that was applied to the view
     */
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

            child.setOnMouseExited(getOnMouseExitedHandler(imageView, pos, scale, exited));
        }
    }

    /**
     * Makes the provided view enlarge when hovered on. Also allows additional actions when the mouse enters and exits the view.
     * @param imageView The view that should be zoomed
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @param entered The additional action to perform on mouse entered
     * @param exited The additional action to perfrom on mouse exited
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param bringToFront true if the view must be brought in front of the other objects currently drawn on screen
     */
    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom, boolean bringToFront){
        addHoveringEffects(imageView, pos, scale, entered, exited, hoverZoom, bringToFront, Coord.NO_ROTATION);
    }

    /**
     * Makes the provided view enlarge when hovered on. Also allows additional actions when the mouse enters and exits the view.
     * Also applies the same zoom effect to the provided children.
     * @param imageView The view that should be zoomed
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @param entered The additional action to perform on mouse entered
     * @param exited The additional action to perfrom on mouse exited
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param children The child nodes of the object to draw
     */
    public static void addHoveringEffects(ImageView imageView, Coord pos, double scale, EventHandler<MouseEvent> entered, EventHandler<MouseEvent> exited, double hoverZoom, List<Node> children){
        addHoveringEffects(imageView, pos, scale, entered, exited, hoverZoom, children, Coord.NO_ROTATION);
    }

    /**
     * Creates a Handler that enlarges the given view.
     * @param imageView The view that should be zoomed
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @return The handler that will be used to enlarge the view
     */
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

    /**
     * Gets the "anchor" of an image in the current screen of the application.
     * (The anchor is a point for which it's true that the ratio between the position on the image, relative to the image current size is the same as the ratio between the position on the screen, relative to the screen size).
     * @param image The Image for which to calculate the anchor
     * @param pos The position of the center of the image on the screen
     * @param scale The scale to be applied to the image (which changes its size and hence its anchor)
     * @return The anchor of the image on the screen
     */
    private static Coord getAnchor(Image image, Coord pos, double scale) {

        return new Coord((pos.x - image.getWidth() * scale / 2) * image.getWidth() * scale / GUIApplication.WINDOW_WIDTH / (1 - image.getWidth() * scale / GUIApplication.WINDOW_WIDTH),
                (pos.y - image.getHeight() * scale / 2) * image.getHeight() * scale / GUIApplication.WINDOW_HEIGHT / (1 - image.getHeight() * scale / GUIApplication.WINDOW_HEIGHT));
    }

    /**
     * Creates a Handler that shrinks the given view.
     * @param imageView The view that should be shrunk
     * @param pos The relative position of the view
     * @param scale The scale that has been previously applied to the view
     * @return The handler that will be used to shrink the view
     */
    private static EventHandler<MouseEvent> exitZoom(ImageView imageView, Coord pos, double scale){

        return event -> {

            Image image = imageView.getImage();

            imageView.setFitWidth(image.getWidth() * scale);
            imageView.setFitHeight(image.getHeight() * scale);
            imageView.setX(pos.x - scale * image.getWidth() / 2);
            imageView.setY(pos.y - scale * image.getHeight() / 2);
        };
    }

    /**
     * Creates a Handler to apply a hovering zoom effect coordinated with the parent effect.
     * ImageView version.
     * @param child The child view
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @param rotation The rotation previously applied to the parent view
     * @return The Handler which will be assigned to the parent to allow him to zoom his children
     */
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

    /**
     * Creates a Handler to apply a hovering zoom effect coordinated with the parent effect.
     * ImageView version.
     * @param child The child view
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @return The Handler which will be assigned to the parent to allow him to zoom his children
     */
    public static EventHandler<MouseEvent> getChildrenEnteredZoom(ImageView child, Coord slot, double scale, double hoverZoom, ImageView parentView){
        return getChildrenEnteredZoom(child, slot, scale, hoverZoom, parentView, Coord.NO_ROTATION);
    }

    /**
     * Creates a Handler to apply a shrink effect on mouse exited coordinated with the parent effect.
     * ImageView version.
     * @param child The child view
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @param rotation The rotation previously applied to the parent view
     * @return The Handler which will be assigned to the parent to allow him to shrink his children
     */
    public static EventHandler<MouseEvent> getChildrenExitedZoom(ImageView child, Coord slot, double scale, double hoverZoom, ImageView parentView, int rotation){

        Coord rotatedSlot = slot.pureRotate(new Coord(0,0), rotation);

        return event -> {
            child.setFitWidth(child.getFitWidth() / hoverZoom);
            child.setFitHeight(child.getFitHeight() / hoverZoom);
            child.setX(rotatedSlot.x * scale + parentView.getX() + parentView.getFitWidth() / 2 - child.getFitWidth() / 2);
            child.setY(rotatedSlot.y * scale + parentView.getY() + parentView.getFitHeight() / 2 - child.getFitHeight() / 2);
        };
    }

    /**
     * Creates a Handler to apply a shrink effect on mouse exited coordinated with the parent effect.
     * ImageView version.
     * @param child The child view
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @return The Handler which will be assigned to the parent to allow him to shrink his children
     */
    public static EventHandler<MouseEvent> getChildrenExitedZoom(ImageView child, Coord slot, double scale, double hoverZoom, ImageView parentView){
        return getChildrenExitedZoom(child, slot, scale, hoverZoom, parentView, Coord.NO_ROTATION);
    }

    /**
     * Creates a Handler to apply a hovering zoom effect coordinated with the parent effect.
     * Rectangle version.
     * @param rectangle The child rectangle
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @return The Handler which will be assigned to the parent to allow him to zoom his children
     */
    public static EventHandler<MouseEvent> getChildrenEnteredZoom(Rectangle rectangle, Coord slot, double scale, double hoverZoom, ImageView parentView){

        double childWidth, childHeight, parentWidth, parentHeight;

        childWidth = rectangle.getWidth();
        childHeight = rectangle.getHeight();
        parentWidth = parentView.getFitWidth();
        parentHeight = parentView.getFitHeight();

        return event -> {
            rectangle.setWidth(rectangle.getWidth() * hoverZoom);
            rectangle.setHeight(rectangle.getHeight() * hoverZoom);
            rectangle.setX(parentView.getX() + parentWidth * hoverZoom / 2 + slot.x * scale * hoverZoom - childWidth * hoverZoom / 2);
            rectangle.setY(parentView.getY() + parentHeight * hoverZoom / 2 + slot.y * scale * hoverZoom - childHeight * hoverZoom / 2);
        };
    }

    /**
     * Creates a Handler to apply a hovering shrinl effect coordinated with the parent effect.
     * Rectangle version.
     * @param rectangle The child rectangle
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @return The Handler which will be assigned to the parent to allow him to shrink his children
     */
    public static EventHandler<MouseEvent> getChildrenExitedZoom(Rectangle rectangle, Coord slot, double scale, double hoverZoom, ImageView parentView){

        double childWidth, childHeight, parentWidth, parentHeight;

        childWidth = rectangle.getWidth();
        childHeight = rectangle.getHeight();
        parentWidth = parentView.getFitWidth();
        parentHeight = parentView.getFitHeight();


        return event -> {
            rectangle.setWidth(rectangle.getWidth() / hoverZoom);
            rectangle.setHeight(rectangle.getHeight() / hoverZoom);
            rectangle.setX(parentView.getX() + parentWidth / 2 + slot.x * scale - childWidth / 2);
            rectangle.setY(parentView.getY() + parentHeight / 2 + slot.y * scale - childHeight / 2);
        };
    }

    /**
     * Creates a Handler to apply a hovering zoom effect coordinated with the parent effect.
     * Text version.
     * @param text The child text
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @return The Handler which will be assigned to the parent to allow him to zoom his children
     */
    public static EventHandler<MouseEvent> getChildrenEnteredZoom(Text text, Coord slot, double scale, double hoverZoom, ImageView parentView){

        return event -> {

            text.setFont(Font.font(text.getFont().getName(), text.getFont().getSize() * hoverZoom));
            text.setWrappingWidth(text.getWrappingWidth() * hoverZoom);
            text.setX(slot.x);
            text.setY(slot.y);
        };
    }

    /**
     * Creates a Handler to apply a hovering shrinl effect coordinated with the parent effect.
     * Text version.
     * @param text The child text
     * @param slot The position of the child (relative to its parent's and not scaled nor rotated)
     * @param scale The scale that was applied to the parent view
     * @param hoverZoom The zoom ratio that will be used when enlarging the view
     * @param parentView The parent view
     * @return The Handler which will be assigned to the parent to allow him to shrink his children
     */
    public static EventHandler<MouseEvent> getChildrenExitedZoom(Text text, Coord slot, double scale, double hoverZoom, ImageView parentView){


        return event -> {
            text.setFont(Font.font(text.getFont().getName(), text.getFont().getSize() / hoverZoom));
            text.setWrappingWidth(text.getWrappingWidth() / hoverZoom);
            text.setX(slot.x);
            text.setY(slot.y);
        };
    }

    /**
     * Adds lighting of the given color to the provided view
     * @param imageView The view to apply the lighting to
     * @param color The color of the lighting to apply
     */
    public static void addLighting(ImageView imageView, Color color){
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(45, 45, color));

        imageView.setEffect(lighting);
    }
}
