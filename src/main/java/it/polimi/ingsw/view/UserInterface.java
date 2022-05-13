package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameElementBean;

import java.util.ArrayList;
import java.util.List;

public abstract class UserInterface {
    protected List<GameElementBean> beans;

    public UserInterface(){
        beans = new ArrayList<>();
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

}
