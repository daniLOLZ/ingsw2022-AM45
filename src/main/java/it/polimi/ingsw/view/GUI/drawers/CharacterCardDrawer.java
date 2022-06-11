package it.polimi.ingsw.view.GUI.drawers;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.beans.CharacterCardBean;
import it.polimi.ingsw.view.GUI.Coord;
import it.polimi.ingsw.view.GUI.handlingToolbox.HandlingToolbox;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.GUI.GUIApplication.right;
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
        studentsSlots.add(upLeftCorner.pureSumX(characterCardWidth / 10).pureSumY(3 * characterCardHeight / 8));
        studentsSlots.add(upLeftCorner.pureSumY(characterCardHeight / 2));
    }

    public static List<Node> drawCharacterCard(CharacterCardBean data, Coord pos, double scale){

        List<Node> toDraw = new ArrayList<>();

        ImageView characterView = drawFromCenterInteractiveImage(characterCards.get(data.getId() - firstCharacterId), pos, scale, HandlingToolbox.NO_EFFECT);

        toDraw.add(characterView);

        Text description = new Text(data.getDescription());
        Rectangle descBox = new Rectangle();
        descBox.setVisible(false);
        description.setVisible(false);

        toDraw.add(descBox);
        toDraw.add(description);

        description.setFont(Font.font(description.getFont().getName(), 120 * scale));

        descBox.setFill(Color.BURLYWOOD);

        List<ImageView> studentViews = new ArrayList<>();

        int studentIndex = 0;

        for (StudentEnum student:
                data.getStudents()) {

            studentViews.add(StudentDrawer.drawStudent(
                    student,
                    pos
                            .pureSumX(studentsSlots.get(studentIndex).x * scale)
                            .pureSumY(studentsSlots.get(studentIndex).y * scale),
                    scale * childrenSize / StudentDrawer.getStudentSize()));

            studentIndex++;
        }

        toDraw.addAll(studentViews);


        EventHandler<MouseEvent> showDescription = event -> {

            description.setX(characterView.getX());
            description.setY(characterView.getY() + 1.15 * characterView.getFitHeight());
            description.setWrappingWidth(characterView.getFitWidth());
            descBox.setX(characterView.getX());
            descBox.setY(characterView.getY() + 1.075 * characterView.getFitHeight());
            descBox.setWidth(characterView.getFitWidth());
            descBox.setHeight(description.getFont().getSize() * 2);
            description.setVisible(true);
            descBox.setVisible(true);

            int index = 0;

            for (ImageView studentView:
                 studentViews) {

                getChildrenEnteredZoom(studentView, studentsSlots.get(index), scale, hoverZoom, characterView).handle(event);

                index++;
            }
        };

        EventHandler<MouseEvent> hideDescription = event -> {
            description.setVisible(false);
            descBox.setVisible(false);

            int index = 0;

            for (ImageView studentView:
                    studentViews) {
                getChildrenExitedZoom(studentView, studentsSlots.get(index), scale, hoverZoom, characterView).handle(event);

                index++;
            }
        };

        addHoveringEffects(characterView, pos, scale, showDescription, hideDescription, hoverZoom, toDraw.subList(1, toDraw.size()));

        return toDraw;
    }

    public static double getCharacterCardHeight() {
        return characterCardHeight;
    }

    public static double getCharacterCardWidth() {
        return characterCardWidth;
    }
}
