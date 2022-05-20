package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.AdvancedGameBoardBean;
import it.polimi.ingsw.view.VirtualView;

public class AdvancedGameWatcher extends Watcher{

    AdvancedGameBoardBean bean;

    public AdvancedGameWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.setAdvancedGameWatcher(this);
    }

    @Override
    public void update() {
        bean = (AdvancedGameBoardBean) source.toBean();
    }

    public AdvancedGameBoardBean getBean() {
        return bean;
    }
}
