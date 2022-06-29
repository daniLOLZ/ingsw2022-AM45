package it.polimi.ingsw.view.GUI;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;


public class GenericEvent extends Event{

    public GenericEvent(EventType<? extends Event> eventType) {
        super(Event.ANY);
    }
}
