package edu.volkov.events.views.practice.low_coupling.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;

import java.io.Serializable;

public interface SelectPublisher extends Serializable {

    default void fireSelectEventTo(Component target, Object selected) {
        ComponentUtil.fireEvent(target, new SelectEvent((Component) this, selected));
    }
}
