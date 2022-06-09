package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.client.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class CharacterCardHandlingToolbox implements HandlingToolbox{

    private EventHandler<MouseEvent> onCharacterCardClick;
    private List<EventHandler<MouseEvent>> onStudentOnCardClick;

    public CharacterCardHandlingToolbox(int numStudents){
        onCharacterCardClick = HandlingToolbox.NO_EFFECT;
        onStudentOnCardClick = new ArrayList<>();

        for (int student = 0; student < numStudents; student++) {
            onStudentOnCardClick.add(HandlingToolbox.NO_EFFECT);
        }
    }

    @Override
    public void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider) {
        //TODO

        if (command == CommandEnum.SELECT_STUDENT_ON_CARD) {

            int index = 0;

            for (EventHandler<MouseEvent> ignored:
                 onStudentOnCardClick) {
                //onStudentOnCardClick.set(index, resourceProvider.selectStudentOnCard(index));
                index++;
            }
        }
    }

    @Override
    public void disableCommand(CommandEnum command) {
        //TODO

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
}
