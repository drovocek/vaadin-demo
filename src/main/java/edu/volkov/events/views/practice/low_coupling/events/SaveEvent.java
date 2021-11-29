package edu.volkov.events.views.practice.low_coupling.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class SaveEvent extends ComponentEvent<Component> {

    private final Object persistent;

    public SaveEvent(Component source, Object persistent) {
        super(source, false);
        this.persistent = persistent;
    }

    public Object getPersistent() {
        return persistent;
    }
}
