package edu.volkov.events.views.practice.low_coupling.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class SelectEvent extends ComponentEvent<Component> {

    private final Object selected;

    public SelectEvent(Component source, Object selected) {
        super(source, false);
        this.selected = selected;
    }

    public Object getSelected() {
        return selected;
    }
}
