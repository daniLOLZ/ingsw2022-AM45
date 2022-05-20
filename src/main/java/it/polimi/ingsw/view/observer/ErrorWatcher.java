package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.ErrorBean;
import it.polimi.ingsw.model.beans.PlayerBean;
import it.polimi.ingsw.view.VirtualView;

public class ErrorWatcher extends Watcher{
    ErrorBean bean;

    public ErrorWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addErrorWatcher(this);
    }

    @Override
    public void update() {
        bean = (ErrorBean) source.toBean();
    }

    public ErrorBean getBean() {
        return bean;
    }
}
