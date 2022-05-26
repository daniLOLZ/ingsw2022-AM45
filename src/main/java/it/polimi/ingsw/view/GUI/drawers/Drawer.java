package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.view.GUI.Coord;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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
}
