package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.view.VirtualView;

public class AdvancedIslandGroupWatcher extends Watcher{

    AdvancedIslandGroupBean bean;

    public AdvancedIslandGroupWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addAdvancedIslandWatcher(this);
    }

    @Override
    public void update() {
        bean = (AdvancedIslandGroupBean) source.toBean();
    }

    public AdvancedIslandGroupBean getBean() {
        return bean;
    }
}
