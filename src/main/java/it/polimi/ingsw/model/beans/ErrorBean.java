package it.polimi.ingsw.model.beans;

public class ErrorBean extends GameElementBean{

    private String error;
    public ErrorBean(String error){
        final int lowestPriority = 9;
        this.error = error;
        priority = lowestPriority;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String drawCLI() {
        return ("ERROR: " + error + "\n");
    }
}
