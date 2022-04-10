package it.polimi.ingsw.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return motherNatureSteps == assistant.motherNatureSteps && turnOrder == assistant.turnOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(motherNatureSteps, turnOrder);
    }
}
