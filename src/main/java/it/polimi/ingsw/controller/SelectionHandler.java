package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.game.AdvancedParameterHandler;
import it.polimi.ingsw.model.game.ParameterHandler;

import java.util.List;

public class SelectionHandler {
    Controller controller;
    ParameterHandler parameter;
    AdvancedParameterHandler advancedParameter;

    public SelectionHandler(Controller controller){
        this.controller = controller;
        parameter = controller.simpleGame.getParameters();
        if(controller.gameRule.id > 4){
            advancedParameter = controller.advancedGame.getAdvancedParameters();
        }
    }

    public void selectIsland(int idIslandGroup){
        controller.simpleGame.selectIslandGroup(idIslandGroup);
    }

    public void selectIsland(List<Integer> idIslandGroup){
        for(Integer id: idIslandGroup){
            selectIsland(id);
        }
    }

    public void selectStudentAtEntrance(int position){
        controller.simpleGame.selectEntranceStudent(position);
    }

    public void selectStudentAtEntrance(List<Integer> positions){
        for(Integer position: positions){
            selectStudentAtEntrance(position);
        }
    }

    public void selectStudentType(StudentEnum type){
        controller.simpleGame.selectStudentType(type);
    }

    public void selectStudentType(List<StudentEnum> types){
        for(StudentEnum type: types){
            selectStudentType(type);
        }
    }

    public void selectStudentOnCard(int position){
        if(advancedParameter != null)
            controller.advancedGame.selectStudentOnCard(position);
    }

    public void selectStudentOnCard(List<Integer> positions){
        for(Integer position: positions){
            selectStudentOnCard(position);
        }
    }
}
