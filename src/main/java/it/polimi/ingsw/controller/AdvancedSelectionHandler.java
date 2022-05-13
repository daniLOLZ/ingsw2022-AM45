package it.polimi.ingsw.controller;

import java.util.List;

public class AdvancedSelectionHandler extends SelectionHandler{

    public AdvancedSelectionHandler(Controller controller){
        super(controller);
        advancedParameter = controller.advancedGame.getAdvancedParameters();

    }

    /**
     * Deselects the student on the character card
     * @param position the position on the card
     */
    public void deselectStudentOnCard(int position){
        controller.advancedGame.deselectStudentOnCard(position);
    }

    /**
     * Deselects all students on a character card
     * @return true if action happened successfully
     */
    public boolean deselectStudentOnCard(){
        controller.advancedGame.deselectAllCardStudents();
        return true;
    }

    public void selectStudentOnCard(int position){
        if(advancedParameter != null)
            controller.advancedGame.selectStudentOnCard(position);
    }

    public boolean selectStudentOnCard(List<Integer> positions){
        for(Integer position: positions){
            selectStudentOnCard(position);
        }
        return true;
    }

    @Override
    public void deselectAll(){
        super.deselectAll();
        deselectStudentOnCard();
    }
}
