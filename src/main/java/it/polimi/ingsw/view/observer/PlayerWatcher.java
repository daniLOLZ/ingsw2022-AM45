package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.view.VirtualView;

public class PlayerWatcher extends Watcher{
    PlayerBean bean;

    public PlayerWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addPlayerWatcher(this);
    }

    @Override
    public void update() {
        bean = (PlayerBean) source.toBean();
    }
}
