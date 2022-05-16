package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.DrawableObject;
import it.polimi.ingsw.model.beans.CharacterCardBean;
import it.polimi.ingsw.view.VirtualView;

public class CharacterWatcher extends Watcher{
    CharacterCardBean bean;

    public CharacterWatcher(DrawableObject object, VirtualView virtualView){
        super(object);
        virtualView.addCharacterWatcher(this);
    }

    @Override
    public void update() {
        bean = (CharacterCardBean) source.toBean();
    }
}
