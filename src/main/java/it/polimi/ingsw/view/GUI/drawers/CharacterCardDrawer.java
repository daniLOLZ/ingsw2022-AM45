package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.CharacterCardBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.CharacterCardHandlingToolbox;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static it.polimi.ingsw.view.GUI.GUIApplication.upLeftCorner;

public class CharacterCardDrawer extends Drawer{

    private static final List<Image> characterCards = new ArrayList<>();

    static {
        characterCards.add(new Image("assets/characters/priest.jpg"));
        characterCards.add(new Image("assets/characters/glutton.jpg"));
        characterCards.add(new Image("assets/characters/flagbearer.jpg"));
        characterCards.add(new Image("assets/characters/mailman.jpg"));
        characterCards.add(new Image("assets/characters/herbalist.jpg"));
        characterCards.add(new Image("assets/characters/centaur.jpg"));
        characterCards.add(new Image("assets/characters/juggler.jpg"));
        characterCards.add(new Image("assets/characters/knight.jpg"));
        characterCards.add(new Image("assets/characters/fungalmancer.jpg"));
        characterCards.add(new Image("assets/characters/minstrel.jpg"));
        characterCards.add(new Image("assets/characters/dame.jpg"));
        characterCards.add(new Image("assets/characters/loanshark.jpg"));
    }

    private static final int firstCharacterId = 1;

    private static final double characterCardWidth = characterCards.get(0).getWidth(),
                                characterCardHeight = characterCards.get(0).getHeight(),
                                hoverZoom = 1.7,
                                childrenSize = 200;

    private static final List<Coord> studentsSlots = new ArrayList<>();

    static {
        studentsSlots.add(upLeftCorner.pureSumX(characterCardWidth / 10).pureSumY(-3 * characterCardHeight / 8));
        studentsSlots.add(upLeftCorner.pureSumX(-2 * characterCardWidth / 5).pureSumY(-characterCardHeight / 8));
        studentsSlots.add(upLeftCorner.pureSumX(3 * characterCardWidth / 10));
        studentsSlots.add(upLeftCorner.pureSumX(-characterCardWidth / 10).pureSumY(3 * characterCardHeight / 8));
        studentsSlots.add(upLeftCorner.pureSumX(-3 * characterCardWidth / 10).pureSumY(characterCardHeight / 6));
        studentsSlots.add(upLeftCorner.pureSumX(2 * characterCardWidth / 5).pureSumY(0.2917 * characterCardHeight)); //oddly specific number but the 6 students on the same character card is a little overcrowded
    }

    private static final List<Coord> blockTileSlots = new ArrayList<>();

    static {
        blockTileSlots.add(upLeftCorner.pureSumX(childrenSize / 2 - 0.93 * characterCardWidth / 2));
        blockTileSlots.add(upLeftCorner.pureSumX(0.88 * characterCardWidth / 2 - childrenSize / 2));
        blockTileSlots.add(upLeftCorner.pureSumX(childrenSize / 2 - 0.86 * characterCardWidth / 2).pureSumY(characterCardHeight /2 - childrenSize));
        blockTileSlots.add(upLeftCorner.pureSumX(0.94 * characterCardWidth / 2 - childrenSize / 2).pureSumY(characterCardHeight / 2 - childrenSize));
    }

    /**
     * Draws a character card with the give parameters.
     * @param data The Bean containing all relevant information about the character card to draw
     * @param pos The position in which the character card should be drawn
     * @param scale The scaling factor to apply to the view
     * @param eventHandlers The Object that contains all the Handlers to respond to the user's actions
     * @return The list of all nodes drawn
     */
    public static List<Node> drawCharacterCard(CharacterCardBean data, Coord pos, double scale, CharacterCardHandlingToolbox eventHandlers){

        List<Node> toDraw = new ArrayList<>();

        ImageView characterView = drawFromCenterInteractiveImage(characterCards.get(data.getId() - firstCharacterId), pos, scale, eventHandlers.getOnCharacterCardClick());

        toDraw.add(characterView);

        Text description = new Text(data.getDescription());
        Rectangle descBox = new Rectangle();
        descBox.setVisible(false);
        description.setVisible(false);

        toDraw.add(descBox);
        toDraw.add(description);

        description.setFont(Font.font(description.getFont().getName(), 110 * scale));

        descBox.setFill(Color.BURLYWOOD);

        //draw students on card

        List<ImageView> studentViews = new ArrayList<>();

        if (data.getStudents() != null) {

            int studentIndex = 0;
            for (StudentEnum student:
                    data.getStudents()) {

                EventHandler<MouseEvent> onStudentClick = eventHandlers.getOnStudentOnCardClick(studentIndex);

                ImageView studentView = StudentDrawer.drawStudent(
                        student,
                        pos
                                .pureSumX(studentsSlots.get(studentIndex).x * scale)
                                .pureSumY(studentsSlots.get(studentIndex).y * scale),
                        scale * childrenSize / StudentDrawer.getStudentSize(),
                        onStudentClick);

                if (onStudentClick == HandlingToolbox.NO_EFFECT) addLighting(studentView, Color.GRAY);

                studentView.setOnMouseClicked(eventHandlers.getOnStudentOnCardClick(studentIndex));

                studentViews.add(studentView);

                studentIndex++;
            }
        }


        toDraw.addAll(studentViews);

        List<ImageView> blockTileViews = new ArrayList<>();

        //draw block tiles
        for (int blockTile = 0; blockTile < Optional.ofNullable(data.getNumBlocks()).orElse(0); blockTile++) {

            blockTileViews.add(BlockTileDrawer.drawBlockTile(
                    pos
                            .pureSumX(blockTileSlots.get(blockTile).x * scale)
                            .pureSumY(blockTileSlots.get(blockTile).y * scale),
                    childrenSize * scale / BlockTileDrawer.getBlockTileSize()));

        }

        toDraw.addAll(blockTileViews);

        //draw cost if increased

        AtomicReference<ImageView> coinView = new AtomicReference<>(null);
        AtomicReference<Text> cardCost = new AtomicReference<>(null);

        if (data.hasBeenUsed()) {
            coinView.set(CoinDrawer.drawCoin(pos.pureSumX(childrenSize * scale / 2 - characterCardWidth * scale / 2).pureSumY(childrenSize * scale / 2 - characterCardHeight * scale / 2), childrenSize * scale / CoinDrawer.getCoinSize()));
            toDraw.add(coinView.get());

            cardCost.set(new Text(String.valueOf(data.getCost())));
            cardCost.get().setTextAlignment(TextAlignment.CENTER);
            cardCost.get().setFont(Font.font(cardCost.get().getFont().getName(), cardCost.get().getFont().getSize() * 1.5));
            cardCost.get().setFill(Color.RED);
            cardCost.get().setX(coinView.get().getX() + coinView.get().getFitWidth() / 5);
            cardCost.get().setY(coinView.get().getY() + 4 * coinView.get().getFitHeight() / 5);

            toDraw.add(cardCost.get());
        }

        EventHandler<MouseEvent> showDescription = event -> {

            //add card description
            description.setX(characterView.getX() - characterView.getFitWidth() * 0.2);
            description.setY(characterView.getY() + 1.15 * characterView.getFitHeight());
            description.setWrappingWidth(characterView.getFitWidth() * 1.4);
            description.setTextAlignment(TextAlignment.JUSTIFY);
            descBox.setX(characterView.getX() - characterView.getFitWidth() * 0.3);
            descBox.setY(characterView.getY() + 1.075 * characterView.getFitHeight());
            descBox.setWidth(characterView.getFitWidth() * 1.6);
            descBox.setHeight(description.maxHeight(-1) * 1.075);
            description.setVisible(true);
            descBox.setVisible(true);

            if (data.hasBeenUsed()){
                getChildrenEnteredZoom(coinView.get(), upLeftCorner.pureSumX(childrenSize / 2 - characterCardWidth / 2).pureSumY(childrenSize / 2 - characterCardHeight / 2), scale, hoverZoom, characterView).handle(event);
                getChildrenEnteredZoom(cardCost.get(), upLeftCorner /*will be changed manually*/, scale, hoverZoom, characterView).handle(event);

                cardCost.get().setX(coinView.get().getX() + coinView.get().getFitWidth() / 5);
                cardCost.get().setY(coinView.get().getY() + 4 * coinView.get().getFitHeight() / 5);
            }

            int studentIndex = 0;

            for (ImageView studentView:
                 studentViews) {

                getChildrenEnteredZoom(studentView, studentsSlots.get(studentIndex), scale, hoverZoom, characterView).handle(event);

                studentIndex++;
            }

            int blockTileIndex = 0;

            for (ImageView blockTileView:
                    blockTileViews) {

                getChildrenEnteredZoom(blockTileView, blockTileSlots.get(blockTileIndex), scale, hoverZoom, characterView).handle(event);

                blockTileIndex++;
            }
        };

        EventHandler<MouseEvent> hideDescription = event -> {
            description.setVisible(false);
            descBox.setVisible(false);

            if (data.hasBeenUsed()){
                getChildrenExitedZoom(coinView.get(), upLeftCorner.pureSumX(childrenSize / 2 - characterCardWidth / 2).pureSumY(childrenSize / 2 - characterCardHeight / 2), scale, hoverZoom, characterView).handle(event);
                getChildrenExitedZoom(cardCost.get(), upLeftCorner /*will be changed manually*/, scale, hoverZoom, characterView).handle(event);

                cardCost.get().setX(coinView.get().getX() + coinView.get().getFitWidth() / 5);
                cardCost.get().setY(coinView.get().getY() + 4 * coinView.get().getFitHeight() / 5);
            }

            int studentIndex = 0;

            for (ImageView studentView:
                    studentViews) {
                getChildrenExitedZoom(studentView, studentsSlots.get(studentIndex), scale, hoverZoom, characterView).handle(event);

                studentIndex++;
            }

            int blockTileIndex = 0;

            for (ImageView blockTileView:
                    blockTileViews) {

                getChildrenExitedZoom(blockTileView, blockTileSlots.get(blockTileIndex), scale, hoverZoom, characterView).handle(event);

                blockTileIndex++;
            }
        };



        addHoveringEffects(characterView, pos, scale, showDescription, hideDescription, hoverZoom, toDraw.subList(1, toDraw.size()));

        return toDraw;
    }

    /**
     * Gets the character card image height.
     * @return The character card image height
     */
    public static double getCharacterCardHeight() {
        return characterCardHeight;
    }

    /**
     * Gets the character card image width.
     * @return The character card image width
     */
    public static double getCharacterCardWidth() {
        return characterCardWidth;
    }
}
