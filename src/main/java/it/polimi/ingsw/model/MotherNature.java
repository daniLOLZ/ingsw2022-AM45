package it.polimi.ingsw.model;

public class MotherNature {
    private IslandGroup position;

    public MotherNature(IslandGroup initPosition){
        position = initPosition;
    }
    /**
     * Moves Mother Nature across IslandGroups, the movement happens via the nextIslandGroup pointers;
     * @requires steps != 0
     * @param steps the number of steps to move Mother Nature
     */
    public void move(int steps){
        IslandGroup currentPosition = position;
        for (int i = 0; i < steps; i++) {
            currentPosition = currentPosition.getNextIslandGroup();
        }
        position = currentPosition;
    }

    public IslandGroup getPosition() {
        return position;
    }
}
