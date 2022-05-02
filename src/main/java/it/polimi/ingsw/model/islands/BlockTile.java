package it.polimi.ingsw.model.islands;

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
