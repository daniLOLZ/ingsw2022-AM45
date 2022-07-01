package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.model.WizardEnum;
import it.polimi.ingsw.model.beans.*;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.client.InitialConnector;
import it.polimi.ingsw.view.GUI.drawers.*;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import it.polimi.ingsw.view.GameInitBean;
import it.polimi.ingsw.view.LobbyBean;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static it.polimi.ingsw.view.GUI.drawers.IslandGroupDrawer.*;


public class GUIApplication extends Application{

    public static final double WINDOW_WIDTH, WINDOW_HEIGHT;

    static {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        WINDOW_WIDTH = gd.getDisplayMode().getWidth() / 1.5;
        WINDOW_HEIGHT =  gd.getDisplayMode().getHeight() / 1.5;
    }
    public static final double cloudSize = 40.0/1520 * WINDOW_WIDTH, assistantWidth = 5.0/76 * WINDOW_WIDTH, islandSize = 2.0 / 13 * WINDOW_HEIGHT, boardWidth = 101.0 / 304 * WINDOW_WIDTH;

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
            userBoardPos = downCenter.pureSumY(-29.0 / 156 * WINDOW_HEIGHT),
            oppositeBoardPos = upCenter.pureSumY(7.0 / 78 * WINDOW_HEIGHT),
            leftBoardPos = centerLeft.pureSumX(5.0 / 76 * WINDOW_WIDTH),
            rightBoardPos = centerRight.pureSumX(-5.0 / 76 * WINDOW_WIDTH);

    private static final Coord firstAssistantSlot = downCenter.pureSumX(WINDOW_WIDTH * 0.28).pureSumY(-WINDOW_HEIGHT / 7);

    private static final Coord firstCharacterCardSlot = upCenter.pureSumX(WINDOW_WIDTH / 5).pureSumY(WINDOW_HEIGHT / 8);

    private static final double characterCardWidth = 9.0 / 152 * WINDOW_WIDTH, characterCardGap = characterCardWidth * 1.2;
    private static final double gameBoardCoinsSize = 5.0 / 304 * WINDOW_WIDTH;

    private static final List<String> availableGameRules = new ArrayList<>(List.of("Normal mode", "Expert mode"));
    private static final List<Integer> availablePlayerNumber = new ArrayList<>(List.of(2, 3, 4));
    private static final Coord gameBoardCoinsSlot = islandsPos.pureSumX(- islandsSemiWidth / 2);

    private static String preselectedGameRule = availableGameRules.get(0);
    private static Integer preselectedNumPlayers = availablePlayerNumber.get(0);
    private static WizardEnum selectedWizard = WizardEnum.NO_WIZARD;
    private static TeamEnum selectedTowerColor = TeamEnum.NOTEAM;

    private static int
            maxIslandsRequired = 0,
            maxStudentsOnCardRequired = 0,
            maxStudentsAtEntranceRequired = 0,
            maxColorsRequired = 0;

    private static boolean
            islandRequired = false,
            studentOnCardRequired = false,
            studentAtEntranceRequired = false,
            colorRequired = false,

            characterCardRequirementsInSelection = false;

    private static Stage stage;
    private static InitialConnector initialConnector;
    private static ClientSender defaultSender;

    private static boolean started = false;

    public static boolean isStarted() {
        return started;
    }

    /**
     * Launches the application and displays the welcome screen.
     * @param primaryStage The stage of the application
     * @throws Exception for any error that may occur
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = new Stage();

        Image icon = new Image("assets/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Eriantys");
        stage.setX(0);
        stage.setY(0);

        VBox root = new VBox(7.0 / 78 * WINDOW_HEIGHT);
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

        welcomeMessage.setFont(Font.font("Pristina",47.0 / 780 * WINDOW_HEIGHT));
        welcomeMessage.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(welcomeMessage);

        playButton.setMaxWidth(35.0 / 76 * WINDOW_WIDTH);
        playButton.setMinHeight(2.0 / 13 * WINDOW_HEIGHT);
        playButton.setBackground(Background.EMPTY);
        playButton.setTextFill(Color.DARKRED);
        playButton.setOnMouseEntered(event -> playButton.setFont(Font.font("Lucida Handwriting", 5.0 / 78 * WINDOW_HEIGHT)));
        playButton.setOnMouseExited(event -> playButton.setFont(Font.font("Lucida Handwriting", 2.0 / 39 * WINDOW_HEIGHT)));
        playButton.setFont(Font.font("Lucida Handwriting", 2.0 / 39 * WINDOW_HEIGHT));

        //</editor-fold>

        root.getChildren().add(playButton);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();

        started = true;

        stage.setOnCloseRequest(event -> System.exit(0));

    }

    public static void showLoginScreen(boolean loginError) {

        //in case of logout I want to reset the action on close request
        //to not try and send a quit message
        //through a non-existent socket
        stage.setOnCloseRequest(event -> System.exit(0));

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

        VBox login = new VBox(WINDOW_HEIGHT / 39);
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
            loginButton.setDisable(true);
            new Thread(() -> {
                initialConnector.login(inputNickname.getText());
                initialConnector.startReceiving();
                initialConnector.reset(); // When the receiving stops we need to reset the connector
            }).start();
        });

        HBox nicknameSelection = new HBox(WINDOW_WIDTH / 152);
        nicknameSelection.getChildren().addAll(loginLabel, inputNickname);
        nicknameSelection.setAlignment(Pos.CENTER);

        login.getChildren().addAll(nicknameSelection, loginButton, errorMessage);

        stage.setScene(loginScene);
    }

    /**
     * Displays the game rule selection screen to allow user to look for a lobby to join
     * @param searchGameError true if an error previously occurred
     */
    public static void showSearchGameScreen(boolean searchGameError){

        //user is logged in. If he quits, the server is notified
        stage.setOnCloseRequest(event -> {
            defaultSender.sendQuit();
            System.exit(0);
        });

        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        DecorationsDrawer.showMenuBackground(root);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        DecorationsDrawer.drawLogo(graphicsContext);
        DecorationsDrawer.showDecorativeIslands(graphicsContext);

        root.getChildren().add(canvas);

        //</editor-fold>

        VBox lookingForLobby = new VBox(WINDOW_HEIGHT / 26);
        lookingForLobby.setAlignment(Pos.CENTER);
        lookingForLobby.setPadding(new Insets(5.0 / 39 * WINDOW_HEIGHT, 0, 0, 0));

        GridPane preferences = new GridPane();
        preferences.setAlignment(Pos.CENTER);
        preferences.setPadding(new Insets(WINDOW_HEIGHT / 39,WINDOW_WIDTH / 76,WINDOW_HEIGHT / 39,WINDOW_WIDTH / 76));
        preferences.setHgap(3.0 / 304 * WINDOW_WIDTH);
        preferences.setVgap(5.0 / 78 * WINDOW_HEIGHT);

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

        HBox bottomBar = new HBox(13.0 / 1520 * WINDOW_WIDTH);
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

        stage.setScene(searchGame);
    }

    /**
     * Sends a request to the server asking to join a lobby.
     * @param gameRule The specified game rules.
     * @param numPlayers The desired number of players
     * @param outNotify The Label which will display that a game is been looked for
     */
    private static void sendSearchGameRequest(String gameRule, int numPlayers, Label outNotify){

        outNotify.setText("Searching...");
        new Thread(() -> defaultSender.sendGameModePreference(availableGameRules.indexOf(gameRule) + 1, numPlayers)).start();

    }

    /**
     * Displays the lobby screen, allowing the player to set himself as ready and or start the game (if host).
     * @param data The Bean containing all relevant information about the lobby
     * @param userSlot The index of the user in the Players' list
     * @param startGameError true if a game previously failed to start
     * @param leavingLobbyError true if a previous attempt to leave the lobby failed
     */
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

        VBox layout = new VBox(WINDOW_HEIGHT / 39);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5.0 / 39 * WINDOW_HEIGHT,0,0,0));

        Label gameDetails = new Label("Game rules: " + "\nPlayers: " + preselectedNumPlayers);
        gameDetails.setFont(Font.font("Verdana", 25));

        VBox players = new VBox(WINDOW_HEIGHT / 52);
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

        HBox userActions = new HBox(7.0 / 304 * WINDOW_WIDTH);
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

        layout.getChildren().addAll(gameDetails, players, userActions, notification);
        root.getChildren().add(layout);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Sends a message to the server, notifying the user's ready status.
     * @param selected true if the user is ready to start
     */
    private static void readyHandle(boolean selected){
        new Thread(() -> defaultSender.sendReadyStatus(selected)).start();
    }

    /**
     * Displays the Tower selection screen.
     * @param data The Bean containing the available and non-available Towers
     * @param colorSelectionError true if the previous attempt to select a Tower failed
     */
    public static void showTowerColorSelection(GameInitBean data, boolean colorSelectionError){
        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        DecorationsDrawer.showMenuBackground(root);

        //</editor-fold>

        VBox layout = new VBox(7.0 / 156 * WINDOW_HEIGHT);
        layout.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);

        Label title = new Label("Select your Tower color!");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Lucida Handwriting", 2.0 / 39 * WINDOW_HEIGHT));
        title.setTextFill(Color.DARKRED);
        layout.getChildren().add(title);

        int numTeams = TeamEnum.getNumTeams() - 1;
        if(preselectedNumPlayers == 3) numTeams = TeamEnum.getNumTeams();

        HBox towers = new HBox(WINDOW_WIDTH / 76 * TeamEnum.getNumTeams() / numTeams);
        towers.setAlignment(Pos.CENTER);
        towers.setMinHeight(TowerDrawer.getTowerSize() * WINDOW_HEIGHT / 1300);
        layout.getChildren().add(towers);

        Button selectTower = new Button("These words shall not be gazed upon by thy mortals!");
        selectTower.setVisible(false);
        selectTower.setBackground(Background.EMPTY);
        selectTower.setFont(Font.font("Lucida Handwriting", 2.0 / 39 * WINDOW_HEIGHT));
        selectTower.setTextFill(Color.DARKRED);
        selectTower.setOnAction(event -> new Thread(() -> {
            defaultSender.sendTeamColorChoice(selectedTowerColor);
            selectedTowerColor = TeamEnum.NOTEAM;
        }).start());
        layout.getChildren().add(selectTower);

        Label errorMessage = new Label("Error! This color has already been selected");
        errorMessage.setVisible(colorSelectionError);
        layout.getChildren().add(errorMessage);

        for (int index = 0; index < numTeams; index++) {

            TeamEnum tower = TeamEnum.getTeamFromId(index);

            ImageView towerView = TowerDrawer.drawTower(tower, center, WINDOW_HEIGHT / 1560);

            if (data != null) {
                if (!data.getChosenColors().contains(tower)){
                    Drawer.addLighting(towerView, Color.RED);
                }

                else {
                    towerView.setOnMouseClicked(event -> {
                        selectedTowerColor = tower;
                        selectTower.setText("Select " + tower.name);
                        selectTower.setVisible(true);
                    });
                    addHoveringEffects(towerView, new Coord(towerView.getX() + towerView.getFitWidth(), towerView.getY() + towerView.getFitHeight()), WINDOW_HEIGHT / 1560, HandlingToolbox.NO_EFFECT, HandlingToolbox.NO_EFFECT, 1.1, false);
                }
            }
            towers.getChildren().add(towerView);
        }



        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    /**
     *
     * @param data The Bean containing all available and non-available Wizards.
     * @param wizardSelectingError true if a previous attempt to select a Wizard failed
     */
    public static void showWizardSelection(GameInitBean data, boolean wizardSelectingError){
        StackPane root = new StackPane();

        //<editor-fold desc="Decorations">

        DecorationsDrawer.showMenuBackground(root);

        //</editor-fold>

        VBox layout = new VBox(7.0 / 156 * WINDOW_HEIGHT);
        layout.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);

        Label title = new Label("Select your Wizard companion!");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Lucida Handwriting", 2.0 / 39 * WINDOW_HEIGHT));
        title.setTextFill(Color.DARKRED);
        layout.getChildren().add(title);

        HBox wizards = new HBox(WINDOW_WIDTH / 76);
        wizards.setAlignment(Pos.CENTER);
        wizards.setMinHeight(WizardDrawer.getWizardHeight() * 3 * WINDOW_HEIGHT / 3950);
        layout.getChildren().add(wizards);

        Button selectWizard = new Button("vgnkdhdrnhs");
        selectWizard.setVisible(false);
        selectWizard.setBackground(Background.EMPTY);
        selectWizard.setFont(Font.font("Lucida Handwriting", 2.0 / 39 * WINDOW_HEIGHT));
        selectWizard.setTextFill(Color.DARKRED);
        selectWizard.setOnAction(event -> new Thread(() -> {
            defaultSender.sendWizardChoice(selectedWizard);
            selectedWizard = WizardEnum.NO_WIZARD;
        }).start());
        layout.getChildren().add(selectWizard);

        for (WizardEnum wizard:
                WizardEnum.getWizards()) {
            ImageView wizardView = WizardDrawer.drawWizard(wizard, center, WINDOW_HEIGHT / 1560);

            if (data != null) {
                if (!data.getChosenWizards().contains(wizard)){

                    Drawer.addLighting(wizardView, Color.RED);
                }

                else {
                    wizardView.setOnMouseClicked(event -> {
                        selectedWizard = wizard;
                        selectWizard.setText("Select " + wizard.name);
                        selectWizard.setVisible(true);
                    });
                    addHoveringEffects(wizardView, new Coord(wizardView.getX() + wizardView.getFitWidth(), wizardView.getY() + wizardView.getFitHeight()), WINDOW_HEIGHT / 1560, HandlingToolbox.NO_EFFECT, HandlingToolbox.NO_EFFECT, 1.1, false);

                }
            }

            wizards.getChildren().add(wizardView);
        }



        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Displays the game interface
     * @param data The Bean containing all the relevant information about the game
     * @param eventHandlerContainer The object containing the non-constant actions to respond to the player input
     * @param user The index of the user in the Player list
     */
    public static void showGameInterface(VirtualViewBean data, GameToolBoxContainer eventHandlerContainer, int user){
        Group root = new Group();

        //<editor-fold desc="Game Board">

        GameBoardBean gameBoard = data.getGameBoardBean();

        if (gameBoard == null){

            AdvancedGameBoardBean advancedGameBoard = data.getAdvancedGameBoardBean();
            gameBoard = advancedGameBoard;

            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.getTransforms().add(new Translate(gameBoardCoinsSlot.x, gameBoardCoinsSlot.y));
            root.getChildren().add(stackPane);

            stackPane.getChildren().add(CoinDrawer.drawCoin(gameBoardCoinsSlot, gameBoardCoinsSize / CoinDrawer.getCoinSize()));

            Text numCoins = new Text(advancedGameBoard.getNumGameCoins().toString());
            stackPane.getChildren().add(numCoins);

        }

        VBox layout = new VBox(WINDOW_HEIGHT / 36);
        layout.setAlignment(Pos.TOP_LEFT);
        root.getChildren().add(layout);

        String currPlayerNickname;

        List<PlayerBean> playerBeans = data.getPlayerBeans();

        Label yourTurn = new Label("It's your turn!");

        if (playerBeans != null){
            currPlayerNickname = playerBeans.get(gameBoard.getCurrentPlayerId()).getNickname();
            yourTurn.setVisible(data.getPlayerBeans().get(user).getNickname().equals(currPlayerNickname));
        }
        else {
            currPlayerNickname = data.getAdvancedPlayerBeans().get(gameBoard.getCurrentPlayerId()).getNickname();
            yourTurn.setVisible(data.getAdvancedPlayerBeans().get(user).getNickname().equals(currPlayerNickname));
        }

        Label currPlayer = new Label("Current Player : " + currPlayerNickname);

        layout.getChildren().add(currPlayer);

        Label turnPhase = new Label("Turn : " + gameBoard.getTurn().toString() + " | Phase : " + gameBoard.getPhase());

        layout.getChildren().addAll(turnPhase, yourTurn);

        //weird helping buttons

        GridPane subLayout = new GridPane();
        subLayout.setPadding(new Insets(WINDOW_HEIGHT / 156, WINDOW_WIDTH / 304, WINDOW_HEIGHT / 156,WINDOW_WIDTH / 304));
        subLayout.setVgap(WINDOW_HEIGHT / 156);
        subLayout.setHgap(WINDOW_WIDTH / 152);
        subLayout.setAlignment(Pos.CENTER);

        layout.getChildren().add(subLayout);

        Button endTurn = new Button("End turn");
        endTurn.setOnAction(eventHandlerContainer.getHelpingToolbox().getOnEndTurnClick());
        if (HandlingToolbox.NO_ACTION.equals(endTurn.getOnAction()))
            endTurn.setVisible(false);

        String tooFewSelectionText = "Select at least one";

        Button sendStudentsOnCardRequirement = new Button("Confirm Students (on card)");
        sendStudentsOnCardRequirement.setVisible(false);
        Label tooFewStudentsOnCard = new Label(tooFewSelectionText);
        tooFewStudentsOnCard.setVisible(false);

        Button sendStudentsAtEntranceRequirement = new Button("Confirm Students (at Entrance)");
        sendStudentsAtEntranceRequirement.setVisible(false);
        Label tooFewStudentsAtEntrance = new Label(tooFewSelectionText);
        tooFewStudentsAtEntrance.setVisible(false);

        Button sendColors = new Button("Confirm Colors");
        sendColors.setVisible(false);
        Label tooFewColors = new Label(tooFewSelectionText);
        tooFewColors.setVisible(false);

        Button playCharacter = new Button("Activate Character effect");
        playCharacter.setVisible(false);
        playCharacter.setOnAction(event -> {
            eventHandlerContainer.getHelpingToolbox().getOnPlayCharacterClick().handle(event);
            characterCardRequirementsInSelection = false;
        });

        subLayout.add(endTurn, 0, 0);
        subLayout.add(playCharacter, 1, 0);
        subLayout.add(sendStudentsOnCardRequirement, 0, 1);
        subLayout.add(tooFewStudentsOnCard, 0, 2);
        subLayout.add(sendStudentsAtEntranceRequirement, 1, 1);
        subLayout.add(tooFewStudentsAtEntrance, 1, 2);
        subLayout.add(sendColors, 2, 1);
        subLayout.add(tooFewColors, 2, 2);

        if (characterCardRequirementsInSelection){

            if (maxStudentsOnCardRequired > 0){
                sendStudentsOnCardRequirement.setVisible(true);
                if (studentOnCardRequired) tooFewStudentsOnCard.setVisible(true);
                sendStudentsOnCardRequirement.setOnAction(event -> {
                    maxStudentsOnCardRequired = 0;
                    eventHandlerContainer.getHelpingToolbox().getOnSendStudentsOnCardRequirementClick().handle(event);
                });
            }

            if (maxStudentsAtEntranceRequired > 0){
                sendStudentsAtEntranceRequirement.setVisible(true);
                if (studentAtEntranceRequired) tooFewStudentsAtEntrance.setVisible(true);
                sendStudentsAtEntranceRequirement.setOnAction(event -> {
                    maxStudentsAtEntranceRequired = 0;
                    eventHandlerContainer.getHelpingToolbox().getOnSendEntranceStudentRequirementsClick().handle(event);
                });
            }

            if (maxColorsRequired > 0){
                sendColors.setVisible(true);
                if (colorRequired) tooFewColors.setVisible(true);
                sendColors.setOnAction(event -> {
                    maxColorsRequired = 0;
                    eventHandlerContainer.getHelpingToolbox().getOnSendStudentColorRequirementClick().handle(event);
                });
            }

            if (maxStudentsOnCardRequired == 0 &&
                maxStudentsAtEntranceRequired == 0 &&
                maxColorsRequired == 0)
                playCharacter.setVisible(true);
        }

        //</editor-fold>

        //<editor-fold desc="Islands">

        List<AdvancedIslandGroupBean> advancedIslandsList = data.getAdvancedIslandGroupBeans();

        int islandIndex = 0;

        if (advancedIslandsList == null) {
            List<IslandGroupBean> islandsList = data.getIslandGroupBeans();
            Iterator<IslandGroupBean> island = islandsList.iterator();

            for (Coord slot: getIslandGroupSlots(islandsList.size(), islandsSemiWidth, islandsSemiHeight, islandsPos)) {
                root.getChildren().addAll(IslandGroupDrawer.drawIslandGroup(island.next(), slot, islandSize / IslandDrawer.getIslandSize(), eventHandlerContainer.getIslandHandlingToolbox().getOnIslandClick(islandIndex)));
                islandIndex++;
            }
        } else {
            Iterator<AdvancedIslandGroupBean> island = advancedIslandsList.iterator();

            for (Coord slot: getIslandGroupSlots(advancedIslandsList.size(), islandsSemiWidth, islandsSemiHeight, islandsPos)) {
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

            root.getChildren().addAll(CloudDrawer.drawCloud(cloudBean, slot, cloudSize / CloudDrawer.getCloudSize(), eventHandlerContainer.getCloudHandlingToolbox().getOnCloudClick(cloudBean.getIdCloud())));
        }

        //</editor-fold>

        //<editor-fold desc="Player Boards">

        PlayerBean userBean;

        List<AdvancedPlayerBean> advancedPlayers = data.getAdvancedPlayerBeans();

        if (advancedPlayers != null){

            userBean = advancedPlayers.get(user);

            int numPlayers = advancedPlayers.size();

            List<Coord> boardSlots = getBoardSlots(numPlayers);
            List<Integer> boardRotations = getBoardRotation(numPlayers);

            root.getChildren().addAll(BoardDrawer.drawBoard(advancedPlayers.get(user), userBoardPos, boardWidth / BoardDrawer.getBoardWidth(), eventHandlerContainer.getBoardHandlingToolbox()));

            for (int board = 1; board < numPlayers; board++) {

                int absIndex = (user + board) % advancedPlayers.size();
                root.getChildren().addAll(BoardDrawer.drawBoard(advancedPlayers.get(absIndex), boardSlots.get(board - 1), 0.5 * boardWidth / BoardDrawer.getBoardWidth(), boardRotations.get(board - 1)));
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

                int absIndex = (user + board) % players.size();
                root.getChildren().addAll(BoardDrawer.drawBoard(players.get(absIndex), boardSlots.get(board - 1), 0.5 * boardWidth / BoardDrawer.getBoardWidth(), boardRotations.get(board - 1)));
            }
        }

        int assistantIndex = 0;

        for (Integer assistant: userBean.getIdAssistants()) {
            Coord slot = firstAssistantSlot.pureSumX(assistantIndex * assistantWidth * 0.28 * 10 / userBean.getIdAssistants().size());
            ImageView assistantView = AssistantDrawer.drawAssistant(assistant, slot,assistantWidth / AssistantDrawer.getAssistantWidth(), eventHandlerContainer.getAssistantHandlingToolbox().getOnAssistantClick(assistant));
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

    /**
     * Shows an AlertBox that notifies a network error occurred.
     */
    public static void showNetworkError(){
        AlertBox.display("Error", "A network error occurred");
    }

    /**
     * Shows an AlertBox that notifies a user disconnected.
     * @param disconnectedUser The nickname of the disconnected user
     */
    public static void showUserDisconnected(String disconnectedUser){
        AlertBox.display("Error", "Game was interrupted because " + disconnectedUser + " left");
    }

    /**
     * Shows the winner.
     * @param winner The winning team
     */
    public static void showWinner(TeamEnum winner){
        AlertBox.display("Temporary", "Team " + winner.name() + " won!");
    }

    /**
     * Returns the slot available for the desired number of player.
     * @param amount The amount of slots needed for all players
     * @return The list containing all available slots
     */
    private static List<Coord> getBoardSlots(int amount){
        List<Coord> slots = new ArrayList<>();

        if (amount > 2) slots.add(rightBoardPos);
        if (amount == 2 || amount == 4) slots.add(oppositeBoardPos);
        if (amount > 2) slots.add(leftBoardPos);

        return slots;
    }

    /**
     * Returns the orientations of the boards for the desired number of player
     * @param total The amount of slots needed for all players
     * @return The list containing all available orientations
     */
    private static List<Integer> getBoardRotation(int total){

        List<Integer> rotations = new ArrayList<>();

        if (total > 2) rotations.add(BoardDrawer.RIGHT);
        if (total == 2 || total > 3) rotations.add(BoardDrawer.TOP);
        if (total > 2) rotations.add(BoardDrawer.LEFT);

        return rotations;
    }

    /**
     * Returns the slots in which the island groups shall be drawn.
     * @param amount The amount of islands groups to draw
     * @param semiWidth The semi-width which the island group slot will extend to
     * @param semiHeight The semi-height which the island group slot will extend to
     * @param centerPos The center of the ideal shape containing all island groups
     * @return The list containing all available slot to draw the islands group
     */
    private static List<Coord> getIslandGroupSlots(int amount, double semiWidth, double semiHeight, Coord centerPos){
        if (amount < 3) return new ArrayList<>();

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

    /**
     * Gets the y position of an island group given its x position and the dimensions of the ideal shape containing all of them.
     * @param x the x position of the island group
     * @param semiWidth The semi-width of the ideal shape containing all island groups
     * @param semiHeight The semi-height of the ideal shape containing all island groups
     * @return The y position of the island group
     */
    private static double getIslandGroupY(double x, double semiWidth, double semiHeight){

        return - semiHeight * Math.sqrt(1 - Math.pow(x, 2) / Math.pow(semiWidth, 2));
    }

    /**
     * Returns the slots for the desired number of clouds.
     * @param amount The amount of clouds to draw
     * @param center The center of the ideal shape containing all the clouds
     * @return The list of all slots available to draw the clouds
     */
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

    /**
     * Sets the Character Card selection requirements
     * @param islandsRequired The number of islands required.
     * @param studentsOnCardRequired The number of students on card required.
     * @param studentsAtEntranceRequired The number of students at entrance required.
     * @param colorsRequired The number of colors required.
     */
    public static void setMaxRequirement(int islandsRequired, int studentsOnCardRequired, int studentsAtEntranceRequired, int colorsRequired){
        maxIslandsRequired = islandsRequired;
        maxStudentsOnCardRequired = studentsOnCardRequired;
        maxStudentsAtEntranceRequired = studentsAtEntranceRequired;
        maxColorsRequired = colorsRequired;
        islandRequired = islandsRequired > 0;
        studentOnCardRequired = studentsOnCardRequired > 0;
        studentAtEntranceRequired = studentsAtEntranceRequired > 0;
        colorRequired = colorsRequired > 0;

        characterCardRequirementsInSelection = true;
    }

    /**
     * Sets the initial connector.
     * @param initialConnector The connector that will be used to log in
     */
    public static void setInitialConnector(InitialConnector initialConnector){
        GUIApplication.initialConnector = initialConnector;
    }

    /**
     * Sets the defaultSender property.
     * @param defaultSender The default sender assigned to the GUI
     */
    public static void setDefaultSender(ClientSender defaultSender) {
        GUIApplication.defaultSender = defaultSender;
    }

    /**
     * Checks if the user is selecting the requirements for a Character Card activation.
     * @return true if requirements are in selection
     */
    public static boolean areCharacterCardRequirementsInSelection() {
        return characterCardRequirementsInSelection;
    }
}
