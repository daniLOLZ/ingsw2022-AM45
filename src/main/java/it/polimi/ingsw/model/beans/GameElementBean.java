package it.polimi.ingsw.model.beans;

public abstract class GameElementBean {
    protected int priority;                 //Priority is useful to draw the element in order

    /**
     * Show in CLI the Bean information
     * @return the string with information about that bean
     */
    public abstract String drawCLI();



    public int getPriority() {
        return priority;
    }

    //todo
}
