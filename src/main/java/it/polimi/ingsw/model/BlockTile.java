package it.polimi.ingsw.model;

public class BlockTile {
    private boolean isAssigned;

    public BlockTile() {
        this.isAssigned = false;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public boolean isAssigned() {
        return isAssigned;
    }
}
