package it.polimi.ingsw.model;

public class Assistant {
    public final int id;

    public final int motherNatureSteps;
    public final int turnOrder;

    public Assistant(int id, int motherNatureSteps, int turnOrder) {
        this.id = id;
        this.motherNatureSteps = motherNatureSteps;
        this.turnOrder = turnOrder;
    }

    public int getId() {
        return id;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public int getTurnOrder() {
        return turnOrder;
    }
}
