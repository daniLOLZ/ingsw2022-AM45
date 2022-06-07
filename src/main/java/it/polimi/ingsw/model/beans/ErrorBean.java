package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.network.BeanEnum;

public class ErrorBean extends GameElementBean{

    private String error;
    public ErrorBean(String error){
        final int lowestPriority = 9;
        this.error = error;
        priority = lowestPriority;
    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.ERROR_BEAN;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return ("ERROR: " + error + "\n");
    }
}
