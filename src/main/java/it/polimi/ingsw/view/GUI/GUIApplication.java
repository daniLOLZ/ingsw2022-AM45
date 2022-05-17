package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUIApplication extends Application {

    private static final double WINDOW_WIDTH = 1500, WINDOW_HEIGHT = 720, REAL_SIZE = 1;

    private static final double left    = 0,
                                right   = WINDOW_WIDTH,
                                up      = 0,
                                down    = WINDOW_HEIGHT,
                                centerX = WINDOW_WIDTH/2,
                                centerY = WINDOW_HEIGHT/2;

    private static final Coord upLeftCorner    = new Coord(left,up),
                               upRightCorner   = new Coord(right,up),
                               upCenter        = new Coord(centerX,up),
                               centerRight     = new Coord(right,centerY),
                               centerLeft      = new Coord(left,centerY),
                               center          = new Coord(centerX,centerY),
                               downLeftCorner  = new Coord(left,down),
                               downRightCorner = new Coord(right,down),
                               downCenter      = new Coord(centerX,down);


    @Override
    public void start(Stage stage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root);

        Image icon = new Image("assets/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Eriantys");

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image image = new Image("assets/icon.png");
        drawImage(gc, image, center);

        root.getChildren().add(canvas);

        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args){
        launch();
    }

    /**
     * Draws an image scaled by the given scaling factor.
     * If alignment coordinates are inside the window, the drawn image is always totally in the window (unless it's too big)
     * @param graphicsContext The graphics context required to draw the image
     * @param image The image to draw
     * @param alignment The point the image should be placed
     * @param scale The scaling factor applied to the image
     */
    private void drawImage (GraphicsContext graphicsContext, Image image, Coord alignment, double scale){

        double imageWidth = image.getWidth()*scale, imageHeight = image.getHeight()*scale;

        Coord pos = new Coord(alignment.x/WINDOW_WIDTH, alignment.y/WINDOW_HEIGHT),
              anchor = new Coord(pos.x * imageWidth, pos.y * imageHeight);

        graphicsContext.drawImage(image, alignment.x - anchor.x, alignment.y - anchor.y, imageWidth, imageHeight);
    }

    private void drawImage (GraphicsContext graphicsContext, Image image, Coord alignment){
        drawImage(graphicsContext, image, alignment, REAL_SIZE);
    }

    /**
     * Draws an image scaled by the given scaling factor, positioning its center at the give coordinates.
     * @param graphicsContext The graphics context required to draw the image
     * @param image The image to draw
     * @param pos The point the image's center should be placed
     * @param scale The scaling factor applied to the image
     */
    private void drawFromCenterImage (GraphicsContext graphicsContext, Image image, Coord pos, double scale){

        double imageWidth = image.getWidth()*scale, imageHeight = image.getHeight()*scale;
        Coord imageCenter = new Coord(imageWidth/2, imageHeight/2);

        graphicsContext.drawImage(image,pos.x - imageCenter.x,pos.y - imageCenter.y, imageWidth, imageHeight);
    }

    private void drawFromCenterImage (GraphicsContext graphicsContext, Image image, Coord pos){
        drawFromCenterImage(graphicsContext, image, pos, REAL_SIZE);
    }
}
