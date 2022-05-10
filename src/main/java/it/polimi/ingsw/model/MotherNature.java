package it.polimi.ingsw.model;

import it.polimi.ingsw.model.islands.IslandGroup;

public class MotherNature {
    private IslandGroup position;

    public MotherNature(IslandGroup initPosition){
        position = initPosition;
    }
    /**
     * Moves Mother Nature across IslandGroups, the movement happens via the nextIslandGroup pointers;
     * @requires steps != 0
     * @param steps the number of steps to move Mother Nature
     * @return IslandGroup of the current position of Mother Nature
     */
    public IslandGroup move(int steps){
        IslandGroup currentPosition = position;
        for (int i = 0; i < steps; i++) {
            currentPosition = currentPosition.getNextIslandGroup();
        }
        position = currentPosition;
        return position;
    }

    public void setPosition(IslandGroup position) {
        this.position = position;
    }

    public IslandGroup getPosition() {
        return position;
    }
}
