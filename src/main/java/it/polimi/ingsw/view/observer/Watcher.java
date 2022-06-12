package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.view.VirtualView;

public abstract class Watcher {
    protected DrawableObject source;
    private boolean alive;

    public Watcher(DrawableObject object){
        this.source = object;
        alive = true;
    }

    public abstract void update();


    /**
     * Use when the obj observed does not exist any more and this watcher will never
     * be called.
     * Virtual view see that this watcher is not alive and ask nothing to it
     */
    public void kill(){
        alive = false;
        source = null;
    }

    public boolean isAlive() {
        return alive;
    }
/*
    public abstract GameElementBean getBean();
 */
}
