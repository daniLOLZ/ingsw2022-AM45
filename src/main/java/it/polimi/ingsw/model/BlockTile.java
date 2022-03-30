package it.polimi.ingsw.model;

public class BlockTile {
    private IslandGroup blockedIsland;
    private boolean isAssigned;

    public BlockTile(IslandGroup blockedIsland, boolean isAssigned) {
        this.blockedIsland = blockedIsland;
        this.isAssigned = isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
