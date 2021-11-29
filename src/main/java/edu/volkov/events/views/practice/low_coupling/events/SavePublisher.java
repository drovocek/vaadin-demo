package edu.volkov.events.views.practice.low_coupling.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;

import java.io.Serializable;

public interface SavePublisher extends Serializable {

    default void fireSaveEventTo(Component target, Object persistent) {
        ComponentUtil.fireEvent(target, new SaveEvent((Component) this, persistent));
    }
}
