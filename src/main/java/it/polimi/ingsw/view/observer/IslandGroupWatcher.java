package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.view.VirtualView;

public class IslandGroupWatcher extends Watcher{
    IslandGroupBean bean;

    public IslandGroupWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addIslandWatcher(this);
    }

    @Override
    public void update() {
        bean = (IslandGroupBean) source.toBean();
    }

}
