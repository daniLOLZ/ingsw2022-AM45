package it.polimi.ingsw.view.GUI;

import javafx.event.Event;

/**
 * The GenericEvent class is an object made to encapsulate the occurrence of a non-specified event. Hence, it has no properties.
 */
public class GenericEvent extends Event{

    public GenericEvent() {
        super(Event.ANY);
    }
}
