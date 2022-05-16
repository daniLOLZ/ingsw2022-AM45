package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.CommandEnum;

import java.util.ArrayList;
import java.util.List;

public abstract class UserInterface {
    protected List<GameElementBean> beans;
    protected List<CommandEnum> availableCommands;

    public UserInterface(){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
    }

    public void addBean(GameElementBean bean){
        beans.add(bean);
    }

    public GameElementBean removeBean(int index){
        return beans.remove(index);
    }

    public void clearBeans(){
        beans.clear();
    }

    public void addCommand(CommandEnum command){
        availableCommands.add(command);
    }

    public CommandEnum removeCommand(int index){
        return removeCommand(index);
    }

    public void clearCommand(){
        availableCommands.clear();
    }

}
