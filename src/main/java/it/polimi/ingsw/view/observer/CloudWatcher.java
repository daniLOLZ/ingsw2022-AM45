package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.view.VirtualView;

public class CloudWatcher extends Watcher {
    private CloudBean bean;


    public CloudWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addCloudWatcher(this);
    }

    public void update() {
        bean = (CloudBean) source.toBean();
    }

    public CloudBean getBean() {
        return bean;
    }
}
