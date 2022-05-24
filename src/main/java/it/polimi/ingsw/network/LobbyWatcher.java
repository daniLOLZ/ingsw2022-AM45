package it.polimi.ingsw.network;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.view.LobbyBean;
import it.polimi.ingsw.view.observer.Watcher;

public class LobbyWatcher extends Watcher {

    private LobbyBean lobbyBean;

    public LobbyWatcher(DrawableObject object){
        super(object);
    }

    @Override
    public void update() {
        lobbyBean = (LobbyBean) source.toBean();
    }
    public LobbyBean getBean(){
        return lobbyBean;
    }
}
