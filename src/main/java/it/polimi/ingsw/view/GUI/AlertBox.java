package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.GUI.drawers.BlockTileDrawer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {

    private static final double alertBoxWidth = 210;
    private static final double alertBoxHeight = 60;
    private static final double alertBoxGap = 20;

    /**
     * Displays the AlertBox on screen
     * @param title The title shown on the AlertBox
     * @param message The message shown on the AlertBox
     */
    public static void display(String title, String message){

        Stage alertBox = new Stage();

        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setTitle(title);
        alertBox.setMinWidth(alertBoxWidth);
        alertBox.setMinHeight(alertBoxHeight);
        alertBox.getIcons().add(new Image("assets/icon.png"));

        Label boxMessage = new Label(message);
        boxMessage.setAlignment(Pos.CENTER_RIGHT);

        Button closeButton = new Button("Ok");
        closeButton.setOnAction(event -> alertBox.close());

        ImageView block = new ImageView();

        block = BlockTileDrawer.drawBlockTile(new Coord(block.getX(), block.getY()), alertBoxHeight / 2 / BlockTileDrawer.getBlockTileSize());

        GridPane layout = new GridPane();

        layout.setVgap(alertBoxGap);
        layout.setHgap(alertBoxGap * 2);
        layout.setPadding(new Insets(alertBoxGap, alertBoxGap, alertBoxGap, alertBoxGap));
        layout.setAlignment(Pos.CENTER);

        layout.add(block, 0, 0);
        layout.add(boxMessage, 1, 0);
        layout.add(closeButton, 1, 1);

        Node dummy = new Label("dummy");
        dummy.setVisible(false);
        layout.add(dummy, 0, 1);


        Scene scene = new Scene(layout);
        alertBox.setScene(scene);
        alertBox.showAndWait();
    }
}
