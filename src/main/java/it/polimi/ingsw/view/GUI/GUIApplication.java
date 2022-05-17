package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;


public class GUIApplication extends Application {

    private static final double WINDOW_WIDTH = 1500, WINDOW_HEIGHT = 720, REAL_SIZE = 1, MAX_REPORTABLE_WRONG_LOGINS = 6;

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

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        VBox root = new VBox(25);
        root.setAlignment(Pos.TOP_CENTER);

        //First scene - Opening the game
        Scene startingScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        Image icon = new Image("assets/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Eriantys");

        //Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        Image image = new Image("assets/icon.png");
        ImageView imageView = new ImageView(image);
        imageView.fitHeightProperty().setValue(image.getHeight()/1.2);
        imageView.fitWidthProperty().setValue(image.getWidth()/1.2);
        //drawImage(gc, image, center);

        Label welcomeMessage = new Label("WELCOME TO ERIANTYS");
        welcomeMessage.setFont(Font.font("Verdana", 42));

        Button playButton = new Button("Play");
        playButton.setTextFill(Color.DARKRED);
        playButton.setOnAction(event -> showLoginScreen());

        root.getChildren().addAll(imageView, welcomeMessage, playButton);


        stage.setScene(startingScene);
        stage.show();
    }
    
    public static void main(String[] args){
        launch();
    }

    //@Override
    public void showLoginScreen() {

        VBox login = new VBox(20);
        login.setAlignment(Pos.CENTER);

        Scene loginScene = new Scene(login, WINDOW_WIDTH, WINDOW_HEIGHT);

        Button loginButton = new Button("Login");

        Label loginLabel = new Label("Nickname");
        TextField textField = new TextField("Testa : di cazzo");
        textField.setMaxWidth(WINDOW_WIDTH/4);
        textField.setOnAction(event -> loginButton.fire());

        AtomicInteger wrongAttempts = new AtomicInteger();

        loginButton.setOnAction(event -> {
            if(ConnectionWithServerHandler.login(textField.getText())) showSearchGameScreen();
            else {

                if (wrongAttempts.incrementAndGet() < MAX_REPORTABLE_WRONG_LOGINS) {
                    Label errorMessage = new Label("Invalid nickname! Please try again");
                    errorMessage.setTextFill(Color.RED);
                    login.getChildren().add(errorMessage);
                }
                if (wrongAttempts.get() == MAX_REPORTABLE_WRONG_LOGINS){
                    Label errorMessage = new Label("You sure are persistent...");
                    errorMessage.setTextFill(Color.RED);
                    errorMessage.setStyle("-fx-font-style: italic");
                    login.getChildren().add(errorMessage);
                }
            }
        });

        HBox inputNickname = new HBox(10);
        inputNickname.getChildren().addAll(loginLabel, textField);
        inputNickname.setAlignment(Pos.CENTER);

        login.getChildren().addAll(inputNickname, loginButton);
        stage.setScene(loginScene);
    }

    public void showSearchGameScreen(){

        VBox lookingForLobby = new VBox(25);
        lookingForLobby.setAlignment(Pos.CENTER);

        Scene searchGame = new Scene(lookingForLobby, WINDOW_WIDTH, WINDOW_HEIGHT);

        Button button = new Button("Search Game");

        lookingForLobby.getChildren().addAll(button);
        stage.setScene(searchGame);
    }

    /**
     * Draws an image scaled by the given scaling factor.
     * If alignment coordinates are inside the scene, the drawn image is always totally inside the scene (unless it's too big)
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
