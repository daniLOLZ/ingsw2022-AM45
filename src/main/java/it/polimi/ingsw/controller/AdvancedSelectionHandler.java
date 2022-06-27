package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.characterCards.InitialEffect;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;

import java.util.List;

public class AdvancedSelectionHandler extends SelectionHandler{

    AdvancedParameterHandler advancedParameters;

    public AdvancedSelectionHandler(Controller controller){
        super(controller);
        advancedParameters = controller.advancedGame.getAdvancedParameters();
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

    /**
     * Selects a single student on the card selected previously
     * @param position the position of the student on the card
     * @return true if the selection happened successfully
     */
    public boolean selectStudentOnCard(int position){

        InitialEffect initialEffectCard;
        try{
            initialEffectCard = (InitialEffect) controller.characterCardHandler.getUsingCard();
        } catch (ClassCastException e){
            return false;
        }

        if(position < initialEffectCard.size()
            && position >= 0
            && !initialEffectCard.getStudents(position).equals(StudentEnum.NOSTUDENT)) {
            controller.advancedGame.selectStudentOnCard(position);
            return true;
        }
        else return false;
    }

    /**
     * Deselects all students on the card and selects the ones in the parameter
     * @param positions the students to select, which will replace the previously selected students
     * @return true if the selection happened successfully.
     * If even just one selection fails, this method returns false
     */
    public boolean selectStudentOnCard(List<Integer> positions){
        //Deselect all previously selected students
        deselectStudentOnCard();

        for(Integer position: positions){
            if(!selectStudentOnCard(position)){
                //If even one failed, deselect everything
                deselectStudentAtEntrance();
                return false;
            }
        }
        return true;
    }

    @Override
    public void deselectAll(){
        super.deselectAll();
        deselectStudentOnCard();
    }
}
