package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.view.GUI.drawers.BoardDrawer;
import it.polimi.ingsw.view.GUI.drawers.DecorationsDrawer;
import it.polimi.ingsw.view.GUI.drawers.Drawer;
import it.polimi.ingsw.view.GUI.drawers.IslandDrawer;
import it.polimi.ingsw.view.UserInterface;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.GUI.drawers.IslandDrawer.*;


public class GUIApplication extends Application implements UserInterface{

    public static final double WINDOW_WIDTH = 1520, WINDOW_HEIGHT = 780;

    public static final double
            up      = 0,
            left    = 0,
            down    = WINDOW_HEIGHT,
            right   = WINDOW_WIDTH,
            centerX = WINDOW_WIDTH/2,
            centerY = WINDOW_HEIGHT/2;

    public static final Coord
            downCenter      = new Coord(centerX,down),
            upCenter        = new Coord(centerX,up),
            center          = new Coord(centerX,centerY),
            centerLeft      = new Coord(left,centerY),
            centerRight     = new Coord(right,centerY),
            downRightCorner = new Coord(right,down),
            downLeftCorner  = new Coord(left,down),
            upRightCorner   = new Coord(right,up),
            upLeftCorner    = new Coord(left,up);

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

        DecorationsDrawer.showMenuBackground(root);

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

    public void showLoginScreen(boolean errorOccurred) {

        //in case of logout I want to reset the action on close request
        //to not try and send a quit message
        //through a non-existent socket
        stage.setOnCloseRequest(null);

        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        DecorationsDrawer.showMenuBackground(root);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        DecorationsDrawer.drawLogo(graphicsContext);

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

        DecorationsDrawer.showMenuBackground(root);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        DecorationsDrawer.drawLogo(graphicsContext);
        DecorationsDrawer.showDecorativeIslands(graphicsContext);

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

        DecorationsDrawer.showMenuBackground(root);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        DecorationsDrawer.drawLogo(graphicsContext);
        DecorationsDrawer.showDecorativeIslands(graphicsContext);

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

        //<editor-fold desc="Debug">

        Button success = new Button("Simulate start game");
        Button failure = new Button("Simulate failure");

        success.setOnAction(event -> startGame());
        failure.setOnAction(event -> showLobbyScreen(false));

        layout.getChildren().addAll(success,failure);

        //</editor-fold>

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
        Group root = new Group();

        List<Coord> useless = getIslandGroupSlots(12, 4, 1, upLeftCorner);

        //<editor-fold desc="Islands">

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
//        ids.add(3);
//        ids.add(4);
//        ids.add(5);
//        ids.add(6);
//        ids.add(7);
//        ids.add(8);
//        ids.add(9);
//        ids.add(10);

        List<StudentEnum> students = new ArrayList<>();
        students.add(StudentEnum.GREEN);
        students.add(StudentEnum.GREEN);
        students.add(StudentEnum.RED);
        students.add(StudentEnum.YELLOW);
        students.add(StudentEnum.BLUE);
        students.add(StudentEnum.BLUE);
        students.add(StudentEnum.YELLOW);
        students.add(StudentEnum.YELLOW);
        students.add(StudentEnum.GREEN);
        students.add(StudentEnum.YELLOW);
        students.add(StudentEnum.YELLOW);

        AdvancedIslandGroupBean bean = new AdvancedIslandGroupBean(0, ids, students,false, TeamEnum.WHITE, 4);

        for (Coord slot: getIslandGroupSlots(6, 280, 150, center.pureSumY(-90))) {
            IslandDrawer.drawAdvancedIslandGroup(root, bean, slot);
        }
        //</editor-fold>

        //<editor-fold desc="Game Boards">

        List<StudentEnum> atEntrance = new ArrayList<>();
        atEntrance.add(StudentEnum.GREEN);
        atEntrance.add(StudentEnum.YELLOW);
        atEntrance.add(StudentEnum.RED);

        List<Integer> inHall = new ArrayList<>();
        inHall.add(1);
        inHall.add(4);
        inHall.add(3);
        inHall.add(1);
        inHall.add(7);

        List<StudentEnum> professors = new ArrayList<>();
        professors.add(StudentEnum.BLUE);
        professors.add(StudentEnum.RED);

        List<Integer> assistants = new ArrayList<>();

        PlayerBean board = new PlayerBean("mock", PlayerEnum.PLAYER1, true, TeamEnum.BLACK, 5, atEntrance, inHall, professors, assistants);

        BoardDrawer.drawBoard(root, board, downCenter.pureSumY(-155));

        //</editor-fold>

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
    }

    private List<Coord> getIslandGroupSlots(int amount, double semiWidth, double semiHeight, Coord centerPos){
        if (amount < 3) return null;

        List<Coord> slots = new ArrayList<>();
        List<Coord> toAddLater = new ArrayList<>();

        double xPos = -semiWidth;

        for (int line = 0; line < amount / 2 + 1; line++){

            double yPos = getIslandGroupY(xPos, semiWidth, semiHeight);

            slots.add(centerPos.pureSumX(xPos).pureSumY(yPos));

            if (line != 0 && (amount % 2 == 1 || line != amount / 2)) toAddLater.add(centerPos.pureSumX(xPos).pureSumY(-yPos));

            xPos = xPos + 4 * semiWidth / amount;
        }

        while (toAddLater.size() != 0){
            Coord toAdd = toAddLater.remove(toAddLater.size() - 1);
            slots.add(toAdd);
        }

        return slots;
    }

    private double getIslandGroupY(double x, double semiWidth, double semiHeight){

        return semiHeight * Math.sqrt(1 - Math.pow(x, 2) / Math.pow(semiWidth, 2));
    }

    @Override
    public void addBean(GameElementBean bean) {

    }

    @Override
    public GameElementBean removeBean(int index) {
        return null;
    }

    @Override
    public void clearBeans() {

    }

    @Override
    public void addCommand(CommandEnum command) {

    }

    @Override
    public CommandEnum removeCommand(int index) {
        return null;
    }

    @Override
    public void clearCommands() {

    }

    @Override
    public void showWelcomeScreen() {

    }

    @Override
    public void showLoginScreen() {
        showLoginScreen(false);
    }

    @Override
    public void showGameruleSelection() {

    }

    @Override
    public void showLobby() {

    }

    @Override
    public void showTowerAndWizardSelection() {

    }

    @Override
    public void showGameInterface() {

    }

    @Override
    public void startInterface() {

    }
}
