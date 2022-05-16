package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.AdvancedPlayerBean;
import it.polimi.ingsw.view.VirtualView;

public class AdvancedPlayerWatcher extends Watcher{
    AdvancedPlayerBean bean;

    public AdvancedPlayerWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addAdvancedPlayerWatcher(this);
    }

    @Override
    public void update() {
        bean = (AdvancedPlayerBean) source.toBean();
    }
}
