package edu.volkov.events.views.practice.low_coupling.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.shared.Registration;

import java.io.Serializable;

public interface SaveNotifier extends Serializable {

    default Registration addSaveListener(Component target, ComponentEventListener<SaveEvent> listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener(target, SaveEvent.class, listener);
        } else {
            throw new IllegalStateException(String.format("The class '%s' doesn't extend '%s'. Make your implementation for the method '%s'.", this.getClass().getName(), Component.class.getSimpleName(), "addKeyDownListener"));
        }
    }
}
