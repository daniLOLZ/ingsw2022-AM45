package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.view.observer.Watcher;

import java.util.List;

public abstract class DrawableObject {

    protected List<Watcher> watchers;

    /**
     *
     * @return a JavaBean with all information about this class
     */
    public abstract GameElementBean toBean();

    public void alert(){
        for(Watcher watcher: watchers){
            watcher.update();
        }
    }

    /**
     * Kill all observers because this obj does not exist anymore
     */
    public void killAll(){
        for(Watcher watcher: watchers){
            watcher.kill();
        }
    }
}
