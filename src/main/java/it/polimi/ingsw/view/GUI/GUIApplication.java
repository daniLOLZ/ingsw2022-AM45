package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.model.player.PlayerEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.client.InitialConnector;
import it.polimi.ingsw.view.GUI.drawers.*;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
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
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.view.GUI.drawers.IslandGroupDrawer.*;


public class GUIApplication extends Application{

    public static final double WINDOW_WIDTH = 1520, WINDOW_HEIGHT = 780;
    public static final double cloudSize = 40, assistantWidth = 100, islandSize = 120, boardWidth = 505;

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

    private static final double islandsSemiWidth = WINDOW_WIDTH/4, islandsSemiHeight = 7.0/39 * WINDOW_HEIGHT;
    private static final Coord islandsPos = center.pureSumY(-5.0/78 * WINDOW_HEIGHT);
    private static final Coord cloudCenterPos = center.pureSumY(-5.0/78 * WINDOW_HEIGHT);
    private static final Coord
            userBoardPos = downCenter.pureSumY(-145),
            oppositeBoardPos = upCenter.pureSumY(120),
            leftBoardPos = centerLeft.pureSumX(100),
            rightBoardPos = centerRight.pureSumX(-100);

    private static final Coord firstAssistantSlot = downCenter.pureSumX(WINDOW_WIDTH * 0.28).pureSumY(-WINDOW_HEIGHT / 7);

    private static final Coord firstCharacterCardSlot = upCenter.pureSumX(WINDOW_WIDTH / 5).pureSumY(WINDOW_HEIGHT / 8);

    private static final double characterCardWidth = 90, characterCardGap = characterCardWidth * 1.2;

    private static final List<String> availableGameRules = new ArrayList<>(List.of("Normal mode", "Expert mode"));
    private static final List<Integer> availablePlayerNumber = new ArrayList<>(List.of(2, 3, 4));

    private static String preselectedGameRule = availableGameRules.get(0);
    private static Integer preselectedNumPlayers = availablePlayerNumber.get(0);
    private static WizardEnum selectedWizard = WizardEnum.NO_WIZARD;
    private static TeamEnum selectedTowerColor = TeamEnum.NOTEAM;

    private static Stage stage;
    private static InitialConnector initialConnector;
    private static ClientSender defaultSender;

    private static boolean started = false;

    public static boolean isStarted() {
        return started;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = new Stage();

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

        started = true;

    }

    public static void showLoginScreen(boolean loginError) {

        //in case of logout I want to reset the action on close request
        //to not try and send a quit message
        //through a non-existent socket
        stage.setOnCloseRequest(event -> {});

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

        Label loginLabel = new Label("Nickname");
        TextField inputNickname = new TextField();
        Button loginButton = new Button("Login");

        inputNickname.setPromptText("Insert your nickname");
        inputNickname.setMaxWidth(WINDOW_WIDTH/4);
        inputNickname.setOnAction(event -> loginButton.fire());
        Label errorMessage = new Label("Invalid nickname! Please try again");
        errorMessage.setTextFill(Color.RED);

        errorMessage.setVisible(loginError);

        //sends the request to the network
        loginButton.setOnAction(event -> {
            new Thread(() -> {
                initialConnector.login(inputNickname.getText());
                initialConnector.startReceiving();
            }).start();
            loginButton.setDisable(true);
        });

        HBox nicknameSelection = new HBox(10);
        nicknameSelection.getChildren().addAll(loginLabel, inputNickname);
        nicknameSelection.setAlignment(Pos.CENTER);

        login.getChildren().addAll(nicknameSelection, loginButton, errorMessage);

        //<editor-fold desc="Debug">
/*

        Button success = new Button("Simulate success");
        Button failure = new Button("Simulate failure");
        Button debugScene = new Button("Show debug scene");

        success.setOnAction(event -> showSearchGameScreen(false));
        failure.setOnAction(event -> showLoginScreen(true));
        //debugScene.setOnAction(event -> debug_showDebugScene());

        login.getChildren().addAll(success, failure, debugScene);
*/

        //</editor-fold>

        stage.setScene(loginScene);
    }

    //<editor-fold desc="Debugging methods">
    private void debug_showDebugScene(){
        Group root = new Group();

        Group group = new Group();
        root.getChildren().add(group);

        ImageView testImage = drawFromCenterInteractiveImage(new Image("assets/board/no_borders.png"), center, 0.08, HandlingToolbox.NO_EFFECT);

        group.getChildren().add(testImage);

        ImageView tracker = drawFromCenterInteractiveImage(new Image("assets/tiles/blockTile.png"), new Coord(testImage.getX(), testImage.getY()), 0.05, HandlingToolbox.NO_EFFECT);
        group.getChildren().add(tracker);

        HBox options = new HBox(5);
        root.getChildren().add(options);

        Button rotateClockwise = new Button("Rotate clockwise");
        //Button rotateCounterclockwise = new Button("Rotate counterclockwise");
        Button zoom = new Button("Zoom");
        Button shrink = new Button("Shrink");
        Button moveLeft = new Button("Move left");
        //Button moveRight = new Button("Move right");
        Button moveUp = new Button("Move up");
        //Button moveDown = new Button("Move down");
        TextField input = new TextField();

        options.getChildren().addAll(rotateClockwise, zoom, shrink, moveLeft, moveUp, input);

        rotateClockwise.setOnAction(event -> {
            testImage.getTransforms().add(new Rotate(-90, testImage.getX() + testImage.getFitWidth() / 2, testImage.getY() + testImage.getFitHeight() / 2));
            debug_trackImage(testImage, tracker);
        });

        zoom.setOnAction(event -> {
            testImage.setFitWidth(testImage.getFitWidth() * 1.3);
            testImage.setFitHeight(testImage.getFitHeight() * 1.3);
            debug_trackImage(testImage, tracker);
        });

        shrink.setOnAction(event -> {
            testImage.setFitWidth(testImage.getFitWidth() / 1.3);
            testImage.setFitHeight(testImage.getFitHeight() / 1.3);
            debug_trackImage(testImage, tracker);
        });

        moveLeft.setOnAction(event -> {
            testImage.setX(Integer.parseInt(input.getCharacters().toString()));
            debug_trackImage(testImage, tracker);
        });

        moveUp.setOnAction(event -> {
            testImage.setY(Integer.parseInt(input.getCharacters().toString()));
            debug_trackImage(testImage, tracker);
        });

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);

    }

    private void debug_trackImage(ImageView imageView, ImageView tracker){
        tracker.setX(imageView.getX() - tracker.getFitWidth() / 2);
        tracker.setY(imageView.getY() - tracker.getFitHeight() / 2);
    }

    //</editor-fold>


    public static void showSearchGameScreen(boolean searchGameError){

        //user is logged in. If he quits, the server is notified
        stage.setOnCloseRequest(event -> defaultSender.sendQuit());

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
        if (searchGameError) searching.setText("Error! Couldn't find your game");
        else searching.setText("Click here to search a game");
        Button searchGameButton = new Button("Search Game");
        searchGameButton.setOnAction(event -> {
            //game must be searched once
            searchGameButton.setDisable(true);

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

        /*Label debugLabel = new Label("Debugging options");
        Button success = new Button("Simulate game lobby");
        Button failure = new Button("Simulate failure");

        List<String> mockPlayers = new ArrayList<>();
        mockPlayers.add("mock1");
        mockPlayers.add("mock2");
        mockPlayers.add("mock3");
        mockPlayers.add("mock69");

        List<Boolean> mockReady = new ArrayList<>();
        mockReady.add(true);
        mockReady.add(false);
        mockReady.add(false);
        mockReady.add(true);

        LobbyBean lobbyBean = new LobbyBean(mockPlayers, mockReady, false, 3);

        success.setOnAction(event -> showLobbyScreen(lobbyBean,false));
        failure.setOnAction(event -> showSearchGameScreen(true));

        HBox debug = new HBox(20);
        debug.setAlignment(Pos.CENTER);
        debug.getChildren().addAll(success, failure);

        lookingForLobby.getChildren().addAll(debugLabel, debug);
*/

        //</editor-fold>


        stage.setScene(searchGame);
    }

    private static void sendSearchGameRequest(String gameRule, int numPlayers, Label outNotify){

        outNotify.setText("Searching...");
        new Thread(() -> defaultSender.sendGameModePreference(availableGameRules.indexOf(gameRule) + 1, numPlayers)).start();

    }

    public static void showLobbyScreen(LobbyBean data, int userSlot, boolean startGameError, boolean leavingLobbyError){

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

        Label gameDetails = new Label("Game rules: " + "\nPlayers: " + preselectedNumPlayers);
        gameDetails.setFont(Font.font("Verdana", 25));

        VBox players = new VBox(15);
        players.setAlignment(Pos.CENTER);
        for (String nickname:
             data.getNicknames()) {

            int playerIndex = data.getNicknames().indexOf(nickname);

            Label player = new Label(
                    nickname +
                    (playerIndex == data.getHost() ? " [host]" :"") +
                    (data.getReadyPlayers().get(playerIndex) ? "  Ready" : ""));
            players.getChildren().add(player);
        }

        HBox userActions = new HBox(35);
        userActions.setAlignment(Pos.CENTER);

        CheckBox ready = new CheckBox("Ready");
        ready.setSelected(data.getReadyPlayers().get(userSlot));
        Button startGame = new Button("Start game");
        Button leaveLobby = new Button("Leave lobby");
        Label notification = new Label();

        userActions.getChildren().add(ready);
        userActions.getChildren().add(startGame);
        userActions.getChildren().add(leaveLobby);

        if (startGameError) {
            notification.setText("Couldn't start game! Some players are not ready");
            ready.setSelected(data.getReadyPlayers().get(userSlot));
            startGame.setVisible(true);
        }

        else if (leavingLobbyError){
            notification.setText("Couldn't leave lobby! Try again");
            ready.setSelected(false);
            startGame.setVisible(false);
        }

        else startGame.setVisible(ready.isSelected() && data.getHost() == userSlot);

        ready.setOnAction(event -> {
            readyHandle(ready.isSelected());
            startGame.setVisible(ready.isSelected() && data.getHost() == userSlot);
        });

        startGame.setOnAction(event -> {
            startGame.setDisable(true);
            ready.setDisable(true);
            leaveLobby.setDisable(true);
            defaultSender.startGame();
        });

        leaveLobby.setOnAction(event -> defaultSender.leaveLobby());

        //<editor-fold desc="Debug">

        /*Button success = new Button("Simulate start game");
        Button failure = new Button("Simulate failure");

        success.setOnAction(event -> showWizardSelection(false));
        //failure.setOnAction(event -> showLobbyScreen(true));

        layout.getChildren().addAll(success,failure);
        */
        //</editor-fold>

        layout.getChildren().addAll(gameDetails, players, userActions, notification);
        root.getChildren().add(layout);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    private static void readyHandle(boolean selected){
        new Thread(() -> defaultSender.sendReadyStatus(selected)).start();
    }

    public static void showTowerColorSelection(GameInitBean data, boolean colorSelectionError){
        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        DecorationsDrawer.showMenuBackground(root);

        //</editor-fold>

        VBox layout = new VBox(35);
        layout.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);

        Label title = new Label("Select your Tower color!");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Lucida Handwriting", 40));
        title.setTextFill(Color.DARKRED);
        layout.getChildren().add(title);

        int numTeams = TeamEnum.getNumTeams() - 1;
        if(preselectedNumPlayers == 3) numTeams = TeamEnum.getNumTeams();

        HBox towers = new HBox(20.0 * TeamEnum.getNumTeams() / numTeams);
        towers.setAlignment(Pos.CENTER);
        towers.setMinHeight(TowerDrawer.getTowerSize() * 0.6);
        layout.getChildren().add(towers);

        Button selectTower = new Button("These words shall not be gazed upon by thy mortals!");
        selectTower.setVisible(false);
        selectTower.setBackground(Background.EMPTY);
        selectTower.setFont(Font.font("Lucida Handwriting", 40));
        selectTower.setTextFill(Color.DARKRED);
        selectTower.setOnAction(event -> new Thread(() -> defaultSender.sendTeamColorChoice(selectedTowerColor)).start());
        layout.getChildren().add(selectTower);

        Label errorMessage = new Label("Error! This color has already been selected");
        errorMessage.setVisible(colorSelectionError);
        layout.getChildren().add(errorMessage);

        for (int index = 0; index < numTeams; index++) {

            TeamEnum tower = TeamEnum.getTeamFromId(index);

            ImageView towerView = TowerDrawer.drawTower(tower, center, 0.5);

            if (!data.getChosenColors().contains(tower)){
                Lighting lighting = new Lighting();
                lighting.setDiffuseConstant(1.0);
                lighting.setSpecularConstant(0.0);
                lighting.setSpecularExponent(0.0);
                lighting.setSurfaceScale(0.0);
                lighting.setLight(new Light.Distant(45, 45, Color.GREY));

                towerView.setEffect(lighting);
            }

            else {
                towerView.setOnMouseClicked(event -> {
                    selectedTowerColor = tower;
                    selectTower.setText("Select " + tower.name);
                    selectTower.setVisible(true);
                });
                addHoveringEffects(towerView, new Coord(towerView.getX() + towerView.getFitWidth(), towerView.getY() + towerView.getFitHeight()), 0.5, HandlingToolbox.NO_EFFECT, HandlingToolbox.NO_EFFECT, 1.1, false);
            }
            towers.getChildren().add(towerView);
        }



        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    public static void showWizardSelection(GameInitBean data, boolean errorOccurred){
        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        DecorationsDrawer.showMenuBackground(root);

        //</editor-fold>

        VBox layout = new VBox(35);
        layout.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);

        Label title = new Label("Select your Wizard companion!");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Lucida Handwriting", 40));
        title.setTextFill(Color.DARKRED);
        layout.getChildren().add(title);

        HBox wizards = new HBox(20);
        wizards.setAlignment(Pos.CENTER);
        wizards.setMinHeight(WizardDrawer.getWizardHeight() * 0.5 * 1.2);
        layout.getChildren().add(wizards);

        Button selectWizard = new Button("vgnkdhdrnhs");
        selectWizard.setVisible(false);
        selectWizard.setBackground(Background.EMPTY);
        selectWizard.setFont(Font.font("Lucida Handwriting", 40));
        selectWizard.setTextFill(Color.DARKRED);
        selectWizard.setOnAction(event -> new Thread(() -> defaultSender.sendWizardChoice(selectedWizard)).start());
        layout.getChildren().add(selectWizard);

        for (WizardEnum wizard:
                WizardEnum.getWizards()) {
            ImageView wizardView = WizardDrawer.drawWizard(wizard, center, 0.5);

            if (!data.getChosenWizards().contains(wizard)){

                Lighting lighting = new Lighting();
                lighting.setDiffuseConstant(1.0);
                lighting.setSpecularConstant(0.0);
                lighting.setSpecularExponent(0.0);
                lighting.setSurfaceScale(0.0);
                lighting.setLight(new Light.Distant(45, 45, Color.GREY));

                wizardView.setEffect(lighting);
            }

            else {
                wizardView.setOnMouseClicked(event -> {
                    selectedWizard = wizard;
                    selectWizard.setText("Select " + wizard.name);
                    selectWizard.setVisible(true);
                });
                addHoveringEffects(wizardView, new Coord(wizardView.getX() + wizardView.getFitWidth(), wizardView.getY() + wizardView.getFitHeight()), 0.5, HandlingToolbox.NO_EFFECT, HandlingToolbox.NO_EFFECT, 1.1, false);

            }

            wizards.getChildren().add(wizardView);
        }



        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    public static void showGameInterface(VirtualViewBean data, GameToolBoxContainer eventHandlerContainer, int user){
        Group root = new Group();

        //<editor-fold desc="Islands">

        List<AdvancedIslandGroupBean> advancedIslandsList = data.getAdvancedIslandGroupBeans();

        int islandIndex = 0;

        if (advancedIslandsList == null) {
            List<IslandGroupBean> islandsList = data.getIslandGroupBeans();
            Iterator<IslandGroupBean> island = islandsList.iterator();

            for (Coord slot: Objects.requireNonNull(getIslandGroupSlots(islandsList.size(), islandsSemiWidth, islandsSemiHeight, islandsPos))) {
                root.getChildren().addAll(IslandGroupDrawer.drawIslandGroup(island.next(), slot, islandSize / IslandDrawer.getIslandSize(), eventHandlerContainer.getIslandHandlingToolbox().getOnIslandClick(islandIndex)));
                islandIndex++;
            }
        } else {
            Iterator<AdvancedIslandGroupBean> island = advancedIslandsList.iterator();

            for (Coord slot: Objects.requireNonNull(getIslandGroupSlots(advancedIslandsList.size(), islandsSemiWidth, islandsSemiHeight, islandsPos))) {
                root.getChildren().addAll(IslandGroupDrawer.drawIslandGroup(island.next(), slot, islandSize / IslandDrawer.getIslandSize(), eventHandlerContainer.getIslandHandlingToolbox().getOnIslandClick(islandIndex)));
                islandIndex++;
            }
        }


        //</editor-fold>

        //<editor-fold desc="Clouds">

        List<CloudBean> cloudsList = data.getCloudBeans();
        Iterator<CloudBean> cloud = cloudsList.iterator();

        for (Coord slot:
             getCloudsSlots(cloudsList.size(), cloudCenterPos)) {

            CloudBean cloudBean = cloud.next();

            root.getChildren().addAll(CloudDrawer.drawCloud(cloudBean, slot, cloudSize / CloudDrawer.getCloudSize(), eventHandlerContainer.getCloudHandlingToolbox().getOnCloudClick(cloudBean.getIdCloud() - 1)));
        }

        //</editor-fold>

        //<editor-fold desc="Game Boards">

        PlayerBean userBean;

        List<AdvancedPlayerBean> advancedPlayers = data.getAdvancedPlayerBeans();

        if (advancedPlayers != null){

            userBean = advancedPlayers.get(user);

            int numPlayers = advancedPlayers.size();

            List<Coord> boardSlots = getBoardSlots(numPlayers);
            List<Integer> boardRotations = getBoardRotation(numPlayers);

            root.getChildren().addAll(BoardDrawer.drawBoard(advancedPlayers.get(user), userBoardPos, boardWidth / BoardDrawer.getBoardWidth(), eventHandlerContainer.getBoardHandlingToolbox()));

            for (int board = 1; board < numPlayers; board++) {

                int absIndex = (user + board) % boardSlots.size();
                root.getChildren().addAll(BoardDrawer.drawBoard(advancedPlayers.get(absIndex), boardSlots.get(absIndex), 0.5 * boardWidth / BoardDrawer.getBoardWidth(), boardRotations.get(absIndex)));
            }
        }

        else {

            List<PlayerBean> players = data.getPlayerBeans();

            userBean = players.get(user);

            int numPlayers = players.size();

            List<Coord> boardSlots = getBoardSlots(numPlayers);
            List<Integer> boardRotations = getBoardRotation(numPlayers);

            root.getChildren().addAll(BoardDrawer.drawBoard(players.get(user), userBoardPos, boardWidth / BoardDrawer.getBoardWidth(), eventHandlerContainer.getBoardHandlingToolbox()));

            for (int board = 1; board < numPlayers; board++) {

                int absIndex = (user + board) % boardSlots.size();
                root.getChildren().addAll(BoardDrawer.drawBoard(players.get(absIndex), boardSlots.get(absIndex), 0.5 * boardWidth / BoardDrawer.getBoardWidth(), boardRotations.get(absIndex)));
            }
        }

        int assistantIndex = 0;

        for (Integer assistant: userBean.getIdAssistants()) {
            Coord slot = firstAssistantSlot.pureSumX(assistantIndex * assistantWidth * 0.28 * 10 / userBean.getIdAssistants().size());
            ImageView assistantView = AssistantDrawer.drawAssistant(assistant, slot,assistantWidth / AssistantDrawer.getAssistantWidth(), eventHandlerContainer.getAssistantHandlingToolbox().getOnAssistantClick(assistantIndex));
            addHoveringEffects(assistantView, slot, assistantWidth / AssistantDrawer.getAssistantWidth(), HandlingToolbox.NO_EFFECT, HandlingToolbox.NO_EFFECT, 1.7, false);
            root.getChildren().add(assistantView);
            assistantIndex++;
        }

        //</editor-fold>

        //<editor-fold desc="Character Cards">

        List<CharacterCardBean> characters = data.getCharacterCardBeans();

        if (characters != null && !characters.isEmpty()) {
            int numCharacter = 0;

            for (CharacterCardBean character:
                 characters) {

                root.getChildren().addAll(CharacterCardDrawer.drawCharacterCard(character, firstCharacterCardSlot.pureSumX(characterCardGap * numCharacter),characterCardWidth / CharacterCardDrawer.getCharacterCardWidth(), eventHandlerContainer.getCharacterCardHandlingToolboxes().get(numCharacter)));
                numCharacter++;
            }
        }

        //</editor-fold>

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.STEELBLUE);
        stage.setScene(scene);
    }

    public static void showNetworkError(){
        AlertBox.display("Error", "A network error occurred");
    }

    private static List<Coord> getBoardSlots(int amount){
        List<Coord> slots = new ArrayList<>();

        if (amount > 2) slots.add(rightBoardPos);
        if (amount == 2 || amount == 4) slots.add(oppositeBoardPos);
        if (amount > 2) slots.add(leftBoardPos);

        return slots;
    }

    private static List<Integer> getBoardRotation(int total){

        List<Integer> rotations = new ArrayList<>();

        if (total > 2) rotations.add(BoardDrawer.RIGHT);
        if (total == 2 || total == 4) rotations.add(BoardDrawer.TOP);
        if (total > 2) rotations.add(BoardDrawer.LEFT);

        return rotations;
    }

    private static List<Coord> getIslandGroupSlots(int amount, double semiWidth, double semiHeight, Coord centerPos){
        if (amount < 3) return null;

        List<Coord> slots = new ArrayList<>();
        List<Coord> toAddLater = new ArrayList<>();

        double xPos = -semiWidth;

        for (int line = 0; line < amount / 2 + 1; line++){

            double yPos = getIslandGroupY(Math.min(xPos, semiWidth), semiWidth, semiHeight);

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

    private static double getIslandGroupY(double x, double semiWidth, double semiHeight){

        return semiHeight * Math.sqrt(1 - Math.pow(x, 2) / Math.pow(semiWidth, 2));
    }

    private static List<Coord> getCloudsSlots(int amount, Coord center){

        List<Coord> slots = new ArrayList<>();

        Coord startingPos = center.pureSumX(-2 * cloudSize / 3 * (amount - 1));
        double fluctuation = cloudSize / 5;

        for (int slot = 0; slot < amount; slot++){
            slots.add(startingPos.pureSumX(slot * 4 * cloudSize / 3).pureSumY(fluctuation));
            fluctuation = - fluctuation;
        }

        return slots;
    }

    public static void setInitialConnector(InitialConnector initialConnector){
        GUIApplication.initialConnector = initialConnector;
    }

    public static void setDefaultSender(ClientSender defaultSender) {
        GUIApplication.defaultSender = defaultSender;
    }
}
