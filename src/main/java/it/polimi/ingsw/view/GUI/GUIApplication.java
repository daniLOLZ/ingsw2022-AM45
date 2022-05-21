package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class GUIApplication extends Application {

    private static final double WINDOW_WIDTH = 1520, WINDOW_HEIGHT = 780, REAL_SIZE = 1;

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

    private String preselectedGameRule = availableGameRules.get(0);
    private Integer preselectedNumPlayers = availablePlayerNumber.get(0);
    private List<CloudBean> clouds;
    private List<IslandGroupBean> islands;

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        Image icon = new Image("assets/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Eriantys");
        stage.setX(0);
        stage.setY(0);

        VBox root = new VBox(70);
        root.setAlignment(Pos.CENTER);

        Button playButton = new Button("Start your journey");
        playButton.setOnAction(event -> showLoginScreen(false));

        //<editor-fold desc="Decorations">

        showMenuBackground(root);

        Label welcomeMessage = new Label("""
                Once upon a time, a boy lifted his eyes,
                And glanced at islands full of magic filling up the skies.
                They enjoy learning, practicing and playing during their leisure,
                They're the Wizards, their assistants, and the gorgeous Mother Nature.
                You can visit the entrance, the dining hall, or the alleys.
                Either way, you're welcome to the world of Eriantys.""");

        welcomeMessage.setFont(Font.font("Pristina",47));
        welcomeMessage.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(welcomeMessage);

        playButton.setMaxWidth(700);
        playButton.setMinHeight(120);
        playButton.setBackground(Background.EMPTY);
        playButton.setTextFill(Color.DARKRED);
        playButton.setOnMouseEntered(event -> playButton.setFont(Font.font("Lucida Handwriting", 50)));
        playButton.setOnMouseExited(event -> playButton.setFont(Font.font("Lucida Handwriting", 40)));
        playButton.setFont(Font.font("Lucida Handwriting", 40));

        //</editor-fold>

        root.getChildren().add(playButton);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();

    }
    
    public static void main(String[] args){
        launch();
    }

    //@Override
    public void showLoginScreen(boolean errorOccurred) {

        //in case of logout I want to reset the action on close request
        //to not try and send a quit message
        //through a non-existent socket
        stage.setOnCloseRequest(null);

        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        showMenuBackground(root);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        drawLogo(graphicsContext);

        Image king = new Image("assets/decorations/login_screen/king_no_bg.png");
        Image pixie = new Image("assets/decorations/login_screen/pixie_no_bg.png");
        Image sorcerer = new Image("assets/decorations/login_screen/sorcerer_no_bg.png");
        Image wizard = new Image("assets/decorations/login_screen/wizard_no_bg.png");

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

        VBox login = new VBox(20);
        login.setAlignment(Pos.CENTER);

        root.getChildren().add(login);

        Scene loginScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);


        Label hostNameLabel = new Label("Hostname");
        TextField inputHostName = new TextField("127.0.0.1");
        inputHostName.setPromptText("Insert hostname");

        Label loginLabel = new Label("Nickname");
        TextField inputNickname = new TextField();
        Button loginButton = new Button("Login");

        HBox hostNameSelection = new HBox(10);
        hostNameSelection.getChildren().addAll(hostNameLabel, inputHostName);
        hostNameSelection.setAlignment(Pos.CENTER);

        inputNickname.setPromptText("Insert your nickname");
        inputNickname.setMaxWidth(WINDOW_WIDTH/4);
        inputNickname.setOnAction(event -> loginButton.fire());
        Label errorMessage = new Label("Invalid nickname! Please try again");
        errorMessage.setTextFill(Color.RED);

        errorMessage.setVisible(errorOccurred);

        //calls external class to check input validity
        loginButton.setOnAction(event -> ConnectionWithServerHandler.login(inputHostName.getText(), inputNickname.getText()));

        HBox nicknameSelection = new HBox(10);
        nicknameSelection.getChildren().addAll(loginLabel, inputNickname);
        nicknameSelection.setAlignment(Pos.CENTER);

        login.getChildren().addAll(hostNameSelection, nicknameSelection, loginButton, errorMessage);

        //<editor-fold desc="Debug">

        Button success = new Button("Simulate success");
        Button failure = new Button("Simulate failure");

        success.setOnAction(event -> notifySuccessfulLogin());
        failure.setOnAction(event -> notifyLoginFailure());

        login.getChildren().addAll(success, failure);

        //</editor-fold>

        stage.setScene(loginScene);
    }

    public void notifySuccessfulLogin(){
        showSearchGameScreen(false);
    }

    public void notifyLoginFailure(){
        showLoginScreen(true);
    }

    private void showSearchGameScreen(boolean errorOccurred){

        //user is logged in. If he quits, the server is notified
        stage.setOnCloseRequest(event -> ConnectionWithServerHandler.quit());

        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        showMenuBackground(root);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        drawLogo(graphicsContext);
        showDecorativeIslands(graphicsContext);

        root.getChildren().add(canvas);

        //</editor-fold>

        VBox lookingForLobby = new VBox(30);
        lookingForLobby.setAlignment(Pos.CENTER);
        lookingForLobby.setPadding(new Insets(100, 0, 0, 0));

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
        gameRuleSelection.setValue(preselectedGameRule);
        GridPane.setConstraints(gameRuleSelection, 1, 0);

        //select number of player
        Label numPlayersLabel = new Label("Select the number of players");
        GridPane.setConstraints(numPlayersLabel, 0, 1);

        ChoiceBox<Integer> numPlayersSelection = new ChoiceBox<>();
        numPlayersSelection.getItems().addAll(availablePlayerNumber);
        numPlayersSelection.setValue(preselectedNumPlayers);
        GridPane.setConstraints(numPlayersSelection, 1, 1);

        preferences.getChildren().addAll(gameRuleLabel, gameRuleSelection, numPlayersLabel, numPlayersSelection);

        HBox bottomBar = new HBox(13);
        bottomBar.setAlignment(Pos.CENTER);
        Label searching = new Label();
        if (errorOccurred) searching.setText("Error! Couldn't find your game");
        else searching.setText("Click here to search a game");
        Button searchGameButton = new Button("Search Game");
        searchGameButton.setOnAction(event -> {
            //game must be searched once
            searchGameButton.setOnAction(null);

            //saving selections in case operation fails
            preselectedGameRule = gameRuleSelection.getValue();
            preselectedNumPlayers = numPlayersSelection.getValue();
            sendSearchGameRequest(gameRuleSelection.getValue(), numPlayersSelection.getValue(), searching);
        });


        bottomBar.getChildren().addAll(searchGameButton, searching);
        lookingForLobby.getChildren().addAll(preferences, bottomBar);

        root.getChildren().add(lookingForLobby);
        Scene searchGame = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);


        //<editor-fold desc="Debug">

        Label debugLabel = new Label("Debugging options");
        Button success = new Button("Simulate game lobby");
        Button failure = new Button("Simulate failure");
        success.setOnAction(event -> notifyEnteredLobby());
        failure.setOnAction(event -> notifyErrorInSearchGame());

        HBox debug = new HBox(20);
        debug.setAlignment(Pos.CENTER);
        debug.getChildren().addAll(success, failure);

        lookingForLobby.getChildren().addAll(debugLabel, debug);

        //</editor-fold>


        stage.setScene(searchGame);
    }

    private void sendSearchGameRequest(String gameRule, int numPlayers, Label outNotify){

        outNotify.setText("Searching...");
        new Thread(() -> ConnectionWithServerHandler.searchGame(gameRule, numPlayers)).start();

    }

    //will be called from above
    public void notifyEnteredLobby(){
        showLobbyScreen(false);
    }

    //will be called from above
    public void notifyErrorInSearchGame(){
        showSearchGameScreen(true);
    }

    private void showLobbyScreen(boolean errorOccured){

        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        showMenuBackground(root);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        drawLogo(graphicsContext);
        showDecorativeIslands(graphicsContext);

        root.getChildren().add(canvas);

        //</editor-fold>

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(100,0,0,0));

        Label gameDetails = new Label("Game rules: " + preselectedGameRule + "\nPlayers: " + preselectedNumPlayers);
        gameDetails.setFont(Font.font("Verdana", 25));

        HBox userActions = new HBox(35);
        userActions.setAlignment(Pos.CENTER);

        CheckBox ready = new CheckBox("Ready");
        Button startGame = new Button("Start game");
        Button leaveLobby = new Button("Leave lobby");
        Label notification = new Label();

        userActions.getChildren().add(ready);
        userActions.getChildren().add(startGame);
        userActions.getChildren().add(leaveLobby);

        if (errorOccured) {
            notification.setText("Couldn't start game! Some players are not ready");
            ready.setSelected(true);
            startGame.setVisible(true);
        }
        else startGame.setVisible(false);

        ready.setOnAction(event -> {
            readyHandle(ready.isSelected());
            startGame.setVisible(ready.isSelected() && ConnectionWithServerHandler.isHost());
        });

        startGame.setOnAction(event -> {
            ready.setDisable(true);
            leaveLobby.setOnAction(null);
            ConnectionWithServerHandler.startGame();
        });

        leaveLobby.setOnAction(event -> {
            ConnectionWithServerHandler.leaveLobby();
            showSearchGameScreen(false);
        });

        layout.getChildren().addAll(gameDetails, userActions, notification);
        root.getChildren().add(layout);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    private void readyHandle(boolean selected){
        if (selected) ConnectionWithServerHandler.ready();
        else ConnectionWithServerHandler.notReady();
    }

    public void notifyStartingGame(){
        startGame();
    }

    public void notifyPlayersNotReady(){
        showLobbyScreen(true);
    }

    private void startGame(){

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

    private void showMenuBackground(Region region){
        Image background = new Image("assets/background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT,0.5,true,Side.TOP,0.5,true),
                new BackgroundSize(WINDOW_WIDTH, WINDOW_WIDTH, false, false, false, false));

        region.setBackground(new Background(backgroundImage));
    }

    private void drawLogo(GraphicsContext graphicsContext){
        Image profsLogo = new Image("assets/decorations/login_screen/professors.png");
        Image textLogo = new Image("assets/decorations/login_screen/eriantys_text_logo.png");
        drawImage(graphicsContext,
                profsLogo,
                new Coord(centerX, centerY * 0.1),
                0.6);
        drawImage(graphicsContext,
                textLogo,
                new Coord(centerX, centerY * 0.35),
                0.15);
    }

    private void showDecorativeIslands(GraphicsContext graphicsContext){

        Image island1 = new Image("assets/tiles/island1.png");
        Image island2 = new Image("assets/tiles/island2.png");
        Image island3 = new Image("assets/tiles/island3.png");

        Image cloud = new Image("assets/tiles/cloud_card.png");

        double scale = 0.2;


        drawImage(graphicsContext, cloud, upLeftCorner, scale);
        drawImage(graphicsContext, island1, upLeftCorner, scale);
        drawImage(graphicsContext, cloud, upRightCorner, scale);
        drawImage(graphicsContext, island1, upRightCorner, scale);

        drawImage(graphicsContext, cloud, centerLeft, scale);
        drawImage(graphicsContext, island2, centerLeft, scale);
        drawImage(graphicsContext, cloud, centerRight, scale);
        drawImage(graphicsContext, island2, centerRight, scale);

        drawImage(graphicsContext, cloud, downLeftCorner, scale);
        drawImage(graphicsContext, island3, downLeftCorner, scale);
        drawImage(graphicsContext, cloud, downRightCorner, scale);
        drawImage(graphicsContext, island3, downRightCorner, scale);
    }
}
