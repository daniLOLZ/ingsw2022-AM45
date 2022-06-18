package it.polimi.ingsw.network.client;

import java.util.concurrent.atomic.AtomicBoolean;

public class InterfaceInterrupt {
    private final boolean activeWhenTrue;
    private final AtomicBoolean interrupt;

    public InterfaceInterrupt(boolean activeWhenTrue, AtomicBoolean interrupt) {
        this.activeWhenTrue = activeWhenTrue;
        this.interrupt = interrupt;
    }

    public boolean isActiveWhenTrue() {
        return activeWhenTrue;
    }

    public AtomicBoolean getInterrupt() {
        return interrupt;
    }

    /**
     * Tells if the interrupt is currently triggered
     * @return true if the activeWhenTrue flag and the interrupt value are the same
     */
    public boolean isTriggered(){
        return activeWhenTrue == interrupt.get();
    }
}
