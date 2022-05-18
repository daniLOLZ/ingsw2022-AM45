package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class GUIApplication extends Application {

    private static final double WINDOW_WIDTH = 1500, WINDOW_HEIGHT = 720, REAL_SIZE = 1, MAX_REPORTABLE_WRONG_ATTEMPTS = 6;

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

    private static final List<String> availableGameRules = new ArrayList<>(List.of("Normal mode", "Expert mode"));
    private static final List<Integer> availablePlayerNumber = new ArrayList<>(List.of(2, 3, 4));

    private Stage stage;
    private String chosenNickname;

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


        Image image = new Image("assets/icon.png");
        ImageView imageView = new ImageView(image);
        imageView.fitHeightProperty().setValue(image.getHeight()/1.2);
        imageView.fitWidthProperty().setValue(image.getWidth()/1.2);

        Label welcomeMessage = new Label("WELCOME TO ERIANTYS");
        welcomeMessage.setFont(Font.font("Verdana", 42));

        //button that brings to login screen
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

        //in case of logout I want to reset the action on close request
        //to not try and send a quit message
        //through a non-existent socket
        stage.setOnCloseRequest(null);

        StackPane root = new StackPane();

        VBox login = new VBox(20);
        login.setAlignment(Pos.CENTER);

        //<editor-fold desc="Decorations">

        Image background = new Image("assets/decorations/login_screen/background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT,0.5,true,Side.TOP,0.5,true),
                new BackgroundSize(WINDOW_WIDTH, WINDOW_WIDTH, false, false, false, false));

        root.setBackground(new Background(backgroundImage));

        Image profsLogo = new Image("assets/decorations/login_screen/professors.png");
        Image textLogo = new Image("assets/decorations/login_screen/eriantys_text_logo.png");
        Image king = new Image("assets/decorations/login_screen/king_no_bg.png");
        Image pixie = new Image("assets/decorations/login_screen/pixie_no_bg.png");
        Image sorcerer = new Image("assets/decorations/login_screen/sorcerer_no_bg.png");
        Image wizard = new Image("assets/decorations/login_screen/wizard_no_bg.png");
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        drawImage(graphicsContext,
                profsLogo,
                new Coord(centerX, centerY * 0.1),
                0.6);
        drawImage(graphicsContext,
                textLogo,
                new Coord(centerX, centerY * 0.35),
                0.15);

        double rescalingFactor = pixie.getHeight()/king.getHeight();


        drawImage(graphicsContext,
                king,
                new Coord(right * 0.2, down),
                0.4);
        drawImage(graphicsContext,
                pixie,
                new Coord(right * 0.4, down),
                0.4/rescalingFactor);
        drawImage(graphicsContext,
                sorcerer,
                new Coord(right * 0.6, down),
                0.4/rescalingFactor);
        drawImage(graphicsContext,
                wizard,
                new Coord(right * 0.8, down),
                0.4/rescalingFactor);

        root.getChildren().add(canvas);

        //</editor-fold>

        root.getChildren().add(login);

        Scene loginScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        Button loginButton = new Button("Login");

        Label loginLabel = new Label("Nickname");
        TextField textField = new TextField();
        textField.setPromptText("Insert your nickname");
        textField.setMaxWidth(WINDOW_WIDTH/4);
        textField.setOnAction(event -> loginButton.fire());

        AtomicInteger wrongAttempts = new AtomicInteger(0);

        loginButton.setOnAction(event -> { //calls external class to check input validity
            if(ConnectionWithServerHandler.login(textField.getText())) showSearchGameScreen();
            else {

                if (wrongAttempts.incrementAndGet() == 1) {
                    Label errorMessage = new Label("Invalid nickname! Please try again");
                    errorMessage.setTextFill(Color.RED);
                    login.getChildren().add(errorMessage);
                }
                if (wrongAttempts.get() == MAX_REPORTABLE_WRONG_ATTEMPTS){
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

        //user is logged in. If he quits, the server is notified
        stage.setOnCloseRequest(event -> ConnectionWithServerHandler.quit());

        VBox lookingForLobby = new VBox(30);
        lookingForLobby.setAlignment(Pos.CENTER);

        GridPane preferences = new GridPane();
        preferences.setAlignment(Pos.CENTER);
        preferences.setPadding(new Insets(20,20,20,20));
        preferences.setHgap(15);
        preferences.setVgap(50);

        //select game rules
        Label gameRuleLabel = new Label("Select game rules");
        GridPane.setConstraints(gameRuleLabel, 0, 0);

        ChoiceBox<String> gameRuleSelection = new ChoiceBox<>();
        gameRuleSelection.getItems().addAll(availableGameRules);
        gameRuleSelection.setValue(availableGameRules.get(0));
        GridPane.setConstraints(gameRuleSelection, 1, 0);

        //select number of player
        Label numPlayersLabel = new Label("Select the number of players");
        GridPane.setConstraints(numPlayersLabel, 0, 1);

        ChoiceBox<Integer> numPlayersSelection = new ChoiceBox<>();
        numPlayersSelection.getItems().addAll(availablePlayerNumber);
        numPlayersSelection.setValue(availablePlayerNumber.get(0));
        GridPane.setConstraints(numPlayersSelection, 1, 1);

        preferences.getChildren().addAll(gameRuleLabel, gameRuleSelection, numPlayersLabel, numPlayersSelection);

        HBox bottomBar = new HBox(13);
        bottomBar.setAlignment(Pos.CENTER);
        Label searching = new Label("");
        Button searchGameButton = new Button("Search Game");
        searchGameButton.setOnAction(event -> sendSearchGameRequest(gameRuleSelection.getValue(), numPlayersSelection.getValue(), searching));



        bottomBar.getChildren().addAll(searchGameButton, searching);
        lookingForLobby.getChildren().addAll(preferences, bottomBar);

        Scene searchGame = new Scene(lookingForLobby, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(searchGame);
    }

    private void sendSearchGameRequest(String gameRule, int numPlayers, Label outNotify){

        outNotify.setText("Searching...");
        new Thread(() -> ConnectionWithServerHandler.searchGame(gameRule, numPlayers)).start();

    }

    //probably will be called from above
    private void showLobbyScreen(){

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