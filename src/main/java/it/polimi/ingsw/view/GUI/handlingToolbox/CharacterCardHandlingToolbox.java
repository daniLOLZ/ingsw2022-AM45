package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.GUI.CharacterCardSelection;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CharacterCardHandlingToolbox implements HandlingToolbox{

    private EventHandler<MouseEvent> onCharacterCardClick;
    private List<EventHandler<MouseEvent>> onStudentOnCardClick;

    //needed to re-allow all commands whenever the island group morphology changes
    private EnumSet<CommandEnum> allowedCommands = EnumSet.noneOf(CommandEnum.class);
    private ClientSender sender;


    private boolean selected = false;
    private int cardIndex;

    private CharacterCardSelection selections;

    public CharacterCardHandlingToolbox(){
        onCharacterCardClick = HandlingToolbox.DISABLED;
        onStudentOnCardClick = new ArrayList<>();
    }

    /**
     * Initializes the Character Card.
     * @param cardIndex The id of the Character Card this Toolbox is assigned to
     * @param numStudents The number of students on the Character Card
     */
    public void setCardInfo(int cardIndex, int numStudents){

        this.cardIndex = cardIndex;

        if (onStudentOnCardClick.isEmpty()) {
            for (int student = 0; student < numStudents; student++) {
                onStudentOnCardClick.add(DISABLED);
            }
        }

        if (!allowedCommands.isEmpty()){
            for (CommandEnum command : allowedCommands) {
                allowCommand(command, sender);
            }
        }
    }

    /**
     * Implements the above method for Character Card handling.
     * @param command          the command for which the toolbox will provide the handling resource if asked
     * @param resourceProvider the provider on which the toolbox relies
     */
    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {

        if (!resourceProvider.equals(sender)) sender = resourceProvider;

        allowedCommands.add(command);

        if (command == CommandEnum.SELECT_CHARACTER){

            selected = false;

            onCharacterCardClick = event -> {
                selected = true;
                new Thread(() -> resourceProvider.sendSelectCharacter(cardIndex)).start();
            };
        }

        if (selected && command == CommandEnum.SELECT_STUDENTS_ON_CARD) {

            int studentIndex = 0;

            for (EventHandler<MouseEvent> ignored:
                 onStudentOnCardClick) {
                int finalIndex = studentIndex;
                onStudentOnCardClick.set(finalIndex, event -> {
                    onStudentOnCardClick.set(finalIndex, NO_EFFECT);
                    selections.addStudentOnCard(finalIndex + 1);
                    System.out.println("Added student on card");
                });
                studentIndex++;
            }
        }
    }

    /**
     * Implements the above method for Character Card handling.
     * @param command the command to disable
     */
    @Override
    public void disableCommand(CommandEnum command) {

        allowedCommands.remove(command);

        if (command == CommandEnum.SELECT_CHARACTER){
            onCharacterCardClick = NO_EFFECT;
        }

        if (command == CommandEnum.SELECT_STUDENTS_ON_CARD){

            int index = 0;

            for (EventHandler<MouseEvent> ignored:
                 onStudentOnCardClick) {
                onStudentOnCardClick.set(index, DISABLED);
                index++;
            }
        }
    }

    /**
     * Sets the container who will store the selections made by the user.
     * @param selections The object able to store user selection
     */
    public void setSelectionsContainer(CharacterCardSelection selections) {
        this.selections = selections;
    }

    /**
     * Returns the allowed action to respond with when the user clicks on the card.
     * @return The EventHandler to assign to the Character Card
     */
    public EventHandler<MouseEvent> getOnCharacterCardClick() {
        return onCharacterCardClick;
    }

    /**
     * Returns the allowed action for the given student on the card.
     * @param pos The position of the student on the card
     * @return The EventHandler to assign to the student on the card
     */
    public EventHandler<MouseEvent> getOnStudentOnCardClick(int pos) {
        return onStudentOnCardClick.get(pos);
    }

    /**
     * Returns the current value of the selected property.
     * @return true if the Character Card is currently selected for activation
     */
    public boolean isSelected(){
        return selected;
    }
}
