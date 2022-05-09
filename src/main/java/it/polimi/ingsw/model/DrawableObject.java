package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.GameElementBean;

public abstract class DrawableObject {

    /**
     *
     * @return a JavaBean with all information about this class
     */
    public abstract GameElementBean toBean();
}
