package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.GameBoardBean;
import it.polimi.ingsw.view.VirtualView;

public class SimpleGameWatcher extends Watcher{
    GameBoardBean bean;

    public SimpleGameWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addSimpleGameWatcher(this);
    }

    @Override
    public void update() {
        bean = (GameBoardBean) source.toBean();
    }

    public GameBoardBean getBean() {
        return bean;
    }
}
