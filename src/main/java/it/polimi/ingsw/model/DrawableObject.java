package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.GameElementBean;

public interface DrawableObject {

    /**
     *
     * @return a JavaBean with all information about this class
     */
    GameElementBean toBean();
}
