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

    /**
     * Tells if the interrupt is currently triggered
     * @return true if the activeWhenTrue flag and the interrupt value are the same
     */
    public boolean isTriggered(){
        return activeWhenTrue == interrupt.get();
    }

    /**
     * Triggers the interrupt, setting it in its active state
     */
    public void trigger(){
        interrupt.set(activeWhenTrue);
    }

    /**
     * Puts the interrupt in an untriggered state
     */
    public void clearInterrupt() {
        interrupt.set(!activeWhenTrue);
    }
}
