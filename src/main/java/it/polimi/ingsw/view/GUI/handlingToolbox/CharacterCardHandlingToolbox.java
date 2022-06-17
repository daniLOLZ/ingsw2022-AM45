package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CharacterCardHandlingToolbox implements HandlingToolbox{

    private EventHandler<MouseEvent> onCharacterCardClick;
    private List<EventHandler<MouseEvent>> onStudentOnCardClick;

    //needed to re-allow all commands whenever the island group morphology changes
    private EnumSet<CommandEnum> allowedCommands = EnumSet.noneOf(CommandEnum.class);
    private ClientSender sender;

    private boolean initialized = false;

    public CharacterCardHandlingToolbox(){
        onCharacterCardClick = HandlingToolbox.NO_EFFECT;
    }

    public void setNumStudents(int numStudents){
        initialized = true;
        for (int student = 0; student < numStudents; student++) {
            onStudentOnCardClick.add(NO_EFFECT);
        }

        if (!allowedCommands.isEmpty()){
            for (CommandEnum command : allowedCommands) {
                allowCommand(command, sender);
            }
        }
    }

    @Override
    public void allowCommand(CommandEnum command, ClientSender resourceProvider) {
        //TODO

        if (!sender.equals(resourceProvider)) sender = resourceProvider;

        allowedCommands.add(command);

        if (command == CommandEnum.SELECT_STUDENT_ON_CARD) {

            int studentIndex = 0;

            for (EventHandler<MouseEvent> ignored:
                 onStudentOnCardClick) {
                int finalIndex = studentIndex;
                onStudentOnCardClick.set(studentIndex, event -> new Thread(() -> resourceProvider.sendSelectStudentOnCard(finalIndex)).start());
                studentIndex++;
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum command) {
        //TODO

        allowedCommands.remove(command);

        if (command == CommandEnum.SELECT_STUDENT_ON_CARD){

            int index = 0;

            for (EventHandler<MouseEvent> ignored:
                 onStudentOnCardClick) {
                onStudentOnCardClick.set(index, DISABLED);
                index++;
            }
        }
    }

    public EventHandler<MouseEvent> getOnCharacterCardClick() {
        return onCharacterCardClick;
    }

    public EventHandler<MouseEvent> getOnStudentOnCardClick(int pos) {
        return onStudentOnCardClick.get(pos);
    }

    public boolean isInitialized() {
        return initialized;
    }
}
