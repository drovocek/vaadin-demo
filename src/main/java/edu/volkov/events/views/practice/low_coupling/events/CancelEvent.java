package edu.volkov.events.views.practice.low_coupling.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class CancelEvent extends ComponentEvent<Component> {

    public CancelEvent(Component source) {
        super(source, false);
    }
}
